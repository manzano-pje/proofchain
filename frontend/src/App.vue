<script setup lang="ts">
import { RouterView } from 'vue-router' 
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'
</script>

<template>
  <div class="app-layout">
    <AppHeader />
    <main>
      <RouterView />
    </main>
    <AppFooter />
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  width: 100%;
  flex: 1; /* This makes the main content expand to fill available space, pushing footer down */
}
</style>
