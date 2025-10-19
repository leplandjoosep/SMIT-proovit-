<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import VinylCard from '../components/vinyl/VinylCard.vue'
import AddVinylDialog from '../components/vinyl/AddVinylDialog.vue'
import EditVinylDialog, { type VinylForm } from '../components/vinyl/EditVinylDialog.vue'
import Icon from '../components/ui/Icons.vue'

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

type PageResponse = {
  content: Vinyl[]
  totalSize: number
  pageable: {
    size: number
    number: number
    sort: any
    mode: string
  }
  first?: boolean
  last?: boolean
  numberOfElements?: number
  empty?: boolean
  totalElements?: number
  totalPages?: number
  total?: number
}

const API_VINYLS = '/api/vinyl'

const vinyls = ref<Vinyl[]>([])
const artists = ref<string[]>([])
const q = ref('')
const selectedArtist = ref('')
const currentPage = ref(0)
const pageSize = ref(12)
const totalPages = ref(0)
const totalElements = ref(0)
const showAdd = ref(false)
const showEdit = ref(false)
const editInit = ref<Vinyl | null>(null)
const error = ref('')
const loading = ref(false)

async function searchVinyls() {
  try {
    loading.value = true
    const params: any = {
      page: currentPage.value,
      size: pageSize.value,
      sort: 'artist,asc'
    }
    if (q.value.trim()) params.q = q.value.trim()
    if (selectedArtist.value) params.artist = selectedArtist.value
    
    const { data } = await axios.get<PageResponse>(`${API_VINYLS}/search`, { params })
    console.log('Vinyl search response:', data)
    vinyls.value = data.content || []
    
    const totalSize = data.totalSize || data.totalElements || data.total || 0
    totalPages.value = Math.ceil(totalSize / pageSize.value)
    totalElements.value = totalSize
  } catch (e: any) {
    console.error('Vinyl search error:', e)
    error.value = e?.response?.data?.message ?? e?.message ?? 'Search failed'
  } finally {
    loading.value = false
  }
}

async function loadArtists() {
  try {
    const { data } = await axios.get<string[]>(`${API_VINYLS}/artists`)
    artists.value = data
  } catch (e) {
    console.error('Failed to load artists', e)
  }
}

async function createRecord(payload: Partial<Vinyl>) {
  try {
    error.value = ''
    await axios.post<Vinyl>(API_VINYLS, payload)
    showAdd.value = false
    await searchVinyls()
    await loadArtists()
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Create failed'
  }
}

function onDeleted(id: string) {
  vinyls.value = vinyls.value.filter(v => v.id !== id)
  totalElements.value--
  if (vinyls.value.length === 0 && currentPage.value > 0) {
    currentPage.value--
  }
  searchVinyls()
}

function openEdit(v: Vinyl) { 
  editInit.value = { ...v }
  showEdit.value = true 
}

async function saveEdit(payload: VinylForm) {
  try {
    const { data } = await axios.put(`${API_VINYLS}/${payload.id}`, payload)
    const i = vinyls.value.findIndex(x => x.id === data.id)
    if (i >= 0) vinyls.value[i] = data
    await loadArtists()
  } catch(e: any) { 
    error.value = e?.response?.data?.message ?? e?.message 
  }
}

function resetSearch() {
  q.value = ''
  selectedArtist.value = ''
  currentPage.value = 0
}

function goToPage(page: number) {
  currentPage.value = page
}

let searchTimeout: ReturnType<typeof setTimeout> | null = null
watch(q, () => {
  if (searchTimeout) clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    currentPage.value = 0
    searchVinyls()
  }, 300)
})

watch(selectedArtist, () => {
  currentPage.value = 0
  searchVinyls()
})

watch(currentPage, () => {
  searchVinyls()
})

onMounted(async () => {
  loadArtists()
  try {
    const { data } = await axios.get(API_VINYLS)
    console.log('Basic vinyl response:', data)
    if (data && data.length > 0) {
      console.log('Found vinyl data, switching to search')
      searchVinyls()
    } else {
      console.log('No vinyl data found')
      searchVinyls()
    }
  } catch (e) {
    console.error('Basic vinyl error:', e)
    searchVinyls()
  }
})

