<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: 'ativo' | 'inativo' | string
}>()

const statusClass = computed(() => `status-badge status-badge--${props.status.toLowerCase()}`)
</script>

<template>
  <span :class="statusClass">
    <span class="status-badge__dot" aria-hidden="true"></span>
    <slot>{{ props.status }}</slot>
  </span>
</template>

<style scoped src="./StatusBadge.css"></style>
