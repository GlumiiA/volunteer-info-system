/**
 * API сервис для авторизации
 * Базовый URL: http://localhost:8080/api/v1
 */

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

/**
 * Регистрация нового пользователя
 * POST /auth/register
 * @param {Object} data - Данные регистрации
 * @param {string} data.email - Email пользователя
 * @param {string} data.password - Пароль (минимум 8 символов)
 * @param {string} data.fullName - Полное имя пользователя
 * @returns {Promise<{token: string, user: Object}>}
 */
export async function register(data) {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      let errorMessage = 'Ошибка регистрации'
      try {
        const error = await response.json()
        errorMessage = error.message || error.detail || errorMessage
      } catch (e) {
        // If response is not JSON, use status text
        errorMessage = response.statusText || errorMessage
      }
      throw new Error(errorMessage)
    }

    return response.json()
  } catch (error) {
    // Handle network errors
    if (error instanceof TypeError && error.message.includes('fetch')) {
      throw new Error('Не удалось подключиться к серверу. Убедитесь, что сервер запущен на http://localhost:8080')
    }
    throw error
  }
}

/**
 * Вход пользователя
 * POST /auth/login
 * @param {Object} data - Данные для входа
 * @param {string} data.email - Email пользователя
 * @param {string} data.password - Пароль
 * @returns {Promise<{token: string, user: Object}>}
 */
export async function login(data) {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      let errorMessage = 'Ошибка входа'
      try {
        const error = await response.json()
        errorMessage = error.message || error.detail || errorMessage
      } catch (e) {
        // If response is not JSON, use status text
        errorMessage = response.statusText || errorMessage
      }
      throw new Error(errorMessage)
    }

    return response.json()
  } catch (error) {
    // Handle network errors
    if (error instanceof TypeError && error.message.includes('fetch')) {
      throw new Error('Не удалось подключиться к серверу. Убедитесь, что сервер запущен на http://localhost:8080')
    }
    throw error
  }
}

/**
 * Выход пользователя
 * POST /auth/logout
 * @param {string} token - JWT токен
 * @returns {Promise<void>}
 */
export async function logout(token) {
  const response = await fetch(`${API_BASE_URL}/auth/logout`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })

  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Ошибка выхода')
  }
}

/**
 * Получить URL для OAuth2 авторизации
 * GET /auth/oauth2/{provider}/authorize
 * @param {string} provider - Провайдер (vk, yandex)
 * @param {string} redirectUri - URL для возврата после авторизации
 * @returns {string} URL для перенаправления
 */
export function getOAuthUrl(provider, redirectUri) {
  const params = new URLSearchParams({
    redirect_uri: redirectUri,
  })
  return `${API_BASE_URL}/auth/oauth2/${provider}/authorize?${params.toString()}`
}

/**
 * Обработать OAuth2 callback
 * GET /auth/oauth2/{provider}/callback
 * @param {string} provider - Провайдер (vk, yandex)
 * @param {string} code - Код авторизации
 * @param {string} state - Состояние для CSRF защиты
 * @returns {Promise<{token: string, user: Object}>}
 */
export async function handleOAuthCallback(provider, code, state) {
  const params = new URLSearchParams({ code, state })
  const response = await fetch(
    `${API_BASE_URL}/auth/oauth2/${provider}/callback?${params.toString()}`,
    {
      method: 'GET',
    }
  )

  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Ошибка OAuth авторизации')
  }

  return response.json()
}

/**
 * Получить текущего пользователя
 * GET /users/me
 * @param {string} token - JWT токен
 * @returns {Promise<Object>} Данные пользователя
 */
export async function getCurrentUser(token) {
  const response = await fetch(`${API_BASE_URL}/users/me`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })

  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Ошибка получения данных пользователя')
  }

  return response.json()
}
