<script setup lang="ts">
import { reactive, watch } from 'vue'

export type VinylForm = {
  id: string
  title: string
  artist: string
  releaseYear?: number | null
  acquiredFrom?: string | null
  acquiredDate?: string | null
  location?: string | null
  notes?: string | null
}

const props = defineProps<{
  modelValue: boolean
  initial: VinylForm | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
  (e: 'save', payload: VinylForm): void
}>()

const form = reactive<VinylForm>({
  id: '',
  title: '',
  artist: '',
  releaseYear: null,
  acquiredFrom: null,
  acquiredDate: null,
  location: null,
  notes: null,
})

watch(
    () => props.initial,
    v => { if (v) Object.assign(form, JSON.parse(JSON.stringify(v))) },
    { immediate: true },
)

function close() { emit('update:modelValue', false) }
function save()  { emit('save', { ...form }); close() }
</script>

<template>
  <div v-if="modelValue" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="absolute inset-0 bg-black/30" @click="close"></div>

    <div class="relative z-10 w-full max-w-lg rounded-2xl bg-white shadow-xl p-5 space-y-3">
      <h3 class="text-lg font-semibold">Muuda plaati</h3>

      <div class="grid gap-3">
        <input class="input" placeholder="Pealkiri" v-model="form.title" />
        <input class="input" placeholder="Artist" v-model="form.artist" />
        <input class="input" type="number" placeholder="Aasta" v-model.number="form.releaseYear" />
        <input class="input" placeholder="Päritolu" v-model="form.acquiredFrom" />
        <input class="input" placeholder="Asukoht" v-model="form.location" />
        <textarea class="input" rows="3" placeholder="Märkmed" v-model="form.notes" data-gramm="false" />
      </div>

      <div class="flex justify-end gap-2 pt-2">
        <button class="btn" @click="close">Tühista</button>
        <button class="btn btn-primary" @click="save">Salvesta</button>
      </div>
    </div>
  </div>
</template>
