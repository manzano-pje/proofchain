# LAYOUT MVP VALIDADOR PRO

## 1. Visão Geral do Sistema

O sistema é uma plataforma de **emissão e validação de certificados digitais**, voltada para **instituições** como escolas, cursos livres, ONGs e empresas de treinamento.
A POC deve demonstrar a experiência completa de navegação, contemplando **três tipos de acesso distintos**:

* Visitante (validação pública de certificados)
* Cliente / Entidade (instituição emissora)
* Administrador do sistema

Todos os dados utilizados na POC deverão ser **mockados em JSON**, sem persistência real.

---

## 2. Modelos de Comercialização (Contexto Visual)

Esses modelos impactam apenas **exibição e estados de tela**, não lógica real na POC.

* Pacote por quantidade de certificados (ex: 10, 20, 50)
* Assinatura com quantidade limitada de certificados e modelo padrão
* Assinatura com quantidade ilimitada de certificados e possibilidade de modelo personalizado (opcional / conceitual)

---

## 3. Fluxo Geral de Acesso

### 3.1 Autenticação

Tela de login única para **Cliente (Entidade)** e **Administrador**.

Campos:

* E-mail
* Senha

Após login, o sistema redireciona conforme o **perfil mockado no JSON**.

---

## 4. Acesso 1 – Visitante (Validação Pública)

### Objetivo

Permitir que qualquer usuário valide um certificado sem login.

### Navegação

* Página inicial

  * Botão de destaque: **“Validar Certificado”**

### Tela: Validação de Certificado

Componentes principais:

* Campo de texto para digitação do **código do certificado**
* Botão “Validar”

Estados do card de resultado:

* **Sucesso**

  * Nome do curso
  * Nome da entidade emissora
  * Data de emissão
  * Status do certificado:

    * Válido
    * Expirado
    * Revogado
* **Erro**

  * Código inválido ou não encontrado

Link: Landing Page
[index.html](./index.html)

---

## 5. Acesso 2 – Cliente / Entidade

Acesso realizado após login.

### 5.1 Estrutura de Navegação (Sidebar)

* Dashboard
* Certificados
* Cursos
* Participantes
* Relatórios
* Configurações
* Fale Conosco

---

### 5.2 Dashboard da Entidade

Objetivo: visão geral rápida da operação.

Componentes:

* Card: Total de certificados emitidos
* Card: Certificados emitidos no mês
* Lista: Últimos certificados emitidos
* Gráfico de barras:

  * Emissão de certificados por curso

---

### 5.3 Configurações da Entidade

Tela de cadastro/edição de dados institucionais.

Campos:

* Nome da pessoa ou instituição
* CPF ou CNPJ
* Cidade
* Estado
* País
* E-mail
* Telefone
* Upload de logotipo

---

### 5.4 Cadastro de Cursos

Tela de listagem + ação de cadastro.

Campos do curso:

* Nome do curso
* Data de início
* Data de término
* Quantidade de horas

---

### 5.5 Cadastro de Participantes

Tela de listagem + ação de cadastro.

Campos:

* Nome
* E-mail
* Telefone

---

### 5.6 Emissão de Certificados

Fluxo guiado em uma única tela ou modal.

Campos:

* Selecionar participante (dropdown)
* Selecionar curso (dropdown)
* Data de início
* Data de término
* Quantidade de horas

Ação:

* Botão “Emitir certificado”

---

### 5.7 Relatórios

Subseções:

* Relatório geral de certificados emitidos

  * Visualização em tela
  * Ações de exportação (CSV / PDF – apenas visual)
* Relatório por curso

---

### 5.8 Fale Conosco

Tela simples para abertura de ticket.

Campos:

* Assunto
* Mensagem
* Botão “Enviar”

---

## 6. Acesso 3 – Administrador

Acesso exclusivo via login, com navegação própria.

### 6.1 Navegação Lateral (Sidebar)

* Dashboard
* Clientes
* Assinaturas
* Certificados
* Validações
* Cobrança
* Relatórios
* Configurações

---

### 6.2 Dashboard do Administrador

Organizado em blocos visuais.

#### Negócio

* Assinaturas ativas
* MRR (mockado)
* Novos clientes no mês

#### Uso do Produto

* Certificados emitidos no mês
* Média de certificados por cliente
* Clientes sem emissão

#### Valor Entregue

* Total de validações realizadas
* Certificados mais validados

#### Alertas

* Emissões com erro
* Clientes próximos do limite
* Assinaturas próximas do vencimento

---

## 7. Observações para a POC Frontend

* Todos os dados devem ser consumidos de **arquivos JSON mockados**
* Não há necessidade de autenticação real
* Estados de sucesso, erro e vazio devem ser considerados no layout
* Prioridade: **clareza de navegação, hierarquia visual e experiência do usuário**
* A POC deve demonstrar o fluxo completo, mesmo sem lógica funcional real

---

Se desejar, posso gerar:

* Estrutura de rotas frontend
* Exemplos de JSON mockado por tela
* Wireframe textual por página
* Sugestão de arquitetura de componentes (React / Vue / Angular)
