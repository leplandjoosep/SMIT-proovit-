<template>
  <section>
    <h2>Loans</h2>

    <div style="display:flex; gap:24px; flex-wrap:wrap; margin-bottom:16px;">
      <!-- List all -->
      <div style="border:1px solid #ddd; padding:12px; width:320px;">
        <h3>List all</h3>
        <button @click="loadAll" :disabled="loading">Load</button>
        <pre v-if="all">{{ all }}</pre>
      </div>

      <!-- Get by id -->
      <div style="border:1px solid #ddd; padding:12px; width:320px;">
        <h3>Get by ID</h3>
        <input v-model="getId" placeholder="UUID" style="width:100%; margin-bottom:8px;" />
        <button @click="loadOne" :disabled="loading || !getId">Get</button>
        <pre v-if="one">{{ one }}</pre>
      </div>

      <!-- Create new -->
      <div style="border:1px solid #ddd; padding:12px; width:320px;">
        <h3>Create</h3>
        <input v-model="form.bikePartId" placeholder="bikePartId (UUID)" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.borrowerName" placeholder="borrowerName" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.dueAt" type="datetime-local" placeholder="dueAt (optional)" style="width:100%; margin-bottom:6px;" />
        <button @click="createOne" :disabled="loading || !form.bikePartId || !form.borrowerName">Create</button>
        <p v-if="createdId">Created: {{ createdId }}</p>
      </div>
    </div>

    <p v-if="error" style="color:red;">{{ error }}</p>
  </section>
</template>

<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'

const base = '/api/loan'
const loading = ref(false)
const error = ref('')
const all = ref<string | null>(null)
const one = ref<string | null>(null)
const getId = ref('')

const form = ref({
  bikePartId: '',
  borrowerName: '',
  dueAt: '' as string | '',
})
const createdId = ref<string | null>(null)

function toIsoOrNull(dtLocal: string) {
  if (!dtLocal) return null
  // datetime-local → ISO: assume local timezone
  const d = new Date(dtLocal)
  return d.toISOString()
}

async function loadAll() {
  try {
    loading.value = true; error.value = ''
    const { data } = await axios.get(base)
    all.value = JSON.stringify(data, null, 2)
  } catch (e: any) { error.value = e?.message ?? 'Failed' }
  finally { loading.value = false }
}

async function loadOne() {
  try {
    loading.value = true; error.value = ''
    const { data } = await axios.get(`${base}/${getId.value}`)
    one.value = JSON.stringify(data, null, 2)
  } catch (e: any) { error.value = e?.response?.data?.message ?? e?.message ?? 'Failed' }
  finally { loading.value = false }
}

async function createOne() {
  try {
    loading.value = true; error.value = ''; createdId.value = null
    const payload: any = {
      bikePartId: form.value.bikePartId,
      borrowerName: form.value.borrowerName,
    }
    const iso = toIsoOrNull(form.value.dueAt)
    if (iso) payload.dueAt = iso

    const { data } = await axios.post(base, payload)
    createdId.value = data.id
    await loadAll()
  } catch (e: any) { error.value = e?.response?.data?.message ?? e?.message ?? 'Failed' }
  finally { loading.value = false }
}
</script>
