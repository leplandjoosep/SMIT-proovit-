<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import AddBikePartDialog from '../components/part/AddBikePartDialog.vue'
import BikePartCard from '../components/part/BikePartCard.vue'
import EditBikePartDialog, { type PartForm } from '../components/part/EditBikePartDialog.vue'
import Icon from '../components/ui/Icons.vue'

type BikePart = {
  id: string
  name: string
  brand?: string | null
  category?: string | null
  quantity: number
  location?: string | null
  notes?: string | null
}

type PageResponse = {
  content: BikePart[]
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

const API_PARTS = '/api/part'

const parts = ref<BikePart[]>([])
const categories = ref<string[]>([])
const q = ref('')
const selectedCategory = ref('')
const currentPage = ref(0)
const pageSize = ref(12)
const totalPages = ref(0)
const totalElements = ref(0)
const showAdd = ref(false)
const showEdit = ref(false)
const editInit = ref<BikePart|null>(null)
const error = ref('')
const loading = ref(false)

async function searchParts() {
  try {
    loading.value = true
    const params: any = {
      page: currentPage.value,
      size: pageSize.value,
      sort: 'name,asc'
    }
    if (q.value.trim()) params.q = q.value.trim()
    if (selectedCategory.value) params.category = selectedCategory.value
    
    const { data } = await axios.get<PageResponse>(`${API_PARTS}/search`, { params })
    console.log('Bike parts search response:', data)
    parts.value = data.content || []
    
    const totalSize = data.totalSize || data.totalElements || data.total || 0
    totalPages.value = Math.ceil(totalSize / pageSize.value)
    totalElements.value = totalSize
  } catch (e: any) {
    console.error('Bike parts search error:', e)
    error.value = e?.response?.data?.message ?? e?.message ?? 'Search failed'
  } finally {
    loading.value = false
  }
}

async function loadCategories() {
  try {
    const { data } = await axios.get<string[]>(`${API_PARTS}/categories`)
    categories.value = data
  } catch (e) {
    console.error('Failed to load categories', e)
  }
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
    await axios.post(API_PARTS, payload)
    showAdd.value = false
    await searchParts()
    await loadCategories()
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Create failed'
  }
}

function onDeleted(id: string) {
  parts.value = parts.value.filter(p => p.id !== id)
  totalElements.value--
  if (parts.value.length === 0 && currentPage.value > 0) {
    currentPage.value--
  }
  searchParts()
}

function openEdit(p: BikePart) { 
  editInit.value = { ...p }
  showEdit.value = true 
}

async function saveEdit(payload: PartForm) {
  try {
    const { data } = await axios.put(`${API_PARTS}/${payload.id}`, payload)
    const i = parts.value.findIndex(x => x.id === data.id)
    if (i >= 0) parts.value[i] = data
    await loadCategories()
  } catch(e: any) { 
    error.value = e?.response?.data?.message ?? e?.message 
  }
}

function resetSearch() {
  q.value = ''
  selectedCategory.value = ''
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
    searchParts()
  }, 300)
})

watch(selectedCategory, () => {
  currentPage.value = 0
  searchParts()
})

watch(currentPage, () => {
  searchParts()
})

onMounted(async () => {
  loadCategories()
  try {
    const { data } = await axios.get(API_PARTS)
    console.log('Basic bike parts response:', data)
    if (data && data.length > 0) {
      console.log('Found bike parts data, switching to search')
      searchParts()
    } else {
      console.log('No bike parts data found')
      searchParts()
    }
  } catch (e) {
    console.error('Basic bike parts error:', e)
    searchParts()
  }
})
</script>

<template>
  <section class="space-y-6">
    <div class="card border-sky-200/60 bg-gradient-to-b from-sky-50 to-white">
      <div class="p-4 flex items-start justify-between">
        <div>
          <h2 class="text-xl font-semibold">
            Rattaosad
          </h2>
          <p class="text-neutral-500 text-sm">
            Rattaosade kollektsioon ({{ totalElements }} osa)
          </p>
        </div>
        <button class="btn btn-primary" @click="showAdd = true">+ Lisa osa</button>
      </div>
      <div class="px-4 pb-4 space-y-3">
        <div class="flex gap-3 flex-wrap sm:flex-nowrap">
          <div class="relative flex-[2] min-w-0 w-full sm:w-auto">
            <input
                class="input pl-10 w-full"
                placeholder="Otsi nime või brändi järgi..."
                v-model="q"
            />
            <Icon name="search" class="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-neutral-400" />
          </div>
          <select 
              class="input flex-[1] min-w-[160px] max-w-[220px]" 
              v-model="selectedCategory"
          >
            <option value="">Kõik kategooriad</option>
            <option v-for="cat in categories" :key="cat" :value="cat">
              {{ cat }}
            </option>
          </select>
        </div>
        <div v-if="q || selectedCategory" class="flex items-center gap-2 text-sm">
          <span class="text-neutral-600">
            Filtrid aktiivsed
          </span>
          <button 
              @click="resetSearch" 
              class="text-sky-600 hover:text-sky-800 underline"
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
      <button @click="searchParts" class="btn btn-outline mt-2">Proovi uuesti</button>
    </div>

    <div v-else-if="parts.length" class="space-y-6">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <BikePartCard
            v-for="p in parts"
            :key="p.id"
            :part="p"
            @deleted="onDeleted"
            @edit="openEdit"
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
              :class="currentPage === page - 1 ? 'btn-primary' : 'btn-outline'"
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
        {{ q || selectedCategory ? 'Osi ei leitud' : 'Lisa oma esimene rattaosa!' }}
      </div>
    </div>

    <AddBikePartDialog v-model="showAdd" @add="createPart" />
    <EditBikePartDialog v-model="showEdit" :initial="editInit" @save="saveEdit" />

    <p v-if="error" class="text-red-600 text-center">{{ error }}</p>
  </section>
</template>
