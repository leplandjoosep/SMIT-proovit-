<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import axios from 'axios'
import LoanDialog from './LoanDialog.vue'

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
  loans: Loan[]
}>()
const emit = defineEmits<{
  (e: 'edit', part: any):void
  (e: 'deleted', id: string):void
  (e: 'updated'): void
}>()

const loans = ref<Loan[]>([])
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

onMounted(loadLoans)
watch(() => props.part.id, loadLoans)

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
  <div class="card p-5">
    <div class="mb-2 flex items-start justify-between gap-2">
      <div>
        <h3 class="text-lg font-semibold">{{ part.name }}</h3>
        <p v-if="part.brand" class="text-sm text-indigo-600">{{ part.brand }}</p>
      </div>
      <div class="mb-2 flex items-start justify-between gap-2">
        <button class="text-neutral-400 hover:text-indigo-600 mr-2" @click="$emit('edit', part)" title="Muuda">
          ✏️
        </button>
        <button class="text-neutral-400 hover:text-red-600" @click="deletePart" title="Kustuta">
          🗑️
        </button>
      </div>
    </div>

    <div class="mb-3">
      <div v-if="part.category" class="pill">{{ part.category }}</div>
    </div>

    <div class="mb-3 text-sm text-neutral-700">
      <span class="mr-2">Kogus:</span>
      <span class="inline-flex items-center gap-2">
        <span class="px-2 py-1">{{ available }}</span>
        <span class="text-neutral-400">/</span>
        <span class="px-2 py-1">{{ part.quantity }}</span>
      </span>
    </div>

    <div v-if="part.location" class="mb-4 flex items-center gap-2 text-sm text-neutral-600">
      <span>📍</span>
      <span>{{ part.location }}</span>
    </div>

    <hr class="my-3" />

    <div class="mb-2 text-sm font-medium text-neutral-600">Laenutatud:</div>

    <div v-if="loadingLoans" class="text-sm text-neutral-500">Laenude laadimine…</div>
    <div v-if="!loans.length" class="text-neutral-400 text-sm">
      Pole laenutusi
    </div>

    <div
        v-for="l in loans"
        :key="l.id"
        class="flex items-center justify-between gap-3 py-2"
    >
      <div class="text-sm">
        <div class="font-medium">
          {{ l.borrowerName }}
        </div>
        <div class="text-neutral-500">
          <span>võetud: {{ fmt(l.borrowedAt) }}</span>
          <span v-if="l.dueAt">
            • tähtaeg:
            <span :class="{'text-red-600 font-semibold': isOverdue(l)}">
              {{ fmt(l.dueAt) }}
            </span>
          </span>
        </div>
      </div>

      <button
          class="btn btn-emerald"
          title="Tagasta"
          @click=returnLoan(l.id)
      >
        ⬇ Tagasta
      </button>
    </div>

    <div class="mt-4">
      <button class="btn btn-outline w-full" :disabled="available === 0" @click="dialogOpen = true">
        <span>👤</span>
        Laenuta välja
      </button>
    </div>

    <LoanDialog :open="dialogOpen" :part-id="part.id"
                @close="dialogOpen = false"
                @created="loadLoans" />
  </div>
</template>
