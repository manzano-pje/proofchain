<!--
=========================================================
Project.......: ProofChain
Component.....: FormDatePicker
File..........: FormDatePicker.vue
Version.......: 1.0.0

Description...:
Date input component.

Responsibility:
Provide a standardized date selection field.

Dependencies..:
- FormDatePicker.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import './FormDatePicker.css'


interface FormDatePickerProps {

  modelValue?: string

  min?: string

  max?: string

  disabled?: boolean

  required?: boolean

  error?: boolean

}


withDefaults(
  defineProps<FormDatePickerProps>(),
  {
    disabled: false,
    required: false,
    error: false
  }
)


const emit = defineEmits<{

  (
    event: 'update:modelValue',
    value: string
  ): void


  (
    event: 'change',
    value: Event
  ): void

}>()


function handleChange(
  event: Event
) {

  const target = event.target as HTMLInputElement


  emit(
    'update:modelValue',
    target.value
  )


  emit(
    'change',
    event
  )

}


</script>


<template>

  <input

    class="date-picker"

    type="date"

    :value="modelValue"

    :min="min"

    :max="max"

    :disabled="disabled"

    :required="required"

    :aria-invalid="error"

    @change="handleChange"

  />

</template>