<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const props = defineProps<{
  open: boolean
  partId: string
}>()
const emit = defineEmits<{
  (e: 'close'): void
  (e: 'created'): void
}>()

const borrower = ref('')
const dueAt = ref<string | null>(null)
const loading = ref(false)
const error = ref('')

async function submit() {
  error.value = ''
  if (!borrower.value.trim()) {
    error.value = 'Laenaja nimi on kohustuslik'
    return
  }
  try {
    loading.value = true
    await axios.post('/api/loan', {
      bikePartId: props.partId,
      borrowerName: borrower.value,
      dueAt: dueAt.value ? new Date(dueAt.value).toISOString() : null
    })
    emit('created')
    emit('close')
    borrower.value = ''
    dueAt.value = null
  } catch (e: any) {
    error.value = e?.response?.data?.message ?? 'Viga: laenu ei saanud luua'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div v-if="open" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
    <div class="w-full max-w-md rounded-2xl bg-white p-6 shadow-xl">
      <h3 class="mb-4 text-lg font-semibold">Laenuta osa</h3>

      <div class="space-y-3">
        <label class="block text-sm">Laenaja nimi</label>
        <input class="input" v-model="borrower" placeholder="Nimi" />

        <label class="block text-sm">Tähtaeg (valikuline)</label>
        <input class="input" type="datetime-local" v-model="dueAt" />
      </div>

      <p v-if="error" class="mt-3 text-sm text-red-600">{{ error }}</p>

      <div class="mt-6 flex justify-end gap-2">
        <button class="btn btn-outline" @click="$emit('close')" :disabled="loading">Loobu</button>
        <button class="btn btn-primary" @click="submit" :disabled="loading">
          {{ loading ? 'Salvestan…' : 'Kinnita' }}
        </button>
      </div>
    </div>
  </div>
</template>
