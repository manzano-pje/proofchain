<!--
=========================================================
Project.......: ProofChain
Component.....: BaseAvatar
File..........: BaseAvatar.vue
Version.......: 1.0.0

Description...:
Reusable avatar component.

Responsibility:
Provide a standardized user or entity
representation.

Dependencies..:
- BaseAvatar.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './BaseAvatar.css'


interface BaseAvatarProps {

  src?: string

  alt?: string

  name?: string

  size?:
    | 'sm'
    | 'md'
    | 'lg'

}


const props = withDefaults(
  defineProps<BaseAvatarProps>(),
  {
    size: 'md',
    alt: 'Avatar'
  }
)


const classes = computed(() => [

  'avatar',

  `avatar--${props.size}`

])


const initials = computed(() => {

  if (!props.name) {
    return ''
  }


  return props.name

    .split(' ')

    .map(
      item => item.charAt(0)
    )

    .slice(0, 2)

    .join('')

    .toUpperCase()

})


</script>


<template>

  <span
    :class="classes"
  >

    <img

      v-if="src"

      class="avatar__image"

      :src="src"

      :alt="alt"

    />


    <span
      v-else
      class="avatar__placeholder"
    >

      {{ initials }}

    </span>


  </span>

</template>