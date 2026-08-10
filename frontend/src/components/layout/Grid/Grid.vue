<!--
=========================================================
Project.......: ProofChain
Component.....: Grid
File..........: Grid.vue
Version.......: 1.0.0

Description...:
CSS Grid layout component.

Responsibility:
Provide standardized grid layouts.

Dependencies..:
- Grid.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Grid.css'

interface GridProps {

    columns?: number

    gap?:
        | 'xs'
        | 'sm'
        | 'md'
        | 'lg'
        | 'xl'

}

const props = withDefaults(
    defineProps<GridProps>(),
    {
        columns: 12,
        gap: 'md'
    }
)

const styles = computed(() => ({

    '--grid-columns': props.columns

}))

const classes = computed(() => [

    'grid',

    `grid--gap-${props.gap}`

])

</script>

<template>

    <div
        :class="classes"
        :style="styles"
    >

        <slot />

    </div>

</template>