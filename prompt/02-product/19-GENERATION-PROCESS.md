# PROOFCHAIN
## Generation Process

Versão: 2.0

---

# Objetivo

Definir o processo obrigatório de geração do frontend MVP.

A IA deve seguir esta sequência para evitar inconsistências, retrabalho ou criação de estruturas fora do padrão.

---

# Regra Principal

A implementação deve seguir a ordem:

```
Produto

↓

Escopo

↓

Arquitetura

↓

Estrutura das páginas

↓

Componentes

↓

Design System

↓

UX/UI

↓

Interações

↓

Código

↓

Validação
```

Nenhuma etapa deve ser antecipada.

---

# Fase 1 - Análise do Produto

Antes de gerar código:

Analisar:

- objetivo do ProofChain;
- público-alvo;
- proposta de valor;
- percepção desejada.

Resultado esperado:

Compreensão completa do produto.

---

# Fase 2 - Definição do MVP

Confirmar:

Páginas:

- Landing;
- Login;
- Dashboard;
- Validação.

Funcionalidades:

- navegação;
- login mock;
- dashboard mock;
- validação mock.

Não adicionar funcionalidades fora do escopo.

---

# Fase 3 - Estrutura da Aplicação

Criar:

- projeto Vue;
- configuração Vite;
- Tailwind;
- estrutura de pastas.

Antes de criar telas.

---

# Fase 4 - Arquitetura das Páginas

Criar primeiro:

Layouts.

Depois:

Páginas.

Ordem:

```
LandingLayout

↓

LandingPage


AuthLayout

↓

LoginPage


DashboardLayout

↓

DashboardPage
```

---

# Fase 5 - Componentização

Após definir páginas:

Criar componentes reutilizáveis.

Exemplo:

```
Button

Card

Input

Navbar

Footer
```

Depois:

Componentes específicos.

---

# Fase 6 - Aplicação do Design System

Aplicar:

- cores;
- tipografia;
- espaçamento;
- bordas;
- sombras;
- gradientes.

Não criar estilos aleatórios.

---

# Fase 7 - Implementação Visual

Implementar:

- layouts;
- imagens;
- componentes;
- responsividade.

Utilizar os assets oficiais.

---

# Fase 8 - Navegação

Configurar:

- Vue Router;
- rotas;
- scroll;
- links.

Validar:

- Navbar;
- Login;
- Dashboard;
- Validação.

---

# Fase 9 - Interações

Adicionar:

- hover;
- transições;
- animações;
- estados.

Aplicar apenas após a estrutura estar pronta.

---

# Fase 10 - Dados Mock

Criar:

- mocks;
- services;
- estados.

Manter arquitetura preparada para API.

---

# Fase 11 - Revisão

Verificar:

Visual.

Código.

Responsividade.

Funcionalidades.

---

# Ordem de Prioridade

Caso exista conflito entre estética e funcionalidade:

1. Funcionamento.

2. Usabilidade.

3. Organização.

4. Estética.

---

# Regras para IA

A IA nunca deve:

- criar componentes antes da estrutura;
- alterar identidade visual;
- substituir assets;
- remover funcionalidades;
- adicionar tecnologias proibidas.

---

# Resultado Esperado

Ao final do processo deverá existir um MVP frontend completo, navegável, profissional e preparado para evolução.