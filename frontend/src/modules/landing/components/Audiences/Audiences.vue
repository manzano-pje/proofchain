<!--
=========================================================
Project.......: ProofChain
Component.....: Audiences
File..........: Audiences.vue
Version.......: 1.0.0

Description...:
Landing page audiences section.

Responsibility:
Present the different audiences served by ProofChain
through a responsive carousel.

Dependencies..:
- Section
- Container
- Card
- Swiper
=========================================================
-->

<script setup lang="ts">
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay, A11y } from 'swiper/modules'

import 'swiper/css'
import './Audiences.css'
import Badge from '@/core/components/base/Badge/Badge.vue'

import Section from '@/core/components/ui/Section/Section.vue'
import Container from '@/core/components/ui/Container/Container.vue'
import Card from '@/core/components/ui/Card/Card.vue'

import { audiences } from '../../content/Audiences.ts'
</script>

<template>
  <Section class="section-audience">
    <Container>
      <div class="audiences">
        <header class="audiences__header">
          <div class="badge__heeader">Para quem é</div>
          <p>Feito para quem valoriza credibilidade</p>
        </header>

        <Swiper
          class="audiences__swiper"
          :modules="[Autoplay, A11y]"
          :slides-per-view="1"
          :space-between="24"
          :loop="true"
          :autoplay="{
            delay: 0,
            disableOnInteraction: false,
          }"
          :speed="10000"
          :breakpoints="{
            768: {
              slidesPerView: 2,
            },
            1024: {
              slidesPerView: 5,
            },
          }"
        >
          <SwiperSlide v-for="audience in audiences" :key="audience.id">
            <Card>
              <div class="audiences__image">
                <img :src="audience.image" :alt="audience.title" />
              </div>

              <div class="audiences__content">
                <h3 class="card__title">{{ audience.title }}</h3>
                <p>{{ audience.description }}</p>
              </div>
            </Card>
          </SwiperSlide>
        </Swiper>
      </div>
    </Container>
  </Section>
</template>
