<!--
=========================================================
Project.......: ProofChain
Component.....: CrownCarousel
File..........: CrownCarousel.vue
Version.......: 1.0.0

Description...:
Landing page "Por que escolher" section.

Responsibility:
Present the 3D crown carousel inside the standard
section/container layout of the landing page.

Dependencies..:
- Section
- Container
- CrownCarousel.ts
- CrownCarousel.css
- CrownCarousel content

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import { computed } from 'vue'

import './CrownCarousel.css'

import Section from '@/core/components/ui/Section/Section.vue'
import Container from '@/core/components/ui/Container/Container.vue'

import { crownCarouselItems } from '../../content/CrownCarousel.ts'
import { useCrownCarousel } from './CrownCarousel.ts'

/* Conteúdo da seção: o carrossel não busca dados; apenas consome o mock local. */
const items = computed(() => crownCarouselItems)

/* Motor visual: geometria, auto-rotação, arraste e flip ficam no composable. */
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
  onPointerDown,
  onPointerMove,
  onPointerUp,
  onPointerCancel,
  incrementHover,
  decrementHover,
} = useCrownCarousel(items)
</script>

<template>
  <!-- Seção da landing: aplica o recorte vertical e o fundo padrão da página. -->
  <Section id="crown-carousel" class="section-crown-carousel">
    <!-- Container: limita a largura e o padding horizontal do conteúdo. -->
    <Container>
      <div class="crown-carousel">
        <!-- Cabeçalho editorial da seção, no mesmo padrão visual do Audiences. -->
        <header class="crown-carousel__header">
          <div id="crown-carousel-title" class="crown-carousel__badge">Por que escolher</div>
          <p>Prova digital verificável para cada credencial emitida.</p>
        </header>

        <!-- Viewport 3D: recorte, perspectiva e captura dos Pointer Events. -->
        <div
          ref="viewportRef"
          class="crown-carousel__viewport"
          :class="{
            'crown-carousel__viewport--dragging': isDragging,
            'crown-carousel__viewport--reduced-motion': prefersReducedMotion,
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
        >
          <p id="crown-carousel-instructions" class="crown-carousel__sr-only">
            Arraste para girar o anel. Clique em um card para ver a descrição no verso.
          </p>

          <!-- Palco da câmera: aplica apenas a inclinação global no eixo X. -->
          <div class="crown-carousel__stage" :style="stageStyle">
            <!-- Anel orbital: sofre somente a rotação Y do auto-rotate e do arraste. -->
            <div class="crown-carousel__ring" :style="ringStyle">
              <div
                v-for="(item, index) in renderItems"
                :key="item.renderKey"
                class="crown-carousel__card"
                :class="{ 'crown-carousel__card--flipped': flippedIndexes[index] }"
                :style="cardStyles[index]"
                :data-crown-index="index"
                role="group"
                :aria-label="item.title"
                @pointerenter="incrementHover"
                @pointerleave="decrementHover"
              >
                <!-- Flip local: rotateY de 180° isolado da órbita do anel. -->
                <div
                  class="crown-carousel__flip"
                  :class="{ 'crown-carousel__flip--flipped': flippedIndexes[index] }"
                >
                  <div class="crown-carousel__front">
                    <img
                      class="crown-carousel__image"
                      :src="item.image"
                      :alt="item.title"
                      draggable="false"
                      loading="lazy"
                    />
                    <span class="crown-carousel__front-title">{{ item.title }}</span>
                  </div>
                  <div class="crown-carousel__back">
                    <h3 class="crown-carousel__back-title">{{ item.title }}</h3>
                    <p class="crown-carousel__description">{{ item.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Container>
  </Section>
</template>
