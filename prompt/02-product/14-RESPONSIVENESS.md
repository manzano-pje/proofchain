# PROOFCHAIN
## Responsiveness

Versão: 2.0

---

# Objetivo

Definir regras para adaptação da interface em diferentes dispositivos.

O ProofChain deve possuir experiência consistente em:

- Desktop;
- Notebook;
- Tablet;
- Mobile.

---

# Princípio

A interface deve se adaptar ao dispositivo.

Não simplesmente reduzir elementos.

A experiência deve permanecer completa.

---

# Breakpoints

Utilizar os breakpoints padrão do Tailwind.

```
sm
md
lg
xl
2xl
```

---

# Desktop

Prioridade:

- amplo espaço;
- duas colunas;
- elementos visuais destacados.

---

# Hero Desktop

Layout:

```
Texto        Imagem

50%          50%
```

Garantir:

- título proporcional;
- certificado destacado;
- CTAs visíveis.

---

# Tablet

Ajustar:

- espaçamentos;
- tamanho dos textos;
- grids.

Evitar quebra de layout.

---

# Mobile

A estrutura deve ser vertical.

Exemplo:

```
Título

Descrição

CTAs

Imagem
```

---

# Navbar Mobile

Substituir menu horizontal.

Utilizar:

- botão hambúrguer;
- menu expandido;
- links organizados.

---

# Botão Entrar

No mobile:

Deve permanecer acessível.

Pode aparecer:

- dentro do menu;
- ou destacado no topo.

---

# Cards

Desktop:

Grid.

Exemplo:

```
3 colunas
```

Tablet:

```
2 colunas
```

Mobile:

```
1 coluna
```

---

# Como Funciona

Desktop:

Linha horizontal.

Mobile:

Fluxo vertical.

Exemplo:

```
Etapa 1

↓

Etapa 2

↓

Etapa 3

↓

Etapa 4
```

A linha de conexão deve continuar visível.

---

# Dashboard

Desktop:

```
Sidebar

Content
```

Mobile:

```
Menu recolhido

Content
```

---

# Inputs

Em dispositivos pequenos:

- largura total;
- altura confortável;
- fácil interação.

---

# Imagens

Nunca permitir:

- distorção;
- corte inadequado;
- perda de proporção.

Utilizar:

```
object-fit
```

quando necessário.

---

# Tipografia

Em telas menores:

Reduzir proporcionalmente.

Evitar:

- títulos quebrados;
- textos cortados;
- excesso de linhas.

---

# Espaçamento

Desktop:

120px - 140px entre seções.

Mobile:

64px - 80px.

---

# Performance Mobile

Priorizar:

- imagens leves;
- carregamento rápido;
- animações reduzidas.

---

# Testes Obrigatórios

Validar:

- 1920px;
- 1440px;
- 1024px;
- 768px;
- 390px.

---

# Resultado Esperado

O ProofChain deve manter aparência premium em qualquer dispositivo, sem perda de funcionalidade ou qualidade visual.