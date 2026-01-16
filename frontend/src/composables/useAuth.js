import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as authService from '@/services/auth'

const TOKEN_KEY = 'auth_token'
const USER_KEY = 'auth_user'

// Глобальное состояние
const token = ref(localStorage.getItem(TOKEN_KEY))
const user = ref(JSON.parse(localStorage.getItem(USER_KEY) || 'null'))

// Тестовые учетные данные для заглушки
const MOCK_CREDENTIALS = {
  email: 'test@example.com',
  password: 'test123456'
}

// Мок-данные пользователя
const MOCK_USER = {
  id: 1,
  email: 'test@example.com',
  name: 'Тестовый Пользователь',
  role: 'USER',
  description: 'Это тестовая учетная запись для демонстрации функционала',
  birthday: new Date('1995-05-15'),
  location: 'Санкт-Петербург',
  volunteerHours: 42.5,
  rating: 4.8,
  organisationId: null,
  organizationName: null
}

// Мок-данные представителя организации для тестирования
const MOCK_ORG_USER = {
  id: 2,
  email: 'org@example.com',
  name: 'Представитель Организации',
  role: 'ORG_REPRESENTATIVE',
  description: 'Я представляю волонтерскую организацию',
  birthday: new Date('1988-03-20'),
  location: 'Санкт-Петербург',
  volunteerHours: 120,
  rating: 4.9,
  organisationId: 1,
  organizationName: 'Красный Крест'
}

const MOCK_TOKEN = 'mock_jwt_token_12345'

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
      // ЗАГЛУШКА: имитация регистрации
      console.log('🔧 Mock registration:', data)
      await new Promise(resolve => setTimeout(resolve, 800))

      const mockUser = {
        ...MOCK_USER,
        email: data.email,
        name: data.fullName
      }

      setAuthData(MOCK_TOKEN, mockUser)
      return { token: MOCK_TOKEN, user: mockUser }

      // Для реального API раскомментируйте:
      // const response = await authService.register(data)
      // setAuthData(response.token, response.user)
      // return response
    } catch (error) {
      throw error
    }
  }

  /**
   * Вход пользователя
   */
  const login = async (data) => {
    try {
      // ЗАГЛУШКА: проверка тестовых учетных данных
      console.log('🔧 Mock login attempt:', data.email)
      await new Promise(resolve => setTimeout(resolve, 800))

      if (data.email === MOCK_CREDENTIALS.email && data.password === MOCK_CREDENTIALS.password) {
        setAuthData(MOCK_TOKEN, MOCK_USER)
        console.log('✅ Mock login successful')
        return { token: MOCK_TOKEN, user: MOCK_USER }
      } else {
        throw new Error('Неверный email или пароль. Используйте test@example.com / test123456')
      }

      // Для реального API раскомментируйте:
      // const response = await authService.login(data)
      // setAuthData(response.token, response.user)
      // return response
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
