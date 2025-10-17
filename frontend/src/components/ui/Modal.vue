<script setup lang="ts">
defineProps<{ modelValue: boolean }>()
defineEmits<{ (e:'update:modelValue', v:boolean):void }>()
</script>
<template>
  <div v-if="modelValue" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="absolute inset-0 bg-black/40" @click="$emit('update:modelValue', false)" />
    <div class="relative w-[90%] max-w-[560px] card p-6">
      <header v-if="$slots.header" class="mb-4"><slot name="header" /></header>
      <section class="space-y-3"><slot /></section>
      <footer v-if="$slots.footer" class="mt-6 flex justify-end gap-2"><slot name="footer" /></footer>
    </div>
  </div>
</template>
