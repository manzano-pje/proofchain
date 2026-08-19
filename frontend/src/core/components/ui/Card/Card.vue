<!--
=========================================================
Project.......: ProofChain
Component.....: Card
File..........: Card.vue
Version.......: 1.0.0

Description...:
Reusable card container component.

Responsibility:
Provide a standardized content container
for interface sections.
Dependencies..: - Card.css
Methodology...: BEM
=========================================================
-->

<script setup lang="ts">
import { computed } from 'vue'
import './Card.css'

interface CardProps {
  padding?: 'sm' | 'md' | 'lg'
  bordered?: boolean
  elevated?: boolean
}

const props = withDefaults(defineProps<CardProps>(), {
  padding: 'md',
  bordered: true,
  elevated: false,
})

const classes = computed(() => [
  'card',
  `card--${props.padding}`,
  {
    'card--bordered': props.bordered,
    'card--elevated': props.elevated,
  },
])
</script>

<template>
  <section :class="classes">
    <header v-if="$slots.header" class="card__header">
      <slot name="header" />
    </header>
    <div class="card__body">
      <slot />
    </div>

    <footer v-if="$slots.footer" class="card__footer">
      <slot name="footer" />
    </footer>
  </section>
</template>
