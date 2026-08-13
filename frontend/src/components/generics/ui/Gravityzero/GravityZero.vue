<script setup lang="ts">
import { ref } from 'vue'
import { useGravityZero } from './GravityZero'

const canvasRef = ref<HTMLCanvasElement | null>(null)

useGravityZero(canvasRef)
</script>

<template>
  <section class="gravity-zero" aria-hidden="true">
    <div class="gravity-zero__glow"></div>

    <canvas ref="canvasRef" class="gravity-zero__canvas"></canvas>

    <div class="gravity-zero__content">
      <slot />
    </div>
  </section>
</template>
