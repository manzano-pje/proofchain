<script setup lang="ts">
defineProps<{
  version?: string
  copyright?: string
}>()
</script>

<template>
  <footer class="app-footer">
    <span class="app-footer__copyright">
      {{ copyright || `© ${new Date().getFullYear()} Todos os direitos reservados.` }}
    </span>
    <span v-if="version" class="app-footer__version">v{{ version }}</span>
  </footer>
</template>

<style scoped src="./AppFooter.css"></style>
