<!--
=========================================================
Project.......: ProofChain
Component.....: Row
File..........: Row.vue
Version.......: 1.0.0

Description...:
Flex row layout component.

Responsibility:
Provide standardized horizontal layouts.

Dependencies..:
- Row.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Row.css'

interface RowProps {

    gap?:
        | 'xs'
        | 'sm'
        | 'md'
        | 'lg'
        | 'xl'

    align?:
        | 'start'
        | 'center'
        | 'end'
        | 'stretch'

    justify?:
        | 'start'
        | 'center'
        | 'end'
        | 'between'
        | 'around'
        | 'evenly'

    wrap?: boolean

}

const props = withDefaults(
    defineProps<RowProps>(),
    {
        gap: 'md',
        align: 'center',
        justify: 'start',
        wrap: true
    }
)

const classes = computed(() => [

    'row',

    `row--gap-${props.gap}`,

    `row--align-${props.align}`,

    `row--justify-${props.justify}`,

    {
        'row--wrap': props.wrap,
        'row--no-wrap': !props.wrap
    }

])

</script>

<template>

    <div :class="classes">

        <slot />

    </div>

</template>