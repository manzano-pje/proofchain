<!--
=========================================================
Project.......: ProofChain
Component.....: Accordion
File..........: Accordion.vue
Version.......: 1.0.0

Description...:
Expandable content container.

Responsibility:
Display collapsible sections.

Dependencies..:
- Accordion.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Accordion.css'

interface AccordionProps {

    open?: boolean

    disabled?: boolean

}

const props = withDefaults(
    defineProps<AccordionProps>(),
    {
        open: false,
        disabled: false
    }
)

const emit = defineEmits<{

    (e: 'update:open', value: boolean): void

}>()

const classes = computed(() => [

    'accordion',

    {
        'accordion--open': props.open,
        'accordion--disabled': props.disabled
    }

])

function toggle() {

    if (props.disabled) return

    emit('update:open', !props.open)

}

</script>

<template>

    <section :class="classes">

        <button
            class="accordion__header"
            type="button"
            @click="toggle"
        >

            <slot name="header"/>

        </button>

        <div
            v-show="open"
            class="accordion__content"
        >

            <slot/>

        </div>

    </section>

</template>