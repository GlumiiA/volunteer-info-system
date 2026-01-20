const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

/**
 * Получение списка всех организаций
 * GET /organizations
 */
export const getOrganizations = async () => {
  const response = await fetch(`${API_BASE_URL}/organizations`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`)
  }

  return await response.json()
}

/**
 * Подача запроса на получение статуса представителя организации
 * @param {Object} request - данные запроса
 * @param {string} request.requestType - тип запроса: 'EXISTING' или 'NEW'
 * @param {number} [request.organizationId] - ID существующей организации (для EXISTING)
 * @param {string} [request.organizationName] - название новой организации (для NEW)
 * @param {string} [request.organizationDescription] - описание новой организации (для NEW)
 * @param {string} [request.organizationAddress] - адрес новой организации (для NEW)
 * @param {string} token - JWT токен
 */
/**
 * Подача запроса на получение статуса представителя организации
 * POST /users/me/organization-request
 * @param {Object} request - данные запроса
 * @param {string} request.requestType - тип запроса: 'EXISTING' или 'NEW'
 * @param {number} [request.organizationId] - ID существующей организации (для EXISTING)
 * @param {string} [request.organizationName] - название новой организации (для NEW)
 * @param {string} [request.organizationDescription] - описание новой организации (для NEW)
 * @param {string} [request.organizationAddress] - адрес новой организации (для NEW)
 * @param {string} token - JWT токен
 */
export const submitOrganizationRequest = async (request, token) => {
  const response = await fetch(`${API_BASE_URL}/users/me/organization-request`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(request),
  })

  if (!response.ok) {
    let errorMessage = `HTTP ${response.status}: ${response.statusText}`
    
    if (response.status === 400) {
      if (response.statusText.includes('pending') || response.headers.get('content-type')?.includes('application/json')) {
        try {
          const error = await response.json()
          errorMessage = error.message || 'Не удалось отправить запрос'
        } catch (e) {
          errorMessage = 'У вас уже есть ожидающий рассмотрения запрос на получение статуса представителя организации'
        }
      } else {
        errorMessage = 'Неверные данные запроса. Пожалуйста, проверьте заполненные поля.'
      }
    } else {
      try {
        const error = await response.json()
        errorMessage = error.message || errorMessage
      } catch (e) {
        // Error is not JSON, use default message
      }
    }
    
    throw new Error(errorMessage)
  }

  return await response.json().catch(() => ({})) // Some endpoints return empty body
}
