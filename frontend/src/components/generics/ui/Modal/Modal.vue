<!--
=========================================================
Project.......: ProofChain
Component.....: Modal
File..........: Modal.vue
Version.......: 1.0.0

Description...:
Modal dialog component.

Responsibility:
Display overlay dialogs.

Dependencies..:
- Modal.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './Modal.css'

interface ModalProps {

    open?: boolean

    size?:
        | 'sm'
        | 'md'
        | 'lg'

}

const props = withDefaults(
    defineProps<ModalProps>(),
    {
        open: false,
        size: 'md'
    }
)

const emit = defineEmits<{

    (e:'close'): void

}>()

const classes = computed(() => [

    'modal',

    `modal--${props.size}`

])

function close() {

    emit('close')

}

</script>

<template>

    <Teleport to="body">

        <div
            v-if="open"
            class="modal__overlay"
            @click="close"
        >

            <section
                :class="classes"
                @click.stop
            >

                <header
                    v-if="$slots.header"
                    class="modal__header"
                >

                    <slot name="header"/>

                </header>


                <main class="modal__body">

                    <slot/>

                </main>


                <footer
                    v-if="$slots.footer"
                    class="modal__footer"
                >

                    <slot name="footer"/>

                </footer>

            </section>

        </div>

    </Teleport>

</template>