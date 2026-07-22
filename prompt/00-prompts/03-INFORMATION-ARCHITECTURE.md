# PROOFCHAIN
## Information Architecture

Versão: 2.0

---

# Objetivo

Este documento define a arquitetura da informação do frontend.

Antes de criar qualquer componente ou página, a IA deverá compreender como o usuário navega pela aplicação.

Nenhuma interface deverá ser construída antes da definição da estrutura de navegação.

---

# Estrutura Geral

O MVP é composto por duas áreas distintas.

Área Pública

↓

Landing

↓

Validação Pública

↓

Login

---

Área Autenticada

↓

Dashboard

---

# Mapa da Aplicação

```
/
│
├── Landing
│
├── Login
│
├── Validation
│
└── Dashboard
```

---

# Estrutura da Landing

A Landing deverá possuir as seguintes seções, nesta ordem:

```
Header

↓

Hero

↓

Sobre

↓

Recursos

↓

Como Funciona

↓

Casos de Uso

↓

Planos

↓

CTA Final

↓

Footer
```

Nenhuma seção deverá ser posicionada fora desta sequência sem justificativa funcional.

---

# Estrutura do Header

O Header deverá permanecer fixo durante toda a navegação.

Itens obrigatórios:

Logo

↓

Sobre

↓

Recursos

↓

Como Funciona

↓

Planos

↓

Contato

↓

Botão Entrar

Os links deverão utilizar rolagem suave (Smooth Scroll).

O botão Entrar deverá navegar para:

```
/login
```

---

# Estrutura da Hero

A Hero deverá ocupar toda a primeira dobra da página.

Ela será composta por dois blocos.

Lado esquerdo

- Badge
- Headline
- Subheadline
- CTA Conheça os Planos
- CTA Validar Certificado

Lado direito

- Imagem oficial do certificado
- Efeitos visuais discretos
- Glow
- Elementos gráficos relacionados à identidade visual

A Hero deverá manter equilíbrio entre texto e imagem.

O título não deverá ocupar largura excessiva.

---

# Sobre

Objetivo.

Explicar rapidamente:

- o problema;
- a solução;
- o diferencial do ProofChain.

A leitura deverá ser rápida.

---

# Recursos

Apresentar os principais diferenciais da plataforma.

Exemplos:

- Certificados Digitais
- Blockchain
- Validação Pública
- Gestão Centralizada
- Multi Instituição

Cada recurso deverá ser apresentado através de um Card.

---

# Como Funciona

Representar visualmente o fluxo da plataforma.

Fluxo recomendado:

```
Emitir

↓

Registrar

↓

Compartilhar

↓

Validar
```

As etapas deverão estar conectadas por uma linha visível.

A linha deverá possuir contraste suficiente em relação ao fundo.

---

# Casos de Uso

Demonstrar onde o ProofChain pode ser utilizado.

Exemplos:

- Universidades
- Empresas
- Escolas Técnicas
- Eventos
- Cursos Livres

Apresentar através de Cards.

---

# Planos

Apresentar os planos disponíveis.

Cada plano deverá possuir:

Nome

Descrição

Benefícios

Preço

Botão de ação

Utilizar dados simulados.

---

# CTA Final

Última chamada para ação.

Objetivo.

Incentivar o usuário a:

Entrar

ou

Conhecer os Planos.

---

# Footer

O Footer deverá conter:

Logo

Descrição resumida

Links rápidos

Contato

Redes sociais (estrutura preparada)

Copyright

---

# Login

Estrutura.

```
Logo

↓

Título

↓

Descrição

↓

Email

↓

Senha

↓

Lembrar acesso

↓

Esqueci minha senha

↓

Entrar
```

Após Login Mock.

↓

Dashboard.

---

# Dashboard

Estrutura.

```
Sidebar

↓

Header

↓

Resumo

↓

Cards

↓

Últimos Certificados

↓

Atividades Recentes
```

Utilizar a referência oficial enviada.

---

# Página de Validação

Estrutura.

```
Título

↓

Descrição

↓

Campo Código

↓

Botão Validar

↓

Resultado
```

Preparar área para futura leitura de QR Code.

---

# Fluxo Principal

```
Landing

↓

Login

↓

Dashboard

↓

Logout

↓

Landing
```

---

# Fluxo Público

```
Landing

↓

Validação Pública

↓

Resultado
```

---

# Estrutura das Rotas

```
/

/login

/dashboard

/validation
```

Preparar estrutura para futuras rotas.

Não implementá-las nesta etapa.

---

# Scroll

Os itens da Navbar deverão navegar para:

Sobre

↓

Seção Sobre

Recursos

↓

Seção Recursos

Como Funciona

↓

Seção Como Funciona

Planos

↓

Seção Planos

Contato

↓

Footer

Utilizar Smooth Scroll.

---

# Scroll Spy

Durante a navegação:

A Navbar deverá indicar automaticamente a seção atualmente visível.

O item ativo deverá possuir destaque visual.

---

# Mobile

No Mobile:

Substituir menu horizontal por Menu Hambúrguer.

O botão Entrar deverá permanecer em destaque.

A navegação deverá continuar utilizando Smooth Scroll.

---

# Próxima Etapa

Após a definição da Arquitetura da Informação, deverá ser criada a estrutura física de cada página, definindo layouts, distribuição dos elementos e organização visual.