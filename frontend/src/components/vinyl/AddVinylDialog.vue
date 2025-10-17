<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import Modal from '../ui/Modal.vue'

const props = defineProps<{ modelValue:boolean }>()
const emit = defineEmits<{ (e:'update:modelValue', v:boolean):void; (e:'add', payload:any):void }>()
const open = ref(props.modelValue)
watch(() => props.modelValue, v => open.value = v)
watch(open, v => emit('update:modelValue', v))

const f = reactive({ title:'', artist:'', releaseYear: undefined as number|undefined, acquiredFrom:'', acquiredDate:'', location:'', notes:'' })
function submit(){
  emit('add', {
    title: f.title, artist: f.artist,
    releaseYear: f.releaseYear || undefined,
    acquiredFrom: f.acquiredFrom || undefined,
    acquiredDate: f.acquiredDate || undefined,
    location: f.location || undefined,
    notes: f.notes || undefined
  })
  Object.assign(f, { title:'', artist:'', releaseYear: undefined, acquiredFrom:'', acquiredDate:'', location:'', notes:'' })
  open.value = false
}
</script>

<template>
  <Modal v-model="open">
    <template #header>
      <div>
        <h3 class="text-lg font-semibold">Lisa plaat</h3>
        <p class="text-sm text-neutral-500">Tärniga (*) märgitud väljad on kohustuslikud.</p>
      </div>
    </template>

    <form @submit.prevent="submit" class="grid gap-3">
      <div>
        <label class="text-sm">Pealkiri *</label>
        <input v-model="f.title" required class="input" />
      </div>
      <div>
        <label class="text-sm">Artist *</label>
        <input v-model="f.artist" required class="input" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="text-sm">Väljaandmise aasta</label>
          <input type="number" v-model.number="f.releaseYear" class="input" placeholder="1982" />
        </div>
        <div>
          <label class="text-sm">Soetamise kuupäev</label>
          <input type="date" v-model="f.acquiredDate" class="input" />
        </div>
      </div>
      <div>
        <label class="text-sm">Kust soetatud</label>
        <input v-model="f.acquiredFrom" class="input" placeholder="Tartu Turg" />
      </div>
      <div>
        <label class="text-sm">Asukoht</label>
        <input v-model="f.location" class="input" placeholder="Riiul B, rida 1" />
      </div>
      <div>
        <label class="text-sm">Märkmed</label>
        <textarea v-model="f.notes" rows="3" class="input" placeholder="Lisainfo..." />
      </div>

      <div class="flex justify-end gap-2 pt-2">
        <button type="button" class="btn btn-outline" @click="open=false">Tühista</button>
        <button type="submit" class="btn btn-emerald">Lisa plaat</button>
      </div>
    </form>
  </Modal>
</template>
