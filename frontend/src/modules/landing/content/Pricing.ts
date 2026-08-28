/* ==========================================================
   PRICING — DATA
   Dados e configuração dos planos da seção de preços.
========================================================== */

export interface PricingFeature {
  text: string
  included: boolean
}

export interface PricingPlan {
  id: string
  name: string
  description: string
  price: string
  period: string
  billing?: string
  features: PricingFeature[]
  cta: string
  featured?: boolean
  badge?: string
}

export const pricingPlans: PricingPlan[] = [
  {
    id: 'free',
    name: 'Free',
    description: 'Para começar a emitir certificados digitais.',
    price: 'R$ 0',
    period: '/mês',
    billing: 'Grátis para sempre',
    features: [
      {
        text: 'Emissão de certificados',
        included: true,
      },
      {
        text: 'Validação de certificados',
        included: true,
      },
      {
        text: 'Registro de autenticidade',
        included: true,
      },
      {
        text: 'Gestão de participantes',
        included: true,
      },
      {
        text: 'Blockchain',
        included: true,
      },
      {
        text: 'Relatórios avançados',
        included: false,
      },
    ],
    cta: 'Começar grátis',
  },

  {
    id: 'starter',
    name: 'Starter',
    description: 'Para pequenas operações que precisam de mais recursos.',
    price: 'R$ 49',
    period: '/mês',
    billing: 'Cobrado mensalmente',
    features: [
      {
        text: 'Tudo do plano Free',
        included: true,
      },
      {
        text: 'Maior volume de certificados',
        included: true,
      },
      {
        text: 'Gestão de cursos e turmas',
        included: true,
      },
      {
        text: 'Personalização de certificados',
        included: true,
      },
      {
        text: 'Relatórios básicos',
        included: true,
      },
      {
        text: 'Suporte prioritário',
        included: false,
      },
    ],
    cta: 'Começar agora',
  },

  {
    id: 'professional',
    name: 'Professional',
    description: 'Para instituições que precisam de escala e controle.',
    price: 'R$ 99',
    period: '/mês',
    billing: 'Cobrado mensalmente',
    featured: true,
    badge: 'Mais escolhido',
    features: [
      {
        text: 'Tudo do plano Starter',
        included: true,
      },
      {
        text: 'Maior volume de certificados',
        included: true,
      },
      {
        text: 'Blockchain para rastreabilidade',
        included: true,
      },
      {
        text: 'Gestão avançada de cursos',
        included: true,
      },
      {
        text: 'Relatórios e métricas',
        included: true,
      },
      {
        text: 'Suporte prioritário',
        included: true,
      },
    ],
    cta: 'Escolher Professional',
  },

  {
    id: 'enterprise',
    name: 'Enterprise',
    description: 'Para operações maiores com necessidades personalizadas.',
    price: 'Personalizado',
    period: '',
    billing: 'Fale com nossa equipe',
    features: [
      {
        text: 'Tudo do plano Professional',
        included: true,
      },
      {
        text: 'Volume personalizado',
        included: true,
      },
      {
        text: 'Multi-instituição',
        included: true,
      },
      {
        text: 'Integrações via API',
        included: true,
      },
      {
        text: 'Recursos personalizados',
        included: true,
      },
      {
        text: 'Atendimento especializado',
        included: true,
      },
    ],
    cta: 'Falar com especialista',
  },
]
