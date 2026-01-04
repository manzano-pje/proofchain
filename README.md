# Valida Pro — Plataforma de Emissão e Validação de Certificados (MVP)

Este documento serve como o blueprint oficial para o desenvolvimento do MVP da plataforma **Valida Pro**.  
O projeto foca em **alta segurança**, **multi-tenancy**, **auditabilidade via blockchain** e uma arquitetura preparada para evolução, mantendo o frontend leve e simples.

---

## 🏗️ Arquitetura do Projeto

O sistema é dividido em três camadas principais: **API Restful (Backend)**, **SPA Leve (Frontend)** e **WEB3**.

---

### 1. Backend: Java Spring Boot

* **Linguagem**: Java 17 (LTS)
* **Framework**: Spring Boot 3.x
* **Segurança**: Spring Security 6 + JWT (Stateless)
* **Arquitetura**: Hexagonal (Ports & Adapters)
* **Banco de Dados**: PostgreSQL (produção) ou H2 (desenvolvimento)
* **Migrations**: Flyway ou Liquibase
* **Testes**: JUnit 5 + Mockito
* **Mensageria / Eventos**:
  * RabbitMQ (produção)
  * Fila leve em memória (MVP)

O backend é responsável por:
* Gestão de usuários e organizações
* Emissão de certificados
* Cálculo do hash criptográfico
* Orquestração do fluxo de registro on-chain
* Exposição de APIs REST

---

### 2. Frontend: Vanilla JS + Bootstrap 5

* **Tecnologia**: JavaScript puro (ES6+), HTML5, CSS3
* **Framework CSS**: Bootstrap 5 (customizado conforme protótipo)
* **Arquitetura**: SPA leve (sem frameworks pesados)
* **Dependências**:
  * `html2canvas` + `jspdf` para geração de PDF no cliente

O frontend é responsável por:
* Autenticação via Magic Link
* Emissão visual de certificados
* Dashboard administrativo
* Validação pública de certificados

#### Estrutura de Pastas (Frontend)
```text
/public
  /assets
    /css
      style.css
      bootstrap.custom.css
    /js
      app.js
      auth.js
      dashboard.js
      cert-generator.js
    /img
      logo.png
  index.html
```

---

### 3. WEB3 / Blockchain

A camada **WEB3** garante **imutabilidade, transparência e prova pública** dos certificados emitidos, funcionando como uma camada de confiança independente da aplicação.

#### Blockchain
* **Rede**: Polygon PoS
* **Motivação**:
  * Baixo custo de transação
  * Compatibilidade com EVM
  * Boa escalabilidade

A blockchain é utilizada **exclusivamente como camada de prova**, e não como banco de dados.

#### Smart Contract

Contrato simples e enxuto, sem lógica complexa ou dados sensíveis.

Funções disponíveis:

* registerCertificate(bytes32 hash, uint256 issuerId)
* getCertificate(bytes32 hash)

* `hash`: Hash criptográfico do certificado (PDF + metadados)
* `issuerId`: Identificador da organização emissora
* Nenhum dado pessoal é armazenado on-chain

#### Integração com o Backend

* **Módulo de Blockchain desacoplado**
  * Implementado seguindo arquitetura hexagonal
  * O core da aplicação não depende diretamente de Web3j
  * Facilita testes, manutenção e troca futura de tecnologia

* **Processamento orientado a eventos**
  * O registro on-chain ocorre de forma assíncrona
  * Permite emissão em lote e maior resiliência

#### Banco de Dados (Relacionamento com Blockchain)

* **PostgreSQL**
  * Preparado para crescimento com particionamento de tabelas
  * Persistência de:
    * Hash do certificado
    * Hash da transação on-chain
    * Timestamp do registro

#### Storage de Arquivos

* **PDF armazenado off-chain**
* Storage compatível com S3:
  * Cloudflare R2
* O hash do PDF é calculado antes do upload e registrado na blockchain

---

## 🔒 Segurança e Autenticação (Prioridade Máxima)

O sistema abandona senhas tradicionais em favor de um fluxo de **Magic Link** seguro.

### Fluxo de Login ("Magic Link")
1.  **Solicitação**: Usuário informa o e-mail no formulário de login.
2.  **Geração de Token**: O backend gera um token único, criptograficamente forte, com validade curta (ex: 15 minutos) e o armazena (Redis ou Tabela `login_tokens` com hash).
3.  **Envio**: Um e-mail é enviado contendo um link: `https://app.validapro.com/auth/verify?token=XYZ...`
4.  **Validação**: Ao clicar, o frontend chama a API. Se válido, o backend invalida o token temporário e retorna um **Par de JWTs** (Access Token + Refresh Token).

