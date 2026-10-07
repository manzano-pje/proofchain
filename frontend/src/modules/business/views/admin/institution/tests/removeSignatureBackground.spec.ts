import { describe, expect, it } from 'vitest'

import { removeLightBackground } from '../util/removeSignatureBackground'

describe('removeLightBackground', () => {
  it('preserves and darkens black signature strokes', () => {
    const pixels = new Uint8ClampedArray([20, 20, 20, 255])

    removeLightBackground(pixels)

    expect(Array.from(pixels)).toEqual([12, 12, 12, 255])
  })

  it('makes white paper transparent', () => {
    const pixels = new Uint8ClampedArray([255, 255, 255, 255])

    removeLightBackground(pixels)

    expect(pixels[3]).toBe(0)
  })

  it('fades gray pixels gradually to soften background and stroke edges', () => {
    const pixels = new Uint8ClampedArray([185, 185, 185, 255])

    removeLightBackground(pixels)

    expect(pixels[3]).toBeGreaterThan(0)
    expect(pixels[3]).toBeLessThan(255)
  })
})
