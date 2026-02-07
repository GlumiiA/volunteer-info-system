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
 * Get user's organized events
 * GET /users/me/events
 */
export const getUserEvents = async (status = 'all') => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/users/me/events?status=${status}`, {
    method: 'GET',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`Failed to fetch user events: ${errorText || response.statusText}`)
  }

  return await response.json()
}

/**
 * Get user's volunteer book entries
 * GET /users/me/volunteer-book
 */
export const getUserVolunteerBook = async () => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/users/me/volunteer-book`, {
    method: 'GET',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`Failed to fetch volunteer book: ${errorText || response.statusText}`)
  }

  return await response.json()
}

/**
 * Update current user profile
 * PUT /users/me
 */
export const updateUser = async (userData) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  // Prepare request body - convert Date to ISO string for birthday
  const requestBody = {
    name: userData.name || null,
    description: userData.description || undefined,
    location: userData.location || undefined,
  }

  // Handle birthday - convert Date object to ISO date string (YYYY-MM-DD)
  if (userData.birthday) {
    if (userData.birthday instanceof Date) {
      // Convert Date to ISO date string (YYYY-MM-DD)
      const year = userData.birthday.getFullYear()
      const month = String(userData.birthday.getMonth() + 1).padStart(2, '0')
      const day = String(userData.birthday.getDate()).padStart(2, '0')
      requestBody.birthday = `${year}-${month}-${day}`
    } else if (typeof userData.birthday === 'string') {
      // If it's already a string, try to parse it
      // Handle various date formats
      const date = new Date(userData.birthday)
      if (!isNaN(date.getTime())) {
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        requestBody.birthday = `${year}-${month}-${day}`
      } else {
        // If it's already in YYYY-MM-DD format, use it as-is
        requestBody.birthday = userData.birthday
      }
    } else {
      requestBody.birthday = userData.birthday
    }
  } else {
    requestBody.birthday = undefined
  }

  const response = await fetch(`${API_BASE_URL}/users/me`, {
    method: 'PUT',
    headers: getHeaders(true),
    credentials: 'include',
    body: JSON.stringify(requestBody),
  })

  if (!response.ok) {
    const errorText = await response.text()
    let errorMessage = `Failed to update user: ${errorText || response.statusText}`
    try {
      const error = await response.json().catch(() => ({}))
      errorMessage = error.message || error.detail || errorMessage
    } catch (e) {
      // Error text is not JSON, use the text as-is
    }
    throw new Error(errorMessage)
  }

  return await response.json()
}

/**
 * Get user by ID
 * GET /users/{id}
 */
export const getUserById = async (userId) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
    method: 'GET',
    headers: getHeaders(true),
    credentials: 'include',
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`Failed to fetch user: ${errorText || response.statusText}`)
  }

  return await response.json()
}

/**
 * Upload avatar
 * POST /users/me/avatar
 */
export const uploadAvatar = async (file) => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const formData = new FormData()
  formData.append('file', file)

  const response = await fetch(`${API_BASE_URL}/users/me/avatar`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
    },
    credentials: 'include',
    body: formData,
  })

  if (!response.ok) {
    const errorText = await response.text()
    let errorMessage = `Failed to upload avatar: ${errorText || response.statusText}`
    try {
      const error = await response.json().catch(() => ({}))
      errorMessage = error.message || error.detail || errorMessage
    } catch (e) {
      // Error text is not JSON, use the text as-is
    }
    throw new Error(errorMessage)
  }

  const result = await response.json()
  return result.avatarUrl
}

/**
 * Delete avatar
 * DELETE /users/me/avatar
 */
export const deleteAvatar = async () => {
  const token = getAuthToken()
  if (!token) {
    throw new Error('Not authenticated')
  }

  const response = await fetch(`${API_BASE_URL}/users/me/avatar`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`,
    },
    credentials: 'include',
  })

  if (!response.ok) {
    const errorText = await response.text()
    let errorMessage = `Failed to delete avatar: ${errorText || response.statusText}`
    try {
      const error = await response.json().catch(() => ({}))
      errorMessage = error.message || error.detail || errorMessage
    } catch (e) {
      // Error text is not JSON, use the text as-is
    }
    throw new Error(errorMessage)
  }
}
