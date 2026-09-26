import { computed, onBeforeUnmount, reactive, ref, toValue, type MaybeRefOrGetter } from 'vue'

/** Elemento `<img>` mantido fora do sistema reativo para export via canvas. */
interface LoadedImage {
  url: string
  width: number
  height: number
  element: HTMLImageElement
  revokeOnCleanup: boolean
}

export interface CropViewport {
  width: number
  height: number
}

/**
 * Composable de enquadramento (pan + zoom) e export de imagem via Canvas API.
 *
 * - Não depende de bibliotecas externas.
 * - O viewport é alimentado pelo componente consumidor (via ResizeObserver),
 *   pois a proporção da área de crop é responsabilidade do componente.
 * - A exportação gera um `File` PNG na largura/altura solicitada.
 */
export function useImageCrop() {
  const viewport = reactive<CropViewport>({ width: 0, height: 0 })
  const image = ref<Omit<LoadedImage, 'element'> | null>(null)
  const element = ref<HTMLImageElement | null>(null)
  const pan = reactive({ x: 0, y: 0 })
  const scale = ref(1)

  const minScale = computed(() => {
    if (!image.value || !viewport.width || !viewport.height) return 1
    return Math.max(viewport.width / image.value.width, viewport.height / image.value.height)
  })

  const maxScale = computed(() => minScale.value * 4)

  function clampPan(): void {
    if (!image.value) return
    const renderedWidth = image.value.width * scale.value
    const renderedHeight = image.value.height * scale.value
    const minX = viewport.width - renderedWidth
    const minY = viewport.height - renderedHeight
    pan.x = Math.min(0, Math.max(minX, pan.x))
    pan.y = Math.min(0, Math.max(minY, pan.y))
  }

  function fitAndCenter(): void {
    if (!image.value) return
    scale.value = minScale.value
    pan.x = (viewport.width - image.value.width * scale.value) / 2
    pan.y = (viewport.height - image.value.height * scale.value) / 2
    clampPan()
  }

  function setViewport(width: number, height: number): void {
    viewport.width = width
    viewport.height = height
    if (image.value) fitAndCenter()
  }

  function panBy(dx: number, dy: number): void {
    if (!image.value) return
    pan.x += dx
    pan.y += dy
    clampPan()
  }

  function setScale(next: number): void {
    if (!image.value) return
    const clamped = Math.min(maxScale.value, Math.max(minScale.value, next))
    if (clamped === scale.value) return
    // Zoom centrado na viewport
    const cx = viewport.width / 2
    const cy = viewport.height / 2
    const ratio = clamped / scale.value
    pan.x = cx - (cx - pan.x) * ratio
    pan.y = cy - (cy - pan.y) * ratio
    scale.value = clamped
    clampPan()
  }

  function cleanupImage(): void {
    if (image.value?.revokeOnCleanup && image.value.url.startsWith('blob:')) {
      URL.revokeObjectURL(image.value.url)
    }
    image.value = null
    element.value = null
  }

  async function loadFromUrl(url: string, revokeOnCleanup = true): Promise<void> {
    const loaded = await new Promise<HTMLImageElement>((resolve, reject) => {
      const el = new Image()
      el.onload = () => resolve(el)
      el.onerror = () => reject(new Error('Não foi possível carregar a imagem.'))
      el.src = url
    })
    cleanupImage()
    image.value = {
      url,
      width: loaded.naturalWidth,
      height: loaded.naturalHeight,
      revokeOnCleanup,
    }
    element.value = loaded
    fitAndCenter()
  }

  /** Exporta a região visível atual como `File` PNG. */
  async function exportCropped(target: { width: number; height: number }): Promise<File | null> {
    if (!element.value || !image.value) return null
    const canvas = document.createElement('canvas')
    canvas.width = target.width
    canvas.height = target.height
    const ctx = canvas.getContext('2d')
    if (!ctx) return null

    const sx = -pan.x / scale.value
    const sy = -pan.y / scale.value
    const sw = viewport.width / scale.value
    const sh = viewport.height / scale.value

    ctx.imageSmoothingEnabled = true
    ctx.imageSmoothingQuality = 'high'
    ctx.drawImage(element.value, sx, sy, sw, sh, 0, 0, target.width, target.height)

    const blob = await new Promise<Blob | null>((resolve) =>
      canvas.toBlob((value) => resolve(value), 'image/png', 0.95),
    )
    if (!blob) return null
    return new File([blob], 'institution-logo.png', { type: 'image/png' })
  }

  function reset(): void {
    cleanupImage()
    pan.x = 0
    pan.y = 0
    scale.value = 1
  }

  onBeforeUnmount(reset)

  return {
    viewport,
    image,
    pan,
    scale,
    minScale,
    maxScale,
    setViewport,
    panBy,
    setScale,
    loadFromUrl,
    exportCropped,
    reset,
  }
}

// Reexport utilitário para permitir `MaybeRefOrGetter` quando o componente
// quiser tornar a proporção configurável no futuro.
export type { MaybeRefOrGetter }
export { toValue }
