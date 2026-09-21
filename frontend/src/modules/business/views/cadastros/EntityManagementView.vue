<!-- Administrative entity management page: navigation, form, list and details modal. -->

<script setup lang="ts">
/** Composes the administrative UI while domain state remains in the composable. */

import { computed, ref } from 'vue'
import Section from '@/core/components/ui/Section/Section.vue'
import Container from '@/core/components/ui/Container/Container.vue'
import { useEntityManagement } from '../../composables/UseEntityManagement'
import type { EntityItem } from '../../types/Entity.types'
import { ADMIN_MENU } from '../../types/Entity.types'

/* --------------------------------------------------------------------------
 * COMPOSABLE — toda a lógica de negócio e estado reativo
 * ------------------------------------------------------------------------ */
/* Domain state and actions. */
const {
  recentItems,
  selectedItem,
  isModalOpen,
  isLoading,
  isSubmitting,
  searchQuery,
  feedback,
  editingId,
  formData,
  roleOptions,
  statusOptions,
  isFormValid,
  totalCount,
  totalIssuedCertificates,
  filteredItems,
  hasResults,
  submitLabel,
  formatDateTime,
  clearFeedback,
  handleCreate,
  handleOpenModal,
  handleCloseModal,
  handleEdit,
  handleDelete,
  handleCancelEdit,
  handleViewFullList,
} = useEntityManagement()

/* --------------------------------------------------------------------------
 * ESTADO LOCAL DE UI (não pertence ao domínio de negócio)
 * ------------------------------------------------------------------------ */
/* View-only state. */

/** Identificador do item de menu atualmente ativo (Usuários por padrão). */
/** Currently selected administrative section. */
const activeMenuItem = ref<string>('users')

/** Controla o recolhimento da sidebar em telas menores. */
/** Controls the compact navigation state. */
const isSidebarCollapsed = ref<boolean>(false)

/** Menu categorizado do painel administrativo. */
/** Categorized administrative navigation. */
const menuGroups = ADMIN_MENU

/** Indica se o formulário está em modo de edição. */
/** Indicates whether the form is editing an existing record. */
const isEditing = computed<boolean>(() => editingId.value !== null)

/**
 * Define o item de menu ativo.
 * @param id Identificador do item clicado.
 */
/** Selects an enabled navigation item. */
const handleMenuSelect = (id: string): void => {
  activeMenuItem.value = id
}

/** Toggles the compact navigation state. */
const toggleSidebar = (): void => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
}

/**
 * Retorna a classe BEM do badge de status.
 * @param status Status do registro.
 */
/** Returns the BEM class for a status badge. */
const statusClass = (status: EntityItem['status']): string =>
  `table-card__status table-card__status--${status.toLowerCase()}`

/**
 * Retorna a classe BEM do badge de papel.
 * @param role Papel do registro.
 */
/** Returns the BEM class for a role badge. */
const roleClass = (role: EntityItem['role']): string =>
  `table-card__role table-card__role--${role.toLowerCase()}`
</script>

