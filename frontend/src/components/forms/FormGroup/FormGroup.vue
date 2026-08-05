<!--
=========================================================
Project.......: ProofChain
Component.....: FormGroup
File..........: FormGroup.vue
Version.......: 1.0.0

Description...:
Reusable form grouping component.

Responsibility:
Organize related form controls into
a consistent visual structure.

Dependencies..:
- FormGroup.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './FormGroup.css'


interface FormGroupProps {

  direction?:
    | 'vertical'
    | 'horizontal'

  disabled?: boolean

}


const props = withDefaults(
  defineProps<FormGroupProps>(),
  {
    direction: 'vertical',
    disabled: false
  }
)


const classes = computed(() => [

  'form-group',

  `form-group--${props.direction}`,

  {
    'form-group--disabled': props.disabled
  }

])


</script>


<template>

  <fieldset
    :class="classes"
    :disabled="disabled"
  >

    <slot />

  </fieldset>


</template>