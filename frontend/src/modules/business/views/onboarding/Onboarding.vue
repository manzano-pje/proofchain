<!--
=========================================================
Project.......: ProofChain
Module........: Business
Feature.......: Onboarding
File..........: Onboarding.vue
Version.......: 1.0.0

Description...:
Tela de configuração inicial da instituição e da conta
administrativa, com seleção do plano contratado.

Responsibilities:
- Apresentar o formulário de onboarding.
- Validar os dados preenchidos pelo usuário.
- Encaminhar os dados válidos para o serviço de onboarding.

Dependencies..:
- Section
- Container
- BaseButton
- Onboarding.service.ts
- OnboardingRequest
- Validators

Methodology...:
BEM
=========================================================
-->

<template>
  <div class="onboarding__page">
    <Section class="onboarding__section">
      <Container class="onboarding__container">
        <!-- ============================================================
             HEADER
             Identidade visual da tela de configuração inicial.
             ============================================================ -->
        <header class="onboarding__header">
          <div class="onboarding__header-brand">
            <!-- Utilizando ícone/logo como na referência image_e03306.jpg -->
            <span class="onboarding__brand-logo">
              <a href="/">
                <img
                  src="@/assets/images/logo/logo_horizontal_light.svg"
                  alt="ProofChain Logo"
                  class="onboarding__header-logo"
                />
              </a>
            </span>
          </div>
        </header>

        <!-- ============================================================
             INTRODUÇÃO E PROGRESSO
             Contextualiza a primeira etapa do cadastro.
             ============================================================ -->
        <div class="onboarding__top-layout">
          <section class="onboarding__intro">
            <div>
              <span class="onboarding__intro-badge">PRIMEIRO ACESSO</span>
              <h1 class="onboarding__intro-title">Crie sua instituição</h1>
            </div>
            <p class="onboarding__intro-description">
              Configure sua conta ProofChain<br />
              em poucos passos.
            </p>
          </section>
        </div>

        <!-- ============================================================
             FORMULÁRIO
             Coleta os dados necessários para o cadastro.
             ============================================================ -->
        <form class="onboarding__form" @submit.prevent="handleSubmit" novalidate>
          <!-- SEÇÃO 01: DADOS DA INSTITUIÇÃO -->
          <section class="onboarding__form-section onboarding__form-section--account">
            <div class="onboarding__section-heading">
              <span class="onboarding__section-number">01</span>
              <div>
                <h2>Dados da instituição</h2>
                <p class="onboarding__section-description">Informações básicas da organização.</p>
              </div>
            </div>

            <div class="onboarding__field-group onboarding__field-group--two-col">
              <div class="onboarding__field">
                <label for="name">Nome da instituição</label>
                <input
                  id="name"
                  v-model="form.name"
                  type="text"
                  placeholder="Ex: Minha Escola"
                  :class="{ 'is-invalid': touched.name && errors.name }"
                  @blur="validateField('name')"
                />
                <span v-if="touched.name && errors.name" class="error-message">{{
                  errors.name
                }}</span>
              </div>

              <div class="onboarding__field">
                <label for="cnpj">CNPJ</label>
                <input
                  id="cnpj"
                  v-model="form.cnpj"
                  type="text"
                  inputmode="numeric"
                  autocomplete="organization"
                  placeholder="00.000.000/0000-00"
                  :class="{ 'is-invalid': touched.cnpj && errors.cnpj }"
                  @blur="validateField('cnpj')"
                />
                <span v-if="touched.cnpj && errors.cnpj" class="error-message">{{
                  errors.cnpj
                }}</span>
              </div>
            </div>
          </section>

          <!-- SEÇÃO 02: CONTA ADMINISTRATIVA -->
          <section class="onboarding__form-section">
            <div class="onboarding__section-heading">
              <span class="onboarding__section-number">02</span>
              <div>
                <h2>Conta administrativa</h2>
                <p class="section-description">Defina as credenciais de acesso.</p>
              </div>
            </div>

            <div class="onboarding__field-group">
              <div class="onboarding__field">
                <label for="userName">Nome de usuário</label>
                <input
                  id="userName"
                  v-model="form.userName"
                  type="text"
                  autocomplete="username"
                  placeholder="Seu nome de acesso"
                  :class="{ 'is-invalid': touched.userName && errors.userName }"
                  @blur="validateField('userName')"
                />
                <span v-if="touched.userName && errors.userName" class="error-message">{{
                  errors.userName
                }}</span>
              </div>

              <div class="onboarding__field">
                <label for="email">E-mail</label>
                <input
                  id="email"
                  v-model="form.email"
                  type="email"
                  autocomplete="email"
                  placeholder="seu@email.com"
                  :class="{ 'is-invalid': touched.email && errors.email }"
                  @blur="validateField('email')"
                />
                <span v-if="touched.email && errors.email" class="error-message">{{
                  errors.email
                }}</span>
              </div>

              <div class="onboarding__field">
                <label for="password">Senha</label>
                <div class="onboarding__input-wrapper">
                  <input
                    id="password"
                    v-model="form.password"
                    type="password"
                    autocomplete="new-password"
                    placeholder="Mínimo 8 caracteres"
                    :class="{ 'is-invalid': touched.password && errors.password }"
                    @blur="validateField('password')"
                  />
                  <span class="password-toggle-icon"></span>
                </div>
                <span v-if="touched.password && errors.password" class="error-message">{{
                  errors.password
                }}</span>
              </div>
            </div>
          </section>

          <!-- SEÇÃO 03: PLANO SELECIONADO -->
          <section class="onboarding__form-section onboarding__plan-section">
            <div class="onboarding__section-heading">
              <span class="onboarding__section-number">03</span>
              <div>
                <h2>Plano selecionado</h2>
                <p class="section-description">Configuração escolhida para sua instituição.</p>
              </div>
            </div>

            <div class="onboarding__plan-summary">
              <div class="onboarding__plan-details-left">
                <span class="onboarding__plan-label">PLANO</span>
                <strong class="onboarding__plan-name">{{
                  selectedPlan?.name || 'Plano não identificado'
                }}</strong>
                <p class="onboarding__plan-description">{{ selectedPlan?.description || '' }}</p>
              </div>
              <div class="onboarding__plan-price">
                {{ selectedPlan?.price || '--' }}
              </div>
            </div>
          </section>

          <!-- AÇÕES: ENVIO DO FORMULÁRIO -->
          <div class="onboarding__form-actions">
            <span class="onboarding__security-badge">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              </svg>
              Configuração segura
            </span>
            <BaseButton
              class="onboarding__submit-button"
              type="submit"
              :loading="isSubmitting"
              :disabled="!isValid || isSubmitting"
            >
              Criar instituição &rarr;
            </BaseButton>
          </div>
        </form>

        <!-- FOOTER: IDENTIFICAÇÃO DA CONFIGURAÇÃO -->
        <footer class="onboarding__footer">
          <span class="onboarding__footer-copy">© ProofChain</span>
          <span class="onboarding__footer-label">Configuração inicial</span>
        </footer>
      </Container>
    </Section>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import Container from '@/core/components/ui/Container/Container.vue'
