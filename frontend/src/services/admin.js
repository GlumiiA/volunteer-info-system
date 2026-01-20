const API_BASE_URL = 'http://localhost:8080/api/v1'

/**
 * Get authentication token from localStorage
 */
function getAuthToken() {
  return localStorage.getItem('auth_token')
}

/**
 * Make authenticated API request
 */
async function apiRequest(url, options = {}) {
  const token = getAuthToken()
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  
  const response = await fetch(`${API_BASE_URL}${url}`, {
    ...options,
    headers,
  })
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Request failed' }))
    throw new Error(error.message || `HTTP ${response.status}`)
  }
  
  return response.json()
}

/**
 * Get all organization requests
 * @param {string} status - Filter by status (PENDING, APPROVED, REJECTED, ALL)
 * @returns {Promise<Array>}
 */
export async function getOrganizationRequests(status = 'PENDING') {
  return apiRequest(`/admin/organization-requests?status=${status}`)
}

/**
 * Approve an organization request
 * @param {number} requestId - Request ID
 * @returns {Promise<void>}
 */
export async function approveOrganizationRequest(requestId) {
  await apiRequest(`/admin/organization-requests/${requestId}/approve`, {
    method: 'POST',
  })
}

/**
 * Reject an organization request
 * @param {number} requestId - Request ID
 * @param {string} reason - Optional rejection reason
 * @returns {Promise<void>}
 */
export async function rejectOrganizationRequest(requestId, reason = null) {
  const body = reason ? { reason } : {}
  await apiRequest(`/admin/organization-requests/${requestId}/reject`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

/**
 * Get all organizations
 * @returns {Promise<Array>}
 */
export async function getOrganizations() {
  return apiRequest('/admin/organizations')
}

/**
 * Create a new organization
 * @param {Object} organization - Organization data
 * @param {string} organization.name - Organization name
 * @param {string} [organization.description] - Organization description
 * @param {string} [organization.address] - Organization address
 * @returns {Promise<Object>}
 */
export async function createOrganization(organization) {
  return apiRequest('/admin/organizations', {
    method: 'POST',
    body: JSON.stringify(organization),
  })
}

/**
 * Update an organization
 * @param {number} id - Organization ID
 * @param {Object} organization - Organization data
 * @returns {Promise<Object>}
 */
export async function updateOrganization(id, organization) {
  return apiRequest(`/admin/organizations/${id}`, {
    method: 'PUT',
    body: JSON.stringify(organization),
  })
}

/**
 * Delete an organization
 * @param {number} id - Organization ID
 * @returns {Promise<void>}
 */
export async function deleteOrganization(id) {
  await apiRequest(`/admin/organizations/${id}`, {
    method: 'DELETE',
  })
}

/**
 * Get all users
 * @returns {Promise<Array>}
 */
export async function getUsers() {
  return apiRequest('/admin/users')
}

/**
 * Update user role
 * @param {number} userId - User ID
 * @param {string} role - New role (USER, ORG_REPRESENTATIVE)
 * @param {number} [organisationId] - Organization ID (required for ORG_REPRESENTATIVE)
 * @returns {Promise<Object>}
 */
export async function updateUserRole(userId, role, organisationId = null) {
  const body = { role }
  if (organisationId !== null) {
    body.organisationId = organisationId
  }
  
  return apiRequest(`/admin/users/${userId}/role`, {
    method: 'PUT',
    body: JSON.stringify(body),
  })
}
