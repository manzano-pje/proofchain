# PROOFCHAIN
## Interaction And Animations

Versão: 2.0

---

# Objetivo

Definir as interações e animações da interface.

O objetivo é criar uma experiência moderna e premium, adicionando movimento sem prejudicar a usabilidade.

As animações devem reforçar:

- qualidade;
- tecnologia;
- fluidez;
- profissionalismo.

---

# Princípios

As animações devem ser:

- suaves;
- rápidas;
- discretas;
- funcionais.

Evitar:

- excesso de movimento;
- efeitos chamativos;
- animações contínuas desnecessárias.

---

# Padrão de Transição

Todas as transições deverão utilizar:

```
200ms - 300ms
```

Com comportamento suave.

Preferência:

```
ease-in-out
```

---

# Entrada das Seções

Ao realizar scroll:

As seções devem aparecer utilizando:

- fade;
- leve deslocamento vertical.

Exemplo:

Estado inicial:

```
opacity: 0

translateY(20px)
```

Estado final:

```
opacity: 1

translateY(0)
```

---

# Hero Animation

A Hero deve possuir movimento discreto.

Elementos:

## Certificado

Aplicar:

- movimento vertical suave;
- sombra dinâmica;
- glow.

Exemplo:

```
translateY(0)

↓

translateY(-6px)

↓

translateY(0)
```

Duração:

5s - 6s

Loop:

suave.

---

# Navbar

Estado inicial:

Fundo transparente.

Após scroll:

Aplicar:

- background com transparência;
- blur;
- sombra discreta.

Transição:

200ms - 300ms.

---

# Links da Navbar

Hover:

Aplicar:

- mudança de cor;
- linha inferior animada.

Exemplo:

```
────────
```

A linha deve surgir da esquerda para direita.

---

# Cards

Aplicar interação nos cards das seguintes seções:

- Recursos;
- Por que contratar;
- Casos de Uso;
- Como Funciona;
- Planos.

---

# Hover dos Cards

Ao passar o mouse:

Aplicar:

```
scale(1.02)

translateY(-4px)
```

Adicionar:

- sombra maior;
- borda destacada;
- transição suave.

---

# Como Funciona

Cada etapa deverá possuir interação.

Ao passar o mouse:

- destacar etapa;
- aumentar levemente o ícone;
- reforçar conexão visual.

A linha do fluxo deve permanecer visível.

---

# Botões

Todos os botões devem possuir estados.

## Default

Estado normal.

---

## Hover

Aplicar:

- aumento de luminosidade;
- glow;
- elevação.

Exemplo:

```
translateY(-2px)
```

---

## Active

Ao clicar:

Reduzir elevação.

Criar sensação de pressionamento.

---

## Disabled

Reduzir contraste.

Impedir interação.

---

# Dashboard

Cards do Dashboard devem possuir:

- hover;
- elevação;
- destaque.

Não utilizar animações excessivas.

---

# Imagens

Imagens importantes podem possuir:

- zoom leve;
- sombra;
- movimento discreto.

Aplicar principalmente:

- certificado;
- mockup dashboard.

---

# Loading

Quando necessário:

Utilizar indicadores simples.

Evitar animações pesadas.

---

# Responsabilidade

As animações não devem alterar:

- layout;
- leitura;
- acessibilidade.

---

# Resultado Esperado

A interface deve parecer viva, moderna e profissional.

O usuário deve perceber qualidade através dos detalhes, não através de efeitos exagerados.