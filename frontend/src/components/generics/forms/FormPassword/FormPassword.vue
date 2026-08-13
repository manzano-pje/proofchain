<!--
=========================================================
Project.......: ProofChain
Component.....: FormPassword
File..........: FormPassword.vue
Version.......: 1.0.0

Description...:
Password input wrapper component.

Responsibility:
Provide a standardized password field
with visibility control.

Dependencies..:
- FormPassword.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed, ref } from 'vue'

import './FormPassword.css'


interface FormPasswordProps {

  modelValue?: string

  placeholder?: string

  disabled?: boolean

  error?: boolean

}


const props = withDefaults(
  defineProps<FormPasswordProps>(),
  {
    disabled: false,
    error: false
  }
)


const emit = defineEmits<{

  (
    event: 'update:modelValue',
    value: string
  ): void

}>()


const visible = ref(false)


const inputType = computed(() =>

  visible.value
    ? 'text'
    : 'password'

)


function handleInput(
  event: Event
) {

  const target = event.target as HTMLInputElement

  emit(
    'update:modelValue',
    target.value
  )

}


function toggleVisibility() {

  visible.value = !visible.value

}


</script>


<template>

  <div class="password">

    <input

      class="password__input"

      :type="inputType"

      :value="modelValue"

      :placeholder="placeholder"

      :disabled="disabled"

      :aria-invalid="error"

      @input="handleInput"

    />


    <button

      class="password__toggle"

      type="button"

      :disabled="disabled"

      @click="toggleVisibility"

    >

      {{ visible ? 'Hide' : 'Show' }}

    </button>


  </div>


</template>