<script setup lang="ts">
import axios from "axios";

const props = defineProps<{
  vinyl: {
    id:string;
    title:string;
    artist:string;
    releaseYear?:number|null;
    acquiredFrom?:string|null;
    location?:string|null;
    notes?:string|null;
  }}>()

const emit = defineEmits<{
  (e:'deleted', id:string):void
  (e:'edit', vinyl:any):void
}>()

async function deleteVinyl() {
  if (!confirm(`Kustuta plaat "${props.vinyl.artist} – ${props.vinyl.title}"?`)) return
  await axios.delete(`/api/vinyl/${props.vinyl.id}`)
  emit('deleted', props.vinyl.id)
}

</script>

<template>
  <div class="card p-5 hover:shadow-lg transition">
    <header class="flex justify-between">
      <div>
        <h3 class="text-lg font-semibold">{{ vinyl.title }}</h3>
        <p class="text-sm text-indigo-600">{{ vinyl.artist }}</p>
      </div>
      <div class="mb-2 flex items-start justify-between">
        <button class="text-neutral-400 hover:text-indigo-600 mr-2" @click="$emit('edit', vinyl)" title="Muuda">
          ✏️
        </button>
        <button class="text-neutral-400 hover:text-red-600" @click="deleteVinyl" title="Kustuta">
          🗑️
        </button>
      </div>
    </header>

    <ul class="mt-3 space-y-1 text-sm text-neutral-600">
      <li v-if="vinyl.releaseYear">📅 {{ vinyl.releaseYear }}</li>
      <li v-if="vinyl.acquiredFrom">🛍️ {{ vinyl.acquiredFrom }}</li>
    </ul>

    <div v-if="vinyl.location" class="mt-2">
      <span class="pill">📍 {{ vinyl.location }}</span>
    </div>

    <p v-if="vinyl.notes" class="mt-2 text-sm italic text-neutral-500">{{ vinyl.notes }}</p>
  </div>
</template>
