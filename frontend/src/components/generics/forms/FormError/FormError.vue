<!--
=========================================================
Project.......: ProofChain
Component.....: FormError
File..........: FormError.vue
Version.......: 1.0.0

Description...:
Reusable form validation message component.

Responsibility:
Display standardized validation errors
for form fields.

Dependencies..:
- FormError.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">


interface FormErrorProps {

  message?: string

}


withDefaults(
  defineProps<FormErrorProps>(),
  {
    message: ''
  }
)


</script>


<template>

  <span
    v-if="message"
    class="form-error"
    role="alert"
  >

    {{ message }}

  </span>


</template>