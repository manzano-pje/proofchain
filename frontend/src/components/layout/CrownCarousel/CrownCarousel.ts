// =========================================================
// Project.......: ProofChain
// Component.....: CrownCarousel
// File..........: CrownCarousel.ts
// Version.......: 1.0.0
//
// Description...:
// Composition API logic for the 3D cylindrical crown carousel.
//
// Responsibility:
// Geometry, auto-rotate motor, Pointer Events (drag vs click),
// ResizeObserver, reduced-motion and lifecycle cleanup.
// =========================================================

import { computed, onMounted, onUnmounted, ref, watch, type CSSProperties, type Ref } from 'vue'

// ---------------------------------------------------------
// Types
// ---------------------------------------------------------

export interface CarouselItemData {
  id: string
  image: string
  title: string
  description: string
}

export interface CrownCarouselProps {
  items: CarouselItemData[]
}

export interface RenderCarouselItem extends CarouselItemData {
  renderKey: string
  sourceIndex: number
}

// ---------------------------------------------------------
// Internal calibration (adjustable by developers)
// ---------------------------------------------------------

/** Largura base do card em pixels. */
const CARD_WIDTH = 280
/** Altura base do card em pixels. */
const CARD_HEIGHT = 400
/** Espaçamento entre as arestas dos cards no cilindro. */
const RADIUS_GAP = 40
/** Perspectiva da câmera em pixels. */
const PERSPECTIVE = 1200
/** Inclinação global da câmera no eixo X (graus). */
const TILT_ANGLE_X = -30
/** Velocidade do auto-rotate em graus por frame (60fps). */
const AUTO_ROTATE_SPEED = 0.5
/** Multiplicador do deslocamento horizontal no eixo Y do anel. */
const DRAG_SENSITIVITY = 1.2
/** Limite em px para diferenciar drag de click (ghost clicks). */
const FLIP_THRESHOLD = 5
/** Pausa em ms antes de retomar o auto-rotate após desengajamento. */
const RESUME_DELAY = 300
/** Quantidade mínima de slots físicos no anel. */
const MIN_RING_SLOTS = 6
/** Duração do flip 3D em milissegundos. */
const FLIP_DURATION_MS = 600

// ---------------------------------------------------------
// Geometry helpers
// ---------------------------------------------------------

function normalizeDegrees(value: number): number {
  const wrapped = value % 360
  return wrapped < 0 ? wrapped + 360 : wrapped
}

function computeRadius(slotCount: number, cardWidth: number, gap: number): number {
  if (slotCount < 2) {
    return cardWidth
  }

  return (cardWidth / 2 + gap / 2) / Math.tan(Math.PI / slotCount)
}

function getDepthOfField(worldAngle: number): { opacity: number; filter: string } {
  const angle = normalizeDegrees(worldAngle)
  const isBack = angle > 90 && angle < 270

  if (!isBack) {
    return { opacity: 1, filter: 'blur(0px)' }
  }

  const rearProximity = 1 - Math.abs(angle - 180) / 90
  const opacity = 1 - rearProximity * 0.7
  const blur = rearProximity * 8

  return {
    opacity,
    filter: `blur(${blur}px)`,
  }
}

function createRenderItems(items: CarouselItemData[]): RenderCarouselItem[] {
  if (items.length === 0) {
    return []
  }

  const packed: CarouselItemData[] = []

  if (items.length >= MIN_RING_SLOTS) {
    packed.push(...items)
  } else {
    while (packed.length < MIN_RING_SLOTS) {
      const source = items[packed.length % items.length]
      if (!source) {
        break
      }
      packed.push(source)
    }
  }

  return packed.map((item, index) => ({
    ...item,
    renderKey: `${item.id}-clone-${index}`,
    sourceIndex: index % items.length,
  }))
}

function pointerDistance(startX: number, startY: number, x: number, y: number): number {
  return Math.hypot(x - startX, y - startY)
}

// ---------------------------------------------------------
// Composable
// ---------------------------------------------------------

