// =========================================================
// Project.......: ProofChain
// Component.....: CrownCarousel
// File..........: content/CrownCarousel.ts
// Version.......: 1.0.0
//
// Description...:
// Mock content for the CrownCarousel in the landing page
// "Por que escolher" section.
// =========================================================

import type { CarouselItemData } from '../CrownCarousel/CrownCarousel.ts'

import companiesImage from '@/modules/landing/assets/images/audiences/company.jpg'
import industriesImage from '@/modules/landing/assets/images/audiences/industries.jpg'
import educationImage from '@/modules/landing/assets/images/audiences/education.jpg'
import eventImage from '@/modules/landing/assets/images/audiences/event.jpg'
import associationImage from '@/modules/landing/assets/images/audiences/association.jpg'
import edtechImage from '@/modules/landing/assets/images/audiences/edtech.jpg'

export type { CarouselItemData }

export const crownCarouselItems: CarouselItemData[] = [
  {
    id: 'security',
    image: companiesImage,
    title: 'Segurança',
    description:
      'Uma camada adicional de segurança para preservar a integridade e a confiabilidade dos certificados emitidos.',
  },
  {
    id: 'authenticity',
    image: eventImage,
    title: 'Autenticidade',
    description:
      'Cada certificado possui uma identidade verificável que permite confirmar sua autenticidade de forma simples.',
  },
  {
    id: 'validation',
    image: associationImage,
    title: 'Validação',
    description:
      'A autenticidade de um certificado pode ser conferida rapidamente, sem depender de processos manuais.',
  },
  {
    id: 'blockchain',
    image: industriesImage,
    title: 'Blockchain',
    description:
      'Uma prova digital registrada em blockchain reforça a rastreabilidade e a confiança na emissão.',
  },
  {
    id: 'traceability',
    image: edtechImage,
    title: 'Rastreabilidade',
    description:
      'Informações verificáveis permitem acompanhar a origem e a validade dos certificados com mais transparência.',
  },
  {
    id: 'management',
    image: educationImage,
    title: 'Gestão',
    description:
      'Uma experiência centralizada para organizar emissões, participantes e certificados em um único ambiente.',
  },
]
