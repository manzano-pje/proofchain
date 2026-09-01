<!--
=========================================================
Project.......: ProofChain
Component.....: Flow
File..........: Flow.vue
Version.......: 1.0.0

Description...:
Landing page flow section.

Responsibility:
Present the main steps of the ProofChain flow,
from registration to credential validation.

Dependencies..:
- Section
- Container
- Flow.ts
- Flow.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import Section from '@/core/components/ui/Section/Section.vue'
import Container from '@/core/components/ui/Container/Container.vue'

import { flowSteps } from '../../content/Flow.ts'

import './Flow.css'
</script>

<template>
  <Section id="flow" class="section-flow">
    <Container>
      <div class="flow">
        <header class="flow__header">
          <div class="badge__header">Como funciona</div>

          <h2 class="flow__heading">Do cadastro à validação</h2>
        </header>

        <div class="flow__steps">
          <article v-for="(step, index) in flowSteps" :key="step.id" class="flow__step">
            <!-- Conteúdo superior -->
            <div class="flow__top">
              <template v-if="index % 2 === 0">
                <div class="flow__text">
                  <h3 class="flow__title">
                    {{ step.title }}
                  </h3>

                  <p class="flow__description">
                    {{ step.description }}
                  </p>
                </div>
              </template>

              <template v-else>
                <div class="flow__image">
                  <img :src="step.image" :alt="step.alt" />
                </div>
              </template>
            </div>

            <!-- Marcador central -->
            <div class="flow__marker">
              {{ step.number }}
            </div>

            <!-- Conteúdo inferior -->
            <div class="flow__bottom">
              <template v-if="index % 2 === 0">
                <div class="flow__image">
                  <img :src="step.image" :alt="step.alt" />
                </div>
              </template>

              <template v-else>
                <div class="flow__text">
                  <h3 class="flow__title">
                    {{ step.title }}
                  </h3>

                  <p class="flow__description">
                    {{ step.description }}
                  </p>
                </div>
              </template>
            </div>
          </article>
        </div>
      </div>
    </Container>
  </Section>
</template>
