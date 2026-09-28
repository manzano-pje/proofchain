<!--
=========================================================
Project.......: ProofChain
Module........: Business / Admin
Feature.......: Gestão de instituição
File..........: InstitutionPage.vue

Description...:
Carrega e atualiza os dados da instituição autenticada.

Responsibilities:
- Consultar os dados da instituição atual.
- Coordenar o envio do formulário e apresentar feedback.

Dependencies..:
- InstitutionForm.vue
- Institution.service.ts
- Auth.store.ts

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'

import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { institutionService, type InstitutionRecord } from '@/modules/business/services/Institution.service'
import type { InstitutionUpdatePayload } from './Types'
import InstitutionForm from './InstitutionForm.vue'

const authStore = useAuthStore()
const institution = ref<InstitutionRecord | null>(null)
const isLoading = ref(false)
const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const readonlyData = computed(() => ({
  name: institution.value?.name ?? '',
  cnpj: institution.value?.cnpj ?? '',
  email: institution.value?.email ?? '',
}))

const initialData = computed(() => ({
  postalCode: institution.value?.postalCode ?? '',
  phone: institution.value?.phone ?? '',
  address: institution.value?.address ?? '',
  number: institution.value?.number ?? '',
  complement: institution.value?.complement ?? '',
  neighborhood: institution.value?.neighborhood ?? '',
  city: institution.value?.city ?? '',
  state: institution.value?.state ?? '',
}))

async function loadInstitution(): Promise<void> {
  const token = authStore.session?.accessToken
  if (!token) {
    errorMessage.value = 'Sua sessão expirou. Entre novamente para continuar.'
    return
  }

  isLoading.value = true
  errorMessage.value = ''
  try {
    institution.value = await institutionService.getCurrent(token)
  } catch (error) {
    errorMessage.value = error instanceof Error
      ? error.message
      : 'Não foi possível carregar os dados da instituição.'
  } finally {
    isLoading.value = false
  }
}

async function saveInstitution(payload: InstitutionUpdatePayload): Promise<void> {
  if (payload.logo || payload.removeLogo) {
    errorMessage.value = 'A API ainda não permite salvar ou remover a logo. Remova essa alteração para salvar os demais dados.'
    return
  }

  const token = authStore.session?.accessToken
  if (!token) {
    errorMessage.value = 'Sua sessão expirou. Entre novamente para continuar.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''
  successMessage.value = ''
  try {
    await institutionService.updateCurrent(token, payload)
    if (institution.value) {
      institution.value = {
        ...institution.value,
        postalCode: payload.postalCode,
        phone: payload.phone,
        address: payload.address,
        number: payload.number,
        complement: payload.complement,
        neighborhood: payload.neighborhood,
        city: payload.city,
        state: payload.state,
      }
    }
    successMessage.value = 'Dados da instituição atualizados.'
  } catch (error) {
    errorMessage.value = error instanceof Error
      ? error.message
      : 'Não foi possível atualizar os dados da instituição.'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadInstitution)
</script>

<template>
  <section class="institution-page" aria-label="Dados da instituição">
    <p v-if="isLoading" role="status">Carregando dados da instituição...</p>
    <p v-if="errorMessage" class="institution-page__message institution-page__message--error" role="alert">
      {{ errorMessage }}
    </p>
    <p v-if="successMessage" class="institution-page__message institution-page__message--success" role="status">
      {{ successMessage }}
    </p>

    <button
      v-if="errorMessage && !institution"
      class="institution-page__retry"
      type="button"
      @click="loadInstitution"
    >
      Tentar novamente
    </button>

    <InstitutionForm
      v-if="institution"
      :readonly-data="readonlyData"
      :initial-data="initialData"
      :submitting="isSubmitting"
      @submit="saveInstitution"
    />
  </section>
</template>

<style scoped src="./InstitutionPage.css"></style>
