/**
 * Validação de CNPJ (com ou sem máscara)
 */
export function validateCNPJ(value: string): boolean {
  // Remove caracteres não numéricos
  const clean = value.replace(/\D/g, '')
  if (clean.length !== 14) return false

  // Verifica se todos os dígitos são iguais
  if (/^(\d)\1+$/.test(clean)) return false

  // Validação dos dígitos verificadores
  const calc = (digits: string, multiplier: number): number => {
    let sum = 0
    for (let i = 0; i < digits.length; i++) {
      sum += Number(digits[i]) * (multiplier - i)
    }
    const remainder = sum % 11
    return remainder < 2 ? 0 : 11 - remainder
  }

  const first = clean.slice(0, 12)
  const firstCheck = calc(first, 5)
  const secondCheck = calc(first + firstCheck, 6)

  return Number(clean[12]) === firstCheck && Number(clean[13]) === secondCheck
}

/**
 * Validação de e-mail simples
 */
export function validateEmail(value: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
}
