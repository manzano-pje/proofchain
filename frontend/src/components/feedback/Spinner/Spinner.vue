<!--
=========================================================
Project.......: ProofChain
Component.....: Spinner
File..........: Spinner.vue
Version.......: 1.0.0

Description...:
Circular spinner component.

Responsibility:
Provide visual feedback for processing states.

Dependencies..:
- Spinner.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Spinner.css'


interface SpinnerProps {

    size?:
        | 'sm'
        | 'md'
        | 'lg'

}


const props = withDefaults(
    defineProps<SpinnerProps>(),
    {
        size: 'md'
    }
)


const classes = computed(() => [

    'spinner',

    `spinner--${props.size}`

])

</script>


<template>

    <span
        :class="classes"
        role="status"
        aria-label="Loading"
    />

</template>