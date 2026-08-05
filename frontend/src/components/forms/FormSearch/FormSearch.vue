<!--
=========================================================
Project.......: ProofChain
Component.....: FormSearch
File..........: FormSearch.vue
Version.......: 1.0.0

Description...:
Search input component.

Responsibility:
Provide a standardized search field
for filtering and querying data.

Dependencies..:
- FormSearch.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">


interface FormSearchProps {

  modelValue?: string

  placeholder?: string

  disabled?: boolean

}


withDefaults(
  defineProps<FormSearchProps>(),
  {
    placeholder: 'Search...',
    disabled: false
  }
)


const emit = defineEmits<{

  (
    event: 'update:modelValue',
    value: string
  ): void


  (
    event: 'search',
    value: string
  ): void

}>()


function handleInput(
  event: Event
) {

  const target = event.target as HTMLInputElement

  emit(
    'update:modelValue',
    target.value
  )

}


function handleSearch(
  value: string
) {

  emit(
    'search',
    value
  )

}


</script>


<template>

  <div class="search">

    <input

      class="search__input"

      type="search"

      :value="modelValue"

      :placeholder="placeholder"

      :disabled="disabled"

      @input="handleInput"

      @keyup.enter="handleSearch(
        ($event.target as HTMLInputElement).value
      )"

    />

  </div>


</template>