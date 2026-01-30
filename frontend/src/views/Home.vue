d<template>
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
            <h2>Autenticação de Certificados</h2>
            <p>Confirme a validade de um certificado em segundos,<br>
                com total transparência e segurança.
            </p>
            <div class="btn-validar">
                <a href="#validar" class="btn btn-outline">Validar Certificado</a>
            </div>
        </div>
    </section>

    <!-- Recursos -->
    <section id="recursos" class="recursos">
        <div class="container">
            <div class="recursos-box">
                <h2>Por que escolher o <span>ProofChain?</span></h2>
                <p>
                Uma plataforma completa para emissão e autenticação <br>
                de certificados digitais com total confiança.
                </p>


                <div class="grid">
                    <div class="card">
                        <i class="fa-solid fa-shield"></i>
                        <h3>Segurança contra fraudes</h3>
                        <p>
                        Cada certificado é registrado em blockchain,
                        tornando qualquer tentativa de alteração
                        impossível.
                        </p>
                    </div>

                    <div class="card">
                        <i class="fa-solid fa-fingerprint"></i>
                        <h3>Autenticidade garantida</h3>
                        <p>
                        Qualquer pessoa pode verificar a
                        autenticidade de um certificado
                        em poucos segundos.
                        </p>
                    </div>

                    <div class="card">
                        <i class="fa-solid fa-users"></i>
                        <h3>Acesso público e institucional</h3>
                        <p>  
                        Visitantes validam certificados
                        livremente, enquanto instituições
                        gerenciam emissões com segurança.
                        </p>
                    </div>

                    <div class="card">
                        <i class="fa-solid fa-certificate"></i>
                        <h3>Identidade visual personalizada</h3>
                        <p>
                        Personalize certificados com logotipo,
                        cores e layout da sua instituição.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!--Como funciona -->
    <section id="funcionamento" class="funcionamento">
         <div class="container">
            <div class="funcionamento-box">
                <h2>Como funciona</h2>
                <p>
                Transparência para o público e controle completo para as instituições.
                Cada nível oferece funcionalidades específicas de acordo com o perfil do usuário.
                </p>

                <div class="grid">
                    <div class="card">
                        <i class="fa-solid fa-shield"></i>
                        <h2>Validação Pública</h2>
                        <p>Acesso livre pela página principal.</p>
                        <span>Qualquer pessoa pode inserir o código do certificado<br> 
                              para visualizar todas as informações e confirmar sua<br>
                              autenticidade de forma rápida e segura.</span>
                    </div>

                    <div class="card">
                        <i class="fa-solid fa-fingerprint"></i>
                        <h2>Instituições</h2>
                        <p>Acesso exclusivo por login.</p>
                        <span>As instituições podem emitir certificados, cadastrar cursos<br>
                              e participantes, gerar relatórios, além de gerenciar todas <br>
                              as informações de forma centralizada e eficiente.
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- PLano -->
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
            <li><i class="fas fa-check"></i> 10 autenticações</li>
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> Válido por 7 dias</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" 
            data-bs-target="#authModal" data-auth="login">Começar teste gratuito
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
            <li><i class="fas fa-check"></i> 50 autenticações/mês</li>
            <li><i class="fas fa-check"></i> Modelo padrão</li>
            <li><i class="fas fa-check"></i> Relatórios básicos</li>
            <li><i class="fas fa-check"></i> Válido por 15 dias</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="register">Comprar
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
            <li><i class="fas fa-check"></i> 200 autenticações/mês</li>
            <li><i class="fas fa-check"></i> Modelos padrão</li>
            <li><i class="fas fa-check"></i> Relatórios avançados</li>
            <li><i class="fas fa-check"></i> Dashboard completo</li>
            <li><i class="fas fa-check"></i> Suporte prioritário</li>
            <li><i class="fas fa-check"></i> Cancele quando quiser</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="register">Comprar
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
            <li><i class="fas fa-check"></i> 2000 autenticações/mês</li>
            <li><i class="fas fa-check"></i> Modelos personalizados</li>
            <li><i class="fas fa-check"></i> Relatórios avançados</li>
            <li><i class="fas fa-check"></i> Dashboard avançado</li>
            <li><i class="fas fa-check"></i> API de integração</li>
            <li><i class="fas fa-check"></i> Suporte dedicado</li>
          </ul>
          <button type="button" class="btn btn-primary btn-outline" data-bs-toggle="modal" data-bs-target="#authModal"
            data-auth="register">Comprar
          </button>
        </div>
      </div>
    </div>
  </section>

<!-- 
  <section id="login" class="login">
     Modal -
    <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="exampleModalLabel">Login</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form id="loginForm">
              <div class="mb-3">
                <label for="email" class="col-form-label">Email:</label>
                <input type="email" class="form-control" id="email" required="true">

                <div class="invalid-feedback">Informe um e-mail válido.</div>
              </div>

              <div class="mb-3">
                <label for="password" class="col-form-label">Password:</label>
                <input type="password" class="form-control" id="password" required="true" minlength="6">
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Save changes</button>
          </div>
        </div>
      </div>
    </div>
  </section> -->


</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';


// const loginModal = ref(null);
// const loginForm = ref(null);

// onMounted(() => {
//   if (loginModal.value) {
//     loginModal.value.addEventListener('show.bs.modal', () => {
//       if (loginForm.value) {
//         loginForm.value.reset();
//         loginForm.value.classList.remove('was-validated');
//       }
//     });
//   }

//   if (loginForm.value) {
//     loginForm.value.addEventListener('submit', (e) => {
//       e.preventDefault();
//       if (!loginForm.value.checkValidity()) {
//         loginForm.value.classList.add('was-validated');
//         return;
//       }
//       const email = loginForm.value.querySelector('#email').value;
//       const password = loginForm.value.querySelector('#password').value;
//       console.log('Login:', email, password);
//       // Chame API aqui
//     });
//   }
// });
</script>



<!-- <script setup lang="ts">
    import Header from '@/components/Header.vue';
    import Footer from '@/components/Footer.vue';
    import { textSpanContainsPosition } from 'typescript';
</script>
<script setup lang="ts">
  const modal = document.getElementById('loginModal');
  const form = document.getElementById('loginForm');

  modal.addEventListener('show.bs.modal', () => {
    form.reset();
    form.classList.remove('was-validated');
  });

  form.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!form.checkValidity()) {
      form.classList.add('was-validated');
      return;
    }

    // ✔️ Formulário válido
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    console.log('Login:', email, senha);

    // Aqui você pode chamar sua API
  });

</script> -->
