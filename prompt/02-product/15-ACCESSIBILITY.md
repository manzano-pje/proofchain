# PROOFCHAIN
## Accessibility

Versão: 2.0

---

# Objetivo

Definir regras de acessibilidade para garantir que a aplicação seja utilizável pelo maior número possível de usuários.

A interface deve equilibrar:

- estética;
- tecnologia;
- facilidade de uso;
- acessibilidade.

---

# Princípios

A aplicação deve seguir boas práticas de acessibilidade:

- percepção;
- compreensão;
- navegação;
- interação.

---

# Contraste

Todos os elementos devem possuir contraste suficiente.

Principalmente:

- textos;
- botões;
- links;
- campos;
- mensagens.

---

# Cores

Não utilizar somente cor para transmitir informação.

Exemplo incorreto:

Vermelho = erro

Sem texto.

Exemplo correto:

```
Ícone + mensagem + cor
```

---

# Tipografia

Garantir:

- tamanho adequado;
- boa leitura;
- espaçamento confortável.

Evitar:

- textos pequenos;
- excesso de texto em blocos longos.

---

# Botões

Todos os botões devem:

Possuir:

- nome claro;
- área de clique adequada;
- estados visuais.

Estados obrigatórios:

- normal;
- hover;
- active;
- focus;
- disabled.

---

# Focus

Todo elemento interativo deve possuir indicador visual de foco.

Exemplo:

- borda azul;
- glow discreto;
- outline.

Nunca remover foco sem substituição.

---

# Navegação por Teclado

Permitir navegação utilizando:

- Tab;
- Enter;
- Esc quando necessário.

A ordem dos elementos deve ser lógica.

---

# Formulários

Todos os campos devem possuir:

- label;
- placeholder quando necessário;
- mensagem de erro clara.

---

# Login

Campos:

Email.

Senha.

Devem possuir:

- identificação clara;
- validação;
- mensagens amigáveis.

---

# Mensagens de Erro

Evitar:

```
Error 401
Invalid Exception
```

Utilizar linguagem humana.

Exemplo:

```
Usuário ou senha inválidos.
```

---

# Imagens

Imagens importantes devem possuir:

```
alt
```

Exemplo:

```
alt="Certificado digital ProofChain"
```

---

# Ícones

Ícones decorativos devem ser ignorados por leitores de tela.

Utilizar:

```
aria-hidden="true"
```

quando aplicável.

---

# Animações

Evitar animações que prejudiquem usuários sensíveis a movimento.

Sempre priorizar:

- suavidade;
- baixa intensidade.

---

# Responsividade

A acessibilidade deve funcionar em:

- desktop;
- tablet;
- mobile.

---

# Resultado Esperado

O usuário deve conseguir navegar e utilizar o ProofChain independentemente do dispositivo ou método de interação.