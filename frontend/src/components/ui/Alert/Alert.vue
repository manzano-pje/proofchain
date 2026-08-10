<!--
=========================================================
Project.......: ProofChain
Component.....: Alert
File..........: Alert.vue
Version.......: 1.0.0

Description...:
Alert message component.

Responsibility:
Display contextual feedback.

Dependencies..:
- Alert.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Alert.css'

interface AlertProps{

    variant?:
        | 'info'
        | 'success'
        | 'warning'
        | 'danger'

}

const props = withDefaults(
    defineProps<AlertProps>(),
    {
        variant:'info'
    }
)

const classes = computed(()=>([

    'alert',

    `alert--${props.variant}`

]))

</script>

<template>

    <div
        :class="classes"
        role="alert"
    >

        <slot/>

    </div>

</template>