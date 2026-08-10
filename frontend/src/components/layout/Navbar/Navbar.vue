<!--
=========================================================
Project.......: ProofChain
Component.....: Navbar
File..........: Navbar.vue
Version.......: 1.0.0

Description...:
Reusable navigation bar component.

Responsibility:
Provide the main navigation container.

Dependencies..:
- Navbar.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import './Navbar.css'

interface NavbarProps {

    fixed?: boolean

}

withDefaults(
    defineProps<NavbarProps>(),
    {
        fixed: false
    }
)

</script>

<template>

    <nav
        class="navbar"
        :class="{
            'navbar--fixed': fixed
        }"
    >

        <div class="navbar__brand">

            <slot name="brand" />

        </div>

        <div class="navbar__menu">

            <slot />

        </div>

        <div class="navbar__actions">

            <slot name="actions" />

        </div>

    </nav>

</template>