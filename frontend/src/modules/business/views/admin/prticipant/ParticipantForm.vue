<!--
  ============================================================
  FORM PARTICIPANT
  ============================================================
  Formulário reutilizável para cadastro e visualização de participantes.

  Edição permanece visualmente preparada e depende da futura integração PATCH.

  Responsabilidade: coletar, validar e emitir payload.
  NÃO realiza chamadas HTTP.
  ============================================================
-->

<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue';
import type { ParticipantRequest } from '../../../types/Participant.types';
import { emptyParticipant } from '../../../types/Participant.types';

// ============================================================
// PROPS
// ============================================================
const props = withDefaults(
  defineProps<{
    initialData?: ParticipantRequest | null;
    mode?: 'create' | 'edit' | 'view';
    submitting?: boolean;
  }>(),
  {
    initialData: null,
    mode: 'create',
    submitting: false,
  }
);

// ============================================================
// EMITS
// ============================================================
const emit = defineEmits<{
  (e: 'submit', payload: ParticipantRequest): void;
  (e: 'cancel'): void;
}>();

// ============================================================
// ESTADO LOCAL
// ============================================================
const form = reactive<ParticipantRequest>(
  props.initialData ? { ...props.initialData } : emptyParticipant()
);

const errors = reactive<Record<string, string>>({});

watch(
  () => props.initialData,
  (data) => {
    if (data) {
      Object.assign(form, data);
    } else {
      Object.assign(form, emptyParticipant());
    }
  },
  { deep: true }
);

// ============================================================
// VALIDAÇÃO
// ============================================================
const validate = (): boolean => {
  Object.keys(errors).forEach((k) => delete errors[k]);

  if (!form.name.trim()) errors.name = 'Nome é obrigatório.';
  if (!form.email.trim()) errors.email = 'E-mail é obrigatório.';
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
    errors.email = 'E-mail inválido.';
  if (!form.cpf.trim()) errors.cpf = 'CPF é obrigatório.';
  if (!form.address.trim()) errors.address = 'Endereço é obrigatório.';
  if (!form.neighborhood.trim()) errors.neighborhood = 'Bairro é obrigatório.';
  if (!form.city.trim()) errors.city = 'Cidade é obrigatória.';
  if (!form.state.trim() || form.state.length !== 2)
    errors.state = 'Estado deve conter 2 caracteres (UF).';
  if (!form.postalCode.trim())
    errors.postalCode = 'CEP é obrigatório.';
  else if (!/^\d{5}-\d{3}$/.test(form.postalCode))
    errors.postalCode = 'CEP deve estar no formato XXXXX-XXX.';

  return Object.keys(errors).length === 0;
};

// ============================================================
// SUBMIT
// ============================================================
const handleSubmit = () => {
  if (!validate()) return;
  const numberValue = Number(form.number);
  emit('submit', {
    ...form,
    number: Number.isFinite(numberValue) && numberValue > 0 ? numberValue : null,
  });
};

// ============================================================
// HELPERS
// ============================================================
const isReadonly = () => props.mode === 'view';
const title = computed(() => {
  if (props.mode === 'view') return 'Visualizar Participante';
  if (props.mode === 'edit') return 'Editar Participante';
  return 'Novo Participante';
});
</script>

