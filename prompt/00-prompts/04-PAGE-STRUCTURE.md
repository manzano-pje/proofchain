# PROOFCHAIN
## Page Structure

Versão: 2.0

---

# Objetivo

Este documento define a estrutura visual das páginas do MVP.

A implementação deverá seguir esta ordem:

Arquitetura da informação

↓

Estrutura da página

↓

Componentes

↓

Design System

↓

Implementação

Não criar componentes antes da definição estrutural da página.

---

# LANDING PAGE

## Estrutura Geral

A Landing Page deverá possuir uma composição vertical.

Estrutura:

```
LandingPage

├── Header
├── Hero Section
├── About Section
├── Features Section
├── How It Works Section
├── Use Cases Section
├── Pricing Section
├── Final CTA Section
└── Footer
```

---

# Header

Responsabilidade:

Apresentar navegação principal.

Estrutura:

```
Logo

Menu

Botão Entrar
```

Características:

- posição fixa;
- fundo transparente inicialmente;
- alteração visual durante scroll;
- backdrop blur após rolagem;
- responsivo.

---

# Hero Section

Objetivo:

Apresentar o produto imediatamente.

Estrutura:

```
Hero

├── Badge
├── Headline
├── Description
├── Primary CTA
├── Secondary CTA
└── Certificate Illustration
```

---

# Hero Layout

Desktop:

Duas colunas.

```
Texto                Imagem

50%                  50%
```

Não permitir que o texto ocupe toda a tela.

A imagem do certificado deverá possuir destaque visual.

---

# Hero Illustration

Elemento visual.

Composto por:

- imagem oficial do certificado;
- glow;
- sombras;
- elementos blockchain discretos.

Não representa uma funcionalidade.

É apenas uma ilustração da proposta da plataforma.

---

# About Section

Objetivo:

Explicar o conceito do ProofChain.

Estrutura:

```
Título

Texto

Cards de benefícios
```

---

# Features Section

Objetivo:

Apresentar funcionalidades.

Estrutura:

```
Título

Grid de Cards

Card
Card
Card
Card
```

Cada card deverá possuir:

- ícone;
- título;
- descrição.

---

# How It Works Section

Objetivo:

Demonstrar o fluxo.

Estrutura:

```
Step 1

↓

Step 2

↓

Step 3

↓

Step 4
```

A conexão visual entre etapas é obrigatória.

---

# Use Cases Section

Objetivo:

Mostrar aplicações.

Estrutura:

```
Cards

Instituições

Empresas

Cursos

Eventos
```

---

# Pricing Section

Objetivo:

Apresentar planos.

Estrutura:

```
Título

Cards de Plano

CTA
```

---

# Final CTA

Estrutura:

```
Título

Descrição

Botão
```

---

# Footer

Estrutura:

```
Logo

Links

Contato

Copyright
```

---

# LOGIN PAGE

Estrutura:

```
LoginPage

├── Background
├── Brand Area
└── Login Card
```

---

# Login Card

Elementos:

```
Título

Descrição

Input Email

Input Senha

Checkbox

Link Recuperação

Botão Entrar
```

---

# DASHBOARD PAGE

Estrutura:

```
Dashboard

├── Sidebar
├── Header
└── Content
```

---

# Sidebar

Elementos:

- Logo
- Menu
- Perfil
- Logout

---

# Dashboard Content

Elementos:

```
Welcome

Statistics Cards

Certificates

Courses

Activity
```

---

# VALIDATION PAGE

Estrutura:

```
ValidationPage

├── Header
├── Validation Card
└── Result Area
```

---

# Validation Card

Elementos:

```
Título

Descrição

Input Código

Botão Validar
```

---

# Resultado

Estados:

Sucesso:

Certificado válido.

Erro:

Certificado não encontrado.

---

# Responsabilidade

Este documento define somente estrutura.

Detalhes de componentes serão definidos no próximo documento.