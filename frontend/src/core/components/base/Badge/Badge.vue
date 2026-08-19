<!--
=========================================================
Project.......: ProofChain
Component.....: Badge
File..........: Badge.vue
Version.......: 1.0.0

Description...:
Reusable status badge component.

Responsibility:
Display labels and statuses consistently
throughout the interface.

Dependencies..:
- Badge.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">
import { computed } from 'vue'
import './Badge.css'

interface BadgeProps {
  variant?: 'primary' | 'success' | 'warning' | 'danger' | 'neutral'

  size?: 'sm' | 'md' | 'lg'
}

const props = withDefaults(defineProps<BadgeProps>(), {
  variant: 'neutral',
  size: 'lg',
})

const classes = computed(() => ['badge', `badge--${props.variant}`, `badge--${props.size}`])
</script>

<template>
  <span :class="classes">
    <slot />
  </span>
</template>
