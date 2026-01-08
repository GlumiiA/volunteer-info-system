<script setup>
import { Button } from 'primevue'
import { onMounted, ref } from 'vue'

const isThemeDark = ref(false)

const toggleTheme = () => {
  isThemeDark.value = !isThemeDark.value
  document.documentElement.classList.toggle('my-app-dark')
}

onMounted(() => {
  const isSystemThemeDark =
    globalThis.matchMedia && globalThis.matchMedia('(prefers-color-scheme: dark)').matches
  if (isSystemThemeDark) {
    document.documentElement.classList.add('my-app-dark')
    isThemeDark.value = true
  }
})
</script>

<template>
  <Button class="round-button" rounded @click="toggleTheme">
    <i :class="isThemeDark ? 'pi pi-moon' : 'pi pi-sun'" />
  </Button>
</template>

<style scoped>
.round-button {
  width: var(--space-xl);
  height: var(--space-xl);
}
</style>
