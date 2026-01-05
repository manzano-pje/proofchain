<script setup lang="ts">
const currentYear = new Date().getFullYear();
const version = 'v0.1.1';
</script>

<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-grid">
        <div class="footer-col">
          <h4>ProofChain</h4>
          <p style="color:#aaa; margin-top: 12px;">Sistema de certificação com blockchain para instituições que
            valorizam autenticidade.</p>
        </div>
        <div class="footer-col">
          <h4>Solução</h4>
          <ul>
            <li><a href="#recursos">Recursos</a></li>
            <li><a href="#como-funciona">Como funciona</a></li>
            <li><a href="#planos">Planos</a></li>
            <li><a href="#validacao">Validar certificado</a></li>
          </ul>
        </div>
        <div class="footer-col">
          <h4>Suporte</h4>
          <ul>
            <li><a href="#faq">FAQ</a></li>
            <li><a href="#">Documentação</a></li>
            <li><a href="#">API</a></li>
            <li><a href="#">Contato</a></li>
          </ul>
        </div>
        <div class="footer-col">
          <h4>Legal</h4>
          <ul>
            <li><a href="#">Termos de uso</a></li>
            <li><a href="#">Política de privacidade</a></li>
          </ul>
        </div>
      </div>
      <div class="copyright">
        <p>&copy; {{ currentYear }} ProofChain. Todos os direitos reservados. 
          <br/><span class="version-info">Versão: {{ version }}</span></p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
footer {
    background: var(--black);
    color: #aaa;
    padding: 60px 0 30px;
}

.footer-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 32px;
    margin-bottom: 40px;
}

.footer-col h4 {
    color: var(--white);
    margin-bottom: 20px;
    font-size: 1.1rem;
}

.footer-col ul {
    list-style: none;
}

.footer-col ul li {
    margin-bottom: 10px;
}

.footer-col ul li a {
    transition: color 0.3s;
}

.footer-col ul li a:hover {
    color: var(--green);
}

.version-info{
  font-weight: 600;
  font-size: 0.8rem;
  color: var(--green);
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>