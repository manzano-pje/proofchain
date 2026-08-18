<!--
=========================================================
Project.......: ProofChain
Component.....: BaseIcon
File..........: BaseIcon.vue
Version.......: 1.0.0

Description...:
Reusable icon component.

Responsibility:
Provide a standardized icon wrapper with
consistent sizing and accessibility.

Dependencies..:
- BaseIcon.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseIcon.css'


interface BaseIconProps {

  name: string

  size?:
    | 'sm'
    | 'md'
    | 'lg'

  ariaLabel?: string

}


const props = withDefaults(
  defineProps<BaseIconProps>(),
  {
    size: 'md'
  }
)


const classes = computed(() => [

  'icon',

  `icon--${props.size}`

])


</script>


<template>

  <span

    :class="classes"

    role="img"

    :aria-label="ariaLabel"

  >

    <slot>
      {{ name }}
    </slot>

  </span>

</template>