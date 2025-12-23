# Start kit arquitetural e técnico

## 1. Arquitetura Geral do SaaS

### Visão macro

* **Frontend**: Vue 3 + Vite (SPA)
* **Backend**: Spring Boot (API REST)
* **Autenticação**: JWT (Access + Refresh Token)
* **Comunicação**: HTTP/HTTPS (Axios)
* **Deploy**: Frontend estático (S3, Vercel, Netlify) + Backend containerizado (Docker)

### Estrutura de URLs

* `meusaas.com/` → Landing Page (pública)
* `meusaas.com/admin` → SPA administrativa (protegida)
* API: `api.meusaas.com/v1/*`

---

## 2. Stack Recomendada – Frontend

### Tecnologias

* **Vue 3** (Composition API)
* **Vite**
* **Vue Router**
* **Pinia** (state management)
* **Axios**
* **Tailwind CSS** (ou Vuetify, se preferir Material)
* **JWT Decode**
* **ESLint + Prettier**

---

## 3. Estrutura de Pastas – Frontend

```bash
frontend/
├── public/
│   └── index.html
├── src/
│   ├── assets/
│   ├── components/
│   │   ├── common/
│   │   └── layout/
│   ├── views/
│   │   ├── landing/
│   │   │   └── LandingPage.vue
│   │   ├── auth/
│   │   │   └── Login.vue
│   │   └── admin/
│   │       ├── Dashboard.vue
│   │       ├── Users.vue
│   │       ├── Certificates.vue
│   │       └── Entities.vue
│   ├── router/
│   │   └── index.js
│   ├── stores/
│   │   └── authStore.js
│   ├── services/
│   │   ├── api.js
│   │   └── authService.js
│   ├── guards/
│   │   └── authGuard.js
│   ├── App.vue
│   └── main.js
└── vite.config.js
```

---

## 4. Configuração Inicial do Projeto Vue

```bash
npm create vite@latest frontend -- --template vue
cd frontend
npm install
npm install vue-router pinia axios jwt-decode
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

---

## 5. Vue Router com Landing + Admin

### `router/index.js`

```js
import { createRouter, createWebHistory } from 'vue-router'
import LandingPage from '@/views/landing/LandingPage.vue'
import Login from '@/views/auth/Login.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import authGuard from '@/guards/authGuard'

const routes = [
  {
    path: '/',
    component: LandingPage
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/admin',
    component: Dashboard,
    beforeEnter: authGuard
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
```

---

## 6. Auth Guard (Controle de Acesso)

### `guards/authGuard.js`

```js
import { useAuthStore } from '@/stores/authStore'

export default (to, from, next) => {
  const auth = useAuthStore()
  if (!auth.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
}
```

---

## 7. Pinia – Store de Autenticação

### `stores/authStore.js`

```js
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: null
  }),
  getters: {
    isAuthenticated: (state) => !!state.token
  },
  actions: {
    login(token, user) {
      this.token = token
      this.user = user
      localStorage.setItem('token', token)
    },
    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
    }
  }
})
```

---

## 8. Axios Centralizado

### `services/api.js`

```js
import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'

const api = axios.create({
  baseURL: 'https://api.meusaas.com/v1'
})

api.interceptors.request.use(config => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

export default api
```

---

## 9. Backend – Spring Boot (Visão Geral)

### Dependências

* Spring Web
* Spring Security
* Spring Data JPA
* JWT
* PostgreSQL ou MySQL
* Flyway
* Lombok

---

## 10. Endpoints Principais (Exemplo)

```http
POST   /auth/login
GET    /users/me
POST   /certificates
GET    /certificates
POST   /entities
GET    /entities
```

---

## 11. Controle de Perfis (RBAC)

No JWT:

```json
{
  "sub": "user@email.com",
  "role": "ADMIN_ENTIDADE"
}
```

No Spring:

```java
@PreAuthorize("hasRole('ADMIN')")
```

No Vue:

```js
v-if="auth.user.role === 'ADMIN'"
```

