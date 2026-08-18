import companiesImage from '../assets/images/audiences/company.png'
import intustriesImage from '../assets/images/audiences/industries.jpg'
import coursesImage from '../assets/images/audiences/courses.png'
import edtechImage from '../assets/images/audiences/edtech.png'
import educationsImage from '../assets/images/audiences/education.png'
import eventsImage from '../assets/images/audiences/events.jpg'
import teste1Image from '../assets/images/audiences/teste1.jpg'
import teste2Image from '../assets/images/audiences/teste2.jpg'

export interface Audience {
  id: string
  title: string
  description: string
  image: string
}

export const audiences: Audience[] = [
  {
    id: 'companies',
    title: 'Empresas e corporações',
    description:
      'Comprove treinamentos, capacitações e qualificações de colaboradores com credenciais digitais verificáveis.',
    image: companiesImage,
  },
  {
    id: 'industries',
    title: 'Indústrias',
    description:
      'Registre e valide treinamentos técnicos e qualificações profissionais de equipes e operações industriais.',
    image: intustriesImage,
  },
  {
    id: 'educational-institutions',
    title: 'Escolas e instituições de ensino',
    description:
      'Emita certificados e diplomas digitais com autenticidade verificável e validação pública.',
    image: educationsImage,
  },
  {
    id: 'course-platforms-edtechs',
    title: 'Plataformas de cursos e EdTechs',
    description:
      'Transforme experiências de aprendizagem e cursos concluídos em credenciais digitais verificáveis.',
    image: coursesImage,
  },
  {
    id: 'events-communities',
    title: 'Eventos e comunidades',
    description:
      'Emita credenciais de participação e conclusão que podem ser compartilhadas e verificadas a qualquer momento.',
    image: eventsImage,
  },
  {
    id: 'associations',
    title: 'Associações',
    description:
      'Reconheça capacitações, participações e conquistas dos membros com credenciais digitais confiáveis.',
    image: teste1Image,
  },
  {
    id: 'professional-organizations',
    title: 'Instituições de classe',
    description:
      'Registre qualificações e certificações profissionais com uma forma simples e confiável de validação.',
    image: edtechImage,
  },
  {
    id: 'professionals-creators',
    title: 'Profissionais e criadores',
    description:
      'Valorize cursos, experiências e conquistas com credenciais digitais fáceis de compartilhar e validar.',
    image: teste2Image,
  },
]
