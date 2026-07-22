# PROOFCHAIN
## MVP Scope

Versão: 2.0

---

# Objetivo

Este documento define exatamente o que faz parte do MVP do ProofChain.

Nenhuma funcionalidade além das descritas deverá ser implementada nesta etapa.

O objetivo é construir um MVP navegável, funcional e preparado para futura integração com o backend.

---

# Objetivo do MVP

O MVP deverá permitir que um usuário:

- conheça a plataforma;
- compreenda sua proposta de valor;
- realize login utilizando autenticação mockada;
- visualize um dashboard administrativo;
- valide um certificado utilizando dados simulados.

Todo o fluxo deverá funcionar sem dependência de APIs reais.

---

# Estrutura do MVP

O MVP será composto por quatro páginas principais.

## Landing Page

Página institucional responsável por apresentar o produto.

Objetivos:

- apresentar o ProofChain;
- explicar seus benefícios;
- demonstrar como funciona;
- apresentar planos;
- incentivar login;
- permitir acesso à validação pública.

---

## Login

Página responsável pela autenticação do usuário.

Nesta etapa:

- autenticação mockada;
- validações locais;
- redirecionamento para Dashboard.

Não implementar autenticação real.

---

## Dashboard

Área autenticada.

Objetivo:

demonstrar como será a plataforma após integração com o backend.

Todos os dados deverão ser simulados.

---

## Validação Pública

Página pública.

Objetivo:

simular a validação de um certificado.

Utilizar dados mockados.

Preparar estrutura para futura integração REST.

---

# Landing Page

A Landing deverá conter obrigatoriamente as seguintes seções.

## Header

Navbar fixa.

Logo.

Links internos.

Botão Entrar.

---

## Hero

Headline.

Subheadline.

Imagem oficial do certificado.

CTA Conheça os Planos.

CTA Validar Certificado.

---

## Sobre a Plataforma

Explicar rapidamente:

- problema;
- solução;
- diferenciais.

---

## Recursos

Apresentar as principais funcionalidades do ProofChain.

Exemplo:

- emissão digital;
- validação pública;
- blockchain;
- gestão centralizada;
- múltiplas instituições.

---

## Como Funciona

Fluxo visual.

Exemplo:

Emitir

↓

Registrar

↓

Compartilhar

↓

Validar

A linha que conecta o fluxo deverá possuir contraste suficiente para permanecer claramente visível.

---

## Casos de Uso

Apresentar aplicações da plataforma.

Exemplo:

- instituições de ensino;
- empresas;
- cursos livres;
- treinamentos internos.

---

## Planos

Apresentar os planos comerciais.

Utilizar dados simulados.

---

## CTA Final

Reforçar a proposta da plataforma.

Estimular Login.

---

## Footer

Links institucionais.

Contato.

Direitos autorais.

---

# Login

A tela deverá deverá seguir a referência oficial fornecida.
Lado esquedo mensagem + imagem dashboard

direita
Logo.

Email.

Senha.

Mostrar senha.

Lembrar acesso.

Esqueci minha senha.

Botão Entrar.

Validações locais.

Loading.

Mensagens de erro.

Login mockado.

---

# Dashboard

O Dashboard deverá seguir a referência oficial fornecida.

Nesta etapa deverá possuir:

Resumo.

Cards informativos.

Últimos certificados.

Cursos.

Instituições.

Menu lateral.

Perfil.

Logout.

Todos os dados deverão ser simulados.

---

# Validação Pública

Página pública.

Deverá seguir referências oficiais:

Campo de código.
Botão Validar.
Resultado mockado.

Estados:

✓ Certificado válido. (validação 1.png) -> mais detalhes (validação 2.png)
!  Certificado inexistente. (validação 4.jpg)
x Certificado expirado (validação 3.png)

Preparar estrutura para futura leitura de QR Code.

---

# Navegação

Fluxo principal.

Landing

↓

Login

↓

Dashboard

↓

Logout

↓

Landing

Fluxo secundário.

Landing

↓

Validação Pública

↓

Resultado

---

# Dados

Não utilizar backend.

Não utilizar banco.

Não utilizar blockchain.

Toda informação deverá utilizar mocks.

---

# Funcionalidades Obrigatórias

Landing completamente navegável.

Navbar funcional.

Smooth Scroll.

Login funcional.

Dashboard funcional.

Validação funcional.

Logout funcional.

Responsividade.

Animações.

Microinterações.

---

# Funcionalidades Futuras

Não implementar nesta etapa:

Cadastro.

CRUD.

Emissão de certificados.

Upload.

QR Code real.

Blockchain real.

Autenticação JWT.

Integração REST.

Banco de dados.

Gestão de usuários.

Gestão de instituições.

Essas funcionalidades deverão apenas possuir arquitetura preparada para futura implementação.

---

# Critérios de Aceitação

O MVP será considerado concluído quando:

✓ Toda navegação funcionar.

✓ Todas as páginas estiverem conectadas.

✓ Login redirecionar corretamente.

✓ Dashboard estiver operacional utilizando mocks.

✓ Validação funcionar utilizando mocks.

✓ Landing apresentar aparência premium.

✓ Responsividade completa.

✓ Nenhuma funcionalidade depender de backend.

✓ Código organizado e preparado para evolução futura.

---

# Próxima Etapa

Após a definição do escopo do MVP, deverá ser construída a Arquitetura da Informação da aplicação, definindo como as páginas, rotas, seções e fluxos se relacionam.