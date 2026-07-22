# PROOFCHAIN
## Code Standards

Versão: 2.0

---

# Objetivo

Definir padrões obrigatórios de qualidade, organização e manutenção do código frontend.

O código gerado deve possuir padrão profissional de desenvolvimento.

---

# Princípios Gerais

Seguir:

- código limpo;
- responsabilidade única;
- baixo acoplamento;
- alta reutilização;
- fácil manutenção.

Evitar soluções temporárias que prejudiquem evolução futura.

---

# Nomeação

Utilizar nomes descritivos.

Preferir:

```
CertificateCard.vue
```

Evitar:

```
Card1.vue
ComponentA.vue
```

---

# Componentes Vue

Cada componente deve:

- possuir uma finalidade clara;
- possuir tamanho controlado;
- evitar múltiplas responsabilidades.

Exemplo correto:

```
HeroSection.vue

responsável pela Hero
```

Exemplo incorreto:

```
LandingPage.vue

contendo todo HTML, animações,
dados e regras
```

---

# Estrutura dos Componentes

Modelo recomendado:

```vue
<script setup lang="ts">

// imports

// types

// props

// emits

// state

// methods

</script>

<template>

</template>

<style scoped>

</style>
```

---

# TypeScript

Uso obrigatório.

Todas as interfaces devem ser declaradas.

Exemplo:

```ts
interface Certificate {
 id: string
 title: string
 date: string
}
```

---

# Props

Toda prop deve possuir tipagem.

Evitar:

```ts
props: ['data']
```

Utilizar:

```ts
defineProps<{
 certificate: Certificate
}>()
```

---

# Emits

Eventos devem possuir tipagem.

Exemplo:

```ts
defineEmits<{
 submit: []
}>()
```

---

# Variáveis

Utilizar nomes claros.

Preferir:

```
certificateList
userProfile
validationResult
```

Evitar:

```
data
item
obj
value1
```

---

# Funções

Funções devem:

- possuir uma única responsabilidade;
- possuir nomes claros;
- evitar efeitos colaterais.

Exemplo:

```
validateCertificate()
```

Melhor que:

```
process()
```

---

# Comentários

Comentários devem explicar:

- intenção;
- decisões importantes;
- regras não óbvias.

Não comentar código evidente.

Evitar:

```
// cria variável

const user = {}
```

---

# Documentação dos Arquivos

Arquivos importantes devem possuir comentário inicial.

Exemplo:

```ts
/**
 * Componente responsável pela apresentação
 * da validação pública de certificados.
 */
```

---

# Imports

Organizar:

1. Bibliotecas externas.

2. Componentes.

3. Composables.

4. Services.

5. Types.

6. Assets.

---

# Tratamento de Erros

Erros devem possuir tratamento adequado.

Evitar:

- console.log espalhado;
- mensagens técnicas ao usuário;
- falhas silenciosas.

---

# Serviços

Serviços devem abstrair comunicação.

Exemplo:

```
services/authService.ts
```

Componentes não devem chamar diretamente APIs.

---

# Mock

Mocks devem possuir estrutura semelhante à futura API.

Exemplo:

```ts
{
 id,
 name,
 status
}
```

---

# Organização de Pastas

Manter:

```
components

layouts

pages

services

stores

types

utils
```

---

# Código Morto

Não manter:

- imports não utilizados;
- componentes abandonados;
- arquivos temporários.

---

# Resultado Esperado

O código deve parecer desenvolvido por uma equipe profissional e estar preparado para manutenção futura.