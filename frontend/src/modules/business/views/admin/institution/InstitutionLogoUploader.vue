<script setup lang="ts">
/**
 * Uploader da logo da instituição.
 *
 * Fluxo de estados:
 *   idle     → área de upload (clique ou arrastar/soltar)
 *   cropping → enquadramento com pan + zoom
 *   ready    → pré-visualização do arquivo final + alterar/remover
 *
 * [PROVISÓRIO] `aspectRatio` ainda não é definitivo — deve ser ajustado
 * quando o layout do certificado definir a área exata da logo.
 * A prop é configurável para que essa mudança não exija reescrita.
 */
import { computed, onBeforeUnmount, ref, watch } from 'vue'

import { useImageCrop } from './useImageCrop'

interface Props {
  /**
   * Proporção (largura / altura) da área de crop.
   * [PROVISÓRIO] Ajustar quando a área da logo no certificado for definida.
   */
  aspectRatio?: number
  /** Largura final do arquivo exportado, em px. */
  outputWidth?: number
  /** Tamanho máximo do arquivo original, em bytes. */
  maxBytes?: number
  /** MIME types aceitos. */
  acceptedMimeTypes?: readonly string[]
  /** Extensões aceitas (para fallback quando `File.type` vem vazio). */
  acceptedExtensions?: readonly string[]
}

const props = withDefaults(defineProps<Props>(), {
  // [PROVISÓRIO] — não é decisão de negócio final; só permite o componente operar.
  aspectRatio: 4 / 1,
  outputWidth: 800,
  maxBytes: 2 * 1024 * 1024,
  acceptedMimeTypes: () => ['image/png', 'image/jpeg', 'image/svg+xml'] as const,
  acceptedExtensions: () => ['.png', '.jpg', '.jpeg', '.svg'] as const,
})

const emit = defineEmits<{
  remove: []
}>()

/** Arquivo final cortado. `null` quando não há logo nova selecionada. */
const model = defineModel<File | null>({ default: null })

type UploaderState = 'idle' | 'cropping' | 'ready'

const state = ref<UploaderState>('idle')
const errorMessage = ref<string | null>(null)
const isDragging = ref(false)
const previewUrl = ref<string | null>(null)

const fileInputRef = ref<HTMLInputElement | null>(null)
const cropViewportEl = ref<HTMLElement | null>(null)
let resizeObserver: ResizeObserver | null = null

const crop = useImageCrop()

const acceptAttr = computed(() =>
  [...props.acceptedMimeTypes, ...props.acceptedExtensions].join(','),
)

const formatHint = computed(() => {
  const labels = props.acceptedMimeTypes
    .map((mime) =>
      mime === 'image/png' ? 'PNG'
      : mime === 'image/jpeg' ? 'JPG'
      : mime === 'image/svg+xml' ? 'SVG'
      : mime,
    )
    .join(', ')
  const mb = (props.maxBytes / (1024 * 1024)).toFixed(0)
  return `${labels} • Máximo ${mb} MB`
})

const cropImageStyle = computed(() => ({
  transform: `translate3d(${crop.pan.x}px, ${crop.pan.y}px, 0) scale(${crop.scale.value})`,
  transformOrigin: '0 0',
  width: crop.image.value ? `${crop.image.value.width}px` : '0',
  height: crop.image.value ? `${crop.image.value.height}px` : '0',
}))

watch(cropViewportEl, (el) => {
  resizeObserver?.disconnect()
  resizeObserver = null
  if (!el) return
  resizeObserver = new ResizeObserver((entries) => {
    const entry = entries[0]
    if (!entry) return
    const { width, height } = entry.contentRect
    crop.setViewport(width, height)
  })
  resizeObserver.observe(el)
  const rect = el.getBoundingClientRect()
  crop.setViewport(rect.width, rect.height)
})

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
    errorMessage.value = 'Formato inválido. Envie PNG, JPG ou SVG.'
    return
  }

  if (file.size > props.maxBytes) {
    const mb = (props.maxBytes / (1024 * 1024)).toFixed(0)
    errorMessage.value = `A imagem deve possuir no máximo ${mb} MB.`
    return
  }

  try {
    const url = URL.createObjectURL(file)
    await crop.loadFromUrl(url, true)
    state.value = 'cropping'
  } catch {
    errorMessage.value = 'Não foi possível carregar a imagem selecionada.'
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
  const height = Math.round(props.outputWidth / props.aspectRatio)
  const file = await crop.exportCropped({ width: props.outputWidth, height })
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
      :aria-label="`Selecionar logo da instituição. ${formatHint}`"
      @click="openFilePicker"
      @keydown.enter.prevent="openFilePicker"
      @keydown.space.prevent="openFilePicker"
      @dragover.prevent="onDragOver"
      @dragleave.prevent="onDragLeave"
      @drop.prevent="onDrop"
    >
      <span class="institution-logo__dropzone-title">Arraste uma imagem aqui</span>
      <span class="institution-logo__dropzone-subtitle">ou clique para selecionar</span>
      <span class="institution-logo__dropzone-hint">{{ formatHint }}</span>
    </div>

    <!-- Estado: cropping -->
    <div v-else-if="state === 'cropping'" class="institution-logo__crop">
      <div
        ref="cropViewportEl"
        class="institution-logo__crop-viewport"
        :style="{ aspectRatio: String(props.aspectRatio) }"
        role="application"
        tabindex="0"
        aria-label="Área de enquadramento. Use as setas do teclado para mover e o controle deslizante para ampliar."
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

      <div class="institution-logo__crop-controls">
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
          <button type="button" class="institution-logo__btn" @click="cancelCrop">
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
    </div>

    <!-- Estado: ready -->
    <div v-else class="institution-logo__preview">
      <img
        v-if="previewUrl"
        class="institution-logo__preview-image"
        :src="previewUrl"
        alt="Pré-visualização da logo que será enviada"
      />
      <div class="institution-logo__preview-actions">
        <button type="button" class="institution-logo__btn" @click="restart">
          Alterar imagem
        </button>
        <button
          type="button"
          class="institution-logo__btn institution-logo__btn--danger"
          @click="removeLogo"
        >
          Remover
        </button>
      </div>
    </div>

    <p v-if="errorMessage" class="institution-logo__error" role="alert">
      {{ errorMessage }}
    </p>
  </div>
</template>

<style scoped src="./InstitutionLogoUploader.css"></style>