export function useCrownCarousel(items: Ref<CarouselItemData[]>) {
  const viewportRef = ref<HTMLElement | null>(null)
  const rotationY = ref(0)
  const isInteracting = ref(false)
  const isDragging = ref(false)
  const prefersReducedMotion = ref(false)
  const containerWidth = ref(CARD_WIDTH * 3)
  const flippedIndexes = ref<boolean[]>([])

  const pointer = {
    id: 0,
    down: false,
    hovering: false,
    startX: 0,
    startY: 0,
    lastX: 0,
    cardIndex: -1,
    exceededThreshold: false,
  }

  let animationFrame = 0
  let lastTime = 0
  let resumeTimer = 0
  let resizeObserver: ResizeObserver | null = null
  let motionQuery: MediaQueryList | null = null

  const renderItems = computed(() => createRenderItems(items.value))

  const cardScale = computed(() => {
    const available = Math.max(containerWidth.value, CARD_WIDTH)
    return Math.min(1, available / (CARD_WIDTH * 3.15))
  })

  const cardWidth = computed(() => CARD_WIDTH * cardScale.value)
  const cardHeight = computed(() => CARD_HEIGHT * cardScale.value)
  const radiusGap = computed(() => RADIUS_GAP * cardScale.value)

  const radius = computed(() =>
    computeRadius(renderItems.value.length, cardWidth.value, radiusGap.value),
  )

  const viewportStyle = computed<CSSProperties>(() => ({
    perspective: `${PERSPECTIVE}px`,
    '--crown-card-width': `${cardWidth.value}px`,
    '--crown-card-height': `${cardHeight.value}px`,
    '--crown-flip-duration': prefersReducedMotion.value ? '0ms' : `${FLIP_DURATION_MS}ms`,
    minHeight: `${Math.max(cardHeight.value * 1.35, radius.value * 0.85 + cardHeight.value * 0.55)}px`,
  }))

  const stageStyle = computed<CSSProperties>(() => ({
    transform: `rotateX(${TILT_ANGLE_X}deg)`,
  }))

  const ringStyle = computed<CSSProperties>(() => ({
    transform: `rotateY(${rotationY.value}deg)`,
  }))

  const cardStyles = computed<CSSProperties[]>(() => {
    const count = renderItems.value.length || 1
    const step = 360 / count

    return renderItems.value.map((_, index) => {
      const localAngle = step * index
      const worldAngle = rotationY.value + localAngle
      const depth = getDepthOfField(worldAngle)
      const facing = Math.cos((normalizeDegrees(worldAngle) * Math.PI) / 180)

      return {
        transform: `rotateY(${localAngle}deg) translateZ(${radius.value}px)`,
        opacity: depth.opacity,
        filter: depth.filter,
        zIndex: Math.round(facing * 100),
      } satisfies CSSProperties
    })
  })

  const syncFlippedState = (count: number) => {
    if (flippedIndexes.value.length === count) {
      return
    }
    flippedIndexes.value = Array.from({ length: count }, () => false)
  }

  const resetFlips = () => {
    if (flippedIndexes.value.some(Boolean)) {
      flippedIndexes.value = flippedIndexes.value.map(() => false)
    }
  }

  const toggleFlip = (index: number) => {
    const current = flippedIndexes.value[index]
    if (current === undefined) {
      return
    }
    const next = [...flippedIndexes.value]
    next[index] = !current
    flippedIndexes.value = next
  }

  const pauseInteraction = () => {
    isInteracting.value = true
    window.clearTimeout(resumeTimer)
    resumeTimer = 0
  }

  const beginResumeCountdown = () => {
    if (pointer.down || pointer.hovering) {
      return
    }

    window.clearTimeout(resumeTimer)
    resumeTimer = window.setTimeout(() => {
      resetFlips()
      isInteracting.value = false
      isDragging.value = false
      resumeTimer = 0
    }, RESUME_DELAY)
  }

  const applyMotionPreference = () => {
    prefersReducedMotion.value = motionQuery?.matches ?? false
    if (prefersReducedMotion.value) {
      isInteracting.value = false
    }
  }

  const onMotionPreferenceChange = () => {
    applyMotionPreference()
  }

  const updateDimensions = () => {
    if (!viewportRef.value) {
      return
    }
    containerWidth.value = viewportRef.value.clientWidth
  }

  const animate = (time: number) => {
    if (!lastTime) {
      lastTime = time
    }

    const delta = Math.min(time - lastTime, 32)
    lastTime = time

    const canAutoRotate = !prefersReducedMotion.value && !isInteracting.value
    if (canAutoRotate) {
      rotationY.value += AUTO_ROTATE_SPEED * (delta / (1000 / 60))
    }

    animationFrame = window.requestAnimationFrame(animate)
  }

  const resolveCardIndex = (event: PointerEvent): number => {
    const target = event.target
    if (!(target instanceof Element)) {
      return -1
    }

    const card = target.closest<HTMLElement>('[data-crown-index]')
    const rawIndex = card?.dataset.crownIndex
    if (!rawIndex) {
      return -1
    }

    const index = Number.parseInt(rawIndex, 10)
    return Number.isFinite(index) ? index : -1
  }

  const onPointerEnter = () => {
    pointer.hovering = true
    pauseInteraction()
  }

  const onPointerLeave = () => {
    pointer.hovering = false
    if (!pointer.down) {
      beginResumeCountdown()
    }
  }

  const onPointerDown = (event: PointerEvent) => {
    if (!viewportRef.value) {
      return
    }

    pointer.down = true
    pointer.id = event.pointerId
    pointer.startX = event.clientX
    pointer.startY = event.clientY
    pointer.lastX = event.clientX
    pointer.cardIndex = resolveCardIndex(event)
    pointer.exceededThreshold = false
    isDragging.value = false

    pauseInteraction()
    viewportRef.value.setPointerCapture?.(event.pointerId)
  }

  const onPointerMove = (event: PointerEvent) => {
    if (!pointer.down) {
      return
    }

    const distance = pointerDistance(pointer.startX, pointer.startY, event.clientX, event.clientY)
    if (distance > FLIP_THRESHOLD) {
      pointer.exceededThreshold = true
      isDragging.value = true
    }

    if (pointer.exceededThreshold) {
      const deltaX = event.clientX - pointer.lastX
      rotationY.value += deltaX * DRAG_SENSITIVITY
    }

    pointer.lastX = event.clientX
  }

  const onPointerUp = (event: PointerEvent) => {
    if (!pointer.down) {
      return
    }

    const distance = pointerDistance(pointer.startX, pointer.startY, event.clientX, event.clientY)
    const isClick = distance <= FLIP_THRESHOLD && !pointer.exceededThreshold

    if (isClick && pointer.cardIndex >= 0) {
      toggleFlip(pointer.cardIndex)
    }

    pointer.down = false
    pointer.exceededThreshold = false
    isDragging.value = false

    if (viewportRef.value?.hasPointerCapture(event.pointerId)) {
      viewportRef.value.releasePointerCapture(event.pointerId)
    }

    beginResumeCountdown()
  }

  const onPointerCancel = (event: PointerEvent) => {
    onPointerUp(event)
  }

  watch(
    renderItems,
    (nextItems) => {
      syncFlippedState(nextItems.length)
    },
    { immediate: true },
  )

  onMounted(() => {
    updateDimensions()

    if (viewportRef.value) {
      resizeObserver = new ResizeObserver(updateDimensions)
      resizeObserver.observe(viewportRef.value)
    }

    motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)')
    applyMotionPreference()
    motionQuery.addEventListener('change', onMotionPreferenceChange)

    lastTime = 0
    animationFrame = window.requestAnimationFrame(animate)
  })

  onUnmounted(() => {
    window.cancelAnimationFrame(animationFrame)
    window.clearTimeout(resumeTimer)
    resizeObserver?.disconnect()
    motionQuery?.removeEventListener('change', onMotionPreferenceChange)

    animationFrame = 0
    resumeTimer = 0
    resizeObserver = null
    motionQuery = null
  })

  return {
    viewportRef,
    renderItems,
    flippedIndexes,
    isDragging,
    prefersReducedMotion,
    viewportStyle,
    stageStyle,
    ringStyle,
    cardStyles,
    onPointerEnter,
    onPointerLeave,
    onPointerDown,
    onPointerMove,
    onPointerUp,
    onPointerCancel,
  }
}
