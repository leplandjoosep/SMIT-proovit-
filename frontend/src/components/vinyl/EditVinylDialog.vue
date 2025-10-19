<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import Modal from '../ui/Modal.vue'

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

const open = ref(props.modelValue)
watch(() => props.modelValue, v => open.value = v)
watch(open, v => emit('update:modelValue', v))

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

function close() { open.value = false }
function save()  { emit('save', { ...form }); close() }
</script>

<template>
  <Modal v-model="open">
    <template #header>
      <div>
        <h3 class="text-lg font-semibold">Muuda plaati</h3>
        <p class="text-sm text-neutral-500">Tärniga (*) märgitud väljad on kohustuslikud.</p>
      </div>
    </template>

    <form @submit.prevent="save" class="grid gap-3">
      <div>
        <label class="text-sm">Pealkiri *</label>
        <input v-model="form.title" required class="input" />
      </div>
      <div>
        <label class="text-sm">Artist *</label>
        <input v-model="form.artist" required class="input" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="text-sm">Väljaandmise aasta</label>
          <input type="number" v-model.number="form.releaseYear" class="input" placeholder="1982" />
        </div>
        <div>
          <label class="text-sm">Soetamise kuupäev</label>
          <input type="date" v-model="form.acquiredDate" class="input" />
        </div>
      </div>
      <div>
        <label class="text-sm">Kust soetatud</label>
        <input v-model="form.acquiredFrom" class="input" placeholder="Tartu Turg" />
      </div>
      <div>
        <label class="text-sm">Asukoht</label>
        <input v-model="form.location" class="input" placeholder="Riiul B, rida 1" />
      </div>
      <div>
        <label class="text-sm">Märkmed</label>
        <textarea v-model="form.notes" rows="3" class="input" placeholder="Lisainfo..." />
      </div>

      <div class="flex justify-end gap-2 pt-2">
        <button type="button" class="btn btn-outline" @click="close">Tühista</button>
        <button type="submit" class="btn btn-emerald">Salvesta</button>
      </div>
    </form>
  </Modal>
</template>
