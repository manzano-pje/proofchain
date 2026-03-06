<template>
  <HeroSection />
  <ValidacaoSection />
  <RecursosSection />
  <FuncionamentoSection />
  <PlanosSection @select-plan="selecionaOpcao" />
  <AcquisitionModal
    :form-data="formData"
    :errors="errors"
    @submit="submitForm"
  />
  <ResponseModal
    :title="modalTitle"
    :message="modalMessage"
    :type="modalType"
  />
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import HeroSection from '@/components/HeroSection.vue'
import ValidacaoSection from '@/components/ValidacaoSection.vue'
import RecursosSection from '@/components/RecursosSection.vue'
import FuncionamentoSection from '@/components/FuncionamentoSection.vue'
import PlanosSection from '@/components/PlanosSection.vue'
import AcquisitionModal from '@/components/AcquisitionModal.vue'
import ResponseModal from '@/components/ResponseModal.vue'

const modalTitle = ref('')
const modalMessage = ref('')
const modalType = ref<'success' | 'danger' | 'warning' | 'info'>('success')

declare const bootstrap: any

function openResponseModal(
  title: string,
  message: string,
  type: 'success' | 'danger' | 'warning' | 'info'
) {
  modalTitle.value = title
  modalMessage.value = message
  modalType.value = type
  const el = document.getElementById('responseModal')
  if (el) {
    const modal = bootstrap.Modal.getOrCreateInstance(el)
    modal.show()
  }
}

const formData = reactive({
  name: '',
  cnpj: '',
  userName: '',
  email: '',
  password: '',
  planId: 0
})

function selecionaOpcao(id: number) {
  formData.planId = id
}

const errors = reactive({
  name: '',
  cnpj: '',
  userName: '',
  email: '',
  password: '',
  planId: 0
})

function validateCNPJ(cnpj: string): boolean {
  cnpj = cnpj.replace(/[^\d]+/g, '')

  if (cnpj.length !== 14) return false
  if (/^(\d)\1+$/.test(cnpj)) return false

  let tamanho = cnpj.length - 2
  let numeros = cnpj.substring(0, tamanho)
  const digitos = cnpj.substring(tamanho)
  let soma = 0
  let pos = tamanho - 7

  for (let i = tamanho; i >= 1; i--) {
    soma += Number(numeros.charAt(tamanho - i)) * pos--
    if (pos < 2) pos = 9
  }

  let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11)
  if (resultado !== Number(digitos.charAt(0))) return false

  tamanho = tamanho + 1
  numeros = cnpj.substring(0, tamanho)
  soma = 0
  pos = tamanho - 7

  for (let i = tamanho; i >= 1; i--) {
    soma += Number(numeros.charAt(tamanho - i)) * pos--
    if (pos < 2) pos = 9
  }

  resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11)
  return resultado === Number(digitos.charAt(1))
}

function validateEmail(email: string): boolean {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

function validatePassword(password: string): boolean {
  const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
  return regex.test(password)
}


function closeAcquisitionModal() {
  const modalElement = document.getElementById('acquisitionModal')
  if (modalElement) {
    const modalInstance =
      bootstrap.Modal.getInstance(modalElement) ||
      new bootstrap.Modal(modalElement)
      formData.name = ''
      formData.cnpj = ''
      formData.userName = ''
      formData.email = ''
      formData.password = ''
      formData.planId = 0
    modalInstance.hide()
  }
}

async function submitForm() {
  errors.name = ''
  errors.cnpj = ''
  errors.userName = ''
  errors.email = ''
  errors.password = ''
  errors.planId = 0
  
  let isValid = true

  if (!validateCNPJ(formData.cnpj)) {
    errors.cnpj = 'CNPJ inválido'
    isValid = false
  }

  if (!validateEmail(formData.email)) {
    errors.email = 'E-mail inválido'
    isValid = false
  }

  if (!validatePassword(formData.password)) {
    errors.password =
      'A senha deve ter no mínimo 8 caracteres, letras e números'
    isValid = false
  }

  if (!isValid) {
    const mensagens = [errors.cnpj, errors.email, errors.password].filter(
      Boolean
    )
    openResponseModal(
      'Erro de validação',
      mensagens.join('. ') || 'Verifique os campos do formulário.',
      'warning'
    )
    return
  }

  try {
    const response = await fetch('http://localhost:8080/instituition', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: formData.name,
        cnpj: formData.cnpj,
        userName: formData.userName,
        email: formData.email,
        password: formData.password,
        idPlan: formData.planId
      })
    })

    let message = 'Resposta inesperada do servidor.'
    try {
      console.log("STATUS DA RESPOSTA:", response.status)
      const data = await response.json()
      if (data?.message) message = data.message
      console.log("RESPOSTA DO BACKEND:", data)
    } catch {
       console.error("ERRO NA REQUISIÇÃO:", error)
      // resposta não é JSON (ex.: HTML de erro)
    }

    switch (response.status) {
      case 200:
      case 201:
        closeAcquisitionModal()
        openResponseModal('Sucesso', message, 'success')
        break
      case 403:
        closeAcquisitionModal()
        openResponseModal('Proibido', message, 'warning')
        break
      case 404:
        closeAcquisitionModal()
        openResponseModal('Não encontrado', message, 'info')
        break  
      case 409:
        // closeAcquisitionModal()
        openResponseModal('Conflito', message, 'warning')
        break
      case 500:
        closeAcquisitionModal()
        openResponseModal('Erro interno', message, 'danger')
        break
      default:
        closeAcquisitionModal()
        openResponseModal('Erro', message, 'danger')
    }
  } catch (error) {
    closeAcquisitionModal()
    const msg =
      error instanceof Error
        ? error.message
        : 'Falha na conexão. Tente novamente.'
    openResponseModal('Erro', msg, 'danger')
  }
}
</script>
