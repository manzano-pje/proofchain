<!--
=========================================================
Project.......: ProofChain
Component.....: Section
File..........: Section.vue
Version.......: 1.0.0

Description...:
Reusable section layout component.

Responsibility:
Standardize page sections with spacing.

Dependencies..:
- Section.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'
import './Section.css'

interface SectionProps {
    spacing?:
        | 'none'
        | 'sm'
        | 'md'
        | 'lg'
        | 'xl'
}

const props = withDefaults(
    defineProps<SectionProps>(),
    {
        spacing: 'lg'
    }
)

const classes = computed(() => [
    'section',
    `section--${props.spacing}`
])

</script>

<template>
    <section :class="classes">
        <slot />
    </section>
</template>