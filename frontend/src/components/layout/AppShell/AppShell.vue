<!--
=========================================================
Project.......: ProofChain
Component.....: AppShell
File..........: AppShell.vue
Version.......: 1.0.0

Description...:
Application layout component.

Responsibility:
Compose the primary application layout.

Dependencies..:
- AppShell.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import './AppShell.css'

</script>

<template>

    <div class="app-shell">

        <header
            v-if="$slots.header"
            class="app-shell__header"
        >
            <slot name="header" />
        </header>

        <aside
            v-if="$slots.sidebar"
            class="app-shell__sidebar"
        >
            <slot name="sidebar" />
        </aside>

        <main class="app-shell__content">

            <slot />

        </main>

        <footer
            v-if="$slots.footer"
            class="app-shell__footer"
        >
            <slot name="footer" />
        </footer>

    </div>

</template>