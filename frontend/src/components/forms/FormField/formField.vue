<!--
=========================================================
Project.......: ProofChain
Component.....: FormField
File..........: FormField.vue
Version.......: 1.0.0

Description...:
Reusable form field wrapper component.

Responsibility:
Provide a standardized structure for form elements,
including label, content and validation messages.

Dependencies..:
- FormField.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './FormField.css'


interface FormFieldProps {

  label?: string

  error?: string

  hint?: string

  required?: boolean

  disabled?: boolean

}


const props = withDefaults(
  defineProps<FormFieldProps>(),
  {
    required: false,
    disabled: false
  }
)


const classes = computed(() => [

  'form-field',

  {
    'form-field--error': Boolean(props.error),
    'form-field--disabled': props.disabled
  }

])


</script>


<template>

  <div :class="classes">

    <label
      v-if="label"
      class="form-field__label"
    >

      {{ label }}

      <span
        v-if="required"
        class="form-field__required"
      >
        *
      </span>

    </label>


    <div class="form-field__control">

      <slot />

    </div>


    <span
      v-if="error"
      class="form-field__error"
    >

      {{ error }}

    </span>


    <span
      v-else-if="hint"
      class="form-field__hint"
    >

      {{ hint }}

    </span>


  </div>

</template>