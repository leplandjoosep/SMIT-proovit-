<script setup lang="ts">
import axios from "axios";
import Icon from '../ui/Icons.vue';

const props = defineProps<{
  vinyl: {
    id:string;
    title:string;
    artist:string;
    releaseYear?:number|null;
    acquiredFrom?:string|null;
    acquiredDate?:string|null;
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
  <div class="group relative overflow-hidden rounded-2xl bg-gradient-to-br from-purple-50 to-pink-50 border border-purple-200/60 p-6 hover:shadow-xl hover:scale-[1.02] transition-all duration-300">
    <div class="relative z-10">
      <header class="flex justify-between items-start mb-4">
        <div class="flex-1 min-w-0">
          <h3 class="text-lg font-bold text-gray-900 mb-1 line-clamp-2">{{ vinyl.title }}</h3>
          <p class="text-sm font-medium text-purple-600">{{ vinyl.artist }}</p>
        </div>
        <div class="flex items-center gap-1 ml-3">
          <button class="p-2 text-gray-400 hover:text-purple-600 hover:bg-purple-100 rounded-lg transition-colors" @click="$emit('edit', vinyl)" title="Muuda">
            <Icon name="edit" class="w-4 h-4" />
          </button>
          <button class="p-2 text-gray-400 hover:text-red-600 hover:bg-red-100 rounded-lg transition-colors" @click="deleteVinyl" title="Kustuta">
            <Icon name="delete" class="w-4 h-4" />
          </button>
        </div>
      </header>

      <div class="space-y-3">
        <ul class="space-y-2 text-sm">
          <li v-if="vinyl.releaseYear" class="flex items-center gap-2 text-gray-600">
            <Icon name="calendar" class="w-4 h-4 text-purple-500" />
            <span class="font-medium">{{ vinyl.releaseYear }}</span>
          </li>
          <li v-if="vinyl.acquiredFrom" class="flex items-center gap-2 text-gray-600">
            <Icon name="store" class="w-4 h-4 text-purple-500" />
            <span>{{ vinyl.acquiredFrom }}</span>
          </li>
          <li v-if="vinyl.acquiredDate" class="flex items-center gap-2 text-gray-600">
            <Icon name="calendar" class="w-4 h-4 text-purple-500" />
            <span>Ostetud: {{ new Date(vinyl.acquiredDate).toLocaleDateString('et-EE') }}</span>
          </li>
        </ul>

        <div v-if="vinyl.location" class="mt-3">
          <span class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium bg-purple-100 text-purple-700 border border-purple-200">
            <Icon name="location" class="w-3 h-3" />
            {{ vinyl.location }}
          </span>
        </div>

        <p v-if="vinyl.notes" class="mt-3 text-sm text-gray-500 italic bg-white/50 rounded-lg p-2 border border-purple-100">
          {{ vinyl.notes }}
        </p>
      </div>
    </div>
  </div>
</template>
