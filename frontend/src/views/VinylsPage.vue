<template>
  <section>
    <h2>Vinyls</h2>

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
        <input v-model="form.title" placeholder="title" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.artist" placeholder="artist" style="width:100%; margin-bottom:6px;" />
        <input v-model.number="form.releaseYear" type="number" placeholder="releaseYear" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.acquiredFrom" placeholder="acquiredFrom" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.acquiredDate" type="date" placeholder="acquiredDate" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.location" placeholder="location" style="width:100%; margin-bottom:6px;" />
        <textarea v-model="form.notes" placeholder="notes" style="width:100%; margin-bottom:6px;"></textarea>
        <button @click="createOne" :disabled="loading || !form.title || !form.artist">Create</button>
        <p v-if="createdId">Created: {{ createdId }}</p>
      </div>
    </div>

    <p v-if="error" style="color:red;">{{ error }}</p>
  </section>
</template>

<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'

const base = '/api/vinyl'
const loading = ref(false)
const error = ref('')
const all = ref<string | null>(null)
const one = ref<string | null>(null)
const getId = ref('')

const form = ref({
  title: '',
  artist: '',
  releaseYear: undefined as number | undefined,
  acquiredFrom: '',
  acquiredDate: '' as string | '',
  location: '',
  notes: '',
})
const createdId = ref<string | null>(null)

async function loadAll() {
  try {
    loading.value = true; error.value = ''
    const { data } = await axios.get(base)
    all.value = JSON.stringify(data, null, 2)
  } catch (e: any) {
    error.value = e?.message ?? 'Failed'
  } finally {
    loading.value = false
  }
}

async function loadOne() {
  try {
    loading.value = true; error.value = ''
    const { data } = await axios.get(`${base}/${getId.value}`)
    one.value = JSON.stringify(data, null, 2)
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Failed'
  } finally {
    loading.value = false
  }
}

async function createOne() {
  try {
    loading.value = true; error.value = ''; createdId.value = null
    const payload: any = { ...form.value }
    if (!payload.acquiredDate) delete payload.acquiredDate
    if (!payload.releaseYear) delete payload.releaseYear
    const { data } = await axios.post(base, payload)
    createdId.value = data.id
    await loadAll()
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? e?.message ?? 'Failed'
  } finally {
    loading.value = false
  }
}
</script>
