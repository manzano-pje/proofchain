<template>
  <div class="container mt-4">
    <h2>Users Management</h2>
    <p>Manage system users here.</p>
    
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>Admin User</td>
            <td>admin@validex.com</td>
            <td>ADMIN</td>
            <td>
              <button class="btn btn-sm btn-primary me-2">Edit</button>
              <button class="btn btn-sm btn-danger">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
// Logic for fetching users will go here
</script>
