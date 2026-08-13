<script setup lang="ts">
import { ref } from 'vue'
import type { CrownCarouselItem } from './CrownCarousel'
import { useCrownCarousel } from './CrownCarousel'

const props = withDefaults(
  defineProps<{
    items: CrownCarouselItem[]
    autoplay?: boolean
    autoplaySpeed?: number
    tilt?: number
  }>(),
  {
    autoplay: true,
    autoplaySpeed: 0.00022,
    tilt: 35,
  },
)

const emit = defineEmits<{
  (event: 'select', item: CrownCarouselItem): void
}>()

const rootRef = ref<HTMLElement | null>(null)

const {
  activeIndex,
  cardStyles,
  isDragging,
  onPointerDown,
  onPointerMove,
  onPointerUp,
  onPointerCancel,
  selectItem,
} = useCrownCarousel(rootRef, props)

const handleSelect = (index: number) => {
  selectItem(index)
  emit('select', props.items[index])
}
</script>

<template>
  <div
    ref="rootRef"
    class="crown-carousel"
    :class="{ 'crown-carousel--dragging': isDragging }"
    @pointerdown="onPointerDown"
    @pointermove="onPointerMove"
    @pointerup="onPointerUp"
    @pointercancel="onPointerCancel"
    @pointerleave="onPointerUp"
  >
    <div class="crown-carousel__stage">
      <button
        v-for="(item, index) in items"
        :key="item.id"
        type="button"
        class="crown-carousel__card"
        :class="{
          'crown-carousel__card--active': index === activeIndex,
        }"
        :style="cardStyles[index]"
        :aria-label="`Selecionar ${item.title}`"
        :aria-pressed="index === activeIndex"
        @click.stop="handleSelect(index)"
      >
        <span class="crown-carousel__image">
          <img
            :src="item.image"
            :alt="item.imageAlt ?? item.title"
            draggable="false"
          />
        </span>

        <span class="crown-carousel__title">
          {{ item.title }}
        </span>
      </button>
    </div>
  </div>
</template>
