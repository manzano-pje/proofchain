Markdown
# AGENTS.MD — Especificação Técnica: CrownCarousel 3D

## Histórico de decisões
- 2026-08-23: Criação inicial do documento, migrando o escopo para a stack **Vue 3 + TypeScript** utilizando exclusivamente CSS 3D Transforms e APIs nativas (Pointer Events, ResizeObserver, requestAnimationFrame).
- 2026-08-23: Definido que o CrownCarousel integrará a seção "Por que escolher" da Landing Page do ProofChain.
- 2026-08-23: Definições arquiteturais incorporadas a partir das respostas do usuário:
  - **Gestão de Keys (Clonagem Mínima)**: Opção A — Concatenação do ID original com sufixo indexado (`item.id + '-clone-' + index`) para evitar conflitos de reatividade no Vue.
  - **Comportamento Pós-Flip no Auto-Rotate**: Opção B — Desvirar automaticamente qualquer card aberto no exato momento em que o auto-rotate for retomado, garantindo uma estética limpa durante o movimento.
  - **Fallback para `prefers-reduced-motion`**: Opção A — Manutenção da geometria 3D, porém desligando o auto-rotate permanentemente e tornando a transição de Flip instantânea (0ms).
  - **Tratamento de SSR e Hidratação**: Opção B — Execução segura e estrita de todo o comportamento geométrico, observadores e loops visuais confinada ao hook `onMounted` (sem renderização prévia em server-side para estes bindings).

---

## 1) Contexto do App e Objetivo

- **Produto**: ProofChain.
- **Módulo**: CrownCarousel 3D.
- **Local de Utilização**: Seção "Por que escolher" da Landing Page.
- **Objetivo**: Implementar um carrossel visual tridimensional no formato de um **anel cilíndrico inclinado**, no qual os cards são distribuídos espacialmente ao redor de um eixo central. Cada card possui duas faces (Frente e Verso) acessíveis via Flip 3D. O componente deve proporcionar uma apresentação visual diferenciada combinando auto-rotação, arraste e efeito de profundidade.

## 2) Escopo e Limites

### 2.1 Dentro do Escopo
- Receber itens via `props` e distribuí-los em circunferência 3D.
- Rotação orbital, inclinação global, e auto-rotação.
- Rotação manual por *drag* (mouse e touch), com diferenciação estrita de clique (Ghost Clicks).
- Flip individual dos cards.
- Aplicação dinâmica de profundidade (opacidade e blur) na parte posterior.
- Recálculo responsivo de geometria (`ResizeObserver`).
- Suporte a `prefers-reduced-motion`.

### 2.2 Fora do Escopo
- Integração com CMS ou carregamento remoto/API (Apenas recebe dados já prontos).
- Persistência de estado (Local/Session Storage, Cookies).
- Navegação por teclado.
- Uso de painéis de configuração visual de terceiros.
- Dependências adicionais externas, especialmente para animação 3D (ex: Swiper, ThreeJS).

---

## 3) Tech Stack & Coding Guideline

### 3.1 Tecnologias Obrigatórias
- **Vue 3** (Composition API)
- **TypeScript**
- **CSS3** (Variáveis, 3D Transforms)
- **APIs Nativas**: `ResizeObserver`, `requestAnimationFrame`, `Pointer Events`, `matchMedia`.

### 3.2 Regras de Implementação
- **Nenhuma dependência externa exclusiva** para a geometria 3D. O Swiper (existente no projeto) NÃO deve ser usado aqui.
- O array recebido via `props` é imutável. Clonagens visuais devem utilizar sufixos controlados de keys (`item.id + '-clone-' + index`) sem corromper a raiz original dos dados.
- Performance: Priorizar propriedades compostas na GPU (`transform`, `opacity`, `filter`). Evitar recálculos de `top`, `left`, `width` durante animações.
- Clean Up obrigatório: Todas as rotinas (`rAF`, observers, listeners, timeouts) devem ser destruídas no hook `onUnmounted`.

---

## 4) Arquitetura de Arquivos

O módulo seguirá o padrão estrutural modular:

