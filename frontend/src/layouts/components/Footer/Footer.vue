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
        <a href="/" class="logo">
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
          <li><a href="#" class="footer_nav-link">Início</a></li>
          <li><a href="#" class="footer_nav-link">Soluções</a></li>
          <li><a href="#" class="footer_nav-link">Como funciona</a></li>
          <li><a href="#" class="footer_nav-link">Aplicações</a></li>
          <li><a href="#" class="footer_nav-link">Planos</a></li>
        </ul>
        <ul class="footer__legal">
          <li><a href="#" class="footer_legal-link">Termos de uso</a></li>
          <li><a href="#" class="footer_legal-link">Privacidade</a></li>
        </ul>
      </div>

      <!--  CONTATO -->
      <div class="footer_contato">
        <a href="#" class="footer_contact-link">contato@proofchain.com.br</a>
      </div>
    </div>
    <div class="footer__copyright">
      <p class="footer_copyright-description">© 2026 ProofChain. Todos os direitos reservados.</p>
    </div>
  </footer>
</template>
