<!--
=========================================================
Project.......: ProofChain
Component.....: Container
File..........: Container.vue
Version.......: 1.0.0

Description...:
Responsive container component.

Responsibility:
Provide standardized horizontal spacing
and maximum width.

Dependencies..:
- Container.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import { computed } from 'vue'
import './Container.css'
interface ContainerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'fluid'
}

const props = withDefaults(defineProps<ContainerProps>(), {
  size: 'lg',
})

const classes = computed(() => ['container', `container--${props.size}`])
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
