 <script setup lang="ts">
/**
 * Uploader reutilizável para imagens da instituição.
 *
 * Fluxo de estados:
 *   idle     → área de upload (clique ou arrastar/soltar)
 *   cropping → enquadramento com pan + zoom
 *   ready    → pré-visualização do arquivo final + alterar/remover
 *
 * As dimensões, o texto e o nome do arquivo final são configuráveis pelo consumidor.
 */
import { computed, onBeforeUnmount, ref, watch } from 'vue'

import { useImageCrop } from './util/useImageCrop'
import { removeSignatureBackground } from './util/removeSignatureBackground'

interface Props {
  /** Texto usado no título, nas instruções e nos atributos acessíveis. */
  title?: string
  /** Largura final do arquivo exportado, em px. */
  outputWidth?: number
  /** Altura final do arquivo exportado, em px. */
  outputHeight?: number
  /** Proporção opcional da área de crop; por padrão deriva das dimensões finais. */
  aspectRatio?: number
  /** Nome do arquivo PNG final. */
  outputFilename?: string
  /** Remove o fundo claro e reforça os traços antes do corte. */
  removeWhiteBackground?: boolean
  /** Tamanho máximo do arquivo original, em bytes. */
  maxBytes?: number
  /** MIME types aceitos. */
  acceptedMimeTypes?: readonly string[]
  /** Extensões aceitas (para fallback quando `File.type` vem vazio). */
  acceptedExtensions?: readonly string[]
}

const props = withDefaults(defineProps<Props>(), {
  title: 'Imagem da instituição',
  outputWidth: 1300,
  outputHeight: 472,
  outputFilename: 'institution-image.png',
  removeWhiteBackground: false,
  maxBytes: 2 * 1024 * 1024,
  acceptedMimeTypes: () => ['image/png', 'image/jpeg', 'image/svg+xml'] as const,
  acceptedExtensions: () => ['.png', '.jpg', '.jpeg', '.svg'] as const,
})

const emit = defineEmits<{
  remove: []
}>()

/** Arquivo final cortado. `null` quando não há logo nova selecionada. */
const model = defineModel<File | null>({ default: null })

type UploaderState = 'idle' | 'processing' | 'cropping' | 'ready'

const state = ref<UploaderState>('idle')
const errorMessage = ref<string | null>(null)
const isDragging = ref(false)
const previewUrl = ref<string | null>(null)

const fileInputRef = ref<HTMLInputElement | null>(null)
const cropStageEl = ref<HTMLElement | null>(null)
const cropViewportEl = ref<HTMLElement | null>(null)
let resizeObserver: ResizeObserver | null = null

const crop = useImageCrop()
const cropAspectRatio = computed(() => props.aspectRatio ?? props.outputWidth / props.outputHeight)

const acceptAttr = computed(() =>
  [...props.acceptedMimeTypes, ...props.acceptedExtensions].join(','),
)

const acceptedFormatLabels = computed(() =>
  props.acceptedMimeTypes
    .map((mime) =>
      mime === 'image/png' ? 'PNG'
      : mime === 'image/jpeg' ? 'JPG'
      : mime === 'image/svg+xml' ? 'SVG'
      : mime,
    )
    .join(', '),
)

const formatHint = computed(() => {
  const mb = (props.maxBytes / (1024 * 1024)).toFixed(0)
  return `${acceptedFormatLabels.value} • Máximo ${mb} MB • Saída ${props.outputWidth} × ${props.outputHeight} px`
})

const cropImageStyle = computed(() => ({
  transform: `translate3d(${crop.pan.x}px, ${crop.pan.y}px, 0) scale(${crop.scale.value})`,
  transformOrigin: '0 0',
  width: crop.image.value ? `${crop.image.value.width}px` : '0',
  height: crop.image.value ? `${crop.image.value.height}px` : '0',
}))

function updateCropViewport(width: number, height: number): void {
  const viewportEl = cropViewportEl.value
  const aspectRatio = cropAspectRatio.value
  if (!viewportEl || width <= 0 || height <= 0 || !Number.isFinite(aspectRatio) || aspectRatio <= 0) {
    return
  }

  const viewportStyle = getComputedStyle(viewportEl)
  const borderWidth =
    Number.parseFloat(viewportStyle.borderLeftWidth) +
    Number.parseFloat(viewportStyle.borderRightWidth)
  const borderHeight =
    Number.parseFloat(viewportStyle.borderTopWidth) +
    Number.parseFloat(viewportStyle.borderBottomWidth)
  const availableWidth = Math.max(0, width - borderWidth)
  const availableHeight = Math.max(0, height - borderHeight)

  let viewportWidth = availableWidth
  let viewportHeight = viewportWidth / aspectRatio
  if (viewportHeight > availableHeight) {
    viewportHeight = availableHeight
    viewportWidth = viewportHeight * aspectRatio
  }

  crop.setViewport(viewportWidth, viewportHeight)
}

function updateCropViewportFromStage(): void {
  const stage = cropStageEl.value
  if (!stage) return
  updateCropViewport(stage.clientWidth, stage.clientHeight)
}

