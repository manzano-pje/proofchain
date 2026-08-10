<!--
=========================================================
Project.......: ProofChain
Component.....: Skeleton
File..........: Skeleton.vue
Version.......: 1.0.0

Description...:
Loading placeholder component.

Responsibility:
Represent unavailable content during loading.

Dependencies..:
- Skeleton.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Skeleton.css'


interface SkeletonProps {

    variant?:
        | 'text'
        | 'circle'
        | 'rectangle'

    width?: string

    height?: string

}


const props = withDefaults(
    defineProps<SkeletonProps>(),
    {
        variant: 'text',
        width: '100%',
        height: '1rem'
    }
)


const classes = computed(() => [

    'skeleton',

    `skeleton--${props.variant}`

])


const style = computed(() => ({

    width: props.width,

    height: props.height

}))

</script>


<template>

    <span
        :class="classes"
        :style="style"
    />

</template>