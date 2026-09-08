# 🔗 ProofChain

> **Plataforma SaaS para Emissão, Gestão e Validação Criptográfica de Certificados Digitais.**

[![Status](https://img.shields.io/badge/status-em_desenvolvimento-yellow.svg)](https://github.com/)
[![Java](https://img.shields.io/badge/Backend-Java_17_%7C_Spring_Boot_3-007396.svg)](https://spring.io/)
[![Vue.js](https://img.shields.io/badge/Frontend-Vue_3_%7C_TypeScript-4FC08D.svg)](https://vuejs.org/)
[![Web3](https://img.shields.io/badge/Web3-Blockchain_Evidences-3C3C3D.svg)](https://ethereum.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)

---

## 📋 Sumário

- [Sobre o ProofChain](#-sobre-o-proofchain)
- [Principais Recursos](#-principais-recursos)
- [Como Funciona](#-como-funciona)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Repositório](#-estrutura-do-repositório)
- [Documentação Técnica](#-documentação-técnica)
- [Como Executar o Projeto](#-como-executar-o-projeto)
  - [Pré-requisitos](#pré-requisitos)
  - [Execução com Docker Compose](#execução-com-docker-compose)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Status e Roadmap](#-status-e-roadmap)
- [Licença](#-licença)

---

## 📌 Sobre o ProofChain

O **ProofChain** é uma plataforma SaaS (*Software as a Service*) desenvolvida para modernizar, simplificar e garantir a autenticidade na emissão, gestão e verificação de certificados digitais acadêmicos e corporativos.

A plataforma possibilita que **instituições de ensino, empresas e organizadores de eventos** emitam certificados digitais estruturados com rastreabilidade total. 

A grande inovação do sistema reside no uso de **provas criptográficas (hashes SHA-256) e tecnologia Blockchain** como camada imutável de auditoria. Enquanto os dados operacionais e pessoais permanecem protegidos sob a responsabilidade da aplicação (em conformidade com a LGPD), a verificação de autenticidade é feita de maneira pública, rápida e inviolável.

---

## ✨ Principais Recursos

- 📄 **Emissão Estruturada:** Geração de certificados com dados parametrizáveis e assinaturas digitais.
- 🔍 **Validação Pública:** Portal aberto para qualquer pessoa ou entidade verificar a autenticidade de um certificado via QR Code ou hash.
- 🏢 **Arquitetura Multi-Tenant:** Isolamento de dados e personalização por instituição.
- 👥 **Gestão de Acessos e Permissões (RBAC):** Controle rigoroso de perfis (Administrador, Emissor, Auditor, Usuário).
- 🔐 **Provas Criptográficas:** Geração de evidências imutáveis baseadas em hash SHA-256 para cada lote ou certificado.
- ⛓️ **Registro em Blockchain:** Ancoragem de evidências em Smart Contracts para auditoria descentralizada.
- 📊 **Dashboard Analítico:** Métricas de emissões, validações e status de ancoragem Web3.

---

## 🔄 Como Funciona

```text
       ┌────────────────────────┐
       │      Instituição       │
       └───────────┬────────────┘
                   │
                   ▼
       ┌────────────────────────┐
       │ Emissão do Certificado │
       └───────────┬────────────┘
                   │
                   ▼
       ┌────────────────────────┐
       │  Geração de Evidência  │  ---> Hash Criptográfico (SHA-256)
       └───────────┬────────────┘
                   │
                   ▼
       ┌────────────────────────┐
       │ Registro em Blockchain │  ---> Camada Imutável de Auditoria
       └───────────┬────────────┘
                   │
                   ▼
       ┌────────────────────────┐
       │     Certificado        │  ---> QR Code / URL de Verificação
       │     Disponível         │
       └───────────┬────────────┘
                   │
                   ▼
       ┌────────────────────────┐
       │   Validação Pública    │  ---> Checagem instantânea de autenticidade
       └────────────────────────┘

       💡 Nota Arquitetural: A blockchain atua exclusivamente como uma camada de prova de existência e integridade. Nenhuma informação pessoal sensível é gravada na rede pública, garantindo conformidade com regulações de privacidade (LGPD/GDPR).
       
       🛠️ Tecnologias UtilizadasCamadaTecnologias & FerramentasBackendJava 17 • Spring Boot 3 • Spring Security • Spring Data JPA • Hibernate • JWT • MavenFrontendVue 3 • TypeScript • Vite • Pinia • Vue Router • Tailwind CSSBanco de DadosPostgreSQL • Flyway (Database Migrations)Web3 / BlockchainSmart Contracts (Solidity) • Web3j / Ethers.js • Ethereum / Polygon TestnetDevOps & InfraDocker • Docker Compose • GitHub Actions (CI/CD)
       
       📁 Estrutura do RepositórioO repositório é organizado no formato mono-repositório com responsabilidades estritamente separadas:Plaintextproofchain/

├── backend/            # API RESTful em Java / Spring Boot
│   ├── src/            # Código-fonte da aplicação
│   ├── pom.xml         # Gerenciador de dependências Maven
│   └── README.md       # Documentação detalhada do Backend
│
├── frontend/           # Interface Web SPA em Vue 3 + TypeScript
│   ├── src/            # Componentes, views, stores e assets
│   ├── package.json    # Gerenciador de dependências Node
│   └── README.md       # Documentação detalhada do Frontend
│
├── docker-compose.yml  # Orquestração local dos serviços
└── README.md           # Documentação principal (este arquivo)  

📚 Documentação TécnicaPara detalhes específicos de arquitetura, instalação e configuração de cada módulo:

⚙️ Documentação do Backend: Arquitetura em camadas, endpoints da API, configuração do Spring Security e conexão com o banco.💻 Documentação do Frontend: Estrutura de componentes, gerenciamento de estado global com Pinia, rotas e guia do Design System.

🚀 Como Executar o ProjetoPré-requisitosCertifique-se de ter instalado em sua máquina:GitDocker e Docker ComposeExecução com Docker Compose (Recomendado)A forma mais simples de subir todo o ecossistema (Backend, Frontend e Banco de Dados PostgreSQL) é utilizando o Docker Compose:Bash# 1. Clonar o repositório

git clone [https://github.com/seu-usuario/proofchain.git](https://github.com/seu-usuario/proofchain.git)

# 2. Navegar até o diretório do projeto
cd proofchain

# 3. Subir todos os contêineres em segundo plano
docker-compose up -d --build

Após a inicialização dos contêineres:
🌐 Frontend: http://localhost:5173
⚙️ Backend API: http://localhost:8080/api/v1
📚 Swagger Docs: http://localhost:8080/swagger-ui.html
🐘 PostgreSQL: localhost:5432
⚙️ Variáveis de AmbienteCrie um arquivo .env na raiz do projeto baseado no .env.example:Snippet de código# Database

POSTGRES_DB=proofchain_db
POSTGRES_USER=proofchain_user
POSTGRES_PASSWORD=secret_password

# Backend
SPRING_PROFILES_ACTIVE=dev
JWT_SECRET=sua_chave_secreta_jwt_super_segura_32_caracteres
BLOCKCHAIN_RPC_URL=[https://polygon-mumbai.infura.io/v3/seu_project_id](https://polygon-mumbai.infura.io/v3/seu_project_id)

# Frontend
VITE_API_BASE_URL=http://localhost:8080/api/v1

🚧 Status e RoadmapO projeto encontra-se atualmente na fase Em Desenvolvimento (v0.5.0) 
