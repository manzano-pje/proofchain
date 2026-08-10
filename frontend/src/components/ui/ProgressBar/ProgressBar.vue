<!--
=========================================================
Project.......: ProofChain
Component.....: ProgressBar
File..........: ProgressBar.vue
Version.......: 1.0.0

Description...:
Progress indicator component.

Responsibility:
Display completion percentage.

Dependencies..:
- ProgressBar.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import { computed } from 'vue'

import './ProgressBar.css'

interface ProgressBarProps {

    value?: number

    max?: number

}

const props = withDefaults(
    defineProps<ProgressBarProps>(),
    {
        value: 0,
        max: 100
    }
)

const percentage = computed(() => {

    const result = (props.value / props.max) * 100

    return Math.min(Math.max(result, 0), 100)

})

const style = computed(() => ({

    width: `${percentage.value}%`

}))

</script>

<template>

    <div class="progress-bar">

        <div class="progress-bar__track">

            <div
                class="progress-bar__value"
                :style="style"
            />

        </div>

    </div>

</template>