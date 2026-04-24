<template>
  <div class="dashboard">
    <div class="container">
      <div class="dashboard-header">
        <h1>Dashboard</h1>
        <p>Bem-vindo ao Painel de Controle</p>
      </div>

      <div class="grid">
        <div class="card">
          <i class="fas fa-certificate"></i>
          <h3>Meus Certificados</h3>
          <p>Gerencie seus certificados emitidos e recebidos.</p>
        </div>
        
        <div class="card">
          <i class="fas fa-check-circle"></i>
          <h3>Validações</h3>
          <p>Histórico de validações realizadas.</p>
        </div>

        <div class="card">
          <i class="fas fa-user-cog"></i>
          <h3>Perfil</h3>
          <p>Atualize suas informações pessoais.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Lógica do dashboard aqui
</script>

<style scoped>
.dashboard {
  padding: 40px 0;
}

.dashboard-header {
  margin-bottom: 40px;
  text-align: center;
}

.dashboard-header h1 {
  color: var(--green);
  margin-bottom: 10px;
}

.dashboard-header p {
  color: var(--gray);
  font-size: 1.2rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 32px;
}

.card {
  background: var(--white);
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  text-align: center;
  border: 1px solid var(--gray-medium);
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

.card i {
  font-size: 3rem;
  color: var(--green);
  margin-bottom: 20px;
}

.card h3 {
  margin-bottom: 12px;
  color: var(--black);
}

.card p {
  color: var(--gray-dark);
}
</style>
