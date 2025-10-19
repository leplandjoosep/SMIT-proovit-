<script setup lang="ts">
</script>

<template>
  <div class="min-h-screen bg-ink-50 text-neutral-900">
    <header class="px-6 py-8 border-b bg-gradient-to-r from-slate-50 to-gray-50">
      <div class="max-w-6xl mx-auto">
        <h1 class="text-3xl font-bold text-slate-800 mb-2">Kollektsioonihaldur</h1>
        <p class="text-slate-600 font-medium">Spetsiifilised kollektsioonid ja nende jaoks vajalikud funktsionaalsused</p>
      </div>
    </header>

    <nav class="mt-5 flex justify-center">
      <div class="inline-flex rounded-full border bg-white p-1">
        <RouterLink
            to="/vinyls"
            class="px-4 py-1.5 rounded-full text-sm transition"
            :class="$route.path.startsWith('/vinyls') ? 'bg-neutral-900 text-white' : 'text-neutral-700 hover:bg-neutral-100'"
        >
          Vinüülid
        </RouterLink>
        <RouterLink
            to="/parts"
            class="px-4 py-1.5 rounded-full text-sm transition"
            :class="$route.path.startsWith('/parts') ? 'bg-neutral-900 text-white' : 'text-neutral-700 hover:bg-neutral-100'"
        >
          Rattaosad
        </RouterLink>
      </div>
    </nav>


    <main class="max-w-6xl mx-auto p-6">
      <RouterView />
    </main>
  </div>
</template>
