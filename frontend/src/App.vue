<template>
  <div class="min-h-screen bg-ink-50 text-neutral-900">
    <header class="px-6 py-6 border-b bg-white">
      <h1 class="text-2xl font-extrabold">Kollektsioonihaldur</h1>
      <p class="text-sm text-neutral-500">Spetsiifilised kollektsioonid ja nende jaoks vajalikud funktsionaalsused</p>
    </header>

    <nav class="mt-5 flex justify-center">
      <div class="inline-flex rounded-full border bg-white p-1">
        <RouterLink
            to="/vinyls"
            class="px-4 py-1.5 rounded-full text-sm transition"
            :class="$route.path.startsWith('/vinyls') ? 'bg-neutral-900 text-white' : 'text-neutral-700 hover:bg-neutral-100'"
        >🎵 Vinüülid</RouterLink>
        <RouterLink
            to="/parts"
            class="px-4 py-1.5 rounded-full text-sm transition"
            :class="$route.path.startsWith('/parts') ? 'bg-neutral-900 text-white' : 'text-neutral-700 hover:bg-neutral-100'"
        >🚴 Rattaosad</RouterLink>
      </div>
    </nav>


    <main class="max-w-6xl mx-auto p-6">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>
