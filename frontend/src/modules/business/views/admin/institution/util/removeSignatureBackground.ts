const MAX_IMAGE_PIXELS = 16_000_000
const BACKGROUND_START_LUMINANCE = 140
const BACKGROUND_END_LUMINANCE = 230
const SIGNATURE_DARKENING = 0.62
const BACKGROUND_FADE_POWER = 2.5

/** Reduz o fundo claro a transparência e escurece os traços da assinatura. */
export function removeLightBackground(pixels: Uint8ClampedArray): void {
  for (let index = 0; index < pixels.length; index += 4) {
    const red = pixels[index] ?? 0
    const green = pixels[index + 1] ?? 0
    const blue = pixels[index + 2] ?? 0
    const luminance = red * 0.2126 + green * 0.7152 + blue * 0.0722
    const progress = Math.min(
      1,
      Math.max(
        0,
        (luminance - BACKGROUND_START_LUMINANCE) /
          (BACKGROUND_END_LUMINANCE - BACKGROUND_START_LUMINANCE),
      ),
    )
    const smoothProgress = progress * progress * (3 - 2 * progress)
    const inkOpacity = Math.pow(1 - smoothProgress, BACKGROUND_FADE_POWER)

    pixels[index] = Math.round(red * SIGNATURE_DARKENING)
    pixels[index + 1] = Math.round(green * SIGNATURE_DARKENING)
    pixels[index + 2] = Math.round(blue * SIGNATURE_DARKENING)
    pixels[index + 3] = Math.round((pixels[index + 3] ?? 0) * inkOpacity)
  }
}

/**
 * Processa uma imagem rasterizada localmente, sem enviar os dados a serviços externos.
 * As dimensões são limitadas para evitar alocações excessivas no Canvas.
 */
export async function removeSignatureBackground(file: File): Promise<File> {
  let bitmap: ImageBitmap | null = null

  try {
    bitmap = await createImageBitmap(file)

    if (bitmap.width * bitmap.height > MAX_IMAGE_PIXELS) {
      throw new Error('A imagem excede o limite de 16 megapixels para processamento.')
    }

    const canvas = document.createElement('canvas')
    canvas.width = bitmap.width
    canvas.height = bitmap.height

    const context = canvas.getContext('2d', { willReadFrequently: true })
    if (!context) {
      throw new Error('Não foi possível iniciar o processamento da assinatura.')
    }

    context.drawImage(bitmap, 0, 0)
    const imageData = context.getImageData(0, 0, bitmap.width, bitmap.height)
    removeLightBackground(imageData.data)
    context.putImageData(imageData, 0, 0)

    const blob = await new Promise<Blob | null>((resolve) =>
      canvas.toBlob(resolve, 'image/png'),
    )
    if (!blob) {
      throw new Error('Não foi possível gerar a assinatura com fundo transparente.')
    }

    return new File([blob], 'responsible-signature-processed.png', { type: 'image/png' })
  } finally {
    bitmap?.close()
  }
}
