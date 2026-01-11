<script setup>
import { Button, Toolbar } from 'primevue'
import router from '@/router'
import RoundButton from './RoundButton.vue'
import { useThemeMode } from '@/composables/useThemeMode'

const { isDark, toggleTheme } = useThemeMode()

const auth_state = true
</script>

<template>
  <Toolbar class="toolbar">
    <template #start>
      <Button @click="router.push({ name: 'home' })" label="Главная страница" />
      <Button @click="router.push({ name: 'search' })" label="Поиск заявок" />
      <Button @click="router.push({ name: 'leaderboard' })" label="Рейтинг волонтеров" />
    </template>
    <template #end>
      <RoundButton :icon="isDark ? 'pi pi-moon' : 'pi pi-sun'" @click="toggleTheme" />
      <template v-if="auth_state">
        <Button @click="router.push({ name: 'profile-view' })" label="Профиль" />
      </template>
      <template v-else>
        <Button @click="router.push({ name: 'auth' })" label="Авторизоваться" />
      </template>
    </template>
  </Toolbar>
</template>

<style scoped>
.p-toolbar .p-button {
  margin-left: 10px;
  margin-right: 10px;
}
</style>
