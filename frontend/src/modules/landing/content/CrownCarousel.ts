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
      'Fortaleça a credibilidade com certificados que transmitem confiança e reconhecimento.',
  },
  {
    id: 'security',
    image: securityImage,
    title: 'Segurança',
    description:
      'Proteja seus certificados contra falsificações, mantendo a integridade dos registros.',
  },
  {
    id: 'agility',
    image: agilityImage,
    title: 'Agilidade',
    description:
      'Valide certificados em segundos por QR Code, sem processos manuais ou burocracia.',
  },
  {
    id: 'utility',
    image: utilityImage,
    title: 'Utilidade',
    description:
      'Mantenha o certificado útil, permitindo que seja compartilhado e verificado sempre.',
  },
  {
    id: 'leadership',
    image: leadershipImage,
    title: 'Liderança',
    description: 'Destaque sua instituição com uma experiência moderna de certificação digital.',
  },
  {
    id: 'simplicity',
    image: simplicityImage,
    title: 'Simplicidade',
    description:
      'Simplifique emissão, gestão e validação, reduzindo tarefas manuais e tornando tudo mais eficiente.',
  },
]
