<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import Modal from '../ui/Modal.vue'

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

const open = ref(props.modelValue)
watch(() => props.modelValue, v => open.value = v)
watch(open, v => emit('update:modelValue', v))

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

function close() { open.value = false }
function save()  { emit('save', { ...form }); close() }
</script>

<template>
  <Modal v-model="open">
    <template #header>
      <div>
        <h3 class="text-lg font-semibold">Muuda osa</h3>
        <p class="text-sm text-neutral-500">Tärniga (*) märgitud väljad on kohustuslikud.</p>
      </div>
    </template>

    <form @submit.prevent="save" class="grid gap-3">
      <div>
        <label class="text-sm">Nimi *</label>
        <input v-model="form.name" required class="input" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="text-sm">Bränd</label>
          <input v-model="form.brand" class="input" placeholder="Shimano" />
        </div>
        <div>
          <label class="text-sm">Kategooria</label>
          <input v-model="form.category" class="input" placeholder="Pidurid" />
        </div>
      </div>
      <div>
        <label class="text-sm">Kogus *</label>
        <input type="number" v-model.number="form.quantity" min="1" class="input" />
      </div>
      <div>
        <label class="text-sm">Asukoht</label>
        <input v-model="form.location" class="input" placeholder="Garaaž, riiul 2" />
      </div>
      <div>
        <label class="text-sm">Märkmed</label>
        <textarea v-model="form.notes" rows="3" class="input" placeholder="Lisainfo..." />
      </div>

      <div class="flex justify-end gap-2 pt-2">
        <button type="button" class="btn btn-outline" @click="close">Tühista</button>
        <button type="submit" class="btn btn-primary">Salvesta</button>
      </div>
    </form>
  </Modal>
</template>
