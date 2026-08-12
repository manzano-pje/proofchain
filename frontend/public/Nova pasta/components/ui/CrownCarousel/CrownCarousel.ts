import {
  computed,
  onMounted,
  onUnmounted,
  ref,
  type CSSProperties,
  type Ref,
} from 'vue'

export interface CrownCarouselItem {
  id: string
  image: string
  imageAlt?: string
  title: string
  description: string
}

interface CrownCarouselOptions {
  items: CrownCarouselItem[]
  autoplay: boolean
  autoplaySpeed: number
  tilt: number
}

export function useCrownCarousel(
  rootRef: Ref<HTMLElement | null>,
  options: CrownCarouselOptions,
) {
  const activeIndex = ref(0)
  const rotation = ref(0)
  const isDragging = ref(false)

  const pointer = {
    x: 0,
    lastX: 0,
    velocity: 0,
  }

  let animationFrame = 0
  let lastTime = 0
  let inertiaVelocity = 0

  const normalizeAngle = (angle: number) => {
    const normalized = angle % (Math.PI * 2)
    return normalized < 0 ? normalized + Math.PI * 2 : normalized
  }

  const getCardAngle = (index: number) => {
    const count = options.items.length || 1
    return (
      rotation.value +
      (index / count) * Math.PI * 2
    )
  }

  const getDepth = (angle: number) => {
    return (Math.sin(angle) + 1) / 2
  }

  const getNearestIndex = () => {
    if (!options.items.length) return 0

    let nearest = 0
    let nearestDistance = Infinity

    options.items.forEach((_, index) => {
      const angle = normalizeAngle(getCardAngle(index) - Math.PI / 2)
      const distance = Math.min(
        angle,
        Math.PI * 2 - angle,
      )

      if (distance < nearestDistance) {
        nearestDistance = distance
        nearest = index
      }
    })

    return nearest
  }

  const activeProgress = computed(() => {
    return getNearestIndex()
  })

  const cardStyles = computed<CSSProperties[]>(() => {
    const count = options.items.length || 1

    return options.items.map((_, index) => {
      const angle = getCardAngle(index)

      const x = Math.cos(angle) * 300
      const y = Math.sin(angle) * 155

      const depth = getDepth(angle)

      const scale = 0.78 + depth * 0.27
      const opacity = 0.48 + depth * 0.52

      const zIndex = Math.round(depth * 100)

      const cardRotation =
        Math.sin(angle) * -7

      return {
        transform: `
          translate3d(${x}px, ${y}px, 0)
          rotate(${cardRotation}deg)
          scale(${scale})
          rotateX(${options.tilt * 0.04}deg)
        `,
        opacity,
        zIndex,
        '--card-index': index,
        '--card-count': count,
      } as CSSProperties
    })
  })

  const updateActive = () => {
    activeIndex.value = activeProgress.value
  }

  const animate = (time: number) => {
    if (!lastTime) lastTime = time

    const delta = Math.min(time - lastTime, 32)
    lastTime = time

    if (!isDragging.value) {
      if (Math.abs(inertiaVelocity) > 0.00001) {
        rotation.value += inertiaVelocity * delta
        inertiaVelocity *= Math.pow(0.94, delta / 16)
      } else if (options.autoplay) {
        rotation.value += options.autoplaySpeed * delta
      }
    }

    updateActive()

    animationFrame = requestAnimationFrame(animate)
  }

  const selectItem = (index: number) => {
    if (!options.items.length) return

    const count = options.items.length
    const target =
      Math.PI / 2 -
      (index / count) * Math.PI * 2

    let difference = target - rotation.value

    while (difference > Math.PI) difference -= Math.PI * 2
    while (difference < -Math.PI) difference += Math.PI * 2

    rotation.value += difference * 0.35
    activeIndex.value = index
  }

  const onPointerDown = (event: PointerEvent) => {
    if (!rootRef.value) return

    isDragging.value = true
    pointer.x = event.clientX
    pointer.lastX = event.clientX
    pointer.velocity = 0
    inertiaVelocity = 0

    rootRef.value.setPointerCapture?.(event.pointerId)
  }

  const onPointerMove = (event: PointerEvent) => {
    if (!isDragging.value) return

    const deltaX = event.clientX - pointer.lastX

    pointer.velocity = deltaX * 0.0025
    rotation.value += deltaX * 0.0025

    pointer.lastX = event.clientX
  }

  const onPointerUp = (event?: PointerEvent) => {
    if (!isDragging.value) return

    isDragging.value = false
    inertiaVelocity = pointer.velocity

    if (event && rootRef.value?.hasPointerCapture(event.pointerId)) {
      rootRef.value.releasePointerCapture(event.pointerId)
    }
  }

  const onPointerCancel = (event: PointerEvent) => {
    onPointerUp(event)
  }

  onMounted(() => {
    animationFrame = requestAnimationFrame(animate)
  })

  onUnmounted(() => {
    cancelAnimationFrame(animationFrame)
  })

  return {
    activeIndex,
    cardStyles,
    isDragging,
    onPointerDown,
    onPointerMove,
    onPointerUp,
    onPointerCancel,
    selectItem,
  }
}
