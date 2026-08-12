<!--
=========================================================
Project.......: ProofChain
Component.....: FormFileUpload
File..........: FormFileUpload.vue
Version.......: 1.0.0

Description...:
File upload input component.

Responsibility:
Provide a standardized file selection
component.

Dependencies..:
- FormFileUpload.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import './FormFileUpload.css'


interface FormFileUploadProps {

  accept?: string

  multiple?: boolean

  disabled?: boolean

  required?: boolean

}


withDefaults(
  defineProps<FormFileUploadProps>(),
  {
    multiple: false,
    disabled: false,
    required: false
  }
)


const emit = defineEmits<{

  (
    event: 'change',
    files: FileList | null
  ): void

}>()


function handleChange(
  event: Event
) {

  const target = event.target as HTMLInputElement


  emit(
    'change',
    target.files
  )

}


</script>


<template>

  <input

    class="file-upload"

    type="file"

    :accept="accept"

    :multiple="multiple"

    :disabled="disabled"

    :required="required"

    @change="handleChange"

  />

</template>