<script setup lang="ts">
const currentYear = new Date().getFullYear();
const version = 'v0.1.0';
</script>

<template>
  <footer class="app-footer">
    <div class="container">
      <div class="footer-content">
        <p>&copy; {{ currentYear }} Validex ProofChain. Todos os direitos reservados.</p>
        <div class="version-info">
          <span>Versão: {{ version }}</span>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  background-color: var(--gray-light);
  border-top: 1px solid var(--gray-medium);
  padding: 20px 0;
  margin-top: auto; /* Pushes footer to bottom if flex container is used */
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  color: var(--gray-dark);
  font-size: 0.9rem;
}

.version-info {
  font-weight: 600;
  font-size: 0.8rem;
  background-color: var(--green-light);
  color: var(--green);
  padding: 2px 8px;
  border-radius: 4px;
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>
