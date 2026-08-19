// =========================================================
// Project.......: ProofChain
// Component.....: Flow
// File..........: Flow.ts
// Version.......: 1.0.0
//
// Description...:
// Content data for the landing page flow section.
//
// Responsibility:
// Define the steps presented in the ProofChain flow.
// =========================================================

import registerImage from '../assets/images/flow/register.jpg'
import issueImage from '../assets/images/flow/issue.jpg'
import shareImage from '../assets/images/flow/share.jpg'
import validateImage from '../assets/images/flow/validate.jpg'

export interface FlowStep {
  id: string
  number: string
  title: string
  description: string
  image: string
  alt: string
}

export const flowSteps: FlowStep[] = [
  {
    id: 'register',
    number: '01',
    title: 'Cadastre',
    description:
      'Cadastre instituições, cursos, eventos e participantes para organizar as informações que farão parte da credencial.',
    image: registerImage,
    alt: 'Cadastro de participantes e informações para emissão de credenciais',
  },
  {
    id: 'issue',
    number: '02',
    title: 'Emita',
    description:
      'Emita credenciais digitais com informações verificáveis e vinculadas à instituição responsável.',
    image: issueImage,
    alt: 'Emissão de uma credencial digital',
  },
  {
    id: 'share',
    number: '03',
    title: 'Compartilhe',
    description:
      'Compartilhe a credencial digital de forma simples e permita que o participante apresente sua conquista.',
    image: shareImage,
    alt: 'Compartilhamento de uma credencial digital',
  },
  {
    id: 'validate',
    number: '04',
    title: 'Valide',
    description:
      'Verifique a autenticidade da credencial de forma rápida, segura e acessível a qualquer pessoa.',
    image: validateImage,
    alt: 'Validação da autenticidade de uma credencial digital',
  },
]
