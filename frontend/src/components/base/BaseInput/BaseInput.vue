<!--
=========================================================
Project.......: ProofChain
Component.....: BaseInput
File..........: BaseInput.vue
Version.......: 1.0.0

Description...:
Reusable input component for text-based user entries.

Responsibility:
Provide a standardized input field with consistent
behavior and accessibility.

Dependencies..:
- BaseInput.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">
import { computed } from 'vue'
import './BaseInput.css'

interface BaseInputProps {

  modelValue?: string | number

  type?:
  | 'text'
  | 'email'
  | 'password'
  | 'number'
  | 'search'

  placeholder?: string

  disabled?: boolean

  readonly?: boolean

  required?: boolean

  error?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseInputProps>(),
  {
    type: 'text',
    disabled: false,
    readonly: false,
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
    event: 'blur',
    value: FocusEvent
  ): void

  (
    event: 'focus',
    value: FocusEvent
  ): void
}>()


const classes = computed(() => [

  'input',

  {
    'input--error': props.error,
    'input--disabled': props.disabled
  }

])


function handleInput(
  event: Event
) {

  const target = event.target as HTMLInputElement

  emit(
    'update:modelValue',
    target.value
  )

}


</script>


<template>

  <input :class="classes" :type="type" :value="modelValue" :placeholder="placeholder" :disabled="disabled"
    :readonly="readonly" :required="required" :aria-label="ariaLabel" :aria-invalid="error" @input="handleInput"
    @blur="emit('blur', $event)" @focus="emit('focus', $event)" />

</template>