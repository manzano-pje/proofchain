<!--
=========================================================
Project.......: ProofChain
Component.....: BaseSelect
File..........: BaseSelect.vue
Version.......: 1.0.0

Description...:
Reusable select component for option selection.

Responsibility:
Provide a standardized select field with consistent
behavior and accessibility.

Dependencies..:
- BaseSelect.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseSelect.css'


interface SelectOption {

  label: string

  value: string | number

}


interface BaseSelectProps {

  modelValue?: string | number

  options?: SelectOption[]

  placeholder?: string

  disabled?: boolean

  required?: boolean

  error?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseSelectProps>(),
  {
    options: () => [],
    disabled: false,
    required: false,
    error: false
  }
)


const emit = defineEmits<{

  (
    event: 'update:modelValue',
    value: string | number
  ): void

  (
    event: 'change',
    value: Event
  ): void

}>()


const classes = computed(() => [

  'select',

  {
    'select--error': props.error,
    'select--disabled': props.disabled
  }

])


function handleChange(
  event: Event
) {

  const target = event.target as HTMLSelectElement

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

  <select

    :class="classes"

    :value="modelValue"

    :disabled="disabled"

    :required="required"

    :aria-label="ariaLabel"

    :aria-invalid="error"

    @change="handleChange"

  >

    <option
      v-if="placeholder"
      disabled
      value=""
    >
      {{ placeholder }}
    </option>


    <option

      v-for="option in options"

      :key="option.value"

      :value="option.value"

    >

      {{ option.label }}

    </option>


  </select>

</template>