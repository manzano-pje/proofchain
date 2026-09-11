/* ============================================================
   VALIDADORES
   ============================================================ */

/**
 * Valida um CNPJ brasileiro.
 *
 * Aceita tanto:
 * 00.000.000/0000-00
 *
 * quanto:
 * 00000000000000
 */
export function validateCNPJ(value: string): boolean {
  const clean = value.replace(/\D/g, '')

  /* CNPJ deve possuir exatamente 14 dígitos */
  if (clean.length !== 14) {
    return false
  }

  /* Rejeita sequências como:
     00000000000000
     11111111111111
     22222222222222
     etc.
  */
  if (/^(\d)\1+$/.test(clean)) {
    return false
  }

  /* ----------------------------------------------------------
     Calcula um dígito verificador
     ---------------------------------------------------------- */

  const calculateDigit = (digits: string, weights: number[]): number => {
    let sum = 0

    for (let index = 0; index < digits.length; index++) {
      sum += Number(digits[index]) * weights[index]
    }

    const remainder = sum % 11

    return remainder < 2 ? 0 : 11 - remainder
  }

  /* ----------------------------------------------------------
     Primeiro dígito verificador
     ---------------------------------------------------------- */

  const firstBase = clean.slice(0, 12)

  const firstDigit = calculateDigit(firstBase, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2])

  /* ----------------------------------------------------------
     Segundo dígito verificador
     ---------------------------------------------------------- */

  const secondBase = firstBase + firstDigit

  const secondDigit = calculateDigit(secondBase, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2])
  /* ----------------------------------------------------------
     Comparação
     ---------------------------------------------------------- */

  return Number(clean[12]) === firstDigit && Number(clean[13]) === secondDigit
}

/**
 * Valida um endereço de e-mail.
 */
export function validateEmail(value: string): boolean {
  const email = value.trim()

  if (!email) {
    return false
  }

  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}
