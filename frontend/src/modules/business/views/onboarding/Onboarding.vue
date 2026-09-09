<template>
  <div class="onboarding-page">
    <Container>
      <Section>
        <!-- Cabeçalho -->
        <header class="onboarding-header">
          <h1>Crie sua instituição</h1>
          <p class="subtitle">Comece a configurar sua conta ProofChain</p>
        </header>

        <!-- Formulário -->
        <form @submit.prevent="handleSubmit" class="onboarding-form">
          <!-- Seção 1: Dados da instituição -->
          <Card class="form-section">
            <h2>Dados da instituição</h2>
            <div class="field-group">
              <div class="field">
                <label for="name">Nome da instituição</label>
                <input
                  id="name"
                  v-model="form.name"
                  type="text"
                  placeholder="Ex: Minha Escola"
                  :class="{ 'is-invalid': errors.name }"
                  @blur="validateField('name')"
                />
                <span v-if="errors.name" class="error-message">{{ errors.name }}</span>
              </div>

              <div class="field">
                <label for="cnpj">CNPJ</label>
                <input
                  id="cnpj"
                  v-model="form.cnpj"
                  type="text"
                  placeholder="00.000.000/0000-00"
                  v-mask="'##.###.###/####-##'"
                  :class="{ 'is-invalid': errors.cnpj }"
                  @blur="validateField('cnpj')"
                />
                <span v-if="errors.cnpj" class="error-message">{{ errors.cnpj }}</span>
              </div>
            </div>
          </Card>

          <!-- Seção 2: Conta administrativa -->
          <Card class="form-section">
            <h2>Conta administrativa</h2>
            <div class="field-group">
              <div class="field">
                <label for="userName">Nome de usuário</label>
                <input
                  id="userName"
                  v-model="form.userName"
                  type="text"
                  placeholder="Seu nome de acesso"
                  :class="{ 'is-invalid': errors.userName }"
                  @blur="validateField('userName')"
                />
                <span v-if="errors.userName" class="error-message">{{ errors.userName }}</span>
              </div>

              <div class="field">
                <label for="email">E-mail</label>
                <input
                  id="email"
                  v-model="form.email"
                  type="email"
                  placeholder="seu@email.com"
                  :class="{ 'is-invalid': errors.email }"
                  @blur="validateField('email')"
                />
                <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
              </div>

              <div class="field">
                <label for="password">Senha</label>
                <input
                  id="password"
                  v-model="form.password"
                  type="password"
                  placeholder="Mínimo 8 caracteres"
                  :class="{ 'is-invalid': errors.password }"
                  @blur="validateField('password')"
                />
                <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
              </div>
            </div>
          </Card>

          <!-- Seção 3: Plano selecionado (resumo) -->
          <Card class="plan-summary">
            <h2>Plano selecionado</h2>
            <div class="plan-details">
              <p>
                <strong>{{ selectedPlan?.name || 'Plano não identificado' }}</strong>
              </p>
              <p class="plan-price">{{ selectedPlan?.price || '--' }}</p>
              <p class="plan-description">{{ selectedPlan?.description || '' }}</p>
            </div>
          </Card>

          <!-- Ação principal -->
          <div class="form-actions">
            <BaseButton type="submit" :loading="isSubmitting" :disabled="!isValid">
              Criar instituição
            </BaseButton>
          </div>
        </form>
      </Section>
    </Container>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Container from '@/core/components/ui/Container/Container.vue'
import Section from '@/core/components/ui/Section/Section.vue'
import Card from '@/core/components/ui/Card/Card.vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { onboardingService } from '@/modules/business/services/Onboarding.service'
import type { OnboardingRequest } from '@/modules/business/types/OnboardingRequest'
import { validateCNPJ, validateEmail } from '@/core/utils/Validator' // supondo que existam

interface PlanSummary {
  id: number
  name: string
  price: string
  description: string
}

// Mock de plano (será substituído pela store/query real)
const mockPlans: PlanSummary[] = [
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
  name: 'InstitutionOnboarding',
  components: {
    Container,
    Section,
    Card,
    BaseButton,
  },
  setup() {
    const route = useRoute()
    const planId = Number(route.query.planId) || 1 // fallback para testes

    const form = reactive<OnboardingRequest>({
      name: '',
      cnpj: '',
      userName: '',
      email: '',
      password: '',
      idPlan: planId,
    })

    const errors = reactive<Record<keyof OnboardingRequest, string>>({
      name: '',
      cnpj: '',
      userName: '',
      email: '',
      password: '',
      idPlan: '',
    })

    const isSubmitting = ref(false)
    const selectedPlan = ref<PlanSummary | null>(
      mockPlans.find((plan) => plan.id === planId) ?? null,
    )

    // Carrega detalhes do plano
    onMounted(() => {
      const plan = mockPlans.find((p) => p.id === planId)
      if (plan) {
        selectedPlan.value = plan
        form.idPlan = plan.id
      } else {
        // Plano não encontrado - redirecionar ou tratar erro
        console.warn('Plano não encontrado')
      }
    })

    // Validação individual
    const validateField = (field: keyof OnboardingRequest) => {
      // const value = form[field]
      let message = ''

      switch (field) {
        case 'name': {
          const value = form.name

          if (!value) message = 'Nome é obrigatório'
          else if (value.length < 5) message = 'Mínimo 5 caracteres'
          else if (value.length > 100) message = 'Máximo 100 caracteres'
          break
        }
        case 'cnpj': {
          const value = form.cnpj
          if (!value) message = 'CNPJ é obrigatório'
          else if (!validateCNPJ(value)) message = 'CNPJ inválido'
          break
        }
        case 'userName': {
          const value = form.userName

          if (!value) message = 'Nome de usuário é obrigatório'
          else if (value.length < 5) message = 'Mínimo 5 caracteres'
          else if (value.length > 30) message = 'Máximo 30 caracteres'
          break
        }
        case 'email': {
          const value = form.email

          if (!value) message = 'E-mail é obrigatório'
          else if (!validateEmail(value)) message = 'E-mail inválido'
          break
        }
        case 'password': {
          const value = form.password

          if (!value) message = 'Senha é obrigatória'
          else if (value.length < 8) message = 'Mínimo 8 caracteres'
          break
        }
        case 'idPlan': {
          const value = form.idPlan
          if (!value || value <= 0) message = 'Plano inválido'
          break
        }
      }

      errors[field] = message
    }

    // Validação global
    const validateAll = (): boolean => {
      const fields: (keyof OnboardingRequest)[] = [
        'name',
        'cnpj',
        'userName',
        'email',
        'password',
        'idPlan',
      ]
      fields.forEach((field) => validateField(field))
      return fields.every((field) => !errors[field])
    }

    const isValid = computed(() => validateAll())

    // Submissão
    const handleSubmit = async () => {
      if (!validateAll()) return

      isSubmitting.value = true
      try {
        const response = await onboardingService.create(form)
        // Sucesso - redirecionar ou exibir mensagem
        console.log('Criação bem-sucedida', response)
        // Exemplo: router.push('/login')
      } catch (error) {
        console.error('Erro ao criar instituição', error)
        // Exibir mensagem de erro
      } finally {
        isSubmitting.value = false
      }
    }

    return {
      form,
      errors,
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
/* Os estilos estão no arquivo separado Onboarding.css */
@import './Onboarding.css';
</style>
