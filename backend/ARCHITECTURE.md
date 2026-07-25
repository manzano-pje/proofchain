Paulo, eu criaria um **contexto fixo de arquitetura** para o Copilot antes de qualquer análise. A ideia é impedir que ele tente avaliar o Proofchain como "DDD puro" ou "CQRS completo".

Você pode colocar este texto no chat do Copilot ou salvar como referência do projeto (por exemplo, em `ARCHITECTURE.md`).

```text
# Contexto Arquitetural do Projeto Proofchain

Você está analisando o projeto Proofchain. 
Antes de sugerir alterações ou avaliar a arquitetura, considere obrigatoriamente as definições abaixo.

## Modelo arquitetural adotado

O Proofchain utiliza uma arquitetura:

Monólito Modular Orientado ao Domínio
(Domain-Oriented Modular Monolith)

O sistema é organizado por módulos funcionais de negócio, e não por camadas globais.

A organização principal é:

com.proofchain

├── auth
├── user
├── platform
├── business
└── shared


Cada módulo representa uma área de negócio independente e possui suas próprias responsabilidades.

Exemplos:

user
- usuários
- autenticação relacionada ao usuário
- permissões do usuário

platform
- instituição
- planos
- assinaturas
- funcionalidades
- configurações da plataforma

business
- cursos
- turmas
- inscrições
- certificados
- blockchain


## Sobre DDD

O projeto utiliza alguns conceitos inspirados em DDD, como:

- organização por contexto de negócio;
- separação de responsabilidades;
- entidades representando conceitos do domínio;
- isolamento de módulos.

Porém, o projeto NÃO utiliza DDD puro.

Não considerar como requisito:

- agregados complexos;
- entidades totalmente independentes de persistência;
- eventos de domínio obrigatórios;
- domain services extensivos;
- bounded contexts independentes como microsserviços.

Avaliar a arquitetura dentro de uma abordagem pragmática.


## Sobre CQRS

O projeto NÃO implementa CQRS.

A estrutura:

application
├── command
├── query
└── handler

NÃO representa CQRS completo.

Ela é apenas uma organização dos casos de uso da aplicação.

Não existe:

- banco separado de leitura e escrita;
- read models;
- event sourcing;
- projeções;
- mensageria baseada em comandos.

Portanto, não sugerir migração para CQRS.


## Estrutura interna dos módulos

Cada módulo segue a separação:

module

├── domain
├── application
├── infrastructure
└── interfaces


### Domain

Responsável pelos conceitos de negócio.

Pode conter:

- entidades;
- enums;
- regras relacionadas ao domínio.


### Application

Responsável pela orquestração dos casos de uso.

Contém:

- commands;
- queries;
- handlers;
- serviços de aplicação.

A camada application coordena o fluxo, mas não representa CQRS.


### Infrastructure

Responsável pelos detalhes técnicos:

- persistência;
- repositories Spring Data;
- integrações externas;
- implementações técnicas.


### Interfaces

Responsável pelas entradas e saídas externas:

- controllers REST;
- DTOs request;
- DTOs response.


## Regras para análise

Ao analisar o projeto:

1. Avalie a consistência com a arquitetura modular definida acima.

2. Não tente converter automaticamente o projeto para:
   - DDD puro;
   - Clean Architecture rígida;
   - Hexagonal Architecture completa;
   - CQRS.

3. Sugestões devem considerar:
   - simplicidade;
   - manutenção;
   - evolução de um SaaS;
   - clareza do código;
   - baixo acoplamento.

4. Priorize problemas reais:
   - dependências circulares;
   - acoplamento indevido entre módulos;
   - responsabilidades mal posicionadas;
   - inconsistência de nomenclatura;
   - problemas de manutenção.

5. Diferencie:
   - problema arquitetural real;
   - escolha arquitetural válida;
   - melhoria opcional.

## Objetivo do projeto

O Proofchain é um SaaS multi-tenant para emissão e validação de certificados digitais utilizando blockchain.

A arquitetura deve permitir:

- crescimento do produto;
- manutenção simples;
- separação clara dos módulos;
- possibilidade futura de extração de serviços independentes caso necessário.

Analise o projeto considerando este contexto.
```

---

Eu colocaria esse contexto **antes da auditoria de cada módulo**.

Um detalhe importante: eu não proibiria ele de sugerir melhorias. Eu só deixaria claro que ele deve separar:

* **"isso está errado"**
* **"isso poderia ser melhor em outra arquitetura"**

Essa distinção é exatamente onde as IAs costumam errar em análises de arquitetura.
