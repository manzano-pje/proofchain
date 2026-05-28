<template>
  <div class="min-h-screen flex">
    <!-- Coluna Esquerda - Visual Blockchain -->
    <div class="hidden lg:flex lg:w-1/2 bg-[#0A2540] relative overflow-hidden items-center justify-center">
      <div class="absolute inset-0 bg-[radial-gradient(at_center,#00C85320_0%,transparent_70%)]"></div>
      
      <div class="relative z-10 text-center px-12">
        <img :src="logoHorizontal" alt="ProofChain" class="h-16 mx-auto mb-12 opacity-90" />
        
        <div class="space-y-8 text-white">
          <h1 class="text-5xl font-semibold leading-tight">
            Autenticidade que <span class="text-accent">confia</span> no blockchain
          </h1>
          <p class="text-xl text-gray-300 max-w-md mx-auto">
            Certificados digitais imutáveis, verificação instantânea e total transparência.
          </p>
        </div>

        <!-- Mock de rede blockchain -->
        <div class="mt-16 relative w-[380px] h-[380px] mx-auto">
          <!-- Cubos e conexões simuladas via CSS/Background -->
          <div class="absolute inset-0 bg-[url('data:image/svg+xml,...')] opacity-30"></div>
        </div>
      </div>
    </div>

    <!-- Coluna Direita - Formulário -->
    <div class="flex-1 flex items-center justify-center bg-white p-8 lg:p-16">
      <div class="w-full max-w-md">
        <div class="flex justify-center mb-10 lg:hidden">
          <img :src="logoHorizontal" alt="ProofChain" class="h-12" />
        </div>

        <h2 class="text-3xl font-semibold text-primary mb-2">Bem-vindo de volta</h2>
        <p class="text-gray-600 mb-8">Acesse sua conta ProofChain</p>

        <LoginForm />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import LoginForm from '@/components/auth/LoginForm.vue'
import logoHorizontal from '@/assets/images/logo-horizontal.png'
</script>