<template>
  <form class="form-participant" @submit.prevent="handleSubmit">
    <h2 id="participant-modal-title" class="form-participant__title">{{ title }}</h2>
    <div class="form-participant__body">

      <div class="form-participant__field">
        <label for="participant-name" class="form-participant__label">Nome</label>
        <input
          id="participant-name"
          v-model="form.name"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.name"
          aria-describedby="participant-name-error"
        />
        <span v-if="errors.name" id="participant-name-error" class="form-participant__error">
          {{ errors.name }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-cpf" class="form-participant__label">CPF</label>
        <input
          id="participant-cpf"
          v-model="form.cpf"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.cpf"
          aria-describedby="participant-cpf-error"
        />
        <span v-if="errors.cpf" id="participant-cpf-error" class="form-participant__error">
          {{ errors.cpf }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-email" class="form-participant__label">E-mail</label>
        <input
          id="participant-email"
          v-model="form.email"
          type="email"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.email"
          aria-describedby="participant-email-error"
        />
        <span v-if="errors.email" id="participant-email-error" class="form-participant__error">
          {{ errors.email }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-phone" class="form-participant__label">Telefone</label>
        <input
          id="participant-phone"
          v-model="form.phone"
          type="tel"
          class="form-participant__input"
          :readonly="isReadonly()"
        />
      </div>
      <div class="form-participant__field">
        <label for="participant-postal-code" class="form-participant__label">CEP</label>
        <input
          id="participant-postal-code"
          v-model="form.postalCode"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.postalCode"
          aria-describedby="participant-postal-code-error"
        />
        <span v-if="errors.postalCode" id="participant-postal-code-error" class="form-participant__error">
          {{ errors.postalCode }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-address" class="form-participant__label">Endereço</label>
        <input
          id="participant-address"
          v-model="form.address"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.address"
          aria-describedby="participant-address-error"
        />
        <span v-if="errors.address" id="participant-address-error" class="form-participant__error">
          {{ errors.address }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-number" class="form-participant__label">Número</label>
        <input
          id="participant-number"
          v-model.number="form.number"
          type="number"
          class="form-participant__input"
          :readonly="isReadonly()"
        />
      </div>

      <div class="form-participant__field">
        <label for="participant-complement" class="form-participant__label">Complemento</label>
        <input
          id="participant-complement"
          v-model="form.complement"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
        />
      </div>

      <div class="form-participant__field">
        <label for="participant-neighborhood" class="form-participant__label">Bairro</label>
        <input
          id="participant-neighborhood"
          v-model="form.neighborhood"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.neighborhood"
          aria-describedby="participant-neighborhood-error"
        />
        <span v-if="errors.neighborhood" id="participant-neighborhood-error" class="form-participant__error">
          {{ errors.neighborhood }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-city" class="form-participant__label">Cidade</label>
        <input
          id="participant-city"
          v-model="form.city"
          type="text"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.city"
          aria-describedby="participant-city-error"
        />
        <span v-if="errors.city" id="participant-city-error" class="form-participant__error">
          {{ errors.city }}
        </span>
      </div>

      <div class="form-participant__field">
        <label for="participant-state" class="form-participant__label">Estado (UF)</label>
        <input
          id="participant-state"
          v-model="form.state"
          type="text"
          maxlength="2"
          class="form-participant__input"
          :readonly="isReadonly()"
          :aria-invalid="!!errors.state"
          aria-describedby="participant-state-error"
        />
        <span v-if="errors.state" id="participant-state-error" class="form-participant__error">
          {{ errors.state }}
        </span>
      </div>
      <div class="form-participant__field form-participant__field--checkbox">
        <label class="form-participant__checkbox-label">
          <input
            v-model="form.isActive"
            type="checkbox"
            class="form-participant__checkbox"
            :disabled="isReadonly()"
          />
          <span>Ativo</span>
        </label>
      </div>
    </div>

    <!-- ========================================================
         AÇÕES
         ======================================================== -->
    <div class="form-participant__actions">
      <BaseButton
        type="button"
        variant="secondary"
        @click="emit('cancel')"
      >
        {{ mode === 'view' ? 'Fechar' : 'Cancelar' }}
      </BaseButton>
      <BaseButton
        v-if="mode !== 'view'"
        type="submit"
        variant="primary"
        :loading="submitting"
        :disabled="submitting"
      >
        {{ submitting ? 'Salvando...' : mode === 'edit' ? 'Salvar alterações' : 'Salvar' }}
      </BaseButton>
    </div>
  </form>
</template>
<style scoped>
@import './ParticipantForm.css';
</style>
