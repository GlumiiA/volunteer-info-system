<script setup>
import { Button, Toolbar } from 'primevue'
import { useRouter } from 'vue-router'
import RoundButton from './RoundButton.vue'
import { useThemeMode } from '@/composables/useThemeMode'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { isDark, toggleTheme } = useThemeMode()
const { isAuthenticated, logout, user } = useAuth()

const handleLogout = async () => {
  await logout()
}

const goToProfile = () => {
  console.log('Navigating to profile, isAuthenticated:', isAuthenticated.value, 'user:', user.value)
  router.push({ name: 'profile-view' })
}
</script>

<template>
  <Toolbar class="toolbar">
    <template #start>
      <Button @click="router.push({ name: 'home' })" label="Главная страница" size="small" />
      <Button @click="router.push({ name: 'search' })" label="Поиск заявок" size="small" />
      <Button
        @click="router.push({ name: 'leaderboard' })"
        label="Рейтинг волонтеров"
        size="small"
      />
    </template>
    <template #end>
      <RoundButton :icon="isDark ? 'pi pi-moon' : 'pi pi-sun'" @click="toggleTheme" />
      <template v-if="isAuthenticated">
        <Button @click="goToProfile" label="Профиль" size="small" />
        <Button @click="handleLogout" label="Выйти" size="small" severity="secondary" />
      </template>
      <template v-else>
        <Button @click="router.push({ name: 'auth' })" label="Авторизоваться" size="small" />
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
