<script setup lang="ts">
/**
 * Formulário administrativo de edição da Instituição.
 *
 * É montado pela página InstitutionPage dentro do AdminLayout. Não faz
 * chamadas HTTP: recebe os dados e emite `submit` para o componente pai.
 * O backend ainda não oferece persistência de logo; esse controle permanece
 * desativado até existir um contrato de upload.
 */
import { computed, reactive, ref, watch } from 'vue'

import InstitutionLogoUploader from './InstitutionLogoUploader.vue'
import type {
  InstitutionEditableFormModel,
  InstitutionEditableInitialData,
  InstitutionFieldErrors,
  InstitutionReadonlyData,
  InstitutionUpdatePayload,
} from './Types'

interface Props {
  /** Dados imutáveis exibidos no primeiro card. */
  readonlyData: InstitutionReadonlyData
  /** Valores atuais dos campos editáveis (endereço/contato). */
  initialData?: InstitutionEditableInitialData
  /** Controla o estado de carregamento do botão de submit. */
  submitting?: boolean
  /** O backend ainda não persiste logos de instituição. */
  logoSupported?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  initialData: () => ({}),
  submitting: false,
  logoSupported: false,
})

const emit = defineEmits<{
  submit: [payload: InstitutionUpdatePayload]
}>()

const form = reactive<InstitutionEditableFormModel>({
  postalCode: '',
  phone: '',
  address: '',
  number: '',
  complement: '',
  neighborhood: '',
  city: '',
  state: '',
})

const errors = reactive<InstitutionFieldErrors>({})

const logoFile = ref<File | null>(null)
const logoRemoved = ref(false)

const isBusy = computed(() => props.submitting)

/* ------------------------------------------------------------------ */
/* Aplicação dos dados iniciais                                        */
/* ------------------------------------------------------------------ */

function applyInitialData(data: InstitutionEditableInitialData): void {
  form.postalCode = data.postalCode ?? ''
  form.phone = data.phone ?? ''
  form.address = data.address ?? ''
  form.number = data.number != null ? String(data.number) : ''
  form.complement = data.complement ?? ''
  form.neighborhood = data.neighborhood ?? ''
  form.city = data.city ?? ''
  form.state = (data.state ?? '').toUpperCase()
}

// `initialData` deve ser passado como referência estável pelo pai.
// O watch dispara quando o objeto muda de referência (ex.: após fetch).
watch(() => props.initialData, applyInitialData, { immediate: true })

/* ------------------------------------------------------------------ */
/* Máscaras                                                            */
/* ------------------------------------------------------------------ */

function maskPostalCode(value: string): string {
  const digits = value.replace(/\D/g, '').slice(0, 8)
  return digits.length > 5 ? `${digits.slice(0, 5)}-${digits.slice(5)}` : digits
}

function maskPhone(value: string): string {
  const digits = value.replace(/\D/g, '').slice(0, 11)
  if (digits.length === 0) return ''
  if (digits.length <= 2) return `(${digits}`
  if (digits.length <= 6) return `(${digits.slice(0, 2)}) ${digits.slice(2)}`
  if (digits.length <= 10) return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`
  return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`
}

function maskNumber(value: string): string {
  return value.replace(/\D/g, '').slice(0, 6)
}

function maskState(value: string): string {
  return value.replace(/[^A-Za-z]/g, '').toUpperCase().slice(0, 2)
}

function clearError(key: keyof InstitutionFieldErrors): void {
  if (errors[key]) delete errors[key]
}

/* ------------------------------------------------------------------ */
/* Handlers                                                            */
/* ------------------------------------------------------------------ */

function onPostalCodeInput(event: Event): void {
  form.postalCode = maskPostalCode((event.target as HTMLInputElement).value)
  clearError('postalCode')
}

function onPhoneInput(event: Event): void {
  form.phone = maskPhone((event.target as HTMLInputElement).value)
  clearError('phone')
}

function onNumberInput(event: Event): void {
  form.number = maskNumber((event.target as HTMLInputElement).value)
  clearError('number')
}

function onStateInput(event: Event): void {
  form.state = maskState((event.target as HTMLInputElement).value)
  clearError('state')
}

function onTextInput(key: keyof InstitutionEditableFormModel, event: Event): void {
  form[key] = (event.target as HTMLInputElement).value
  clearError(key)
}

