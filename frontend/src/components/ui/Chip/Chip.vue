<!--
=========================================================
Project.......: ProofChain
Component.....: Chip
File..........: Chip.vue
Version.......: 1.0.0

Description...:
Compact selectable label component.

Responsibility:
Display small tags with optional removal.

Dependencies..:
- Chip.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Chip.css'

interface ChipProps {

    removable?: boolean

    disabled?: boolean

}

const props = withDefaults(
    defineProps<ChipProps>(),
    {
        removable: false,
        disabled: false
    }
)

const emit = defineEmits<{

    (e: 'remove'): void

}>()

const classes = computed(() => [

    'chip',

    {
        'chip--disabled': props.disabled
    }

])

function remove() {

    if (props.disabled) return

    emit('remove')

}

</script>

<template>

    <span :class="classes">

        <span class="chip__label">

            <slot />

        </span>

        <button
            v-if="removable"
            class="chip__remove"
            type="button"
            @click="remove"
        >

            ×

        </button>

    </span>

</template>