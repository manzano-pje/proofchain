// =========================================================
// Project.......: ProofChain
// Component.....: CrownCarousel
// File..........: CrownCarousel.ts
// Version.......: 1.0.1
//
// Description...:
// Lógica de Composition API do carrossel cilíndrico 3D.
// Responsividade com diferenciação touch/desktop.
// =========================================================

import { computed, onMounted, onUnmounted, ref, watch, type CSSProperties, type Ref } from 'vue'

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

const CARD_WIDTH = 280
const CARD_HEIGHT = 280
const RADIUS_GAP = 60
const PERSPECTIVE = 1200
const TILT_ANGLE_X = -10
const AUTO_ROTATE_SPEED = 0.2
const DRAG_SENSITIVITY = 1.2
const FLIP_THRESHOLD = 5
const RESUME_DELAY = 300
const MIN_RING_SLOTS = 6
const FLIP_DURATION_MS = 600
const AUTO_CLOSE_DELAY = 5000 // 5 segundos para fechamento automático em touch

function normalizeDegrees(value: number): number {
  const wrapped = value % 360
  return wrapped < 0 ? wrapped + 360 : wrapped
}

function computeRadius(slotCount: number, cardWidth: number, gap: number): number {
  if (slotCount < 2) return cardWidth
  return (cardWidth / 2 + gap / 2) / Math.tan(Math.PI / slotCount)
}

function getDepthOfField(worldAngle: number): { opacity: number; filter: string } {
  const angle = normalizeDegrees(worldAngle)
  const isBack = angle > 90 && angle < 270
  if (!isBack) return { opacity: 1, filter: 'blur(0px)' }
  const rearProximity = 1 - Math.abs(angle - 180) / 90
  return {
    opacity: 1 - rearProximity * 0.7,
    filter: `blur(${rearProximity * 8}px)`,
  }
}

