<script setup>
import { Button, Toolbar } from 'primevue'
import { useRouter } from 'vue-router'
import RoundButton from './RoundButton.vue'
import { useThemeMode } from '@/composables/useThemeMode'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { isDark, toggleTheme } = useThemeMode()
const { isAuthenticated, logout, user, isAdmin, setAuthData } = useAuth()

const handleLogout = async () => {
  await logout()
}

const goToProfile = () => {
  console.log('Navigating to profile, isAuthenticated:', isAuthenticated.value, 'user:', user.value)
  router.push({ name: 'profile-view' })
}

const goToAdmin = () => {
  router.push({ name: 'admin' })
}

// Тестовая функция для переключения роли
const toggleAdminRole = () => {
  if (!user.value) return

  const newRole = user.value.role === 'ADMIN' ? 'USER' : 'ADMIN'
  const updatedUser = {
    ...user.value,
    role: newRole,
  }

  // Обновляем данные пользователя
  const currentToken = localStorage.getItem('auth_token')
  setAuthData(currentToken, updatedUser)

  console.log(`🔄 Роль изменена на: ${newRole}`)
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
      <Button v-if="isAdmin" @click="goToAdmin" size="small" severity="info">
        <i class="pi pi-shield" />
        <span>Панель админа</span>
      </Button>
    </template>
    <template #end>
      <RoundButton :icon="isDark ? 'pi pi-moon' : 'pi pi-sun'" @click="toggleTheme" />
      <template v-if="isAuthenticated">
        <!-- Тестовая кнопка для переключения роли -->
        <Button
          @click="toggleAdminRole"
          :label="isAdmin ? '🔧 Тест: → Юзер' : '🔧 Тест: → Админ'"
          size="small"
          severity="help"
          outlined
        />
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