/* ------------------------------------------------------------------ */
/* Validação                                                           */
/* ------------------------------------------------------------------ */

const POSTAL_CODE_REGEX = /^\d{5}-?\d{3}$/
const PHONE_REGEX = /^\(?\d{2}\)?\s?\d{4,5}-?\d{4}$/
const UF_REGEX = /^[A-Z]{2}$/

function validate(): boolean {
  const next: InstitutionFieldErrors = {}

  if (!form.postalCode.trim()) next.postalCode = 'Informe o CEP.'
  else if (!POSTAL_CODE_REGEX.test(form.postalCode)) next.postalCode = 'CEP inválido.'

  if (!form.phone.trim()) next.phone = 'Informe o telefone.'
  else if (!PHONE_REGEX.test(form.phone)) next.phone = 'Telefone inválido.'

  if (!form.address.trim()) next.address = 'Informe o endereço.'

  if (!form.number.trim()) next.number = 'Informe o número.'
  else if (!/^\d+$/.test(form.number)) next.number = 'Use apenas dígitos.'

  if (!form.neighborhood.trim()) next.neighborhood = 'Informe o bairro.'
  if (!form.city.trim()) next.city = 'Informe a cidade.'

  if (!form.state.trim()) next.state = 'Informe o estado (UF).'
  else if (!UF_REGEX.test(form.state)) next.state = 'Use a sigla com 2 letras (ex.: SP).'

  ;(Object.keys(errors) as Array<keyof InstitutionFieldErrors>).forEach((key) => {
    delete errors[key]
  })
  Object.assign(errors, next)

  return Object.keys(next).length === 0
}

/* ------------------------------------------------------------------ */
/* Logo                                                                */
/* ------------------------------------------------------------------ */

function onLogoRemove(): void {
  logoRemoved.value = true
}

watch(logoFile, (next) => {
  if (next) logoRemoved.value = false
})

/* ------------------------------------------------------------------ */
/* Submit                                                              */
/* ------------------------------------------------------------------ */

function onSubmit(): void {
  if (!validate()) return

  const payload: InstitutionUpdatePayload = {
    postalCode: form.postalCode,
    phone: form.phone,
    address: form.address.trim(),
    number: Number(form.number),
    complement: form.complement.trim(),
    neighborhood: form.neighborhood.trim(),
    city: form.city.trim(),
    state: form.state.toUpperCase(),
    logo: logoFile.value,
    removeLogo: logoRemoved.value && logoFile.value === null,
  }

  emit('submit', payload)
}
</script>

