<template>
  <div class="form-course">
    <h2 class="form-course__title">
      {{ title }}
    </h2>

    <form class="form-course__body" @submit.prevent="handleSubmit">
      <!-- Nome -->
      <div class="form-course__field">
        <label for="course-name" class="form-course__label">Nome</label>
        <input
          id="course-name"
          v-model="form.name"
          type="text"
          maxlength="100"
          class="form-course__input"
          :class="{ 'form-course__input--error': errors.name }"
          :readonly="isViewMode"
          :aria-invalid="!!errors.name"
          aria-describedby="course-name-error"
          @blur="validateField('name')"
        />
        <span
          v-if="errors.name"
          id="course-name-error"
          class="form-course__error"
        >
          {{ errors.name }}
        </span>
      </div>

      <!-- Descrição -->
      <div class="form-course__field">
        <label for="course-description" class="form-course__label">Descrição</label>
        <textarea
          id="course-description"
          v-model="form.description"
          maxlength="200"
          rows="4"
          class="form-course__textarea"
          :class="{ 'form-course__textarea--error': errors.description }"
          :readonly="isViewMode"
          :aria-invalid="!!errors.description"
          aria-describedby="course-description-error"
          @blur="validateField('description')"
        />
        <span
          v-if="errors.description"
          id="course-description-error"
          class="form-course__error"
        >
          {{ errors.description }}
        </span>
      </div>

      <!-- Carga horária -->
      <div class="form-course__field">
        <label for="course-hours" class="form-course__label">Carga horária (horas)</label>
        <input
          id="course-hours"
          v-model.number="form.hours"
          type="number"
          min="1"
          step="1"
          class="form-course__input"
          :class="{ 'form-course__input--error': errors.hours }"
          :readonly="isViewMode"
          :aria-invalid="!!errors.hours"
          aria-describedby="course-hours-error"
          @blur="validateField('hours')"
        />
        <span
          v-if="errors.hours"
          id="course-hours-error"
          class="form-course__error"
        >
          {{ errors.hours }}
        </span>
      </div>

      <!-- Ações -->
      <div class="form-course__actions">
        <BaseButton
          v-if="!isViewMode"
          type="button"
          variant="secondary"
          @click="emit('cancel')"
        >
          Cancelar
        </BaseButton>

        <BaseButton
          v-if="!isViewMode"
          type="submit"
          :loading="submitting"
          :disabled="submitting"
        >
          {{ isEditMode ? 'Salvar alterações' : 'Salvar' }}
        </BaseButton>

        <BaseButton
          v-if="isViewMode"
          type="button"
          @click="emit('cancel')"
        >
          Fechar
        </BaseButton>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, watch } from 'vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import type { Course, FormMode, CourseCreatePayload, CourseUpdatePayload } from '@/modules/business/types/Course.types'

/* ============================================================
   PROPS
   ============================================================ */
const props = defineProps<{
  initialData?: Course | null
  mode: FormMode
  submitting?: boolean
}>()

/* ============================================================
   EMITS
   ============================================================ */
const emit = defineEmits<{
  (e: 'submit', payload: CourseCreatePayload | CourseUpdatePayload): void
  (e: 'cancel'): void
}>()

/* ============================================================
   ESTADO DO FORMULÁRIO
   ============================================================ */
const form = reactive({
  name: '',
  description: '',
  hours: 0,
})

const errors = reactive({
  name: '',
  description: '',
  hours: '',
})

/* ============================================================
   MODO
   ============================================================ */
const isViewMode = computed(() => props.mode === 'view')
const isEditMode = computed(() => props.mode === 'edit')

const title = computed(() => {
  if (props.mode === 'create') return 'Novo Curso'
  if (props.mode === 'edit') return 'Editar Curso'
  return 'Visualizar Curso'
})

/* ============================================================
   PREENCHIMENTO INICIAL
   ============================================================ */
watch(
  () => props.initialData,
  (data) => {
    if (data) {
      form.name = data.name
      form.description = data.description
      form.hours = data.hours
    } else {
      form.name = ''
      form.description = ''
      form.hours = 0
    }
  },
  { immediate: true },
)

/* ============================================================
   VALIDAÇÃO
   ============================================================ */
const validateField = (field: keyof typeof errors) => {
  switch (field) {
    case 'name': {
      const value = form.name.trim()
      if (!value) errors.name = 'Nome é obrigatório'
      else if (value.length > 100) errors.name = 'Máximo 100 caracteres'
      else errors.name = ''
      break
    }
    case 'description': {
      const value = form.description.trim()
      if (!value) errors.description = 'Descrição é obrigatória'
      else if (value.length > 200) errors.description = 'Máximo 200 caracteres'
      else errors.description = ''
      break
    }
    case 'hours': {
      const value = Number(form.hours)
      if (!value || value <= 0) errors.hours = 'Carga horária deve ser maior que zero'
      else if (!Number.isInteger(value)) errors.hours = 'Informe um número inteiro'
      else errors.hours = ''
      break
    }
  }
}

const validateAll = (): boolean => {
  validateField('name')
  validateField('description')
  validateField('hours')
  return !errors.name && !errors.description && !errors.hours
}

/* ============================================================
   SUBMIT
   ============================================================ */
const handleSubmit = () => {
  if (isViewMode.value) return
  if (!validateAll()) return

  const payload: CourseCreatePayload | CourseUpdatePayload = {
    name: form.name.trim(),
    description: form.description.trim(),
    hours: Number(form.hours),
  }

  // No modo edição, inclui o id
  if (isEditMode.value && props.initialData) {
    ;(payload as CourseUpdatePayload).id = props.initialData.id
  }

  emit('submit', payload)
}
</script>

<style scoped>
@import './FormCourse.css';
</style>
