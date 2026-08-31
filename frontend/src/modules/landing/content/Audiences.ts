import companiesImage from '../assets/images/audiences/company-500.jpg'
import intustriesImage from '../assets/images/audiences/industries-500.jpg'
import edtechImage from '../assets/images/audiences/edtech-500.jpg'
import educationsImage from '../assets/images/audiences/education-500.jpg'
import eventsImage from '../assets/images/audiences/event-500.jpg'
import associationImage from '../assets/images/audiences/association-500.jpg'

export interface Audience {
  id: string
  title: string
  description: string
  image: string
}

export const audiences: Audience[] = [
  {
    id: 'companies',
    title: 'Empresas',
    description:
      'Comprove treinamentos, capacitações e qualificações de colaboradores com credenciais digitais verificáveis.',
    image: companiesImage,
  },
  {
    id: 'industries',
    title: 'Indústrias',
    description:
      'Registre e valide treinamentos técnicos e qualificações profissionais de equipes e colaboradores.',
    image: intustriesImage,
  },
  {
    id: 'educational-institutions',
    title: 'Instituições de Ensino',
    description:
      'Emita certificados e diplomas digitais com autenticidade verificável e validação pública.',
    image: educationsImage,
  },
  {
    id: 'events-communities',
    title: 'Eventos',
    description:
      'Emita credenciais de participação e conclusão que podem ser compartilhadas e verificadas a qualquer momento.',
    image: eventsImage,
  },
  {
    id: 'associations',
    title: 'Associações',
    description:
      'Reconheça a participação, capacitação e conquistas dos membros com credenciais digitais verificáveis.',
    image: associationImage,
  },
  {
    id: 'edTech',
    title: 'EdTechs',
    description:
      'Transforme cursos e experiências de aprendizagem digital em credenciais verificáveis, fáceis de compartilhar e validar.',
    image: edtechImage,
  },
]
