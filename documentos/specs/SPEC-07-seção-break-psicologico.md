# AGENTS.MD - Especificação Técnica e de Produto (ProofChain)

## Histórico de decisões

- 2026-08-28: Escolha da Opção A (HTML5 Canvas 2D + Spring Physics) como arquitetura de renderização técnica.
- 2026-08-28: Definida a presença de 2 a 3 estruturas abstratas de corais com alturas e densidades assimétricas para cobrir a largura full-width e enriquecer o comportamento de ressonância.
- 2026-08-28: Criação da especificação para a seção "Break Psicológico". Conceito estético definido como "Paisagem Orgânica Submarina Abstrata", abandonando representações literais de tecnologia (ex: partículas, redes blockchain, dashboards).
- 2026-08-28: Removidas abordagens de bibliotecas React (ex: Framer Motion) em favor de componente Vue 3 (`<canvas>`) encapsulado e leve.

---

## 1. Visão Geral (O que é e por que estamos fazendo)

**Conceito:** Uma seção visual independente ("Break Psicológico") que atua como um respiro contemplativo entre os carrosséis em movimento e a tabela de preços dos planos.
**Objetivo do Produto:** Desacelerar a carga cognitiva do usuário. Não há transmissão de dados, métricas, textos promocionais ou benefícios de produto. A resposta emocional alvo é: _"Parece que estou olhando para algo vivo se movimentando suavemente na água"_.
**Declaração de Visão:** Uma paisagem submarina abstrata em _full-width_, formada por 2 a 3 estruturas orgânicas de corais ancoradas em uma base densa que sobem de forma delicada, permanecendo contemplativas até que o mouse provoque uma onda de ressonância e propagação suave entre as estruturas.

---

## 2. Requisitos de UI/UX e Composição Visual

### 2.1. Estrutura e Layout

- **Dimensões:** Ocupar 100% da largura da viewport (`width: 100vw`). Altura fluida (~400px a 500px), garantindo amplo espaço negativo na metade superior.
- **Quantidade e Disposição dos Corais:** A cena deve conter **2 a 3 organismos de corais**, distribuídos de forma assimétrica ao longo da base inferior:
  - _Coral Principal:_ Maior altura, posição centro-esquerda.
  - _Coral Secundário:_ Altura média, mais aberto, posição direita.
  - _Coral de Apoio (Opcional):_ Pequeno aglomerado compacto no canto esquerdo.
- **Cores e Estilo Visual:** Fundo escuro azul-noite/grafite da marca. Estruturas em tons quentes e sutis de dourado/amarelado e refrações suaves de iluminação, reproduzindo o tom de anêmonas sob iluminação subaquática.
- **Restrições Estritas:** Proibido o uso de tentáculos sci-fi, neon tech berrante, fios digitais rígidos, código, ícones ou qualquer alusão a circuitos/dashboard.

### 2.2. Arquitetura do Organismo (Bottom-Up)

As estruturas obedece a uma regra estrita de crescimento geométrico contínuo:

1.  **Raiz (Base Inferior):** Aglomerado compacto e denso de pequenos pontos e nós fixos na parte inferior da tela.
2.  **Ramificação (Crescimento):** Os elementos se conectam e sobem em uma hierarquia de árvore/esqueleto, abrindo braços curvos.
3.  **Hastes e Pontas (Topo):** Conforme sobem, as linhas ficam progressivamente mais finas, espaçadas, curvas e menos densas.
4.  **Espaço Negativo:** A metade superior da seção permanece predominantemente limpa.

---

## 3. Comportamento e Física (HTML5 Canvas 2D + Spring Physics)

### 3.1. Estado de Repouso (Idle)

- As hastes possuem um movimento natural e sutil de maré, simulado por funções senoidais de baixa frequência rodando em tempos assíncronos para cada nó do coral.

### 3.2. Perturbação e Ressonância via Mouse

A interação do mouse não deve ser um simples rastreador rígido do cursor. Segue o ciclo físico de 4 fases:

1.  **Perturbação Local:** O cursor do mouse projeta uma força repulsiva/hidrodinâmica sutil ao passar próximo às hastes.
2.  **Propagação:** A força viaja hierarquicamente do ponto afetado para os nós filhos e braços vizinhos.
3.  **Ressonância Inter-Organismos:** A onda de choque criada no coral principal transmite uma leve vibração por simulação de fluido para o coral secundário mais próximo.
4.  **Amortecimento (Damping):** Ao retirar ou parar o mouse, a energia se dissipa gradualmente (fator de amortecimento ~0.88 - 0.92), fazendo os ramos oscilarem suavemente até retornarem ao estado de maré contínua.

---

## 4. Arquitetura de Engenharia Frontend (Vue 3 Component)

- **Tecnologia:** HTML5 Canvas 2D nativo encapsulado em um componente Vue 3 (`SubmarineBreak.vue`).
- **Modelagem Matemática:** **Verlet Integration** (Grafo de Nós e Molas com Restrições de Distância).
- **Estrutura de Dados:**
  ```javascript
  // Estrutura conceitual dos nós dos corais
  class CoralNode {
    x, y;           // Posição atual
    originX, originY; // Posição de repouso
    vx, vy;         // Vetores de velocidade
    parent;         // Referência ao nó pai (propagação de força)
    children;       // Sub-ramos
    isRoot;         // Booleano (se true, fixo na base)
  }
  ```
- **Performance & Lifecycle Management:**
  - Uso de `requestAnimationFrame` gerenciado pelo ciclo de vida do Vue.
  - Execução dos métodos `cancelAnimationFrame` e remoção de listeners de eventos no `onUnmounted()` para evitar vazamento de memória (_memory leaks_).

---

## 5. Critérios de Aceite (Definition of Done)

- [ ] A seção ocupa 100% da largura sem travar ou interferir no scroll vertical do usuário.
- [ ] Renderização fluida de 2 a 3 corais assimétricos sem queda de FPS.
- [ ] O movimento do mouse aplica uma força fluida e a onda se propaga de forma elástica pelas hastes antes de amortecer.
- [ ] O topo da seção mantém espaço negativo limpo, sem poluição visual.
- [ ] O componente desativa seus loops de animação ao ser desmontado no Vue.
  - Execução dos métodos `cancelAnimationFrame` e remoção de listeners de eventos no `onUnmounted()` para evitar vazamento de memória (_memory leaks_).

---

## 5. Critérios de Aceite (Definition of Done)

- [ ] A seção ocupa 100% da largura sem travar ou interferir no scroll vertical do usuário.
- [ ] Renderização fluida de 2 a 3 corais assimétricos sem queda de FPS.
- [ ] O movimento do mouse aplica uma força fluida e a onda se propaga de forma elástica pelas hastes antes de amortecer.
- [ ] O topo da seção mantém espaço negativo limpo, sem poluição visual.
- [ ] O componente desativa seus loops de animação ao ser desmontado no Vue.
