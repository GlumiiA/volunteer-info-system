<script setup>
import { ref } from 'vue'
import { Button, Card, InputText, Password, Divider } from 'primevue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const toast = useToast()
const { login: authLogin, register: authRegister } = useAuth()

const isLogin = ref(true)

// Поля для логина
const loginData = ref({
  email: '',
  password: '',
})

// Поля для регистрации
const registerData = ref({
  email: '',
  password: '',
  fullName: '',
})

const loading = ref(false)


// Переключение между формами
const toggleForm = () => {
  isLogin.value = !isLogin.value
  // Очистка полей при переключении
  loginData.value = { email: '', password: '' }
  registerData.value = { email: '', password: '', fullName: '' }
}

// Обработка логина
const handleLogin = async () => {
  loading.value = true
  try {
    await authLogin(loginData.value)
    
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Вы успешно вошли в систему',
      life: 3000,
    })
    
    router.push('/')
  } catch (error) {
    console.error('Login error:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка входа',
      detail: error.message || 'Неверный email или пароль',
      life: 5000,
    })
  } finally {
    loading.value = false
  }
}

// Обработка регистрации
const handleRegister = async () => {
  loading.value = true
  try {
    await authRegister(registerData.value)
    
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Регистрация прошла успешно',
      life: 3000,
    })
    
    router.push('/')
  } catch (error) {
    console.error('Register error:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка регистрации',
      detail: error.message || 'Не удалось зарегистрироваться',
      life: 5000,
    })
  } finally {
    loading.value = false
  }
}

// OAuth2 авторизация
const handleOAuth = (provider) => {
  const redirectUri = `${window.location.origin}/auth/callback`
  // Store provider in sessionStorage for callback
  sessionStorage.setItem('oauth_provider', provider)
  window.location.href = `http://localhost:8080/api/v1/auth/oauth2/${provider}/authorize?redirect_uri=${encodeURIComponent(redirectUri)}`
}
</script>

<template>
  <div class="auth-view">
    <div class="auth-container">
      <Card class="auth-card">
        <template #title>
          <h2 class="auth-title">{{ isLogin ? 'Вход' : 'Регистрация' }}</h2>
        </template>

        <template #content>
          <!-- Форма логина -->
          <form v-if="isLogin" @submit.prevent="handleLogin" class="auth-form">
            <div class="field">
              <label for="login-email">Email</label>
              <InputText
                id="login-email"
                v-model="loginData.email"
                type="email"
                placeholder="example@mail.com"
                required
                class="w-full"
              />
            </div>

            <div class="field">
              <label for="login-password">Пароль</label>
              <Password
                id="login-password"
                v-model="loginData.password"
                placeholder="Введите пароль"
                :feedback="false"
                toggleMask
                required
                class="w-full"
              />
            </div>

            <Button
              type="submit"
              label="Войти"
              :loading="loading"
              class="w-full"
              severity="primary"
            />
          </form>

          <!-- Форма регистрации -->
          <form v-else @submit.prevent="handleRegister" class="auth-form">
            <div class="field">
              <label for="register-fullname">Полное имя</label>
              <InputText
                id="register-fullname"
                v-model="registerData.fullName"
                placeholder="Иван Иванов"
                required
                class="w-full"
              />
            </div>

            <div class="field">
              <label for="register-email">Email</label>
              <InputText
                id="register-email"
                v-model="registerData.email"
                type="email"
                placeholder="example@mail.com"
                required
                class="w-full"
              />
            </div>

            <div class="field">
              <label for="register-password">Пароль</label>
              <Password
                id="register-password"
                v-model="registerData.password"
                placeholder="Минимум 8 символов"
                toggleMask
                required
                class="w-full"
              >
                <template #footer>
                  <p class="password-hint">Минимум 8 символов</p>
                </template>
              </Password>
            </div>

            <Button
              type="submit"
              label="Зарегистрироваться"
              :loading="loading"
              class="w-full"
              severity="primary"
            />
          </form>

          <!-- Разделитель -->
          <Divider>или</Divider>

          <!-- OAuth кнопки -->
          <div class="oauth-buttons">
            <Button
              @click="handleOAuth('yandex')"
              class="w-full oauth-button"
              severity="secondary"
              outlined
            >
              <i class="pi pi-yandex" />
              <span>Войти через Яндекс</span>
            </Button>
          </div>

          <!-- Переключение между формами -->
          <div class="toggle-form">
            <p v-if="isLogin">
              Нет аккаунта?
              <a @click.prevent="toggleForm" href="#" class="toggle-link">
                Зарегистрироваться
              </a>
            </p>
            <p v-else>
              Уже есть аккаунт?
              <a @click.prevent="toggleForm" href="#" class="toggle-link">Войти</a>
            </p>
          </div>
        </template>
      </Card>
    </div>
  </div>
</template>

<style scoped>
.auth-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: var(--space-m);
  background: linear-gradient(135deg, var(--primary-50) 0%, var(--primary-100) 100%);
}

.auth-container {
  width: 100%;
  max-width: 450px;
}

.auth-card {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.auth-title {
  text-align: center;
  margin: 0;
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text-color);
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-m);
  margin-bottom: var(--space-m);
}


.field {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.field label {
  font-weight: 600;
  color: var(--text-color);
  font-size: 0.95rem;
}

.w-full {
  width: 100%;
}

.oauth-buttons {
  display: flex;
  flex-direction: column;
  gap: var(--space-s);
  margin-top: var(--space-m);
}

.oauth-button {
  justify-content: center;
}

.toggle-form {
  text-align: center;
  margin-top: var(--space-l);
  padding-top: var(--space-m);
  border-top: 1px solid var(--surface-border);
}

.toggle-form p {
  margin: 0;
  color: var(--text-color-secondary);
}

.toggle-link {
  color: var(--primary-color);
  font-weight: 600;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.2s;
}

.toggle-link:hover {
  color: var(--primary-color);
  text-decoration: underline;
}

.password-hint {
  margin: 0;
  font-size: 0.875rem;
  color: var(--text-color-secondary);
}

:deep(.p-password) {
  width: 100%;
}

:deep(.p-password input) {
  width: 100%;
}
</style>
