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
import './Footer.css'
</script>

<template>
  <footer id="rodape" class="footer">
    <div class="footer__content">
      <!-- Logo -->
      <div class="footer__brand">
        <a href="/" class="footer__logo">
          <img
            src="@/assets/images/logo/logo_horizontal_black.svg"
            alt="ProofChain Logo"
            class="footer__logo"
          />
        </a>
        <P class="footer__description"
          >Plataforma completa de emissão, gestão e validação de certificados e diplomas digitais
          com tecnologia Blockchain.</P
        >
      </div>
      <!-- Navegação desktop/tablet -->
      <div class="footer__menu">
        <ul class="footer__nav">
          <p class="footer__nav-titulo">PRODUTO</p>
          <li><a href="#audiences" class="footer_nav-link">Para quem é</a></li>
          <li><a href="#flow" class="footer_nav-link">Como funciona</a></li>
          <li><a href="#crown-carousel" class="footer_nav-link">Por que escolher</a></li>
          <li><a href="#planos" class="footer_nav-link">Planos</a></li>
        </ul>
        <ul class="footer__legal">
          <p class="footer__nav-titulo">LEGAL</p>
          <li><a href="#" class="footer_legal-link">Termos de uso</a></li>
          <li><a href="#" class="footer_legal-link">Privacidade</a></li>
        </ul>
      </div>
    </div>
    <div class="footer__copyright">
      <p class="footer__copyright-description">© 2026 ProofChain. Todos os direitos reservados.</p>
    </div>
  </footer>
</template>
