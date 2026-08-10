<!--
=========================================================
Project.......: ProofChain
Component.....: Sidebar
File..........: Sidebar.vue
Version.......: 1.0.0

Description...:
Reusable sidebar layout component.

Responsibility:
Provide a standardized lateral navigation area.

Dependencies..:
- Sidebar.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Sidebar.css'

interface SidebarProps {

    width?:
        | 'sm'
        | 'md'
        | 'lg'

    collapsed?: boolean

}

const props = withDefaults(
    defineProps<SidebarProps>(),
    {
        width: 'md',
        collapsed: false
    }
)

const classes = computed(() => [

    'sidebar',

    `sidebar--${props.width}`,

    {
        'sidebar--collapsed': props.collapsed
    }

])

</script>

<template>

    <aside :class="classes">

        <slot />

    </aside>

</template>