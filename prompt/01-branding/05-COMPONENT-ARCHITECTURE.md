# PROOFCHAIN
## Component Architecture

Versão: 2.0

---

# Objetivo

Definir os componentes reutilizáveis da aplicação.

Todos os componentes deverão possuir responsabilidade única.

Evitar componentes gigantes.

Evitar duplicação de código.

---

# Estrutura

```
src

├── components

├── layouts

├── pages

├── composables

├── services

├── stores

└── types
```

---

# Componentes Globais

## BaseButton

Responsável por todos os botões.

Tipos:

- Primary
- Secondary
- Outline
- Ghost

Estados:

- Default
- Hover
- Active
- Loading
- Disabled

---

## BaseInput

Responsável pelos campos.

Suportar:

- label;
- placeholder;
- erro;
- loading;
- validação.

---

## BaseCard

Responsável por conteúdos em blocos.

Aplicações:

- Features;
- Dashboard;
- Pricing.

---

## BaseModal

Modal reutilizável.

---

## BaseBadge

Pequenos indicadores.

---

# Layout Components

## LandingLayout

Estrutura:

```
Header

Content

Footer
```

---

## AuthLayout

Utilizado para:

Login.

---

## DashboardLayout

Estrutura:

```
Sidebar

Header

Content
```

---

# Landing Components

## HeroSection

Responsável pela primeira dobra.

Contém:

- headline;
- CTAs;
- ilustração.

---

## Navbar

Responsável por:

- links;
- scroll;
- login.

---

## FeatureCard

Cards de funcionalidades.

---

## ProcessStep

Representa etapas do fluxo.

---

## UseCaseCard

Representa casos de uso.

---

## PricingCard

Representa planos.

---

## CTASection

Chamada final.

---

# Authentication Components

## LoginForm

Responsável pelo formulário.

Possui:

- validação;
- loading;
- submit.

---

# Dashboard Components

## DashboardSidebar

Menu lateral.

---

## StatCard

Indicadores.

---

## CertificateCard

Exibição de certificados.

---

## ActivityCard

Atividades.

---

# Validation Components

## ValidationForm

Formulário de consulta.

---

## ValidationResult

Resultado da validação.

---

# Componentes Visuais

Todos os componentes devem suportar:

- responsividade;
- tema escuro;
- animações;
- estados interativos.

---

# Regras

Componentes não devem:

- acessar API diretamente;
- possuir regras de negócio;
- controlar rotas.

Essas responsabilidades pertencem a camadas específicas.

---

# Próxima Etapa

Após a arquitetura dos componentes, aplicar o Design System oficial.