watch(cropStageEl, (el) => {
  resizeObserver?.disconnect()
  resizeObserver = null
  if (!el) return
  resizeObserver = new ResizeObserver((entries) => {
    const entry = entries[0]
    if (!entry) return
    updateCropViewport(entry.contentRect.width, entry.contentRect.height)
  })
  resizeObserver.observe(el)
  updateCropViewportFromStage()
})

watch(cropAspectRatio, updateCropViewportFromStage)

onBeforeUnmount(() => {
  resizeObserver?.disconnect()
  resizeObserver = null
  setPreviewUrl(null)
})

function setPreviewUrl(file: File | null): void {
  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
  previewUrl.value = file ? URL.createObjectURL(file) : null
}

function openFilePicker(): void {
  errorMessage.value = null
  fileInputRef.value?.click()
}

function onDragOver(): void {
  isDragging.value = true
}

function onDragLeave(): void {
  isDragging.value = false
}

function onDrop(event: DragEvent): void {
  isDragging.value = false
  const file = event.dataTransfer?.files?.[0]
  if (file) void handleFile(file)
}

function onFileChange(event: Event): void {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  target.value = ''
  if (file) void handleFile(file)
}

function isAcceptedFile(file: File): boolean {
  if (props.acceptedMimeTypes.includes(file.type)) return true
  const dot = file.name.lastIndexOf('.')
  if (dot < 0) return false
  const ext = file.name.slice(dot).toLowerCase()
  return props.acceptedExtensions.includes(ext)
}

async function handleFile(file: File): Promise<void> {
  errorMessage.value = null

  if (!isAcceptedFile(file)) {
    errorMessage.value = `Formato inválido. Envie: ${acceptedFormatLabels.value}.`
    return
  }

  if (file.size > props.maxBytes) {
    const mb = (props.maxBytes / (1024 * 1024)).toFixed(0)
    errorMessage.value = `A imagem deve possuir no máximo ${mb} MB.`
    return
  }

  try {
    if (props.removeWhiteBackground) {
      state.value = 'processing'
      await new Promise<void>((resolve) => requestAnimationFrame(() => resolve()))
    }

    const imageFile = props.removeWhiteBackground
      ? await removeSignatureBackground(file)
      : file
    const url = URL.createObjectURL(imageFile)
    try {
      await crop.loadFromUrl(url, true)
    } catch (error) {
      URL.revokeObjectURL(url)
      throw error
    }
    state.value = 'cropping'
  } catch (error) {
    state.value = 'idle'
    errorMessage.value = error instanceof Error
      ? error.message
      : 'Não foi possível processar a imagem selecionada.'
  }
}

function onPointerDown(event: PointerEvent): void {
  if (state.value !== 'cropping') return
  const target = event.currentTarget as HTMLElement
  target.setPointerCapture(event.pointerId)
  lastPointer = { x: event.clientX, y: event.clientY }
}

let lastPointer: { x: number; y: number } | null = null

function onPointerMove(event: PointerEvent): void {
  if (!lastPointer) return
  crop.panBy(event.clientX - lastPointer.x, event.clientY - lastPointer.y)
  lastPointer = { x: event.clientX, y: event.clientY }
}

function onPointerUp(event: PointerEvent): void {
  const target = event.currentTarget as HTMLElement
  if (target.hasPointerCapture(event.pointerId)) {
    target.releasePointerCapture(event.pointerId)
  }
  lastPointer = null
}

function onWheel(event: WheelEvent): void {
  const factor = event.deltaY < 0 ? 1.1 : 1 / 1.1
  crop.setScale(crop.scale.value * factor)
}

function onZoomInput(event: Event): void {
  const target = event.target as HTMLInputElement
  crop.setScale(Number(target.value))
}

function onKeydown(event: KeyboardEvent): void {
  const step = 20
  switch (event.key) {
    case 'ArrowLeft':
      crop.panBy(-step, 0)
      break
    case 'ArrowRight':
      crop.panBy(step, 0)
      break
    case 'ArrowUp':
      crop.panBy(0, -step)
      break
    case 'ArrowDown':
      crop.panBy(0, step)
      break
    default:
      return
  }
  event.preventDefault()
}

async function confirmCrop(): Promise<void> {
  const file = await crop.exportCropped(
    { width: props.outputWidth, height: props.outputHeight },
    props.outputFilename,
  )
  if (!file) {
    errorMessage.value = 'Não foi possível gerar a imagem final.'
    return
  }
  model.value = file
  setPreviewUrl(file)
  state.value = 'ready'
}

function cancelCrop(): void {
  crop.reset()
  state.value = 'idle'
}

function restart(): void {
  errorMessage.value = null
  openFilePicker()
}

function removeLogo(): void {
  crop.reset()
  model.value = null
  setPreviewUrl(null)
  state.value = 'idle'
  emit('remove')
}
</script>

