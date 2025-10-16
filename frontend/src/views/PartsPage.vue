<template>
  <section>
    <h2>Bike Parts</h2>

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
        <input v-model="form.name" placeholder="name" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.brand" placeholder="brand" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.category" placeholder="category" style="width:100%; margin-bottom:6px;" />
        <input v-model.number="form.quantity" type="number" placeholder="quantity" style="width:100%; margin-bottom:6px;" />
        <input v-model="form.location" placeholder="location" style="width:100%; margin-bottom:6px;" />
        <textarea v-model="form.notes" placeholder="notes" style="width:100%; margin-bottom:6px;"></textarea>
        <button @click="createOne" :disabled="loading || !form.name">Create</button>
        <p v-if="createdId">Created: {{ createdId }}</p>
      </div>
    </div>

    <p v-if="error" style="color:red;">{{ error }}</p>
  </section>
</template>

<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'

const base = '/api/part' // your controller base
const loading = ref(false)
const error = ref('')
const all = ref<string | null>(null)
const one = ref<string | null>(null)
const getId = ref('')

const form = ref({
  name: '',
  brand: '',
  category: '',
  quantity: 1,
  location: '',
  notes: '',
})
const createdId = ref<string | null>(null)

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
    const payload = { ...form.value }
    const { data } = await axios.post(base, payload)
    createdId.value = data.id
    await loadAll()
  } catch (e: any) { error.value = e?.response?.data?.message ?? e?.message ?? 'Failed' }
  finally { loading.value = false }
}
</script>
