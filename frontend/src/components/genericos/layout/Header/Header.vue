<!--
=========================================================
Project.......: ProofChain
Component.....: Header
File..........: Header.vue
Version.......: 1.0.0

Description...:
Reusable page header component.

Responsibility:
Provide a standardized page heading area.

Dependencies..:
- Header.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import './Header.css'
interface HeaderProps {
  title?: string
  subtitle?: string
}

withDefaults(defineProps<HeaderProps>(), {})
</script>

<template>
  <header class="header">
    <div class="header__content">
      <h1 v-if="title" class="header__title">
        {{ title }}
      </h1>
      <H1>este é o header</H1>
      <p v-if="subtitle" class="header__subtitle">
        {{ subtitle }}
      </p>
      <slot />
    </div>
    <div v-if="$slots.actions" class="header__actions">
      <slot name="actions" />
    </div>
  </header>
</template>
