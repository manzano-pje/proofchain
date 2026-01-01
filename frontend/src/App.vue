<script setup lang="ts">
  import {RouterView } from 'vue-router' 
</script>

<template>
  <header class="container">
     <div class="header-content">
        <RouterLink to="/" class="logo">
          <img :src="logo" alt="ProofChain Logo" />
        </RouterLink>

        <button class="mobile-toggle" @click="toggleMenu">
          <i class="fas fa-bars"></i>
        </button>

        <nav :class="{open: isMenuOpen}">
          <ul>
            <li><a href="#recursos">Recursos</a></li>
            <li><a href="#como-funciona">Como funciona</a></li>
            <li><a href="#planos">Planos</a></li>
            <li><a href="#autenticacao" class="btn btn-outline">Autenticador</a></li>
            <li>
              <RouterLink to="/login" class="btn btn-outline btn-primary">Login</RouterLink>                
            </li>
          </ul>
        </nav>
      </div>
  </header>
</template>

<style scoped >

  :root {
    
    --green-light: #e6f7ee;
    --green-medium: #a8e6c1;
    --green: #08602a;
    --green-logo: #30a335;
    --green-dark-logo: #07672a;

    --white: #ffffff;
    --gray-light: #f8f9fa;
    --gray: #6c757d;
    --gray-medium: #e7e7e7;
    --gray-dark: #495057;
    --black: #212525;

    --red-ligth:#f98d8d;
    --red: #d50404;
}

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: var(--black);
    line-height: 1.6;
    background-color: var(--white);
    scroll-behavior: smooth;
}
a {
    text-decoration: none;
    color: inherit;
}

.container {
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    z-index: 1000;
}

.header-content {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    background-color: var(--white);
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.logo img {
    height: 40px;
    display: flex;
    align-items: center;
    gap: 10px;
}
.logo i {
    color: var(--green);   
}

nav ul {
    display: flex;
    list-style: none;
    gap: 24px;
}

nav a {
    font-weight: 600;
    transition: color 0.3s;
}

nav a:hover {
    color: var(--green);
}

.btn {
    display: inline-block;
    padding: 5px 24px;
    border-radius: 8px;
    font-weight: 600;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s;
}

.btn-close{
    font-size: x-small;
    border: none;
}

.btn-primary {
    background-color: var(--green-light);
    color: var(--green);
}

.btn-primary:hover {
    background-color:var(--gray-medium);
    color: var(--white);
    transform: translateY(-2px);
}

.btn-outline {
    background: transparent;
    border: 2px solid var(--green);
    color: var(--green);
}

.btn-outline:hover {
    background-color: var(--green);
    color: var(--white);
}

.mobile-toggle {
    display: none;
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    color: var(--black);
}
</style>
