// =========================================================
// Project.......: ProofChain
// Component.....: CrownCarousel
// File..........: content/CrownCarousel.ts
// Version.......: 1.0.0
//
// Description...:
// Conteúdo da seção "Por que escolher" da landing page.
// =========================================================

import type { CarouselItemData } from '../components/CrownCarousel/CrownCarousel.ts'

import reputationImage from '@/modules/landing/content/images/reputation.jpg'
import securityImage from '@/modules/landing/content/images/security.jpg'
import agilityImage from '@/modules/landing/content/images/agility.jpg'
import utilityImage from '@/modules/landing/content/images/utility.jpg'
import leadershipImage from '@/modules/landing/content/images/leadership.jpg'
import simplicityImage from '@/modules/landing/content/images/simplcity.jpg'

export type { CarouselItemData }

export const crownCarouselItems: CarouselItemData[] = [
  {
    id: 'reputatoin',
    image: reputationImage,
    title: 'Reputação',
    description:
      'Fortaleça a credibilidade da sua instituição com certificados que transmitem confiança e tornam cada conquista mais reconhecida.',
  },
  {
    id: 'security',
    image: securityImage,
    title: 'Segurança',
    description:
      'Proteja seus certificados contra falsificações e alterações, preservando a integridade das informações e a confiança em cada registro.',
  },
  {
    id: 'agility',
    image: agilityImage,
    title: 'Agilidade',
    description:
      'Valide certificados em poucos segundos por meio do QR Code, sem processos manuais ou burocracia.',
  },
  {
    id: 'utility',
    image: utilityImage,
    title: 'Utilidade',
    description:
      'Mantenha o certificado útil após a emissão, permitindo que seja compartilhado e verificado sempre que necessário.',
  },
  {
    id: 'leadership',
    image: leadershipImage,
    title: 'Liderança',
    description:
      'Destaque sua instituição com uma experiência moderna de certificação, alinhada às novas necessidades do mercado.',
  },
  {
    id: 'simplicity',
    image: simplicityImage,
    title: 'Simplicidade',
    description:
      'Simplifique a emissão, o gerenciamento e a validação, reduzindo tarefas manuais e tornando o processo mais eficiente.',
  },
]