<template>
  <form class="institution-form" novalidate @submit.prevent="onSubmit">
    <!-- Card 1 — dados existentes (somente leitura) -->
    <section class="institution-form__card" aria-label="Dados cadastrais">
      <div class="institution-form__grid">
        <div class="institution-form__field institution-form__field--full">
          <label for="institution-name" class="institution-form__label">Nome</label>
          <input
            id="institution-name"
            class="institution-form__input institution-form__input--readonly"
            type="text"
            :value="props.readonlyData.name"
            readonly
            aria-readonly="true"
          />
        </div>

        <div class="institution-form__field">
          <label for="institution-cnpj" class="institution-form__label">CNPJ</label>
          <input
            id="institution-cnpj"
            class="institution-form__input institution-form__input--readonly"
            type="text"
            :value="props.readonlyData.cnpj"
            readonly
            aria-readonly="true"
          />
        </div>

        <div class="institution-form__field">
          <label for="institution-email" class="institution-form__label">E-mail</label>
          <input
            id="institution-email"
            class="institution-form__input institution-form__input--readonly"
            type="email"
            :value="props.readonlyData.email"
            readonly
            aria-readonly="true"
          />
        </div>
      </div>
    </section>

    <!-- Card 2 — dados editáveis -->
    <section class="institution-form__card" aria-label="Endereço e contato">
      <div class="institution-form__grid">
        <div class="institution-form__field">
          <label for="institution-postal-code" class="institution-form__label">CEP</label>
          <input
            id="institution-postal-code"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.postalCode }"
            type="text"
            inputmode="numeric"
            autocomplete="postal-code"
            placeholder="00000-000"
            :value="form.postalCode"
            :aria-invalid="Boolean(errors.postalCode)"
            :aria-describedby="errors.postalCode ? 'institution-postal-code-error' : undefined"
            @input="onPostalCodeInput"
          />
          <p
            v-if="errors.postalCode"
            id="institution-postal-code-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.postalCode }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-phone" class="institution-form__label">Telefone</label>
          <input
            id="institution-phone"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.phone }"
            type="tel"
            inputmode="tel"
            autocomplete="tel"
            placeholder="(00) 00000-0000"
            :value="form.phone"
            :aria-invalid="Boolean(errors.phone)"
            :aria-describedby="errors.phone ? 'institution-phone-error' : undefined"
            @input="onPhoneInput"
          />
          <p
            v-if="errors.phone"
            id="institution-phone-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.phone }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-address" class="institution-form__label">Endereço</label>
          <input
            id="institution-address"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.address }"
            type="text"
            autocomplete="street-address"
            placeholder="Rua, Avenida, etc."
            :value="form.address"
            :aria-invalid="Boolean(errors.address)"
            :aria-describedby="errors.address ? 'institution-address-error' : undefined"
            @input="onTextInput('address', $event)"
          />
          <p
            v-if="errors.address"
            id="institution-address-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.address }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-number" class="institution-form__label">Número</label>
          <input
            id="institution-number"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.number }"
            type="text"
            inputmode="numeric"
            placeholder="123"
            :value="form.number"
            :aria-invalid="Boolean(errors.number)"
            :aria-describedby="errors.number ? 'institution-number-error' : undefined"
            @input="onNumberInput"
          />
          <p
            v-if="errors.number"
            id="institution-number-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.number }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-complement" class="institution-form__label">Complemento</label>
          <input
            id="institution-complement"
            class="institution-form__input"
            type="text"
            autocomplete="address-line2"
            placeholder="Sala, bloco, andar..."
            :value="form.complement"
            @input="onTextInput('complement', $event)"
          />
        </div>

        <div class="institution-form__field">
          <label for="institution-neighborhood" class="institution-form__label">Bairro</label>
          <input
            id="institution-neighborhood"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.neighborhood }"
            type="text"
            autocomplete="address-line3"
            placeholder="Nome do bairro"
            :value="form.neighborhood"
            :aria-invalid="Boolean(errors.neighborhood)"
            :aria-describedby="errors.neighborhood ? 'institution-neighborhood-error' : undefined"
            @input="onTextInput('neighborhood', $event)"
          />
          <p
            v-if="errors.neighborhood"
            id="institution-neighborhood-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.neighborhood }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-city" class="institution-form__label">Cidade</label>
          <input
            id="institution-city"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.city }"
            type="text"
            autocomplete="address-level2"
            placeholder="Nome da cidade"
            :value="form.city"
            :aria-invalid="Boolean(errors.city)"
            :aria-describedby="errors.city ? 'institution-city-error' : undefined"
            @input="onTextInput('city', $event)"
          />
          <p
            v-if="errors.city"
            id="institution-city-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.city }}
          </p>
        </div>

        <div class="institution-form__field">
          <label for="institution-state" class="institution-form__label">Estado</label>
          <input
            id="institution-state"
            class="institution-form__input"
            :class="{ 'institution-form__input--error': errors.state }"
            type="text"
            autocomplete="address-level1"
            placeholder="SP"
            :value="form.state"
            :aria-invalid="Boolean(errors.state)"
            :aria-describedby="errors.state ? 'institution-state-error' : undefined"
            @input="onStateInput"
          />
          <p
            v-if="errors.state"
            id="institution-state-error"
            class="institution-form__error"
            role="alert"
          >
            {{ errors.state }}
          </p>
        </div>
      </div>
    </section>

    <!-- Card 3 — logo -->
    <section v-if="props.logoSupported" class="institution-form__card" aria-label="Logo da instituição">
      <InstitutionLogoUploader v-model="logoFile" @remove="onLogoRemove" />
    </section>

    <div class="institution-form__actions">
      <button
        type="submit"
        class="institution-form__button"
        :disabled="isBusy"
        :aria-busy="isBusy"
      >
        {{ isBusy ? 'Salvando…' : 'Salvar alterações' }}
      </button>
    </div>
  </form>
</template>

<style scoped src="./InstitutionForm.css"></style>
