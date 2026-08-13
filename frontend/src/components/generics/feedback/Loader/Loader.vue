<!--
=========================================================
Project.......: ProofChain
Component.....: Loader
File..........: Loader.vue
Version.......: 1.0.0

Description...:
Loading state component.

Responsibility:
Display loading feedback during async operations.

Dependencies..:
- Loader.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Loader.css'

interface LoaderProps {

    size?:
        | 'sm'
        | 'md'
        | 'lg'

    text?: string

}

const props = withDefaults(
    defineProps<LoaderProps>(),
    {
        size: 'md',
        text: ''
    }
)

const classes = computed(() => [

    'loader',

    `loader--${props.size}`

])

</script>

<template>

    <div class="loader-container">

        <span :class="classes"></span>

        <span
            v-if="text"
            class="loader__text"
        >
            {{ text }}
        </span>

    </div>

</template>