<template>
  <div class="admin-page">
    <!-- ==================================================================
         MENU PRINCIPAL (SIDEBAR LATERAL)
         ================================================================== -->
    <aside class="sidebar" :class="{ 'sidebar--collapsed': isSidebarCollapsed }">
      <div class="sidebar__brand">
        <span class="sidebar__logo">⛓️</span>
        <div class="sidebar__brand-text">
          <strong class="sidebar__brand-name">ProofChain</strong>
          <span class="sidebar__brand-sub">Admin Console</span>
        </div>
        <button
          type="button"
          class="sidebar__toggle"
          aria-label="Recolher menu"
          @click="toggleSidebar"
        >
          ☰
        </button>
      </div>

      <nav class="sidebar__nav" aria-label="Navegação administrativa">
        <div v-for="group in menuGroups" :key="group.category" class="sidebar__group">
          <span class="sidebar__group-label">{{ group.label }}</span>

          <ul class="sidebar__list">
            <li v-for="item in group.items" :key="item.id" class="sidebar__item">
              <button
                type="button"
                class="sidebar__link"
                :class="{
                  'sidebar__link--active': activeMenuItem === item.id,
                  'sidebar__link--disabled': item.disabled,
                }"
                :disabled="item.disabled"
                :aria-current="activeMenuItem === item.id ? 'page' : undefined"
                @click="handleMenuSelect(item.id)"
              >
                <span class="sidebar__icon" aria-hidden="true">{{ item.icon }}</span>
                <span class="sidebar__text">{{ item.label }}</span>
                <span v-if="item.badge" class="sidebar__badge">{{ item.badge }}</span>
              </button>
            </li>
          </ul>
        </div>
      </nav>

      <div class="sidebar__footer">
        <button type="button" class="sidebar__logout">
          <span class="sidebar__icon" aria-hidden="true">⎋</span>
          <span class="sidebar__text">Sair</span>
        </button>
      </div>
    </aside>

    <!-- ==================================================================
         ÁREA PRINCIPAL
         ================================================================== -->
    <div class="admin-page__main">
      <!-- ----------------------------------------------------------------
           HEADER BAR
           ---------------------------------------------------------------- -->
      <header class="top-header">
        <div class="top-header__title-block">
          <h1 class="top-header__title">Cadastros e Gestão</h1>
          <p class="top-header__subtitle">
            Gerencie usuários, papéis e permissões da plataforma ProofChain
          </p>
        </div>

        <div class="top-header__actions">
          <div class="top-header__search">
            <span class="top-header__search-icon" aria-hidden="true">🔍</span>
            <input
              v-model="searchQuery"
              type="search"
              class="top-header__search-input"
              placeholder="Buscar por nome, e-mail, perfil..."
              aria-label="Busca global"
            />
          </div>

          <div class="top-header__profile">
            <div class="top-header__avatar" aria-hidden="true">AB</div>
            <div class="top-header__profile-info">
              <strong class="top-header__profile-name">Ana Beatriz</strong>
              <span class="top-header__profile-role">Administradora</span>
            </div>
          </div>
        </div>
      </header>

      <!-- ----------------------------------------------------------------
           SECTION -> CONTAINER -> CONTEÚDO
           ---------------------------------------------------------------- -->
      <Section class="admin-section">
        <Container class="admin-container">
          <!-- Feedback transiente -->
          <div
            v-if="feedback"
            class="admin-feedback"
            :class="`admin-feedback--${feedback.type}`"
            role="status"
          >
            <span class="admin-feedback__message">{{ feedback.message }}</span>
            <button
              type="button"
              class="admin-feedback__close"
              aria-label="Fechar aviso"
              @click="clearFeedback"
            >
              ✕
            </button>
          </div>

          <!-- ============================================================
               4. CADASTRO (PAINEL SUPERIOR - FORMULÁRIO)
               ============================================================ -->
          <section class="form-card">
            <header class="form-card__header">
              <div class="form-card__heading">
                <h2 class="form-card__title">
                  {{ isEditing ? 'Editar Registro' : 'Novo Cadastro' }}
                </h2>
                <p class="form-card__description">
                  Preencha os dados abaixo para
                  {{
                    isEditing ? 'atualizar o registro selecionado' : 'adicionar um novo registro'
                  }}
                  à plataforma.
                </p>
              </div>

              <div class="form-card__metrics">
                <span class="form-card__metric">
                  <strong>{{ totalCount }}</strong> recentes
                </span>
                <span class="form-card__metric">
                  <strong>{{ totalIssuedCertificates }}</strong> certificados
                </span>
              </div>
            </header>

            <form class="form-card__form" @submit.prevent="handleCreate">
              <div class="form-card__grid">
                <!-- Nome -->
                <div class="form-card__field">
                  <label class="form-card__label" for="entity-name">
                    Nome Completo <span class="form-card__required">*</span>
                  </label>
                  <input
                    id="entity-name"
                    v-model="formData.name"
                    type="text"
                    class="form-card__input"
                    placeholder="Ex.: Maria Fernanda Souza"
                    autocomplete="name"
                  />
                  <span
                    v-if="formData.name.length > 0 && formData.name.trim().length < 3"
                    class="form-card__error"
                  >
                    Informe ao menos 3 caracteres.
                  </span>
                </div>

                <!-- E-mail -->
                <div class="form-card__field">
                  <label class="form-card__label" for="entity-email">
                    E-mail Institucional <span class="form-card__required">*</span>
                  </label>
                  <input
                    id="entity-email"
                    v-model="formData.email"
                    type="email"
                    class="form-card__input"
                    placeholder="nome@instituicao.edu.br"
                    autocomplete="email"
                  />
                  <span
                    v-if="
                      formData.email.length > 0 &&
                      !/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(formData.email)
                    "
                    class="form-card__error"
                  >
                    Informe um e-mail válido.
                  </span>
                </div>

                <!-- Perfil -->
                <div class="form-card__field">
                  <label class="form-card__label" for="entity-role">
                    Perfil de Acesso <span class="form-card__required">*</span>
                  </label>
                  <select id="entity-role" v-model="formData.role" class="form-card__select">
                    <option v-for="role in roleOptions" :key="role" :value="role">
                      {{ role }}
                    </option>
                  </select>
                </div>

                <!-- Status -->
                <div class="form-card__field">
                  <label class="form-card__label" for="entity-status">
                    Status <span class="form-card__required">*</span>
                  </label>
                  <select id="entity-status" v-model="formData.status" class="form-card__select">
                    <option v-for="status in statusOptions" :key="status" :value="status">
                      {{ status }}
                    </option>
                  </select>
                </div>
              </div>

              <footer class="form-card__actions">
                <button
                  v-if="isEditing"
                  type="button"
                  class="form-card__button form-card__button--ghost"
                  @click="handleCancelEdit"
                >
                  Cancelar
                </button>

                <button
                  type="submit"
                  class="form-card__button form-card__button--primary"
                  :disabled="!isFormValid || isSubmitting"
                >
                  <span v-if="isSubmitting" class="form-card__spinner" aria-hidden="true"></span>
                  {{ isSubmitting ? 'Processando...' : submitLabel }}
                </button>
              </footer>
            </form>
          </section>

          <!-- ============================================================
               5. LISTAGEM INFERIOR (MINI-TABELA DE REGISTROS)
               ============================================================ -->
          <section class="table-card">
            <header class="table-card__header">
              <div class="table-card__heading">
                <h2 class="table-card__title">Registros Recentes</h2>
                <p class="table-card__description">Últimos cadastros realizados na plataforma</p>
              </div>
              <span class="table-card__counter"
                >{{ filteredItems.length }} de {{ totalCount }}</span
              >
            </header>

            <div class="table-card__body">
              <div v-if="isLoading" class="table-card__state">
                <span class="table-card__spinner" aria-hidden="true"></span>
                Carregando registros...
              </div>

              <div v-else-if="!hasResults" class="table-card__state">
                Nenhum registro encontrado para os critérios informados.
              </div>

              <table v-else class="table-card__table">
                <thead class="table-card__thead">
                  <tr>
                    <th scope="col" class="table-card__th">Nome</th>
                    <th scope="col" class="table-card__th">E-mail</th>
                    <th scope="col" class="table-card__th">Perfil</th>
                    <th scope="col" class="table-card__th">Status</th>
                    <th scope="col" class="table-card__th table-card__th--actions">Ações</th>
                  </tr>
                </thead>

                <tbody class="table-card__tbody">
                  <tr v-for="item in filteredItems" :key="item.id" class="table-card__row">
                    <td class="table-card__td">
                      <div class="table-card__name-cell">
                        <span class="table-card__avatar" aria-hidden="true">
                          {{ item.name.charAt(0).toUpperCase() }}
                        </span>
                        <div class="table-card__name-block">
                          <strong class="table-card__name">{{ item.name }}</strong>
                          <span class="table-card__meta">
                            {{ item.totalIssuedCertificates }} certificados emitidos
                          </span>
                        </div>
                      </div>
                    </td>

                    <td class="table-card__td">{{ item.email }}</td>

                    <td class="table-card__td">
                      <span :class="roleClass(item.role)">{{ item.role }}</span>
                    </td>

                    <td class="table-card__td">
                      <span :class="statusClass(item.status)">
                        <span class="table-card__status-dot" aria-hidden="true"></span>
                        {{ item.status }}
                      </span>
                    </td>

                    <td class="table-card__td table-card__td--actions">
                      <div class="table-card__actions">
                        <button
                          type="button"
                          class="table-card__action table-card__action--edit"
                          title="Editar registro"
                          aria-label="Editar registro"
                          @click="handleEdit(item)"
                        >
                          ✏️
                        </button>
                        <button
                          type="button"
                          class="table-card__action table-card__action--view"
                          title="Visualizar detalhes"
                          aria-label="Visualizar detalhes"
                          @click="handleOpenModal(item)"
                        >
                          👁️
                        </button>
                        <button
                          type="button"
                          class="table-card__action table-card__action--delete"
                          title="Excluir registro"
                          aria-label="Excluir registro"
                          @click="handleDelete(item.id)"
                        >
                          🗑️
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <footer class="table-card__footer">
              <button type="button" class="table-card__link" @click="handleViewFullList">
                Ver Listagem Completa →
              </button>
            </footer>
          </section>
        </Container>
      </Section>
    </div>

    <!-- ==================================================================
         6. MODAL CENTRALIZADO (GLASSMORPHIC OVERLAY)
         ================================================================== -->
    <div
      v-if="isModalOpen && selectedItem"
      class="modal-backdrop"
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
      @click.self="handleCloseModal"
    >
      <div class="modal-content">
        <header class="modal-content__header">
          <div class="modal-content__heading">
            <span class="modal-content__avatar" aria-hidden="true">
              {{ selectedItem.name.charAt(0).toUpperCase() }}
            </span>
            <div class="modal-content__title-block">
              <h2 id="modal-title" class="modal-content__title">
                {{ selectedItem.name }}
              </h2>
              <span class="modal-content__subtitle">{{ selectedItem.email }}</span>
            </div>
          </div>

          <button
            type="button"
            class="modal-content__close"
            aria-label="Fechar detalhes"
            @click="handleCloseModal"
          >
            ✕
          </button>
        </header>

        <div class="modal-content__body">
          <dl class="modal-content__details">
            <div class="modal-content__detail">
              <dt class="modal-content__label">Identificador</dt>
              <dd class="modal-content__value modal-content__value--mono">
                {{ selectedItem.id }}
              </dd>
            </div>

            <div class="modal-content__detail">
              <dt class="modal-content__label">Perfil de Acesso</dt>
              <dd class="modal-content__value">
                <span :class="roleClass(selectedItem.role)">{{ selectedItem.role }}</span>
              </dd>
            </div>

            <div class="modal-content__detail">
              <dt class="modal-content__label">Status</dt>
              <dd class="modal-content__value">
                <span :class="statusClass(selectedItem.status)">
                  <span class="table-card__status-dot" aria-hidden="true"></span>
                  {{ selectedItem.status }}
                </span>
              </dd>
            </div>

            <div class="modal-content__detail">
              <dt class="modal-content__label">Certificados Emitidos</dt>
              <dd class="modal-content__value">
                {{ selectedItem.totalIssuedCertificates }}
              </dd>
            </div>

            <div class="modal-content__detail">
              <dt class="modal-content__label">Criado em</dt>
              <dd class="modal-content__value">{{ formatDateTime(selectedItem.createdAt) }}</dd>
            </div>

            <div class="modal-content__detail">
              <dt class="modal-content__label">Último Acesso</dt>
              <dd class="modal-content__value">{{ formatDateTime(selectedItem.lastAccess) }}</dd>
            </div>
          </dl>
        </div>

        <footer class="modal-content__footer">
          <button
            type="button"
            class="modal-content__button modal-content__button--ghost"
            @click="handleCloseModal"
          >
            Fechar
          </button>
          <button
            type="button"
            class="modal-content__button modal-content__button--primary"
            @click="handleEdit(selectedItem)"
          >
            ✏️ Editar Registro
          </button>
        </footer>
      </div>
    </div>
  </div>
</template>

<style src="./EntityManagement.css"></style>
