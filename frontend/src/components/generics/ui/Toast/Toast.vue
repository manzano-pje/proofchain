<!--
=========================================================
Project.......: ProofChain
Component.....: Toast
File..........: Toast.vue
Version.......: 1.0.0

Description...:
Toast notification component.

Responsibility:
Display temporary system notifications.

Dependencies..:
- Toast.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Toast.css'


interface ToastProps {

    variant?:
        | 'info'
        | 'success'
        | 'warning'
        | 'danger'

    title?: string

    message?: string

}


const props = withDefaults(
    defineProps<ToastProps>(),
    {
        variant: 'info'
    }
)


const emit = defineEmits<{

    (e: 'close'): void

}>()


const classes = computed(() => [

    'toast',

    `toast--${props.variant}`

])


function close() {

    emit('close')

}

</script>


<template>

    <aside :class="classes">

        <div class="toast__content">

            <strong
                v-if="title"
                class="toast__title"
            >

                {{ title }}

            </strong>


            <p
                v-if="message"
                class="toast__message"
            >

                {{ message }}

            </p>


            <slot />

        </div>


        <button
            class="toast__close"
            type="button"
            @click="close"
        >

            ×

        </button>


    </aside>

</template>