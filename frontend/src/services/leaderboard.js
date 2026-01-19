/**
 * Сервис для работы с лидербордом
 * API: GET /leaderboard
 */

const API_BASE_URL = 'http://localhost:8080/api/v1'

/**
 * Получить список лидеров
 * @param {number} limit - Максимальное количество записей
 * @param {string} token - JWT токен (опционально для авторизованных пользователей)
 * @returns {Promise<import('@/types/leaderboard').LeaderboardResponse>}
 */
export async function getLeaderboard(limit = 50, token = null) {
  const url = new URL(`${API_BASE_URL}/leaderboard`)
  if (limit) {
    url.searchParams.append('limit', limit)
  }

  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  }

  // Добавляем токен если есть
  if (token) {
    options.headers['Authorization'] = `Bearer ${token}`
  }

  const response = await fetch(url.toString(), options)

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`)
  }

  return await response.json()
}
