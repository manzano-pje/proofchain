<!--
=========================================================
Project.......: ProofChain
Component.....: Divider
File..........: Divider.vue
Version.......: 1.0.0

Description...:
Reusable divider component.

Responsibility:
Provide a standardized horizontal or vertical
separator for layouts.

Dependencies..:
- Divider.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Divider.css'

interface DividerProps {

    orientation?:
        | 'horizontal'
        | 'vertical'

    spacing?:
        | 'none'
        | 'sm'
        | 'md'
        | 'lg'

}

const props = withDefaults(
    defineProps<DividerProps>(),
    {
        orientation: 'horizontal',
        spacing: 'md'
    }
)

const classes = computed(() => [

    'divider',

    `divider--${props.orientation}`,

    `divider--${props.spacing}`

])

</script>

<template>

    <hr
        v-if="orientation === 'horizontal'"
        :class="classes"
    >

    <div
        v-else
        :class="classes"
        aria-hidden="true"
    />

</template>