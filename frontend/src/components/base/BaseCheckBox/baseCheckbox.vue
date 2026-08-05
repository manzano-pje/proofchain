<!--
=========================================================
Project.......: ProofChain
Component.....: BaseCheckbox
File..........: BaseCheckbox.vue
Version.......: 1.0.0

Description...:
Reusable checkbox component.

Responsibility:
Provide a standardized checkbox input with
consistent behavior and accessibility.

Dependencies..:
- BaseCheckbox.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseCheckbox.css'


interface BaseCheckboxProps {

  modelValue?: boolean

  label?: string

  disabled?: boolean

  required?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseCheckboxProps>(),
  {
    modelValue: false,
    disabled: false,
    required: false
  }
)


const emit = defineEmits<{

  (
    event: 'update:modelValue',
    value: boolean
  ): void


  (
    event: 'change',
    value: Event
  ): void

}>()


const classes = computed(() => [

  'checkbox',

  {
    'checkbox--disabled': props.disabled
  }

])


function handleChange(
  event: Event
) {

  const target = event.target as HTMLInputElement


  emit(
    'update:modelValue',
    target.checked
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

      class="checkbox__input"

      type="checkbox"

      :checked="modelValue"

      :disabled="disabled"

      :required="required"

      :aria-label="ariaLabel"

      @change="handleChange"

    />


    <span class="checkbox__box"></span>


    <span
      v-if="label"
      class="checkbox__label"
    >

      {{ label }}

    </span>


  </label>

</template>