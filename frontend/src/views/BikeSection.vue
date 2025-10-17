<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import AddBikePartDialog from '../components/part/AddBikePartDialog.vue'
import BikePartCard from '../components/part/BikePartCard.vue'
import EditBikePartDialog, { type PartForm } from '../components/part/EditBikePartDialog.vue'

type BikePart = {
  id: string
  name: string
  brand?: string | null
  category?: string | null
  quantity: number
  location?: string | null
  notes?: string | null
}

const API_PARTS = '/api/part'

const parts = ref<BikePart[]>([])
const q = ref('')
const showAdd = ref(false)
const showEdit = ref(false)
const editInit = ref<BikePart|null>(null)
const error = ref('')

const filtered = computed(() => {
  const s = q.value.trim().toLowerCase()
  if (!s) return parts.value
  return parts.value.filter(p =>
      p.name.toLowerCase().includes(s) ||
      (p.brand?.toLowerCase().includes(s)) ||
      (p.category?.toLowerCase().includes(s))
  )
})

async function loadParts() {
  const { data } = await axios.get(API_PARTS)
  parts.value = data
}

async function createPart(payload: {
  name: string
  brand?: string
  category?: string
  quantity?: number
  location?: string
  notes?: string
}) {
  try {
    error.value = ''
    const { data } = await axios.post(API_PARTS, payload)
    parts.value = [data, ...parts.value]
    showAdd.value = false
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Create failed'
  }
}

function onDeleted(id: string) {
  parts.value = parts.value.filter(p => p.id !== id)
}


function openEdit(p:BikePart){ editInit.value = { ...p }; showEdit.value = true }

async function saveEdit(payload: PartForm){
  try {
    const { data } = await axios.put(`${API_PARTS}/${payload.id}`, payload)
    const i = parts.value.findIndex(x => x.id === data.id)
    if (i >= 0) parts.value[i] = data
  } catch(e:any){ error.value = e?.response?.data?.message ?? e?.message }
}

onMounted(loadParts)
</script>

<template>
  <section class="space-y-6">
    <div class="card border-sky-200/60 bg-gradient-to-b from-sky-50 to-white">
      <div class="p-4 flex items-start justify-between">
        <div>
          <h2 class="text-xl font-semibold flex items-center gap-2">🚴 Rattaosad</h2>
          <p class="text-neutral-500 text-sm">
            Sinu rattaosade inventar ({{ parts.length }} tüüpi)
          </p>
        </div>
        <button class="btn btn-primary" @click="showAdd = true">+ Lisa osa</button>
      </div>
      <div class="px-4 pb-4">
        <div class="relative">
          <input
              class="input pl-10"
              placeholder="Otsi nime, brändi või kategooria järgi..."
              v-model="q"
          />
          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-400">🔎</span>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <BikePartCard
          v-for="p in filtered"
          :key="p.id"
          :part="p"
          @deleted="onDeleted"
          @edit="openEdit"
      />
    </div>

    <div v-if="!filtered.length" class="card border-dashed">
      <div class="p-12 text-center text-neutral-500">
        {{ q ? 'Osi ei leitud' : 'Lisa oma esimene rattaosa!' }}
      </div>
    </div>

    <AddBikePartDialog v-model="showAdd" @add="createPart" />
    <EditBikePartDialog v-model="showEdit" :initial="editInit" @save="saveEdit" />

    <p v-if="error" class="text-red-600">{{ error }}</p>
  </section>
</template>
