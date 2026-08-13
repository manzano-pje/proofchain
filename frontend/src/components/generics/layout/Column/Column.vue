<!--
=========================================================
Project.......: ProofChain
Component.....: Column
File..........: Column.vue
Version.......: 1.0.0

Description...:
Responsive grid column component.

Responsibility:
Represent a column inside Grid layouts.

Dependencies..:
- Column.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Column.css'

interface ColumnProps {

    span?: number

}

const props = withDefaults(
    defineProps<ColumnProps>(),
    {
        span: 12
    }
)

const styles = computed(() => ({

    gridColumn: `span ${props.span}`

}))

</script>

<template>

    <div
        class="column"
        :style="styles"
    >

        <slot />

    </div>

</template>