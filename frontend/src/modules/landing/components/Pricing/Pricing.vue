<template>
  <!-- ========================================================
       PRICING
       Seção de apresentação dos planos do ProofChain.
  ========================================================= -->

  <section id="planos" class="pricing" aria-labelledby="pricing-title">
    <div class="pricing__container">
      <!-- ======================================================
           HEADER
           Apresentação e contextualização da seção.
      ====================================================== -->

      <header class="pricing__header">
        <div class="title">Planos</div>

        <h2 class="subtitle">Escolha o plano ideal para o seu negócio</h2>

        <p class="pricing__subtitle">
          Tenha controle sobre seus certificados, garanta autenticidade e ofereça uma experiência
          segura para quem recebe suas credenciais.
        </p>
      </header>

      <!-- ======================================================
           PLANS
           Grid responsável pela apresentação dos quatro planos.
      ====================================================== -->

      <div class="pricing__plans">
        <article
          v-for="plan in pricingPlans"
          :key="plan.id"
          class="pricing__card"
          :class="{
            'pricing__card--featured': plan.featured,
          }"
        >
          <!-- ==================================================
               BADGE
               Identificação visual do plano em destaque.
          ================================================== -->

          <div v-if="plan.badge" class="pricing__badge">
            {{ plan.badge }}
          </div>

          <!-- ==================================================
               CARD HEADER
               Nome e descrição do plano.
          ================================================== -->

          <header class="pricing__card-header">
            <h3 class="pricing__card-title">
              {{ plan.name }}
            </h3>

            <p class="pricing__card-description">
              {{ plan.description }}
            </p>
          </header>

          <!-- ==================================================
               PRICE
               Apresentação do valor e periodicidade.
          ================================================== -->

          <div class="pricing__price">
            <span
              class="pricing__price-value"
              :class="{
                'pricing__price-value--custom': plan.price === 'Personalizado',
              }"
            >
              {{ plan.price }}
            </span>

            <span v-if="plan.period" class="pricing__price-period">
              {{ plan.period }}
            </span>
          </div>

          <p v-if="plan.billing" class="pricing__billing">
            {{ plan.billing }}
          </p>

          <!-- ==================================================
               DIVIDER
               Separação visual entre preço e recursos.
          ================================================== -->

          <div class="pricing__divider" aria-hidden="true" />

          <!-- ==================================================
               FEATURES
               Recursos disponíveis em cada plano.
          ================================================== -->

          <ul class="pricing__features">
            <li
              v-for="feature in plan.features"
              :key="feature.text"
              class="pricing__feature"
              :class="{
                'pricing__feature--disabled': !feature.included,
              }"
            >
              <span class="pricing__feature-icon" aria-hidden="true">
                {{ feature.included ? '✓' : '−' }}
              </span>

              <span class="pricing__feature-text">
                {{ feature.text }}
              </span>
            </li>
          </ul>

          <!-- ==================================================
               ACTION
               CTA específico de cada plano.
          ================================================== -->

          <div class="pricing__actions">
            <a
              href="#"
              class="pricing__button"
              :class="{
                'pricing__button--primary': plan.featured,
                'pricing__button--secondary': !plan.featured,
              }"
            >
              {{ plan.cta }}
            </a>
          </div>
        </article>
      </div>

      <!-- ======================================================
           FOOTER
           Observação complementar da seção.
      ====================================================== -->

      <footer class="pricing__footer">
        <p class="pricing__footer-text">Precisa de uma solução personalizada?</p>
        <p class="pricing__footer-text">
          Fale com nossa equipe para encontrar a melhor configuração para sua operação.
        </p>
      </footer>
    </div>
  </section>
</template>

<script setup lang="ts">
import { pricingPlans } from '../../content/Pricing.ts'
</script>

<style src="./Pricing.css"></style>