### Controle de Acesso (RBAC)
O sistema implementa 3 níveis de permissão via Spring Security Authorities:
*   `ROLE_SUPER_ADMIN`: Acesso total. Vê todas as empresas, gerencia planos, pagamentos e métricas globais.
*   `ROLE_TENANT_ADMIN` (Admin da Empresa): Gerencia alunos, emissões e configurações apenas da sua empresa (Tenant).
*   `ROLE_USER` (Operador): Apenas emite certificados, sem acesso a configurações financeiras ou de API.

---

## 🏢 Múli-Tenancy (Multi-empresas)

O sistema deve isolar dados de diferentes empresas.

*   **Estratégia**: Discriminator Column (Coluna `organization_id` em todas as tabelas principais).
*   **Implementação**:
    *   Um `Filter` do Spring intercepta o JWT.
    *   Extrai o `organization_id` do claim do token.
    *   Injeta no contexto (`SecurityContextHolder` ou `ThreadLocal`).
    *   Hibernate/JPA Filter aplica automaticamente `WHERE organization_id = ?` em todas as consultas.

---

## 💰 Super Admin e Gestão de Planos

O painel do Super Admin difere do painel comum. Ele foca em métricas de negócio (MRR, inadimplência).

*   **Funcionalidades Exclusivas**:
    *   Listagem de todas as Organizações (Tenants).
    *   Bloqueio/Desbloqueio de acesso de empresas.
    *   Visualização de faturas e status de pagamento (Integração futura com gateway como Stripe/Asaas).

---

## 🎨 Frontend e Design System

O frontend deve seguir estritamente os tokens visuais definidos no protótipo `certificados.html`.

### Paleta de Cores (Tokens)
```css
:root {
  --bg: #f6fafc;
  --card: #ffffff;
  --muted: #64748b;
  --accent: #60a5fa;   /* Azul Principal */
  --accent-2: #7dd3fc; /* Azul Secundário */
  --success: #10b981;  /* Verde Flux */
  --danger: #ef4444;
  --navy: #0b2540;     /* Textos e Títulos */
  --radius: 12px;
}
```

### Estrutura de Pastas Sugerida (Frontend)
```
/public
  /assets
    /css
      style.css
      bootstrap.custom.css
    /js
      app.js (Router simples e lógica global)
      auth.js (Login, Refresh Token)
      dashboard.js
      cert-generator.js (Canvas & PDF)
    /img
      logo.png
  index.html (Single Page Application shell)
```

---

## 🗄️ Modelo de Dados (Sugestão ER)

*   **Organizations**: `id`, `name`, `plan_type` (demo, pro), `status`, `wallet_address`.
*   **Users**: `id`, `email`, `role`, `organization_id (FK)`.
*   **Certificates**: `id (UUID)`, `student_name`, `course_name`, `issue_date`, `hash_proof`, `organization_id (FK)`.
*   **Transactions**: `id`, `chain_tx_hash`, `certificate_id (FK)`, `timestamp`.

---

## 🚀 Como Rodar o Projeto

### Backend
1.  Configure as variáveis de ambiente em `application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/validapro
    spring.datasource.username=postgres
    spring.datasource.password=suasenha
    app.jwt.secret=UMA_CHAVE_MUITO_LONGA_E_SEGURA_BASE64
    app.domain.frontend=http://localhost:5500
    ```
2.  Execute `mvn spring-boot:run`.

### Frontend
1.  Basta servir a pasta estática.
2.  Use o **Live Server** do VS Code ou `python -m http.server 5500`.

---

## ✅ Checklist de Desenvolvimento (MVP)

- [ ] Criar projeto Spring Boot (Web, Security, JPA, Postgres, Mail).
- [ ] Implementar fluxo de envio de e-mail (Mock para dev).
- [ ] Criar Entidades JPA e relacionamentos Multi-tenant.
- [ ] Configurar Spring Security com filtro JWT.
- [ ] Migrar layout do `certificados.html` para estrutura SPA.
- [ ] Conectar formulário de Login do Frontend com API `/auth/login`.
- [ ] Implementar Dashboard com dados reais da API.
- [ ] Implementar rotina de "Validação" pública (sem login).