</script>

<template>
  <section class="space-y-6">
    <div class="card border-fuchsia-200/60 bg-gradient-to-b from-fuchsia-50 to-white">
      <div class="p-4 flex items-start justify-between">
        <div>
          <h2 class="text-xl font-semibold">
            Vinüülid
          </h2>
          <p class="text-neutral-500 text-sm">
            Vinüülplaatide kollektsioon ({{ totalElements }} plaati)
          </p>
        </div>
        <button class="btn btn-emerald" @click="showAdd = true">+ Lisa plaat</button>
      </div>
      <div class="px-4 pb-4 space-y-3">
        <div class="flex gap-3 flex-wrap sm:flex-nowrap">
          <div class="relative flex-[2] min-w-0 w-full sm:w-auto">
            <input
                class="input pl-10 w-full"
                placeholder="Otsi artisti või pealkirja järgi..."
                v-model="q"
            />
            <Icon name="search" class="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-neutral-400" />
          </div>
          <select 
              class="input flex-[1] min-w-[160px] max-w-[220px]" 
              v-model="selectedArtist"
          >
            <option value="">Kõik artistid</option>
            <option v-for="artist in artists" :key="artist" :value="artist">
              {{ artist }}
            </option>
          </select>
        </div>
        <div v-if="q || selectedArtist" class="flex items-center gap-2 text-sm">
          <span class="text-neutral-600">
            Filtrid aktiivsed
          </span>
          <button 
              @click="resetSearch" 
              class="text-fuchsia-600 hover:text-fuchsia-800 underline"
          >
            Tühista filtrid
          </button>
        </div>
      </div>
    </div>

    <div v-if="loading" class="text-center py-12 text-neutral-500">
      Laadimine...
    </div>

    <div v-else-if="error" class="text-center py-12 text-red-500">
      <p>Viga: {{ error }}</p>
      <button @click="searchVinyls" class="btn btn-outline mt-2">Proovi uuesti</button>
    </div>

    <div v-else-if="vinyls.length" class="space-y-6">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <VinylCard
            v-for="v in vinyls"
            :key="v.id"
            :vinyl="v"
            @edit="openEdit"
            @deleted="onDeleted"
        />
      </div>

      <div class="text-center text-sm text-neutral-600">
        Lehekülg {{ currentPage + 1 }} / {{ totalPages }}
      </div>
      

      <div v-if="totalPages > 0" class="flex items-center justify-center gap-2 flex-wrap">
        <button 
            @click="goToPage(currentPage - 1)" 
            :disabled="currentPage === 0"
            class="btn btn-sm btn-outline"
            :class="currentPage === 0 ? 'opacity-50 cursor-not-allowed' : ''"
        >
          < Eelmine
        </button>
        
        <div class="flex gap-1 items-center">
          <button
              v-for="page in Math.max(1, totalPages)"
              :key="page"
              @click="goToPage(page - 1)"
              class="btn btn-sm min-w-[40px]"
              :class="currentPage === page - 1 ? 'btn-emerald' : 'btn-outline'"
          >
            {{ page }}
          </button>
        </div>

        <button 
            @click="goToPage(currentPage + 1)" 
            :disabled="currentPage >= totalPages - 1"
            class="btn btn-sm btn-outline"
            :class="currentPage >= totalPages - 1 ? 'opacity-50 cursor-not-allowed' : ''"
        >
          Järgmine >
        </button>
      </div>
    </div>

    <div v-else class="card border-dashed">
      <div class="p-12 text-center text-neutral-500">
        {{ q || selectedArtist ? 'Plaate ei leitud' : 'Lisa oma esimene vinüülplaat!' }}
      </div>
    </div>

    <AddVinylDialog v-model="showAdd" @add="createRecord" />
    <EditVinylDialog v-model="showEdit" :initial="editInit" @save="saveEdit" />
    <p v-if="error" class="text-red-600 text-center">{{ error }}</p>
  </section>
</template>
