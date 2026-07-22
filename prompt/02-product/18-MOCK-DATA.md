# PROOFCHAIN
## Mock Data

Versão: 2.0

---

# Objetivo

Definir a utilização de dados simulados no MVP.

Nenhuma informação deverá depender de backend ou banco de dados nesta etapa.

---

# Princípios

Os dados mockados devem:

- parecer reais;
- possuir estrutura semelhante à futura API;
- estar isolados da interface.

---

# Organização

Criar uma estrutura:

```
src

└── mocks

    ├── users.ts

    ├── certificates.ts

    ├── courses.ts

    ├── dashboard.ts

    └── plans.ts
```

---

# Usuários

Exemplo:

```ts
{
 id: "001",
 name: "Administrador",
 email: "admin@proofchain.com",
 role: "ADMIN"
}
```

---

# Login Mock

Fluxo:

Usuário informa:

- email;
- senha.

Sistema valida contra dados simulados.

---

# Resultado Login

Sucesso:

Retornar usuário.

Redirecionar:

```
/dashboard
```

Erro:

Retornar mensagem amigável.

---

# Certificados

Estrutura sugerida:

```ts
{
 id: "CERT-001",
 holder: "João Silva",
 course: "Blockchain Fundamentals",
 institution: "Proof Academy",
 status: "VALID"
}
```

---

# Validação

Entrada:

```
certificateCode
```

Processo:

Consultar mock.

Resultado:

## Válido

Exibir:

- nome;
- curso;
- instituição;
- data.

---

## Inválido

Exibir:

Mensagem de erro.

---

# Dashboard Data

Utilizar dados simulados:

## Indicadores

Exemplo:

```
Certificates Issued: 245

Courses: 18

Validations: 1200
```

---

# Lista de Certificados

Criar registros simulados.

Campos:

- código;
- nome;
- curso;
- status;
- data.

---

# Cursos

Estrutura:

```ts
{
 id: "COURSE-001",
 title: "Web3 Development",
 students: 120
}
```

---

# Planos

Estrutura:

```ts
{
 name: "Professional",
 price: "99",
 features: [
   "Certificados digitais",
   "Validação pública"
 ]
}
```

---

# Services

Mesmo utilizando mocks, criar camada de serviço.

Exemplo:

```
certificateService.ts
```

Responsável por:

- buscar certificados;
- validar dados;
- retornar respostas.

---

# Regras

Não colocar dados diretamente:

Dentro de:

- páginas;
- componentes;
- templates.

---

# Preparação Futura

A substituição por API real deverá alterar apenas:

Services.

Componentes e páginas devem permanecer funcionando.

---

# Resultado Esperado

O MVP deve parecer uma aplicação real, mesmo utilizando dados simulados.