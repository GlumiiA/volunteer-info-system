<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ProgressSpinner } from 'primevue'

const router = useRouter()
const route = useRoute()
const error = ref(null)

onMounted(async () => {
  try {
    const { code, state, provider } = route.query

    if (!code) {
      throw new Error('Отсутствует код авторизации')
    }

    // TODO: Реализовать запрос к API GET /api/v1/auth/oauth2/{provider}/callback
    console.log('OAuth callback:', { code, state, provider })

    // Заглушка: имитация обработки callback
    await new Promise((resolve) => setTimeout(resolve, 1500))

    // После успешной авторизации сохранить токен и перенаправить
    // localStorage.setItem('token', response.token)
    
    router.push('/')
  } catch (err) {
    console.error('OAuth callback error:', err)
    error.value = err.message || 'Ошибка авторизации'
    
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