<template>
  <div class="institution-logo">
    <input
      ref="fileInputRef"
      class="institution-logo__input"
      type="file"
      :accept="acceptAttr"
      tabindex="-1"
      aria-hidden="true"
      @change="onFileChange"
    />

    <!-- Estado: idle -->
    <div
      v-if="state === 'idle'"
      class="institution-logo__dropzone"
      :class="{ 'institution-logo__dropzone--drag': isDragging }"
      role="button"
      tabindex="0"
      :aria-label="`Selecionar ${props.title}. ${formatHint}`"
      @click="openFilePicker"
      @keydown.enter.prevent="openFilePicker"
      @keydown.space.prevent="openFilePicker"
      @dragover.prevent="onDragOver"
      @dragleave.prevent="onDragLeave"
      @drop.prevent="onDrop"
    >
      <span class="institution-logo__dropzone-title">{{ props.title }}</span>
      <span class="institution-logo__dropzone-subtitle">Arraste uma imagem aqui ou clique para selecionar</span>
      <span class="institution-logo__dropzone-hint">{{ formatHint }}</span>
    </div>

    <!-- Estado: processing -->
    <p v-else-if="state === 'processing'" class="institution-logo__status" role="status">
      Removendo o fundo claro e reforçando a assinatura...
    </p>

    <!-- Estado: ready -->
    <div
      v-else-if="state === 'ready'"
      class="institution-logo__preview"
      :class="{ 'institution-logo__preview--transparent': props.removeWhiteBackground }"
    >
      <img
        v-if="previewUrl"
        class="institution-logo__preview-image"
        :class="{ 'institution-logo__preview-image--transparent': props.removeWhiteBackground }"
        :src="previewUrl"
        :alt="`Pré-visualização de ${props.title}`"
      />
      <div class="institution-logo__preview-actions">
        <button type="button" class="institution-logo__btn institution-logo__btn--primary" @click="restart">
          Alterar imagem
        </button>
        <button
          type="button"
          class="institution-logo__btn"
          @click="removeLogo"
        >
          Remover
        </button>
      </div>
    </div>

    <Teleport to="body">
      <div
        v-if="state === 'cropping'"
        class="institution-logo__modal-overlay"
        role="presentation"
        @click.self="cancelCrop"
        @keydown.esc.stop.prevent="cancelCrop"
      >
        <section
          class="institution-logo__crop-modal"
          role="dialog"
          aria-modal="true"
          aria-labelledby="institution-logo-crop-title"
          aria-describedby="institution-logo-crop-instructions"
        >
          <header class="institution-logo__crop-modal-header">
            <div>
              <h2 id="institution-logo-crop-title">Ajustar {{ props.title }}</h2>
              <p id="institution-logo-crop-instructions">
                Arraste para posicionar, use a roda do mouse ou o controle para ampliar.
              </p>
            </div>
            <button
              type="button"
              class="institution-logo__modal-close"
              aria-label="Cancelar corte e fechar"
              @click="cancelCrop"
            >
              ×
            </button>
          </header>

          <div ref="cropStageEl" class="institution-logo__crop-stage">
            <div
              ref="cropViewportEl"
              class="institution-logo__crop-viewport"
              :class="{ 'institution-logo__crop-viewport--transparent': props.removeWhiteBackground }"
              :style="{ width: `${crop.viewport.width}px`, height: `${crop.viewport.height}px` }"
              role="application"
              tabindex="0"
              :aria-label="`Área de enquadramento de ${props.title}. Use as setas do teclado para mover e o controle deslizante para ampliar.`"
              @pointerdown="onPointerDown"
              @pointermove="onPointerMove"
              @pointerup="onPointerUp"
              @pointercancel="onPointerUp"
              @wheel.prevent="onWheel"
              @keydown="onKeydown"
            >
              <img
                v-if="crop.image.value"
                class="institution-logo__crop-image"
                :src="crop.image.value.url"
                :style="cropImageStyle"
                alt=""
                draggable="false"
              />
              <span class="institution-logo__crop-frame" aria-hidden="true"></span>
            </div>
          </div>

          <div class="institution-logo__crop-modal-controls">
            <label class="institution-logo__zoom">
              <span class="institution-logo__zoom-label">Zoom</span>
              <input
                class="institution-logo__zoom-range"
                type="range"
                :min="crop.minScale.value"
                :max="crop.maxScale.value"
                :step="0.001"
                :value="crop.scale.value"
                @input="onZoomInput"
              />
            </label>
            <div class="institution-logo__crop-actions">
              <button type="button" class="institution-logo__btn institution-logo__btn--secondary" @click="cancelCrop">
                Cancelar
              </button>
              <button
                type="button"
                class="institution-logo__btn institution-logo__btn--primary"
                @click="confirmCrop"
              >
                Confirmar corte
              </button>
            </div>
          </div>
        </section>
      </div>
    </Teleport>

    <p v-if="errorMessage" class="institution-logo__error" role="alert">
      {{ errorMessage }}
    </p>
  </div>
</template>

<style scoped src="./InstitutionLogoUploader.css"></style>
