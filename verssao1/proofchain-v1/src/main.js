import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

import '@./assets/style.css'

// Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css'

// Bootstrap JS (opcional – dropdown, modal, etc.)
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

createApp(App)
  .use(createPinia())
  .use(router)
  .mount('#app')
