<!--
=========================================================
Project.......: ProofChain
Component.....: FormLabel
File..........: FormLabel.vue
Version.......: 1.0.0

Description...:
Reusable form label component.

Responsibility:
Provide a standardized label element
for form controls.

Dependencies..:
- FormLabel.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">


interface FormLabelProps {

  for?: string

  required?: boolean

}


withDefaults(
  defineProps<FormLabelProps>(),
  {
    required: false
  }
)


</script>


<template>

  <label

    class="form-label"

    :for="for"

  >

    <slot />


    <span
      v-if="required"
      class="form-label__required"
    >

      *

    </span>


  </label>

</template>