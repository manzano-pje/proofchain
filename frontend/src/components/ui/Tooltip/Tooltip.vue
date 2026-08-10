<!--
=========================================================
Project.......: ProofChain
Component.....: Tooltip
File..........: Tooltip.vue
Version.......: 1.0.0

Description...:
Tooltip information component.

Responsibility:
Display contextual information on interaction.

Dependencies..:
- Tooltip.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Tooltip.css'


interface TooltipProps {

    text: string

    position?:
        | 'top'
        | 'bottom'
        | 'left'
        | 'right'

}


const props = withDefaults(
    defineProps<TooltipProps>(),
    {
        position:'top'
    }
)


const classes = computed(() => [

    'tooltip',

    `tooltip--${props.position}`

])

</script>


<template>

    <span class="tooltip-wrapper">

        <slot />


        <span :class="classes">

            {{ text }}

        </span>


    </span>

</template>