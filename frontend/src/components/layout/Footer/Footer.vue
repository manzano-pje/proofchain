<!--
=========================================================
Project.......: ProofChain
Component.....: Footer
File..........: Footer.vue
Version.......: 1.0.0

Description...:
Reusable page footer component.

Responsibility:
Provide a standardized footer area for pages.

Dependencies..:
- PageFooter.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">

import './PageFooter.css'

</script>

<template>

    <footer class="footer">

        <slot />

    </footer>

</template>