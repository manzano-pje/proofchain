# PROOFCHAIN
## NAVIGATION FLOW

Versão: 2.0

---

# Objetivo

Definir todos os fluxos de navegação do MVP.

---

# Rotas Principais

```
/

/login

/dashboard

/validation
```

---

# Fluxo Público

Usuário acessa:

```
/
```

Landing Page.

A partir dela:

Pode navegar pelas seções.

Pode validar certificado.

Pode acessar login.

---

# Fluxo Landing

```
Landing

↓

Navbar

↓

Seções

↓

CTA
```

---

# Navbar

Links:

```
Sobre

Recursos

Como Funciona

Planos

Contato
```

Todos utilizando:

Smooth Scroll.

---

# Botão Entrar

Localização:

Navbar.

Comportamento:

```
Click

↓

/login
```

---

# Hero

CTAs:

## Conheça os Planos

Scroll:

```
/#pricing
```

---

## Validar Certificado

Navegação:

```
/validation
```

---

# Login

Fluxo:

```
Usuário informa dados

↓

Validação Mock

↓

Dashboard
```

---

# Dashboard

Após autenticação:

```
/dashboard
```

Permitir:

- navegação interna;
- visualização dos dados;
- logout.

---

# Logout

Fluxo:

```
Dashboard

↓

Logout

↓

Landing
```

---

# Validação Pública

Fluxo:

```
Landing

↓

Validation

↓

Código

↓

Resultado
```

---

# Scroll Spy

Durante navegação:

A seção ativa deve atualizar o menu.

Exemplo:

Usuário na seção Recursos.

Menu:

```
Recursos ativo
```

---

# Mobile Navigation

No mobile:

Substituir menu por botão hambúrguer.

Ao abrir:

Exibir todos os links.

Manter:

Botão Entrar destacado.

---

# Regra Final

Toda navegação deve funcionar antes de qualquer integração backend.