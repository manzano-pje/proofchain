<template>
  <section id="planos" class="planos">
    <div class="container">
      <div class="section-title">
        <h2>Planos simples e transparentes</h2>
        <p>Escolha o modelo ideal para sua instituição.</p>
      </div>

      <div class="grid">
        <div class="plano">
          <h3>Plano Gratuito</h3>
          <p>Teste gratuito por 7 dias</p>
          <div class="valor">Free</div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Cadastro da instituição</li>
            <li><i class="fas fa-check"></i> Cadastro de 1 curso</li>
            <li><i class="fas fa-check"></i> Cadastro de 1 professor</li>
            <li><i class="fas fa-check"></i> Emissão de até 5 certificados</li>
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> Válido por 7 dias</li>
          </ul>
          <button
            type="button"
            class="btn btn-primary btn-outline"
            @click="$emit('select-plan', 1)"
            data-bs-toggle="modal"
            data-bs-target="#acquisitionModal"
            data-auth="login"
          >
            Começar teste gratuito
          </button>
        </div>

        <div class="plano">
          <h3>Pacote Inicial</h3>
          <p>10 Certificados</p>
          <div class="valor">R$ 10,00 <span>/único</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Cadastro da instituição</li>
            <li><i class="fas fa-check"></i> Cadastro de até 2 cursos</li>
            <li><i class="fas fa-check"></i> Cadastro de até 2 professores</li>
            <li><i class="fas fa-check"></i> Emissão de até 10 certificados</li>
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> Relatórios básicos</li>
            <li><i class="fas fa-check"></i> Válido por 15 dias</li>
          </ul>
          <button
            type="button"
            class="btn btn-primary btn-outline"
            @click="$emit('select-plan', 2)"
            data-bs-toggle="modal"
            data-bs-target="#acquisitionModal"
            data-auth="register"
          >
            Comprar
          </button>
        </div>

        <div class="plano popular">
          <div class="popular-badge">Mais Popular</div>
          <h3>Assinatura Básica</h3>
          <p>50 Certificados</p>
          <div class="valor">R$ 49,90 <span>/mês</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Cadastro da instituição</li>
            <li><i class="fas fa-check"></i> Cadastro de até 10 cursos</li>
            <li><i class="fas fa-check"></i> Cadastro de até 10 professores</li>
            <li><i class="fas fa-check"></i> Emissão de até 10 certificados</li>
            <li><i class="fas fa-check"></i> Modelos padrão</li>
            <li><i class="fas fa-check"></i> Relatórios avançados</li>
            <li><i class="fas fa-check"></i> Dashboard completo</li>
            <li><i class="fas fa-check"></i> Suporte técnico</li>
          </ul>
          <button
            type="button"
            class="btn btn-primary btn-outline"
            @click="$emit('select-plan', 3)"
            data-bs-toggle="modal"
            data-bs-target="#acquisitionModal"
            data-auth="register"
          >
            Comprar
          </button>
        </div>

        <div class="plano">
          <h3>Assinatura Premium</h3>
          <p>Certificados ilimitados</p>
          <div class="valor">R$ 99,90 <span>/mês</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Cadastro da instituição</li>
            <li><i class="fas fa-check"></i> Cursos ilimitados</li>
            <li><i class="fas fa-check"></i> Professores ilimitados</li>
            <li><i class="fas fa-check"></i> Certificados ilimitados</li>
            <li><i class="fas fa-check"></i> Modelos personalizados</li>
            <li><i class="fas fa-check"></i> Relatórios avançados</li>
            <li><i class="fas fa-check"></i> Dashboard avançado</li>
            <li><i class="fas fa-check"></i> API de integração</li>
            <li><i class="fas fa-check"></i> Suporte prioritário</li>
          </ul>
          <button
            type="button"
            class="btn btn-primary btn-outline"
            @click="$emit('select-plan', 4)"
            data-bs-toggle="modal"
            data-bs-target="#acquisitionModal"
            data-auth="register"
          >
            Comprar
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
defineEmits<{
  (e: 'select-plan', planId: number): void
}>()
</script>
