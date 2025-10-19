<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import axios from 'axios'
import LoanDialog from './LoanDialog.vue'
import Icon from '../ui/Icons.vue'

type Loan = {
  id: string
  borrowerName: string
  borrowedAt: string
  dueAt?: string | null
  returnedAt?: string | null
}

type BikePart = {
  id: string
  name: string
  brand?: string | null
  category?: string | null
  quantity: number
  location?: string | null
  notes?: string | null
}

const props = defineProps<{
  part: BikePart
  loans?: Loan[]
}>()
const emit = defineEmits<{
  (e: 'edit', part: any):void
  (e: 'deleted', id: string):void
  (e: 'updated'): void
}>()

const loans = ref<Loan[]>(props.loans || [])
const loadingLoans = ref(false)
const dialogOpen = ref(false)

const available = computed(() => {
  const active = loans.value.length
  return Math.max(props.part.quantity - active, 0)
})

async function loadLoans() {
  loadingLoans.value = true
  try {
    const { data } = await axios.get(`/api/loan/part/${props.part.id}?activeOnly=true`)
    loans.value = data
  } finally {
    loadingLoans.value = false
  }
}

async function returnLoan(id: string) {
  await axios.post(`/api/loan/${id}/return`)
  await loadLoans()
  emit('updated')
}

onMounted(() => {
  if (!props.loans) {
    loadLoans()
  }
})
watch(() => props.part.id, () => {
  if (!props.loans) {
    loadLoans()
  }
})

async function deletePart() {
  if (!confirm(`Kustuta osa "${props.part.name}"?`)) return
  await axios.delete(`/api/part/${props.part.id}`)
  emit('deleted', props.part.id)
}

function fmt(dt?: string | null, opts?: Intl.DateTimeFormatOptions) {
  if (!dt) return ''
  return new Date(dt).toLocaleString('et-EE', opts ?? { dateStyle: 'medium', timeStyle: 'short' })
}

function isOverdue(l: Loan) {
  const due = l.dueAt
  const ret = l.returnedAt
  return !!due && !ret && new Date(due) < new Date()
}

</script>

<template>
  <div class="group relative overflow-hidden rounded-2xl bg-gradient-to-br from-sky-50 to-blue-50 border border-sky-200/60 p-6 hover:shadow-xl hover:scale-[1.02] transition-all duration-300">
    <div class="relative z-10">
      <div class="flex items-start justify-between mb-4">
        <div class="flex-1 min-w-0">
          <h3 class="text-lg font-bold text-gray-900 mb-1 line-clamp-2">{{ part.name }}</h3>
          <p v-if="part.brand" class="text-sm font-medium text-sky-600">{{ part.brand }}</p>
        </div>
        <div class="flex items-center gap-1 ml-3">
          <button class="p-2 text-gray-400 hover:text-sky-600 hover:bg-sky-100 rounded-lg transition-colors" @click="$emit('edit', part)" title="Muuda">
            <Icon name="edit" class="w-4 h-4" />
          </button>
          <button class="p-2 text-gray-400 hover:text-red-600 hover:bg-red-100 rounded-lg transition-colors" @click="deletePart" title="Kustuta">
            <Icon name="delete" class="w-4 h-4" />
          </button>
        </div>
      </div>

      <div class="flex items-center justify-between mb-4">
        <div v-if="part.category" class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-sky-100 text-sky-700 border border-sky-200">
          {{ part.category }}
        </div>
        <div class="flex items-center gap-2 text-sm font-medium">
          <span class="text-gray-600">Kogus:</span>
          <div class="flex items-center gap-1 bg-white/70 rounded-lg px-3 py-1 border border-sky-200">
            <span class="text-sky-600 font-bold">{{ available }}</span>
            <span class="text-gray-400">/</span>
            <span class="text-gray-600">{{ part.quantity }}</span>
          </div>
        </div>
      </div>

      <div v-if="part.location" class="mb-4 flex items-center gap-2 text-sm text-gray-600 bg-white/50 rounded-lg p-2 border border-sky-100">
        <Icon name="location" class="w-4 h-4 text-sky-500" />
        <span>{{ part.location }}</span>
      </div>

      <p v-if="part.notes" class="mb-4 text-sm text-gray-500 italic bg-white/50 rounded-lg p-2 border border-sky-100">
        {{ part.notes }}
      </p>

      <div class="border-t border-sky-200/60 pt-4">
        <div class="mb-3 text-sm font-semibold text-gray-700 flex items-center gap-2">
          <Icon name="user" class="w-4 h-4 text-sky-500" />
          Laenutatud
        </div>

        <div v-if="loadingLoans" class="text-sm text-gray-500 text-center py-2">Laenude laadimine…</div>
        <div v-if="!loans.length" class="text-gray-400 text-sm text-center py-2 bg-white/50 rounded-lg border border-sky-100">
          Pole laenutusi
        </div>

        <div v-for="l in loans" :key="l.id" class="mb-3 p-3 bg-white/70 rounded-lg border border-sky-100">
          <div class="flex items-center justify-between gap-3">
            <div class="flex-1 min-w-0">
              <div class="font-medium text-gray-900 text-sm mb-1">
                {{ l.borrowerName }}
              </div>
              <div class="text-xs text-gray-500 space-y-1">
                <div>võetud: {{ fmt(l.borrowedAt) }}</div>
                <div v-if="l.dueAt" class="flex items-center gap-1">
                  <span>tähtaeg:</span>
                  <span :class="{'text-red-600 font-semibold': isOverdue(l)}">
                    {{ fmt(l.dueAt) }}
                  </span>
                </div>
              </div>
            </div>
            <button
                class="btn btn-emerald text-xs px-3 py-1.5 flex items-center gap-1"
                title="Tagasta"
                @click="returnLoan(l.id)"
            >
              <Icon name="arrow-down" class="w-3 h-3" />
              Tagasta
            </button>
          </div>
        </div>

        <button
          class="w-full btn btn-outline text-sm py-2.5 flex items-center justify-center gap-2" 
          :disabled="available === 0" 
          @click="dialogOpen = true"
          :class="available === 0 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-sky-50'"
        >
          <Icon name="user" class="w-4 h-4" />
          Laenuta välja
        </button>
      </div>
    </div>

    <LoanDialog :open="dialogOpen" :part-id="part.id"
                @close="dialogOpen = false"
                @created="loadLoans" />
  </div>
</template>
