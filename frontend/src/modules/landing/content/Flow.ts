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
    description: '...',
    image: '',
    alt: '...',
  },
  {
    id: 'issue',
    number: '02',
    title: 'Emita',
    description: '...',
    image: '',
    alt: '...',
  },
  {
    id: 'share',
    number: '03',
    title: 'Compartilhe',
    description: '...',
    image: '',
    alt: '...',
  },
  {
    id: 'validate',
    number: '04',
    title: 'Valide',
    description: '...',
    image: '',
    alt: '...',
  },
]
