<template>
  <div class="container-fluid p-0">
    <nav class="navbar navbar-expand-lg navbar-light bg-light px-4">
      <a class="navbar-brand" href="#">Validex</a>
      <div class="ms-auto">
        <router-link to="/login" class="btn btn-outline-primary">Login</router-link>
      </div>
    </nav>
    
    <div class="px-4 py-5 my-5 text-center">
      <h1 class="display-5 fw-bold">Welcome to Validex</h1>
      <div class="col-lg-6 mx-auto">
        <p class="lead mb-4">Secure, fast and reliable validation services for your certificates and entities.</p>
        <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
          <router-link to="/login" class="btn btn-primary btn-lg px-4 gap-3">Get Started</router-link>
          <button type="button" class="btn btn-outline-secondary btn-lg px-4">Learn More</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>
