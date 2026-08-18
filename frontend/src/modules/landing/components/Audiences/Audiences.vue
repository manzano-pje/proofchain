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
import { Navigation, Pagination, A11y } from 'swiper/modules'

import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'

import Section from '@/core/components/ui/Section/Section.vue'
import Container from '@/core/components/ui/Container/Container.vue'
import Card from '@/core/components/ui/Card/Card.vue'

import { audiences } from '../../content/Audiences.ts'
</script>

<template>
  <Section>
    <Container>
      <div class="audiences">
        <header class="audiences__header">
          <h2>Para quem é</h2>

          <p>Feito para quem valoriza credibilidade</p>
        </header>

        <Swiper
          :modules="[Navigation, Pagination, A11y]"
          :slides-per-view="1"
          :space-between="24"
          navigation
          :pagination="{ clickable: true }"
          :breakpoints="{
            768: {
              slidesPerView: 2,
            },
            1024: {
              slidesPerView: 3,
            },
          }"
        >
          <SwiperSlide v-for="audience in audiences" :key="audience.id">
            <Card>
              <img :src="audience.image" :alt="audience.title" />

              <h3>{{ audience.title }}</h3>

              <p>{{ audience.description }}</p>
            </Card>
          </SwiperSlide>
        </Swiper>
      </div>
    </Container>
  </Section>
</template>
