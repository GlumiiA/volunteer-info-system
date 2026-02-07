import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as authService from '@/services/auth'

const TOKEN_KEY = 'auth_token'
const USER_KEY = 'auth_user'

// Глобальное состояние
const token = ref(localStorage.getItem(TOKEN_KEY))
const user = ref(JSON.parse(localStorage.getItem(USER_KEY) || 'null'))

/**
 * Composable для управления авторизацией
 */
export function useAuth() {
  const router = useRouter()

  // Проверка авторизации
  const isAuthenticated = computed(() => !!token.value)

  /**
   * Регистрация пользователя
   */
  const register = async (data) => {
    try {
      const response = await authService.register(data)
      setAuthData(response.token, response.user)
      return response
    } catch (error) {
      throw error
    }
  }

  /**
   * Вход пользователя
   */
  const login = async (data) => {
    try {
      const response = await authService.login(data)
      setAuthData(response.token, response.user)
      return response
    } catch (error) {
      throw error
    }
  }

  /**
   * Выход пользователя
   */
  const logout = async () => {
    try {
      if (token.value) {
        await authService.logout(token.value)
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      clearAuthData()
      router.push({ name: 'auth' })
    }
  }

  /**
   * Обновить данные текущего пользователя
   */
  const refreshUser = async () => {
    if (!token.value) {
      return null
    }

    try {
      const userData = await authService.getCurrentUser(token.value)
      user.value = userData
      localStorage.setItem(USER_KEY, JSON.stringify(userData))
      return userData
    } catch (error) {
      console.error('Refresh user error:', error)
      clearAuthData()
      return null
    }
  }

  /**
   * Сохранить данные авторизации
   */
  const setAuthData = (newToken, newUser) => {
    token.value = newToken
    user.value = newUser
    localStorage.setItem(TOKEN_KEY, newToken)
    localStorage.setItem(USER_KEY, JSON.stringify(newUser))
  }

  /**
   * Очистить данные авторизации
   */
  const clearAuthData = () => {
    token.value = null
    user.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  /**
   * Проверить роль пользователя
   */
  const hasRole = (role) => {
    return user.value?.role === role
  }

  /**
   * Проверить, является ли пользователь администратором
   */
  const isAdmin = computed(() => hasRole('ADMIN'))

  /**
   * Проверить, является ли пользователь представителем организации
   */
  const isOrgRepresentative = computed(() => hasRole('ORG_REPRESENTATIVE'))

  /**
   * Проверить, является ли пользователь обычным пользователем
   */
  const isUser = computed(() => hasRole('USER'))

  return {
    // Состояние
    token,
    user,
    isAuthenticated,
    isAdmin,
    isOrgRepresentative,
    isUser,

    // Методы
    register,
    login,
    logout,
    refreshUser,
    setAuthData,
    clearAuthData,
    hasRole,
  }
}
