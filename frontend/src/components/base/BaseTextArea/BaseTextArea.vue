<!--
=========================================================
Project.......: ProofChain
Component.....: BaseTextarea
File..........: BaseTextarea.vue
Version.......: 1.0.0

Description...:
Reusable textarea component for multiline text input.

Responsibility:
Provide a standardized multiline input element
with consistent behavior and accessibility.

Dependencies..:
- BaseTextarea.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseTextarea.css'


interface BaseTextareaProps {

  modelValue?: string

  placeholder?: string

  rows?: number

  disabled?: boolean

  readonly?: boolean

  required?: boolean

  error?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseTextareaProps>(),
  {
    rows: 4,
    disabled: false,
    readonly: false,
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
    event: 'blur',
    value: FocusEvent
  ): void


  (
    event: 'focus',
    value: FocusEvent
  ): void

}>()


const classes = computed(() => [

  'textarea',

  {
    'textarea--error': props.error,
    'textarea--disabled': props.disabled
  }

])


function handleInput(
  event: Event
) {

  const target = event.target as HTMLTextAreaElement

  emit(
    'update:modelValue',
    target.value
  )

}


</script>


<template>

  <textarea

    :class="classes"

    :value="modelValue"

    :rows="rows"

    :placeholder="placeholder"

    :disabled="disabled"

    :readonly="readonly"

    :required="required"

    :aria-label="ariaLabel"

    :aria-invalid="error"

    @input="handleInput"

    @blur="emit('blur', $event)"

    @focus="emit('focus', $event)"

  />

</template>