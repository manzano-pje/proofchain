<template>
  <Header />
  <RouterView />
</template>

<style>
/* Global styles can be added here if needed */
</style>

<script setup>
import { RouterView } from 'vue-router'
import Header from './components/header.vue';
</script>