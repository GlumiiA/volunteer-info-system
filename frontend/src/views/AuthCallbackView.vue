<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ProgressSpinner } from 'primevue'
import { useAuth } from '@/composables/useAuth'
import * as authService from '@/services/auth'

const router = useRouter()
const route = useRoute()
const error = ref(null)
const { setAuthData } = useAuth()

onMounted(async () => {
  try {
    // Check if token is in URL (from backend redirect after successful OAuth)
    const urlParams = new URLSearchParams(window.location.search)
    const token = urlParams.get('token')
    
    if (token) {
      // Backend already handled the OAuth and redirected with token
      // Fetch user data
      try {
        const userData = await authService.getCurrentUser(token)
        setAuthData(token, userData)
        router.push('/')
        return
      } catch (err) {
        console.error('Failed to fetch user data:', err)
        throw new Error('Не удалось получить данные пользователя')
      }
    }

    // If no token, OAuth provider redirected back with code
    const { code, state } = route.query
    
    if (!code) {
      throw new Error('Отсутствует код авторизации')
    }

    // Try to detect provider from referrer or stored state
    // For now, we'll need to pass provider in state or detect from URL
    // Check if we can get provider from state or sessionStorage
    let provider = sessionStorage.getItem('oauth_provider')
    
    if (!provider) {
      // Try to detect from URL or default to yandex
      // In a real implementation, store provider when initiating OAuth
      provider = 'yandex' // Default fallback
    }

    // Make API call to handle callback
    const response = await authService.handleOAuthCallback(provider, code, state || '')
    setAuthData(response.token, response.user)
    sessionStorage.removeItem('oauth_provider')
    router.push('/')
  } catch (err) {
    console.error('OAuth callback error:', err)
    error.value = err.message || 'Ошибка авторизации'
    sessionStorage.removeItem('oauth_provider')
    
    // Перенаправить на страницу авторизации через 3 секунды
    setTimeout(() => {
      router.push({ name: 'auth' })
    }, 3000)
  }
})
</script>

<template>
  <div class="callback-view">
    <div class="callback-container">
      <template v-if="!error">
        <ProgressSpinner />
        <h2>Завершение авторизации...</h2>
        <p>Пожалуйста, подождите</p>
      </template>
      <template v-else>
        <i class="pi pi-times-circle error-icon"></i>
        <h2>Ошибка авторизации</h2>
        <p>{{ error }}</p>
        <p class="redirect-text">Перенаправление на страницу входа...</p>
      </template>
    </div>
  </div>
</template>

<style scoped>
.callback-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: var(--space-m);
}

.callback-container {
  text-align: center;
  max-width: 500px;
}

.callback-container h2 {
  margin-top: var(--space-l);
  color: var(--text-color);
}

.callback-container p {
  color: var(--text-color-secondary);
  margin-top: var(--space-s);
}

.error-icon {
  font-size: 4rem;
  color: var(--red-500);
}

.redirect-text {
  font-size: 0.875rem;
  font-style: italic;
  margin-top: var(--space-m);
}
</style>
