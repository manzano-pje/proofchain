<!--
=========================================================
Project.......: ProofChain
Component.....: BaseSwitch
File..........: BaseSwitch.vue
Version.......: 1.0.0

Description...:
Reusable switch component.

Responsibility:
Provide a standardized toggle control for
boolean states.

Dependencies..:
- BaseSwitch.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseSwitch.css'


interface BaseSwitchProps {

  modelValue?: boolean

  label?: string

  disabled?: boolean

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseSwitchProps>(),
  {
    modelValue: false,
    disabled: false
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

  'switch',

  {
    'switch--active': props.modelValue,
    'switch--disabled': props.disabled
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

      class="switch__input"

      type="checkbox"

      :checked="modelValue"

      :disabled="disabled"

      :aria-label="ariaLabel"

      @change="handleChange"

    />


    <span class="switch__track">

      <span class="switch__thumb"></span>

    </span>


    <span
      v-if="label"
      class="switch__label"
    >

      {{ label }}

    </span>


  </label>

</template>