// const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1'

/**
 * Получение списка всех организаций
 */
export const getOrganizations = async () => {
  // ЗАГЛУШКА: мок-данные организаций
  console.log('🔧 Mock: Getting organizations list')
  await new Promise(resolve => setTimeout(resolve, 300))

  return [
    { id: 1, name: 'Красный Крест', description: 'Международная организация помощи', address: 'ул. Большая Морская, 1' },
    { id: 2, name: 'Волонтеры Победы', description: 'Патриотическая организация', address: 'пр. Ветеранов, 45' },
    { id: 3, name: 'Добровольцы России', description: 'Всероссийское общественное движение', address: 'ул. Ленина, 10' }
  ]

  // Для реального API раскомментируйте:
  // const response = await axios.get(`${API_URL}/organizations`)
  // return response.data
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
export const submitOrganizationRequest = async (request, token) => {
  // ЗАГЛУШКА: имитация отправки запроса
  console.log('🔧 Mock: Submitting organization request', request)
  await new Promise(resolve => setTimeout(resolve, 500))

  return {
    id: Math.floor(Math.random() * 1000),
    userId: 1,
    ...request,
    status: 'PENDING',
    createdAt: new Date().toISOString()
  }

  // Для реального API раскомментируйте:
  // const response = await axios.post(
  //   `${API_URL}/users/me/organization-request`,
  //   request,
  //   {
  //     headers: {
  //       Authorization: `Bearer ${token}`
  //     }
  //   }
  // )
  // return response.data
}
