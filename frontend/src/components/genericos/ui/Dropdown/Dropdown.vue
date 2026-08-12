<!--
=========================================================
Project.......: ProofChain
Component.....: Dropdown
File..........: Dropdown.vue
Version.......: 1.0.0

Description...:
Dropdown menu component.

Responsibility:
Display contextual actions.

Dependencies..:
- Dropdown.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Dropdown.css'

interface DropdownProps {

    open?: boolean

}

const props = withDefaults(
    defineProps<DropdownProps>(),
    {
        open: false
    }
)

const emit = defineEmits<{

    (e: 'update:open', value: boolean): void

}>()

const classes = computed(() => [

    'dropdown',

    {
        'dropdown--open': props.open
    }

])

function toggle() {

    emit('update:open', !props.open)

}

</script>

<template>

    <div :class="classes">

        <button
            class="dropdown__trigger"
            type="button"
            @click="toggle"
        >

            <slot name="trigger"/>

        </button>

        <div
            v-show="open"
            class="dropdown__menu"
        >

            <slot/>

        </div>

    </div>

</template>