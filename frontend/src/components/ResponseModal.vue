<template>
  <div
    class="modal fade"
    id="responseModal"
    tabindex="-1"
    aria-labelledby="responseModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header" :class="headerClass">
          <h2 class="modal-title fs-5 d-flex align-items-center gap-2" id="responseModalLabel">
            <i :class="iconClass" aria-hidden="true"></i>
            {{ title }}
          </h2>
          <button
            type="button"
            :class="['btn-close', props.type === 'warning' ? '' : 'btn-close-white']"
            data-bs-dismiss="modal"
            aria-label="Fechar"
          ></button>
        </div>
        <div class="modal-body">
          <p class="mb-0">{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            Fechar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    title: string
    message: string
    type?: 'success' | 'danger' | 'warning' | 'info'
  }>(),
  { type: 'info' }
)

const headerClass = computed(() => {
  const map = {
    success: 'bg-success text-white',
    danger: 'bg-danger text-white',
    warning: 'bg-warning text-dark',
    info: 'bg-info text-white'
  }
  return map[props.type] ?? map.info
})

const iconClass = computed(() => {
  const map = {
    success: 'fas fa-check-circle',
    danger: 'fas fa-times-circle',
    warning: 'fas fa-exclamation-triangle',
    info: 'fas fa-info-circle'
  }
  return map[props.type] ?? map.info
})
</script>
