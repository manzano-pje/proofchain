<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import logo from '@/assets/img/logo-horizontal.png'

const isMenuOpen = ref(false)

const toggleMenu = () => {
    isMenuOpen.value = !isMenuOpen.value
}
</script>

<template>    
<header class="container">
    <div class="header-content">
    <RouterLink to="/" class="logo">
        <img :src="logo" alt="ProofChain Logo" />
    </RouterLink>

    <button class="mobile-toggle" @click="toggleMenu" aria-label="Toggle navigation">
        <i class="fas fa-bars"></i>
    </button>

    <nav :class="{open: isMenuOpen}">
        <ul>
        <li><a href="#recursos" @click="isMenuOpen = false">Recursos</a></li>
        <li><a href="#como-funciona" @click="isMenuOpen = false">Como funciona</a></li>
        <li><a href="#planos" @click="isMenuOpen = false">Planos</a></li>
        <li><a href="#autenticacao" class="btn btn-outline" @click="isMenuOpen = false">Autenticador</a></li>
        <li>
            <RouterLink to="/login" class="btn btn-outline btn-primary" @click="isMenuOpen = false">Login</RouterLink>                
        </li>
        </ul>
    </nav>
    </div>
</header>
</template>

<style scoped>
.container {
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 16px 20px;
    z-index: 1000;
    position: relative;
    height: 80px; /* placeholder height to prevent layout shift */
}

.header-content {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 80px;
    padding: 0 20px;
    background-color: var(--white);
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 1000;
}

.logo {
    display: flex;
    align-items: center;
    gap: 10px;
    z-index: 1001;
}

.logo img {
    height: 40px;
}

nav ul {
    display: flex;
    list-style: none;
    gap: 24px;
    margin: 0;
    padding: 0;
    align-items: center;
}

nav a {
    font-weight: 600;
    transition: color 0.3s;
    color: var(--black);
    text-decoration: none;
}

nav a:hover {
    color: var(--green);
}

.btn {
    display: inline-block;
    padding: 8px 24px;
    border-radius: 8px;
    font-weight: 600;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s;
    text-decoration: none;
}

.btn-primary {
    background-color: var(--green-light);
    color: var(--green);
    border: 2px solid transparent;
}

.btn-primary:hover {
    background-color: var(--green);
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
    z-index: 1001;
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
    .mobile-toggle {
        display: block;
    }

    nav {
        position: fixed;
        top: 0;
        right: -100%;
        width: 70%;
        height: 100vh;
        background-color: var(--white);
        box-shadow: -2px 0 10px rgba(0,0,0,0.2);
        padding-top: 80px;
        transition: right 0.3s ease-in-out;
        z-index: 1000;
    }

    nav.open {
        right: 0;
    }

    nav ul {
        flex-direction: column;
        align-items: center;
        gap: 32px;
    }
}
</style>
