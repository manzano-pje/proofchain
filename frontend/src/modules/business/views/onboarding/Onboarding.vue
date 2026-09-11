<template>
  <div class="onboarding-page">
    <Section class="onboarding-section">
      <Container class="onboarding-container">
        <!-- ============================================================
             HEADER
             ============================================================ -->
        <header class="onboarding-header">
          <div class="header-brand">
            <!-- Utilizando ícone/logo como na referência image_e03306.jpg -->
            <span class="brand-logo">
              <img
                src="@/assets/images/logo/logo_horizontal_light.svg"
                alt="ProofChain Logo"
                class="header__logo"
              />
            </span>
          </div>
        </header>

        <!-- ============================================================
             INTRODUÇÃO E PROGRESSO
             ============================================================ -->
        <div class="onboarding-top-layout">
          <section class="onboarding-intro">
            <div>
              <span class="intro-badge">PRIMEIRO ACESSO</span>
              <h1>Crie sua instituição</h1>
            </div>
            <p class="intro-description">
              Configure sua conta ProofChain<br />
              em poucos passos.
            </p>
          </section>

          <!-- <nav class="onboarding-progress" aria-label="Progresso do cadastro">
            <ol class="progress-list">
              <li class="progress-item is-active">
                <span class="progress-number">01</span>
                <div class="progress-content">
                  <span class="progress-title">Instituição</span>
                  <span class="progress-sub">Dados da instituição</span>
                </div>
              </li>
              <li class="progress-item">
                <span class="progress-number">02</span>
                <div class="progress-content">
                  <span class="progress-title">Conta administrativa</span>
                  <span class="progress-sub">Seu acesso ao sistema</span>
                </div>
              </li>
              <li class="progress-item">
                <span class="progress-number">03</span>
                <div class="progress-content">
                  <span class="progress-title">Plano</span>
                  <span class="progress-sub">Configuração escolhida</span>
                </div>
              </li>
            </ol>
          </nav> -->
        </div>

        <!-- ============================================================
             FORMULÁRIO
             ============================================================ -->
        <form class="onboarding-form" @submit.prevent="handleSubmit" novalidate>
          <!-- SEÇÃO 01 -->
          <section class="form-section">
            <div class="section-heading">
              <span class="section-number">01</span>
              <div>
                <h2>Dados da instituição</h2>
                <p class="section-description">Informações básicas da organização.</p>
              </div>
            </div>

            <div class="field-group field-group--two-col">
              <div class="field">
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

              <div class="field">
                <label for="cnpj">CNPJ</label>
                <input
                  id="cnpj"
                  v-model="form.cnpj"
                  type="text"
                  inputmode="numeric"
                  autocomplete="organization"
                  placeholder="00.000.000/0000-00"
                  v-mask="'##.###.###/####-##'"
                  :class="{ 'is-invalid': touched.cnpj && errors.cnpj }"
                  @blur="validateField('cnpj')"
                />
                <span v-if="touched.cnpj && errors.cnpj" class="error-message">{{
                  errors.cnpj
                }}</span>
              </div>
            </div>
          </section>

          <!-- SEÇÃO 02 -->
          <section class="form-section">
            <div class="section-heading">
              <span class="section-number">02</span>
              <div>
                <h2>Conta administrativa</h2>
                <p class="section-description">Defina as credenciais de acesso.</p>
              </div>
            </div>

            <div class="field-group">
              <div class="field">
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

              <div class="field">
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

              <div class="field-group field-group--two-col">
                <div class="field">
                  <label for="password">Senha</label>
                  <div class="input-wrapper">
                    <input
                      id="password"
                      v-model="form.password"
                      type="password"
                      autocomplete="new-password"
                      placeholder="Mínimo 8 caracteres"
                      :class="{ 'is-invalid': touched.password && errors.password }"
                      @blur="validateField('password')"
                    />
                    <!-- Ícone simulado visualmente via CSS baseado no DS -->
                    <span class="password-toggle-icon"></span>
                  </div>
                  <span v-if="touched.password && errors.password" class="error-message">{{
                    errors.password
                  }}</span>
                </div>
              </div>
            </div>
          </section>

          <!-- SEÇÃO 03 -->
          <section class="form-section plan-section">
            <div class="section-heading">
              <span class="section-number">03</span>
              <div>
                <h2>Plano selecionado</h2>
                <p class="section-description">Configuração escolhida para sua instituição.</p>
              </div>
            </div>

            <div class="plan-summary">
              <div class="plan-details-left">
                <span class="plan-label">PLANO</span>
                <strong class="plan-name">{{
                  selectedPlan?.name || 'Plano não identificado'
                }}</strong>
                <p class="plan-description">{{ selectedPlan?.description || '' }}</p>
              </div>
              <div class="plan-price">
                {{ selectedPlan?.price || '--' }}
              </div>
            </div>
          </section>

          <!-- AÇÕES -->
          <div class="form-actions">
            <span class="security-badge">
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
              class="submit-btn"
              type="submit"
              :loading="isSubmitting"
              :disabled="!isValid || isSubmitting"
            >
              Criar instituição &rarr;
            </BaseButton>
          </div>
        </form>

        <!-- FOOTER -->
        <footer class="onboarding-footer">
          <span class="footer-copy">© ProofChain</span>
          <span class="footer-label">Configuração inicial</span>
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
import Card from '@/core/components/ui/Card/Card.vue'
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
   MOCK DE PLANOS
   Futuramente será substituído pela store/query real.
   ============================================================ */

const mockPlans = [
  {
    id: 1,
    name: 'Plano Básico',
    price: 'R$ 99,90/mês',
    description: 'Ideal para pequenas instituições.',
  },
  {
    id: 2,
    name: 'Plano Profissional',
    price: 'R$ 199,90/mês',
    description: 'Recursos completos para gestão.',
  },
]

export default defineComponent({
  name: 'DataOnboarding',

  components: {
    Container,
    Section,
    Card,
    BaseButton,
  },

  setup() {
    /* ==========================================================
       ROTA
       ========================================================== */

    const route = useRoute()

    const planId = Number(route.query.planId) || 1

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

    const selectedPlan = ref<(typeof mockPlans)[number] | null>(null)

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

          if (!value || value <= 0) {
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
      const plan = mockPlans.find((item) => item.id === planId)

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
        const response = await onboardingService.create(form)

        console.log('Criação bem-sucedida', response)

        /*
         * Futuramente:
         *
         * router.push(...)
         *
         * ou
         *
         * mensagem de sucesso.
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
