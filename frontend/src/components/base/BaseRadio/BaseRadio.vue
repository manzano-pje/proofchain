<!--
=========================================================
Project.......: ProofChain
Component.....: BaseRadio
File..........: BaseRadio.vue
Version.......: 1.0.0

Description...:
Reusable radio input component.

Responsibility:
Provide a standardized radio selection component
with consistent behavior and accessibility.

Dependencies..:
- BaseRadio.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseRadio.css'


interface BaseRadioProps {

  modelValue?: string | number

  value: string | number

  label?: string

  name?: string

  disabled?: boolean

  required?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseRadioProps>(),
  {
    disabled: false,
    required: false
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

  'radio',

  {
    'radio--disabled': props.disabled
  }

])


function handleChange(
  event: Event
) {

  emit(
    'update:modelValue',
    props.value
  )

  emit(
    'change',
    event
  )

}


</script>


<template>

  <label
    :class="classes"
  >

    <input

      class="radio__input"

      type="radio"

      :name="name"

      :value="value"

      :checked="modelValue === value"

      :disabled="disabled"

      :required="required"

      :aria-label="ariaLabel"

      @change="handleChange"

    />


    <span class="radio__circle"></span>


    <span
      v-if="label"
      class="radio__label"
    >

      {{ label }}

    </span>


  </label>

</template>