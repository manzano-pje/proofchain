<template>
  <section id="modal-acquisition" class="modal-acquisition">
    <div
      class="modal fade",
      id="acquisitionModal"
      tabindex="-1"
      aria-labelledby="acquisitionModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="acquisitionModalLabel">
              Aquisição de Assinatura
            </h1>
          </div>
          <div class="modal-body">
            <form @submit.prevent="$emit('submit')">
              <div class="mb-3">
                <label for="name" class="col-form-label">Nome da Instituição:</label>
                <input
                  type="text"
                  class="form-control"
                  v-model="formData.name"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="cnpj" class="col-form-label">CNPJ:</label>
                <input
                  type="text"
                  class="form-control"
                  v-model="formData.cnpj"
                  :class="{ 'is-invalid': errors.cnpj }"
                  required
                />
                <div class="invalid-feedback">{{ errors.cnpj }}</div>
              </div>

              <div class="mb-3">
                <label for="userName" class="col-form-label"
                  >Nome do Usuário Maste:</label
                >
                <input
                  type="text"
                  class="form-control"
                  v-model="formData.userName"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="email" class="col-form-label">Email:</label>
                <input
                  type="email"
                  class="form-control"
                  v-model="formData.email"
                  :class="{ 'is-invalid': errors.email }"
                  required
                />
                <div class="invalid-feedback">{{ errors.email }}</div>
              </div>

              <div class="mb-3">
                <label for="password" class="col-form-label">Senha:</label>
                <input
                  type="password"
                  class="form-control"
                  v-model="formData.password"
                  :class="{ 'is-invalid': errors.password }"
                  required
                  minlength="8"
                />
                <div class="invalid-feedback">{{ errors.password }}</div>
                <input type="hidden" v-model="formData.planId" />
              </div>

              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                  Cancelar
                </button>
                <button type="submit" class="btn btn-primary">Assinar</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>


<script setup lang="ts">
defineProps<{
  formData: {
    name: string
    cnpj: string
    userName: string
    email: string
    password: string
    planId: number
  }
  errors: {
    cnpj: string
    email: string
    password: string
  }
}>()

defineEmits<{
  (e: 'submit'): void
}>()

declare const bootstrap: any

function closeModal() {
  const modalElement = document.getElementById('acquisitionModal')
  if (modalElement) {
    const modalInstance =
      bootstrap.Modal.getInstance(modalElement) ||
      new bootstrap.Modal(modalElement)
    modalInstance.hide()
  }
}

defineExpose({
  closeModal
})
</script>
