<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  role: 'ADMINISTRADOR' | 'USUÁRIO' | string
}>()

const roleClass = computed(() => `role-badge role-badge--${props.role.toLowerCase()}`)
</script>

<template>
  <span :class="roleClass">
    <slot>{{ props.role }}</slot>
  </span>
</template>

<style scoped src="./RoleBadge.css"></style>