```text
CrownCarousel/
├── CrownCarousel.vue (Apresentação Visual e Conexão)
├── CrownCarousel.ts (Lógica de Negócio UI, Cálculos)
└── CrownCarousel.css (Estilos, Estrutura 3D, Animações)
Responsabilidades
CrownCarousel.vue: Estrutura de template, renderização com v-for, binds de props/classes/eventos. Nenhuma lógica matemática densa no <script setup>, apenas repasse do .ts.

CrownCarousel.ts: Tipagens, estado, geometria, motores de interação (drag/click/ghost-click), listeners, lógica de ResizeObserver e requestAnimationFrame. Confinado estritamente a escopos de montagem onMounted para compatibilidade com SSR.

CrownCarousel.css: Configuração de perspective, transform-style: preserve-3d, responsividade e design das faces do card.

5) Configuração Interna e Modelo de Dados
5.1 Interface Mínima (Entrada de Dados)
O componente não conhece a origem; a seção pai injeta o conteúdo.

TypeScript
export interface CarouselItemData {
  id: string;
  image: string;
  title: string;
  description: string;
}

export interface CrownCarouselProps {
  items: CarouselItemData[];
}
5.2 Variáveis de Configuração Internas
Alocadas no CrownCarousel.ts. Devem possuir comentários para futura calibração por desenvolvedores. NENHUM controle administrativo visual será feito para estes.

TypeScript
// Ajustável futuramente para calibrar proporções
const CARD_WIDTH = 280; 
const CARD_HEIGHT = 400;
const RADIUS_GAP = 40; // Espaçamento entre as arestas
const PERSPECTIVE = 1200;
const TILT_ANGLE_X = -10; // Inclinação global da câmera
const AUTO_ROTATE_SPEED = 0.5; // Graus por frame
const DRAG_SENSITIVITY = 1.2; 
const FLIP_THRESHOLD = 5; // Limite de px para diferenciar drag de click
const RESUME_DELAY = 1000; // MS de pausa antes de retomar auto-rotate
6) Arquitetura Espacial e UI/UX (DOM Tree)
6.1 Estrutura em Níveis 3D
HTML
<!-- Viewport: Define a perspectiva, reage ao ResizeObserver e corta overflow -->
<div class="crown-carousel-viewport">
  <!-- Camera/Stage: Inclinação X do conjunto -->
  <div class="crown-carousel-stage">
    <!-- Ring/Rotator: preserve-3d, sofre rotação orbital interativa no eixo Y -->
    <div class="crown-carousel-ring">
      <!-- Card: Posicionado radialmente (rotateY + translateZ). Aplica opacidade/blur -->
      <div class="crown-carousel-card">
        <!-- Flip Container: Rotação local 180deg (independente da orbital) -->
        <div class="crown-carousel-flip-container">
           <div class="front">Frente (Imagem + Título)</div>
           <div class="back">Verso (Descrição)</div>
        </div>
      </div>
    </div>
  </div>
</div>
6.2 Cálculo Geométrico, Quantidade Mínima e Keys
O raio do cilindro e a distância translateZ são derivados matematicamente em CrownCarousel.ts da largura do card e do RADIUS_GAP.

Mínimo Estético e Chaves: O anel exige no mínimo 6 posições. Se items.length < 6, o array interno de renderização duplica os itens utilizando chaves tratadas por ID e índice (:key="item.id + '-clone-' + index"), garantindo integridade e evitando bugs de reatividade no Virtual DOM do Vue sem alterar a raiz original dos dados.

6.3 Efeito de Profundidade (Depth of Field)
Durante a rotação ativa via rAF, mapeia-se o ângulo global atual do anel somado à posição relativa de cada card.

Frente (270° → 360° / 0° → 90°): opacity: 1, filter: blur(0px).

Trás (90° → 270°): Redução dinâmica de opacidade (ex: 0.3) e aplicação de blur progressivo para não competir visualmente com o conteúdo frontal.

7) Mecânica das Interações (Motor TS)
7.1 Diferenciação Drag vs Click (Ghost Clicks)
Uso obrigatório de Pointer Events (pointerdown, pointermove, pointerup).

pointerdown registra startX, startY.

pointerup calcula o delta (X e Y).

Se delta > FLIP_THRESHOLD (5px), define ação como DRAG (rotaciona o anel global, sem flip).

Se delta <= FLIP_THRESHOLD, define ação como CLICK e executa o Flip local no card tocado.

7.2 Orquestração do Auto-Rotate e Reset de Flips
Animado por requestAnimationFrame (NUNCA por setInterval).

Interação do usuário (pointerdown, pointerenter) altera estado isInteracting = true, interrompe o loop e desarma aberturas pendentes.

Desengajamento limpa as flags e ativa setTimeout de RESUME_DELAY (1000ms). Ao concluir sem interrupção, todos os cards virados são automaticamente desvirados (recolocados em face frontal) e o motor retoma o auto-rotate suavemente.

7.3 Flip Independente e Segregação de Eixos
Estado individual (isFlipped) para cada card durante a interação parada.

Regra Férrea: O giro do anel (Orbital Transform) ocorre na .crown-carousel-ring e no mapeamento base do .crown-carousel-card. A rotação do flip (rotateY(180deg)) ocorre exclusivamente no .crown-carousel-flip-container, evitando a destruição vetorial do card em relação ao anel principal. Ambas as faces (.front, .back) levam backface-visibility: hidden.

8) Tratamentos Especiais
Reduced Motion:

CSS @media (prefers-reduced-motion: reduce) + JS window.matchMedia.

Se ativado, o script desabilita o auto-rotate permanentemente e retira as interpolações de transição do Flip configurando-as para 0ms (instantâneas), priorizando estritamente a acessibilidade.

SSR/Inicialização (Client-Only Safe):

Assinaturas de ResizeObserver, loops de requestAnimationFrame e checagens de window são envelopados estritamente dentro do ciclo de vida onMounted(), blindando o build contra erros de execução em servidores sem contexto visual.

Cleanup Seguro:

onUnmounted() deve obrigatoriamente chamar cancelAnimationFrame, resizeObserver.disconnect() e limpar listeners para previnir memory leaks.

9) Critérios de Aceite
Funcionais e Geométricos
CA-001: O componente renderiza 100% dos itens via Vue iterando sobre os dados passados em props.

CA-002: A formação visual garante o aspecto tridimensional cilíndrico (Cards distribuídos em 360 graus, com inclinação).

CA-003: Se os props trouxerem menos de 6 itens, a renderização os multiplica visualmente mapeando chaves seguras (item.id + '-clone-' + index) para fechar 6 slots físicos sem distorcer o array base.

CA-004: Ao arrastar a tela ou redimensionar a janela, ResizeObserver calibra o layout mantendo as proporções (Responsividade sem quebra).

CA-014: Cards orbitando na metade traseira sofrem diminuição de opacidade e recebem blur.

CA-015: Múltiplos transforms 3D (Orbital vs Flip) nunca coexistem ou conflitam no mesmo node DOM.

Interativos
CA-005: O anel gira de maneira automatizada e suave em idle state (requestAnimationFrame).

CA-006 / CA-007: Auto-rotate pausa instantaneamente sob qualquer toque/hover e retorna 1s exato após encerramento da ação, resetando e desvirando automaticamente quaisquer cards que estivessem abertos.

CA-008: Arrasto horizontal nativo (Mouse/Touch unificados via Pointer) altera o eixo Y global proporcionalmente à sensibilidade.

CA-009 / CA-010: Mover o cursor mais de 5px (Drag) cancela as intenções de clique, não ativando flip sob nenhuma circunstância de rolagem. Cliques puros engatilham flips localizados.

CA-011 / CA-012: O flip roda 180º via CSS3 no container, apresentando no verso título e descrição isolados da frente (backface-visibility).

CA-013: Flips são independentes durante o manuseio estático.

Técnicos / Performance
CA-017: Acessibilidade contemplada em estrutura, labels semânticos nos controles ocultos e fallback legível.

CA-018: Conformidade total à detecção de sistema prefers-reduced-motion: reduce (desligando rotação automática e zerando o tempo de animação do flip).

CA-019: Ao desmontar (unmount), a memória global permanece virgem (Nenhum listener pendente ou Observer ativo).

CA-020: A compilação pelo Vite (npm run type-check e npm run build) termina livre de erros de tipagem.