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
    <section id="id-validacao" class="validacao">
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

    <!-- Recursos -->
    <section id="id-recursos" class="recursos">
        <div class="container">
            <div class="recursos-box">
                <h3>Por que escolher o <span>ProofChain?</span></h3>
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
</template>

<script setup lang="ts">
    import Header from '@/components/Header.vue';
    import Footer from '@/components/Footer.vue';
</script>
