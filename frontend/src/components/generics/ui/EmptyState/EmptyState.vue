<!--
=========================================================
Project.......: ProofChain
Component.....: EmptyState
File..........: EmptyState.vue
Version.......: 1.0.0

Description...:
Empty state component.

Responsibility:
Display informative content when no data
is available.

Dependencies..:
- EmptyState.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import './EmptyState.css'

interface EmptyStateProps {

    title?: string

    description?: string

}

withDefaults(
    defineProps<EmptyStateProps>(),
    {}
)

</script>

<template>

    <section class="empty-state">

        <div
            v-if="$slots.icon"
            class="empty-state__icon"
        >

            <slot name="icon"/>

        </div>

        <h3
            v-if="title"
            class="empty-state__title"
        >

            {{ title }}

        </h3>

        <p
            v-if="description"
            class="empty-state__description"
        >

            {{ description }}

        </p>

        <div
            v-if="$slots.default"
            class="empty-state__actions"
        >

            <slot/>

        </div>

    </section>

</template>