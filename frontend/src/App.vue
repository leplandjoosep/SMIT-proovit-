<template>
  <header style="padding:12px; border-bottom:1px solid #ddd; display:flex; gap:16px;">
    <router-link to="/vinyls">Vinyls</router-link>
    <router-link to="/parts">Bike Parts</router-link>
    <router-link to="/loans">Loans</router-link>
  </header>
  <main style="padding:16px;">
    <router-view />
  </main>
</template>

<script setup lang="ts">
</script>

<style>
a.router-link-active { font-weight: 600; text-decoration: underline; }
</style>
