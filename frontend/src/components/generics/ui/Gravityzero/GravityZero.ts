import { onMounted, onUnmounted, ref } from 'vue'

export interface GravityParticle {
  x: number
  y: number
  baseX: number
  baseY: number
  vx: number
  vy: number
  radius: number
  alpha: number
  phase: number
}

export interface GravityZeroOptions {
  particleCount?: number
  mouseRadius?: number
  mouseForce?: number
  connectionDistance?: number
  breathingDuration?: number
}

const DEFAULT_OPTIONS: Required<GravityZeroOptions> = {
  particleCount: 95,
  mouseRadius: 125,
  mouseForce: 0.035,
  connectionDistance: 105,
  breathingDuration: 8000,
}

export function useGravityZero(
  canvasRef: ReturnType<typeof ref<HTMLCanvasElement | null>>,
  options: GravityZeroOptions = {},
) {
  const config = {
    ...DEFAULT_OPTIONS,
    ...options,
  }

  const particles: GravityParticle[] = []

  const mouse = {
    x: 0,
    y: 0,
    active: false,
  }

  let canvas: HTMLCanvasElement | null = null
  let context: CanvasRenderingContext2D | null = null

  let animationFrame = 0
  let resizeObserver: ResizeObserver | null = null

  let width = 0
  let height = 0
  let dpr = 1
  let breathingStart = 0

  const reducedMotion = ref(false)

  const random = (min: number, max: number): number => {
    return Math.random() * (max - min) + min
  }

  const createParticle = (): GravityParticle => {
    const x = random(0, width)
    const y = random(0, height)

    return {
      x,
      y,
      baseX: x,
      baseY: y,
      vx: random(-0.08, 0.08),
      vy: random(-0.08, 0.08),
      radius: random(0.8, 1.8),
      alpha: random(0.35, 0.85),
      phase: random(0, Math.PI * 2),
    }
  }

  const createParticles = (): void => {
    particles.length = 0

    for (let index = 0; index < config.particleCount; index += 1) {
      particles.push(createParticle())
    }
  }

  const resize = (): void => {
    if (!canvas) {
      return
    }

    const rect = canvas.getBoundingClientRect()

    width = Math.max(1, rect.width)
    height = Math.max(1, rect.height)

    dpr = Math.min(window.devicePixelRatio || 1, 2)

    canvas.width = Math.round(width * dpr)
    canvas.height = Math.round(height * dpr)

    context?.setTransform(dpr, 0, 0, dpr, 0, 0)

    createParticles()
  }

  const breathingScale = (time: number): number => {
    const elapsed = (time - breathingStart) % config.breathingDuration

    const progress = elapsed / config.breathingDuration

    /*
     * 4 segundos expandindo
     * 4 segundos contraindo
     */
    return 0.86 + ((1 - Math.cos(progress * Math.PI * 2)) / 2) * 0.14
  }

  const updateParticle = (particle: GravityParticle, time: number, scale: number): void => {
    const targetX = width / 2 + (particle.baseX - width / 2) * scale

    const targetY = height / 2 + (particle.baseY - height / 2) * scale

    particle.vx += (targetX - particle.x) * 0.0009

    particle.vy += (targetY - particle.y) * 0.0009

    /*
     * Repulsão suave do cursor.
     */
    if (mouse.active) {
      const dx = particle.x - mouse.x
      const dy = particle.y - mouse.y

      const distance = Math.sqrt(dx * dx + dy * dy)

      if (distance > 0 && distance < config.mouseRadius) {
        const influence = 1 - distance / config.mouseRadius

        const force = influence * influence * config.mouseForce

        particle.vx += (dx / distance) * force

        particle.vy += (dy / distance) * force
      }
    }

    /*
     * Movimento orgânico extremamente sutil.
     */
    particle.vx += Math.cos(time * 0.00025 + particle.phase) * 0.0007

    particle.vy += Math.sin(time * 0.00022 + particle.phase) * 0.0007

    particle.vx *= 0.985
    particle.vy *= 0.985

    particle.x += particle.vx
    particle.y += particle.vy
  }

  const drawParticle = (particle: GravityParticle): void => {
    if (!context) {
      return
    }

    context.beginPath()

    context.arc(particle.x, particle.y, particle.radius, 0, Math.PI * 2)

    context.fillStyle = `rgba(103, 232, 249, ${particle.alpha})`

    context.fill()
  }

  const drawConnections = (): void => {
    if (!context) {
      return
    }

    for (let i = 0; i < particles.length; i += 1) {
      const first = particles[i]

      if (!first) {
        continue
      }

      for (let j = i + 1; j < particles.length; j += 1) {
        const second = particles[j]

        if (!second) {
          continue
        }

        const dx = first.x - second.x
        const dy = first.y - second.y

        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance >= config.connectionDistance) {
          continue
        }

        const opacity = (1 - distance / config.connectionDistance) * 0.16

        context.beginPath()

        context.moveTo(first.x, first.y)

        context.lineTo(second.x, second.y)

        context.strokeStyle = `rgba(103, 232, 249, ${opacity})`

        context.lineWidth = 0.55

        context.stroke()
      }
    }
  }

  const render = (time: number): void => {
    if (!context || !canvas) {
      return
    }

    const scale = reducedMotion.value ? 1 : breathingScale(time)

    context.clearRect(0, 0, width, height)

    for (const particle of particles) {
      if (!reducedMotion.value) {
        updateParticle(particle, time, scale)
      } else {
        particle.x += (particle.baseX - particle.x) * 0.04

        particle.y += (particle.baseY - particle.y) * 0.04
      }

      drawParticle(particle)
    }

    drawConnections()

    animationFrame = window.requestAnimationFrame(render)
  }

  const setPointer = (event: PointerEvent): void => {
    if (!canvas) {
      return
    }

    const rect = canvas.getBoundingClientRect()

    mouse.x = event.clientX - rect.left

    mouse.y = event.clientY - rect.top

    mouse.active = true
  }

  const clearPointer = (): void => {
    mouse.active = false
  }

  const start = (): void => {
    canvas = canvasRef.value ?? null

    if (!canvas) {
      return
    }

    context = canvas.getContext('2d', {
      alpha: true,
    })

    if (!context) {
      return
    }

    reducedMotion.value = window.matchMedia('(prefers-reduced-motion: reduce)').matches

    breathingStart = performance.now()

    resize()

    canvas.addEventListener('pointermove', setPointer, { passive: true })

    canvas.addEventListener('pointerleave', clearPointer)

    canvas.addEventListener('pointercancel', clearPointer)

    resizeObserver = new ResizeObserver(resize)

    resizeObserver.observe(canvas)

    animationFrame = window.requestAnimationFrame(render)
  }

  const stop = (): void => {
    window.cancelAnimationFrame(animationFrame)

    canvas?.removeEventListener('pointermove', setPointer)

    canvas?.removeEventListener('pointerleave', clearPointer)

    canvas?.removeEventListener('pointercancel', clearPointer)

    resizeObserver?.disconnect()

    resizeObserver = null
    context = null
    canvas = null
  }

  onMounted(start)
  onUnmounted(stop)

  return {
    reducedMotion,
  }
}