function createRenderItems(items: CarouselItemData[]): RenderCarouselItem[] {
  if (items.length === 0) return []
  const packed: CarouselItemData[] = []
  if (items.length >= MIN_RING_SLOTS) {
    packed.push(...items)
  } else {
    while (packed.length < MIN_RING_SLOTS) {
      const source = items[packed.length % items.length]
      if (!source) break
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

export function useCrownCarousel(items: Ref<CarouselItemData[]>) {
  // ---------- ESTADOS REATIVOS ----------
  const viewportRef = ref<HTMLElement | null>(null)
  const rotationY = ref(0)
  const isInteracting = ref(false)
  const isDragging = ref(false)
  const prefersReducedMotion = ref(false)
  const containerWidth = ref(CARD_WIDTH * 3)
  const flippedIndexes = ref<boolean[]>([])
  const hoverCount = ref(0)

  // ALTERAÇÃO: detecção de touch e timeout automático
  const isTouchDevice = ref(false)
  const autoCloseTimer = ref(0)

  const hasFlippedCard = computed(() => flippedIndexes.value.some(Boolean))

  const pointer = {
    id: 0,
    down: false,
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

  // ---------- FLIP ----------
  const syncFlippedState = (count: number) => {
    if (flippedIndexes.value.length === count) return
    flippedIndexes.value = Array.from({ length: count }, () => false)
  }

  // ALTERAÇÃO: reset também cancela o timer automático
  const resetFlips = () => {
    if (flippedIndexes.value.some(Boolean)) {
      flippedIndexes.value = flippedIndexes.value.map(() => false)
      window.clearTimeout(autoCloseTimer.value)
      autoCloseTimer.value = 0
    }
  }

  // ALTERAÇÃO: toggle com timeout automático para touch
  const toggleFlip = (index: number) => {
    const current = flippedIndexes.value[index]
    if (current) {
      // Desvira
      const next = [...flippedIndexes.value]
      next[index] = false
      flippedIndexes.value = next
      window.clearTimeout(autoCloseTimer.value)
      autoCloseTimer.value = 0
      return
    }
    // Fecha todos e abre o novo
    const next = flippedIndexes.value.map(() => false)
    next[index] = true
    flippedIndexes.value = next

    // Se for touch, agenda fechamento automático
    if (isTouchDevice.value) {
      window.clearTimeout(autoCloseTimer.value)
      autoCloseTimer.value = window.setTimeout(() => {
        resetFlips()
        isInteracting.value = false
        isDragging.value = false
        autoCloseTimer.value = 0
      }, AUTO_CLOSE_DELAY)
    }
  }

  // ---------- AUTO-ROTATE ----------
  const pauseInteraction = () => {
    isInteracting.value = true
    window.clearTimeout(resumeTimer)
    resumeTimer = 0
  }

  // ALTERAÇÃO: bloqueia countdown apenas se for touch E houver card virado
  const beginResumeCountdown = () => {
    if (pointer.down || hoverCount.value > 0 || (isTouchDevice.value && hasFlippedCard.value)) {
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

  const incrementHover = () => {
    hoverCount.value++
    if (hoverCount.value > 0) pauseInteraction()
  }

  const decrementHover = () => {
    hoverCount.value = Math.max(0, hoverCount.value - 1)
    if (hoverCount.value === 0 && !pointer.down) {
      beginResumeCountdown()
    }
  }

  // ---------- PREFERS-REDUCED-MOTION ----------
  const applyMotionPreference = () => {
    prefersReducedMotion.value = motionQuery?.matches ?? false
    if (prefersReducedMotion.value) isInteracting.value = false
  }

  const onMotionPreferenceChange = () => applyMotionPreference()

  const updateDimensions = () => {
    if (!viewportRef.value) return
    containerWidth.value = viewportRef.value.clientWidth
  }

  const animate = (time: number) => {
    if (!lastTime) lastTime = time
    const delta = Math.min(time - lastTime, 32)
    lastTime = time
    const canAutoRotate = !prefersReducedMotion.value && !isInteracting.value
    if (canAutoRotate) {
      rotationY.value += AUTO_ROTATE_SPEED * (delta / (1000 / 60))
    }
    animationFrame = window.requestAnimationFrame(animate)
  }

  // ---------- POINTER EVENTS ----------
  const resolveCardIndex = (event: PointerEvent): number => {
    const target = event.target
    if (!(target instanceof Element)) return -1
    const card = target.closest<HTMLElement>('[data-crown-index]')
    const rawIndex = card?.dataset.crownIndex
    if (!rawIndex) return -1
    const index = Number.parseInt(rawIndex, 10)
    return Number.isFinite(index) ? index : -1
  }

  // ALTERAÇÃO: cancela timeout automático ao iniciar interação
  const onPointerDown = (event: PointerEvent) => {
    if (!viewportRef.value) return
    const cardIndex = resolveCardIndex(event)
    if (cardIndex < 0) return

    pointer.down = true
    pointer.id = event.pointerId
    pointer.startX = event.clientX
    pointer.startY = event.clientY
    pointer.lastX = event.clientX
    pointer.cardIndex = cardIndex
    pointer.exceededThreshold = false
    isDragging.value = false

    window.clearTimeout(autoCloseTimer.value)
    autoCloseTimer.value = 0

    pauseInteraction()
    viewportRef.value.setPointerCapture?.(event.pointerId)
  }

  const onPointerMove = (event: PointerEvent) => {
    if (!pointer.down) return
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
    if (!pointer.down) return

    const distance = pointerDistance(pointer.startX, pointer.startY, event.clientX, event.clientY)
    const isClick = distance <= FLIP_THRESHOLD && !pointer.exceededThreshold

    if (isClick) {
      if (pointer.cardIndex >= 0) {
        toggleFlip(pointer.cardIndex)
      } else if (hasFlippedCard.value) {
        // Clique fora: fecha todos e libera imediatamente
        resetFlips()
        isInteracting.value = false
        window.clearTimeout(resumeTimer)
        resumeTimer = 0
      }
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

  // ---------- CICLO DE VIDA ----------
  onMounted(() => {
    updateDimensions()

    if (viewportRef.value) {
      resizeObserver = new ResizeObserver(updateDimensions)
      resizeObserver.observe(viewportRef.value)
    }

    motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)')
    applyMotionPreference()
    motionQuery.addEventListener('change', onMotionPreferenceChange)

    // ALTERAÇÃO: detecta se é dispositivo touch
    isTouchDevice.value = window.matchMedia('(pointer: coarse)').matches

    lastTime = 0
    animationFrame = window.requestAnimationFrame(animate)
  })

  onUnmounted(() => {
    window.cancelAnimationFrame(animationFrame)
    window.clearTimeout(resumeTimer)
    window.clearTimeout(autoCloseTimer.value)
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
    onPointerDown,
    onPointerMove,
    onPointerUp,
    onPointerCancel,
    incrementHover,
    decrementHover,
  }
}
