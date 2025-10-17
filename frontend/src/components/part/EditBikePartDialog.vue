<script setup lang="ts">
import { reactive, watch } from 'vue'

export type PartForm = {
  id: string
  name: string
  brand?: string | null
  category?: string | null
  quantity?: number | null
  location?: string | null
  notes?: string | null
}

const props = defineProps<{
  modelValue: boolean
  initial: PartForm | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
  (e: 'save', payload: PartForm): void
}>()

const form = reactive<PartForm>({
  id: '',
  name: '',
  brand: null,
  category: null,
  quantity: 1,
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
      <h3 class="text-lg font-semibold">Muuda osa</h3>

      <div class="grid gap-3">
        <input class="input" placeholder="Nimi" v-model="form.name" />
        <input class="input" placeholder="Bränd" v-model="form.brand" />
        <input class="input" placeholder="Kategooria" v-model="form.category" />
        <input class="input" type="number" min="1" placeholder="Kogus" v-model.number="form.quantity" />
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
