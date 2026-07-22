# PROOFCHAIN
## Implementation Rules

Versão: 2.0

---

# Objetivo

Definir regras obrigatórias para implementação do frontend MVP.

---

# Stack Obrigatória

Utilizar:

- Vue 3;
- Vite;
- TypeScript;
- Tailwind CSS;
- Composition API;
- Script Setup.

---

# Arquitetura

Organizar o projeto seguindo separação de responsabilidades.

Estrutura sugerida:

```
src

├── assets

├── components

├── layouts

├── pages

├── router

├── stores

├── services

├── composables

├── mocks

├── types

└── utils
```

---

# Pages

Pages representam telas completas.

Exemplos:

```
LandingPage.vue

LoginPage.vue

DashboardPage.vue

ValidationPage.vue
```

Pages não devem conter componentes gigantes.

---

# Components

Componentes devem:

- possuir responsabilidade única;
- ser reutilizáveis;
- receber dados via props;
- emitir eventos.

Evitar lógica complexa.

---

# Composables

Utilizar composables para lógica reutilizável.

Exemplos:

```
useAuth()

useScroll()

useValidation()
```

---

# Services

Mesmo utilizando Mock, criar camada de serviço.

Exemplo:

```
authService.ts

certificateService.ts
```

Futura integração REST deverá substituir apenas essa camada.

---

# Mock Data

Dados simulados devem ficar isolados.

Exemplo:

```
src/mocks
```

Nunca espalhar dados falsos diretamente nos componentes.

---

# Router

Todas as rotas devem estar configuradas.

Obrigatório:

```
/

/login

/dashboard

/validation
```

---

# Estado Global

Preparar estrutura para Pinia.

Não criar estados globais desnecessários.

---

# TypeScript

Obrigatório:

- interfaces;
- tipos;
- props tipadas;
- retorno de funções tipado.

Evitar:

```
any
```

---

# Tailwind

Utilizar classes Tailwind.

Evitar CSS espalhado.

Quando necessário:

Criar arquivos organizados.

---

# Responsividade

Toda implementação deve considerar:

Desktop.

Tablet.

Mobile.

---

# Design System

Nunca criar cores diretamente.

Utilizar tokens definidos.

Exemplo:

Não usar:

```
bg-[#2979D6]
```

Preferir:

```
bg-primary
```

---

# Assets

Utilizar os arquivos oficiais fornecidos.

Não substituir por imagens genéricas.

---

# Funcionalidades Mock

Implementar:

Login Mock.

Dashboard Mock.

Validação Mock.

---

# Não Implementar

Nesta fase:

- backend;
- banco;
- blockchain;
- API real;
- JWT;
- CRUD;
- emissão real de certificados.

---

# Qualidade de Código

O código deve possuir:

- nomes claros;
- componentes organizados;
- comentários quando necessário;
- ausência de código morto.

---

# Documentação

Cada componente importante deve possuir comentário inicial indicando:

- finalidade;
- responsabilidade;
- utilização.

---

# Processo

Seguir obrigatoriamente:

1. Criar estrutura de páginas.

2. Criar layouts.

3. Criar componentes.

4. Aplicar Design System.

5. Aplicar interações.

6. Implementar mocks.

7. Validar funcionamento.

---

# Resultado Esperado

O código final deve representar um MVP profissional, organizado e preparado para evolução futura.