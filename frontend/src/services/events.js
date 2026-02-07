const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

/**
 * Get authentication token from localStorage
 */
const getAuthToken = () => {
  return localStorage.getItem('auth_token')
}

/**
 * Get headers for API requests
 */
const getHeaders = (includeAuth = false) => {
  const headers = {
    'Content-Type': 'application/json',
  }
  if (includeAuth) {
    const token = getAuthToken()
    if (token) {
      headers['Authorization'] = `Bearer ${token}`
    }
  }
  return headers
}

/**
 * Fetch a mass event by ID
 */
export const getMassEvent = async (id) => {
  const response = await fetch(`${API_BASE_URL}/events/mass/${id}`, {
    method: 'GET',
    headers: getHeaders(),
    credentials: 'include',
  })

  if (!response.ok) {
    if (response.status === 404) {
      return null
    }
    throw new Error(`Failed to fetch mass event: ${response.statusText}`)
  }

  return await response.json()
}

/**
 * Fetch an individual event by ID
 */
export const getIndividualEvent = async (id) => {
  const response = await fetch(`${API_BASE_URL}/events/individual/${id}`, {
    method: 'GET',
    headers: getHeaders(),
    credentials: 'include',
  })

  if (!response.ok) {
    if (response.status === 404) {
      return null
    }
    throw new Error(`Failed to fetch individual event: ${response.statusText}`)
  }

  return await response.json()
}

/**
 * Fetch event participants (for event authors)
 */
export const getEventParticipants = async (eventType, eventId) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/events/${eventType}/${eventId}/participants`, {
    method: 'GET',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    if (response.status === 404) {
      return { participants: [], totalCount: 0 }
    }
    throw new Error(`Failed to fetch participants: ${response.statusText}`)
  }

  return await response.json()
}

/**
 * Fetch event requests (for event authors)
 */
export const getEventRequests = async (eventType, eventId) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/events/${eventType}/${eventId}/requests`, {
    method: 'GET',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    if (response.status === 404) {
      return []
    }
    throw new Error(`Failed to fetch requests: ${response.statusText}`)
  }

  return await response.json()
}

/**
 * Participate in an event
 */
export const participateInEvent = async (eventType, eventId) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/events/${eventType}/${eventId}/participate`, {
    method: 'POST',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`Failed to participate: ${errorText || response.statusText}`)
  }
}

/**
 * Get organization by ID
 */
export const getOrganization = async (id) => {
  if (!id) return null

  try {
    const response = await fetch(`${API_BASE_URL}/organizations`, {
      method: 'GET',
      headers: getHeaders(),
      credentials: 'include',
    })

    if (!response.ok) {
      return null
    }

    const organizations = await response.json()
    return organizations.find(org => org.id === id) || null
  } catch (error) {
    console.error('Failed to fetch organizations:', error)
    return null
  }
}

/**
 * Get all organizations
 */
export const getOrganizations = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/organizations`, {
      method: 'GET',
      headers: getHeaders(),
      credentials: 'include',
    })

    if (!response.ok) {
      return []
    }

    return await response.json()
  } catch (error) {
    console.error('Failed to fetch organizations:', error)
    return []
  }
}

/**
 * Delete an event (mass or individual)
 * Note: The backend service allows event owners to delete their own events
 * Uses admin endpoint but backend service checks ownership
 */
export const deleteEvent = async (eventType, eventId) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  // Delete endpoint - backend service checks if user is owner or admin
  // Note: Uses admin endpoint path, but backend allows owners too
  const response = await fetch(`${API_BASE_URL}/events/${eventType}/${eventId}`, {
    method: 'DELETE',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    if (response.status === 403) {
      throw new Error('У вас нет прав для удаления этой заявки. Вы можете удалять только свои заявки.')
    }
    if (response.status === 404) {
      throw new Error('Заявка не найдена')
    }
    const errorText = await response.text()
    throw new Error(`Не удалось удалить заявку: ${errorText || response.statusText}`)
  }
}
