<template>
  <div class="container mt-4">
    <h1>Admin Dashboard</h1>
    <div class="row mt-4">
      <div class="col-md-4">
        <div class="card text-white bg-primary mb-3">
          <div class="card-header">Users</div>
          <div class="card-body">
            <h5 class="card-title">15 Registered</h5>
            <router-link to="/admin/users" class="btn btn-light btn-sm mt-2">Manage Users</router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card text-white bg-success mb-3">
          <div class="card-header">Certificates</div>
          <div class="card-body">
            <h5 class="card-title">1,234 Issued</h5>
            <router-link to="/admin/certificates" class="btn btn-light btn-sm mt-2">View Certificates</router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card text-white bg-warning mb-3">
          <div class="card-header">Entities</div>
          <div class="card-body">
            <h5 class="card-title">5 Active</h5>
            <router-link to="/admin/entities" class="btn btn-light btn-sm mt-2">Manage Entities</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>
