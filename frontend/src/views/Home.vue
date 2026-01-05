<template>
    <!-- <Hero /> -->
    <section class="section hero ">
        <div class="container">
            <h1>Certificados realmente<br>
                <span>seguros contra fraudes</span>
            </h1>
            <p>Emissão e autenticação de <br>certificados com tecnologia blockchain.</p>
            <div class="hero-buttons">
                <a href="#planos" class="btn btn-outline">Comece agora</a>
                <a href="#validacao" class="btn btn-outline">Autenticar certificado</a>
            </div>
        </div>
    </section>   

    <!-- Autenticação -->
     <section id="validacao" class="validacao">
        <div class="validation-box">
            <h3>Autenticação de Certificados</h3>
            <p>Confirme a validade de um certificado em segundos,<br>
                com total transparência e segurança.
            </p>
            <div class="btn-validar">
                <a href="#validar" class="btn btn-outline">Validar Certificado</a>
            </div>
        </div>
     </section>

     <!-- Planos -->
  <section id="planos" class="plans">
    <div class="container">
      <div class="section-title">
        <h2>Planos simples e transparentes</h2>
        <p>Escolha o modelo ideal para sua instituição.</p>
      </div>

      <div class="grid">
        <div class="plan">
          <h3>Por Quantidade</h3>
          <p>Pacotes de 10, 20, 50 ou 100 certificados</p>
          <div class="price">R$ 4,90 <span>/certificado</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> validação ilimitada</li>
            <li><i class="fas fa-check"></i> Relatórios básicos</li>
            <li><i class="fas fa-check"></i> Suporte por e-mail</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="login">
            Comprar
          </button>
        </div>

        <div class="plan">
          <h3>Assinatura Básica</h3>
          <p>Até 50 certificados/mês</p>
          <div class="price">R$ 49,90 <span>/mês</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> Dashboard completo</li>
            <li><i class="fas fa-check"></i> Relatórios avançados</li>
            <li><i class="fas fa-check"></i> Suporte prioritário</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="register">Comprar
          </button>
        </div>

        <div class="plan popular">
          <div class="popular-badge">Mais Popular</div>
          <h3>Assinatura Premium</h3>
          <p>Certificados ilimitados</p>
          <div class="price">R$ 99,90 <span>/mês</span></div>
          <ul class="features">
            <li><i class="fas fa-check"></i> Modelos personalizados</li>
            <li><i class="fas fa-check"></i> API de integração</li>
            <li><i class="fas fa-check"></i> Dashboard avançado</li>
            <li><i class="fas fa-check"></i> Suporte dedicado</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="register">Comprar
          </button>
        </div>
      </div>
    </div>
  </section>

</template>

<script setup lang="ts">
    
</script>
