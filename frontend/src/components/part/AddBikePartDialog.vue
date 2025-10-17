<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import Modal from '../ui/Modal.vue'

const props = defineProps<{ modelValue:boolean }>()
const emit = defineEmits<{ (e:'update:modelValue', v:boolean):void; (e:'add', payload:any):void }>()
const open = ref(props.modelValue)
watch(() => props.modelValue, v => open.value = v)
watch(open, v => emit('update:modelValue', v))

const f = reactive({ name:'', brand:'', category:'', quantity:1, location:'', notes:'' })
function submit(){
  emit('add', {
    name: f.name,
    brand: f.brand || undefined,
    category: f.category || undefined,
    quantity: f.quantity ?? 1,
    location: f.location || undefined,
    notes: f.notes || undefined,
  })
  Object.assign(f, { name:'', brand:'', category:'', quantity:1, location:'', notes:'' })
  open.value = false
}
</script>

<template>
  <Modal v-model="open">
    <template #header>
      <div>
        <h3 class="text-lg font-semibold">Lisa osa</h3>
        <p class="text-sm text-neutral-500">Tärniga (*) märgitud väljad on kohustuslikud.</p>
      </div>
    </template>

    <form @submit.prevent="submit" class="grid gap-3">
      <div>
        <label class="text-sm">Nimi *</label>
        <input v-model="f.name" required class="input" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="text-sm">Bränd</label>
          <input v-model="f.brand" class="input" placeholder="Shimano" />
        </div>
        <div>
          <label class="text-sm">Kategooria</label>
          <input v-model="f.category" class="input" placeholder="Pidurid" />
        </div>
      </div>
      <div>
        <label class="text-sm">Kogus *</label>
        <input type="number" v-model.number="f.quantity" min="0" class="input" />
      </div>
      <div>
        <label class="text-sm">Asukoht</label>
        <input v-model="f.location" class="input" placeholder="Garaaž, riiul 2" />
      </div>
      <div>
        <label class="text-sm">Märkmed</label>
        <textarea v-model="f.notes" rows="3" class="input" />
      </div>

      <div class="flex justify-end gap-2 pt-2">
        <button type="button" class="btn btn-outline" @click="open=false">Tühista</button>
        <button type="submit" class="btn btn-primary">Lisa osa</button>
      </div>
    </form>
  </Modal>
</template>
