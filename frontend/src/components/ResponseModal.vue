<template>
  <div
    class="modal fade"
    id="responseModal"
    tabindex="-1"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered modal-sm">
      <div class="modal-content shadow-lg border-0">

        <div class="modal-body text-center p-4">

          <div :class="iconWrapper">
            <i :class="iconClass"></i>
          </div>

          <h4 class="fw-bold mt-3 mb-2">
            {{ title }}
          </h4>

          <p class="text-muted mb-4">
            {{ message }}
          </p>

          <button
            class="btn btn-primary px-4"
            data-bs-dismiss="modal"
          >
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

const iconClass = computed(() => {
  const map = {
    success: 'fas fa-check',
    danger: 'fas fa-times',
    warning: 'fas fa-exclamation',
    info: 'fas fa-info'
  }
  return map[props.type]
})

const iconWrapper = computed(() => {
  const map = {
    success: 'icon-circle bg-success',
    danger: 'icon-circle bg-danger',
    warning: 'icon-circle bg-warning',
    info: 'icon-circle bg-info'
  }
  return map[props.type]
})


</script>

<style scoped>

.icon-circle{
  width:60px;
  height:60px;
  margin:auto;
  border-radius:50%;
  display:flex;
  align-items:center;
  justify-content:center;
  color:white;
  font-size:24px;
}

.modal-content{
  border-radius:12px;
}

.modal-content{
  border-radius:12px;
  animation: modalFade 0.2s ease;
}

@keyframes modalFade{
  from{
    transform: translateY(10px);
    opacity:0;
  }
  to{
    transform: translateY(0);
    opacity:1;
  }
}

</style>