import Section from '@/core/components/ui/Section/Section.vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { onboardingService } from '@/modules/business/services/Onboarding.service'
import type { OnboardingRequest } from '@/modules/business/types/OnboardingRequest'
import { validateCNPJ, validateEmail } from '@/core/utils/Validators'

/* ============================================================
   TIPOS
   ============================================================ */

type FormField = keyof OnboardingRequest
type EditableField = Exclude<FormField, 'idPlan'>

/* ============================================================
   PLANOS
   ============================================================ */

const plans = [
  {
    id: 1,
    name: 'Free',
    price: 'R$ 0,00 /mês',
    description: 'Para começar a emitir certificados digitais.',
  },
  {
    id: 2,
    name: 'Starter',
    price: 'R$ 49,00/mês',
    description: 'Para pequenas operações que precisam de mais recursos.',
  },
  {
    id: 3,
    name: 'Professional',
    price: 'R$ 99,00/mês',
    description: 'Para instituições que precisam de escala e controle.',
  },
]

export default defineComponent({
  name: 'DataOnboarding',

  components: {
    Container,
    Section,
    BaseButton,
  },

  setup() {
    /* ==========================================================
       ROTA
       ========================================================== */

    const route = useRoute()
    const queryPlanId = route.query.planId

    const planId =
      typeof queryPlanId === 'string' && /^\d+$/.test(queryPlanId) ? Number(queryPlanId) : 0

    /* ==========================================================
       FORMULÁRIO
       ========================================================== */

    const form = reactive<OnboardingRequest>({
      name: '',
      cnpj: '',
      userName: '',
      email: '',
      password: '',
      idPlan: planId,
    })

    /* ==========================================================
       ERROS
       ========================================================== */

    const errors = reactive<Record<FormField, string>>({
      name: '',
      cnpj: '',
      userName: '',
      email: '',
      password: '',
      idPlan: '',
    })

    /* ==========================================================
       TOUCHED
       Controla quando o erro deve aparecer visualmente.
       ========================================================== */

    const touched = reactive<Record<EditableField, boolean>>({
      name: false,
      cnpj: false,
      userName: false,
      email: false,
      password: false,
    })

    /* ==========================================================
       ESTADOS
       ========================================================== */

    const isSubmitting = ref(false)
    const selectedPlan = ref<(typeof plans)[number] | null>(null)

    /* ==========================================================
       VALIDAÇÃO DE CAMPO
       ========================================================== */

    const validateFieldPure = (field: FormField): string => {
      switch (field) {
        /* ------------------------------------------------------
           NOME DA INSTITUIÇÃO
           ------------------------------------------------------ */
        case 'name': {
          const value = form.name.trim()

          if (!value) {
            return 'Nome é obrigatório'
          }

          if (value.length < 5) {
            return 'Mínimo 5 caracteres'
          }

          if (value.length > 100) {
            return 'Máximo 100 caracteres'
          }

          return ''
        }

        /* ------------------------------------------------------
           CNPJ
           ------------------------------------------------------ */
        case 'cnpj': {
          const value = form.cnpj.trim()

          if (!value) {
            return 'CNPJ é obrigatório'
          }

          if (!validateCNPJ(value)) {
            return 'CNPJ inválido'
          }

          return ''
        }

        /* ------------------------------------------------------
           NOME DE USUÁRIO
           ------------------------------------------------------ */
        case 'userName': {
          const value = form.userName.trim()

          if (!value) {
            return 'Nome de usuário é obrigatório'
          }

          if (value.length < 5) {
            return 'Mínimo 5 caracteres'
          }

          if (value.length > 30) {
            return 'Máximo 30 caracteres'
          }

          return ''
        }

        /* ------------------------------------------------------
           E-MAIL
           ------------------------------------------------------ */
        case 'email': {
          const value = form.email.trim()

          if (!value) {
            return 'E-mail é obrigatório'
          }

          if (!validateEmail(value)) {
            return 'E-mail inválido'
          }

          return ''
        }

        /* ------------------------------------------------------
           SENHA
           ------------------------------------------------------ */
        case 'password': {
          const value = form.password

          if (!value) {
            return 'Senha é obrigatória'
          }

          if (value.length < 8) {
            return 'Mínimo 8 caracteres'
          }

          return ''
        }

        /* ------------------------------------------------------
           PLANO
           ------------------------------------------------------ */
        case 'idPlan': {
          const value = form.idPlan

          if (!value || value <= 0 || value >= 4) {
            return 'Plano inválido'
          }

          return ''
        }

        /* ------------------------------------------------------
           SEGURANÇA DO TYPESCRIPT
           ------------------------------------------------------ */
        default:
          return ''
      }
    }

    /* ==========================================================
       VALIDA UM CAMPO
       Esta é a ÚNICA função pública validateField.
       ========================================================== */

    const validateField = (field: FormField): void => {
      if (field !== 'idPlan') {
        touched[field] = true
      }

      errors[field] = validateFieldPure(field)
    }

    /* ==========================================================
       CAMPOS DO FORMULÁRIO
       ========================================================== */

    const fields: FormField[] = ['name', 'cnpj', 'userName', 'email', 'password', 'idPlan']

    /* ==========================================================
       VALIDAÇÃO SILENCIOSA
       Atualiza errors sem alterar touched.
       Assim:
       - errors pode ser atualizado internamente;
       - mensagens não aparecem antes da interação;
       - isValid sempre possui o estado correto.
       ========================================================== */

    const validateAllSilent = (): boolean => {
      fields.forEach((field) => {
        errors[field] = validateFieldPure(field)
      })

      return fields.every((field) => errors[field] === '')
    }

    /* ==========================================================
       TOUCH ALL
       Usado quando o usuário tenta enviar o formulário.
       ========================================================== */

    const touchAll = (): void => {
      const editableFields: EditableField[] = ['name', 'cnpj', 'userName', 'email', 'password']

      editableFields.forEach((field) => {
        touched[field] = true
      })

      validateAllSilent()
    }

    /* ==========================================================
       VALIDADE DO FORMULÁRIO
       ========================================================== */

    const isValid = computed(() => {
      return fields.every((field) => errors[field] === '')
    })

    /* ==========================================================
       WATCH
       Importante:
       watch está no topo dos imports.
       Não existe mais:
       import { watch } from 'vue'
       dentro do setup().
       ========================================================== */

    watch(
      form,
      () => {
        validateAllSilent()
      },
      {
        deep: true,
      },
    )

    /* ==========================================================
       CARREGAMENTO DO PLANO
       ========================================================== */

    onMounted(() => {
      const plan = plans.find((item) => item.id === planId)

      if (plan) {
        selectedPlan.value = plan
        form.idPlan = plan.id

        errors.idPlan = ''
      } else {
        selectedPlan.value = null
        form.idPlan = 0
        errors.idPlan = 'Plano inválido'

        console.warn(`Plano não encontrado: ${planId}`)
      }
    })

    /* ==========================================================
       SUBMISSÃO
       Valida os dados e delega o envio ao serviço da API.
       ========================================================== */

    const handleSubmit = async (): Promise<void> => {
      /* --------------------------------------------------------
         Marca todos os campos como tocados.
         -------------------------------------------------------- */

      touchAll()

      /* --------------------------------------------------------
         Se existir qualquer erro, não envia.
         -------------------------------------------------------- */

      if (!isValid.value) {
        return
      }

      /* --------------------------------------------------------
        Inicia envio.
        -------------------------------------------------------- */

      isSubmitting.value = true

      try {
        // Envia o formulário com os dados no formato esperado pela API.
        const response = await onboardingService.create(form)
        console.log('Criação bem-sucedida', response)

        /*
         * Caso a API exija o CNPJ sem máscara, substitua a chamada acima por:
         *
         * const response = await onboardingService.create({
         *   ...form,
         *   cnpj: form.cnpj.replace(/\D/g, ''),
         * })
         */

        /*
         * Quando houver uma tela de destino, o redirecionamento pode ser feito aqui:
         *
         * router.push(...)
         */
      } catch (error) {
        console.error('Erro ao criar instituição', error)
      } finally {
        isSubmitting.value = false
      }
    }

    /* ==========================================================
       RETORNO PARA O TEMPLATE
       ========================================================== */

    return {
      form,
      errors,
      touched,
      selectedPlan,
      isSubmitting,
      isValid,
      validateField,
      handleSubmit,
    }
  },
})
</script>

<style scoped>
/*
 * Os estilos estão no arquivo separado.
 */
@import './Onboarding.css';
</style>
