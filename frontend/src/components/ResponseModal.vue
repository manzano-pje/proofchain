<template>
  <div class="modal fade" id="responseModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">

        <div class="modal-header" :class="headerClass">
          <h5 class="modal-title">{{ title }}</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>

        <div class="modal-body">
          <p>{{ message }}</p>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  title: string
  message: string
  type: string
}>()

const headerClass = computed(() => {
  return {
    'bg-success text-white': props.type === 'success',
    'bg-danger text-white': props.type === 'danger',
    'bg-warning text-dark': props.type === 'warning',
    'bg-info text-white': props.type === 'info'
  }
})
</script>