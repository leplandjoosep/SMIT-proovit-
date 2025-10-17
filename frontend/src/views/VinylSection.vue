<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import VinylCard from '../components/vinyl/VinylCard.vue'
import AddVinylDialog from '../components/vinyl/AddVinylDialog.vue'
import EditVinylDialog, { type VinylForm } from '../components/vinyl/EditVinylDialog.vue'

type Vinyl = {
  id: string
  title: string
  artist: string
  releaseYear?: number | null
  acquiredFrom?: string | null
  acquiredDate?: string | null
  location?: string | null
  notes?: string | null
}

const API_VINYLS = '/api/vinyl'

const q = ref('')
const vinyls = ref<Vinyl[]>([])
const showAdd = ref(false)
const showEdit = ref(false)
const editInit = ref<Vinyl | null>(null)
const error = ref('')

const filtered = computed(() => {
  const s = q.value.trim().toLowerCase()
  if (!s) return vinyls.value
  return vinyls.value.filter(
      (r) =>
          r.title.toLowerCase().includes(s) ||
          r.artist.toLowerCase().includes(s)
  )
})

async function loadAll() {
  const { data } = await axios.get<Vinyl[]>(API_VINYLS)
  vinyls.value = data
}

async function createRecord(payload: Partial<Vinyl>) {
  try {
    error.value = ''
    const { data } = await axios.post<Vinyl>(API_VINYLS, payload)
    vinyls.value = [data, ...vinyls.value]
    showAdd.value = false
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Create failed'
  }
}

onMounted(loadAll)

function onDeleted(id: string) {
  vinyls.value = vinyls.value.filter(v => v.id !== id)
}

function openEdit(v:Vinyl){ editInit.value = { ...v }; showEdit.value = true }

async function saveEdit(payload: VinylForm){
  try {
    const { data } = await axios.put(`${API_VINYLS}/${payload.id}`, payload)
    const i = vinyls.value.findIndex(x => x.id === data.id)
    if (i >= 0) vinyls.value[i] = data
  } catch(e:any){ error.value = e?.response?.data?.message ?? e?.message }
}

</script>

<template>
  <section class="space-y-6">
    <div class="card border-fuchsia-200/60 bg-gradient-to-b from-fuchsia-50 to-white">
      <div class="p-4 flex items-start justify-between">
        <div>
          <h2 class="text-xl font-semibold flex items-center gap-2">🎵 Vinüülid</h2>
          <p class="text-neutral-500 text-sm">
            Sinu vinüülplaatide kollektsioon ({{ vinyls.length }} plaati)
          </p>
        </div>
        <button class="btn btn-emerald" @click="showAdd = true">+ Lisa plaat</button>
      </div>
      <div class="px-4 pb-4">
        <div class="relative">
          <input
              class="input pl-10"
              placeholder="Otsi artisti või pealkirja järgi..."
              v-model="q"
          />
          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-400">🔎</span>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <VinylCard
          v-for="v in filtered"
          :key="v.id"
          :vinyl="v"
          @edit="openEdit"
          @deleted="onDeleted"
      />
    </div>

    <div v-if="!filtered.length" class="card border-dashed">
      <div class="p-12 text-center text-neutral-500">
        {{ q ? 'Plaate ei leitud' : 'Lisa oma esimene vinüülplaat!' }}
      </div>
    </div>

    <AddVinylDialog v-model="showAdd" @add="createRecord" />
    <EditVinylDialog v-model="showEdit" :initial="editInit" @save="saveEdit" />
    <p v-if="error" class="text-red-600">{{ error }}</p>
  </section>
</template>
