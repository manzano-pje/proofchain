<!--
=========================================================
Project.......: ProofChain
Component.....: CrownCarousel
File..........: CrownCarousel.vue
Version.......: 1.0.0

Description...:
3D cylindrical crown carousel with independent card flip.

Responsibility:
Render the spatial DOM tree and bind props, classes and events.
No dense geometry lives in this file.

Dependencies..:
- CrownCarousel.ts
- CrownCarousel.css

Methodology...:
BEM-inspired class names from the technical specification.
=========================================================
-->

<script setup lang="ts">
import { toRef } from 'vue'
import { useCrownCarousel, type CrownCarouselProps } from './CrownCarousel.ts'
import './CrownCarousel.css'

const props = defineProps<CrownCarouselProps>()

const {
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
} = useCrownCarousel(toRef(props, 'items'))
</script>

<template>
  <div
    ref="viewportRef"
    class="crown-carousel-viewport"
    :class="{
      'crown-carousel-viewport--dragging': isDragging,
      'crown-carousel-viewport--reduced-motion': prefersReducedMotion,
    }"
    :style="viewportStyle"
    role="region"
    aria-roledescription="carrossel"
    aria-labelledby="crown-carousel-title"
    aria-describedby="crown-carousel-instructions"
    @pointerdown="onPointerDown"
    @pointermove="onPointerMove"
    @pointerup="onPointerUp"
    @pointercancel="onPointerCancel"
    @pointerenter="onPointerEnter"
    @pointerleave="onPointerLeave"
  >
    <h2 id="crown-carousel-title" class="crown-carousel-sr-only">Por que escolher</h2>
    <p id="crown-carousel-instructions" class="crown-carousel-sr-only">
      Arraste para girar o anel. Clique em um card para ver a descrição no verso.
    </p>

    <div class="crown-carousel-stage" :style="stageStyle">
      <div class="crown-carousel-ring" :style="ringStyle">
        <div
          v-for="(item, index) in renderItems"
          :key="item.renderKey"
          class="crown-carousel-card"
          :class="{ 'crown-carousel-card--flipped': flippedIndexes[index] }"
          :style="cardStyles[index]"
          :data-crown-index="index"
          role="group"
          :aria-label="item.title"
        >
          <div
            class="crown-carousel-flip-container"
            :class="{ 'crown-carousel-flip-container--flipped': flippedIndexes[index] }"
          >
            <div class="front">
              <img
                class="front__image"
                :src="item.image"
                :alt="item.title"
                draggable="false"
                loading="lazy"
              />
              <span class="front__title">{{ item.title }}</span>
            </div>
            <div class="back">
              <h3 class="back__title">{{ item.title }}</h3>
              <p class="back__description">{{ item.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
