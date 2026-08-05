/*
=========================================================
Project.......: ProofChain
Component.....: BaseButton
File..........: BaseButton.vue
Version.......: 1.0.0

Description...:
Reusable button component for user interactions.

Responsibility:
Provide a standardized button interface for the
ProofChain Design System.

Dependencies..:
- BaseButton.css

Methodology...:
BEM

=========================================================
*/

<script setup lang="ts">

import { computed } from 'vue'
import './BaseButton.css'

interface BaseButtonProps {

    variant?:
    | 'primary'
    | 'secondary'
    | 'outline'
    | 'ghost'
    | 'danger'
    | 'success'

    size?:
    | 'sm'
    | 'md'
    | 'lg'

    type?:
    | 'button'
    | 'submit'
    | 'reset'

    disabled?: boolean
    loading?: boolean
    fullWidth?: boolean
    ariaLabel?: string
}

const props = withDefaults(
    defineProps<BaseButtonProps>(),
    {
        variant: 'primary',
        size: 'md',
        type: 'button',
        disabled: false,
        loading: false,
        fullWidth: false
    }
)

const emit = defineEmits<{
    (e: 'click', event: MouseEvent): void
}>()

const classes = computed(() => [
    'button',
    `button--${props.variant}`,
    `button--${props.size}`,
    {
        'button--loading': props.loading,
        'button--full': props.fullWidth
    }
])

function handleClick(event: MouseEvent) {

    if (props.disabled || props.loading) {
        return
    }

    emit('click', event)

}

</script>

<template>

    <button :type="type" :class="classes" :disabled="disabled || loading" :aria-label="ariaLabel"
        :aria-disabled="disabled || loading" @click="handleClick">

        <span v-if="$slots['icon-left']" class="button__icon">
            <slot name="icon-left" />
        </span>

        <span class="button__content">
            <slot />
        </span>

        <span v-if="loading" class="button__spinner" />

        <span v-else-if="$slots['icon-right']" class="button__icon">
            <slot name="icon-right" />
        </span>

    </button>

</template>