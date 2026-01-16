export interface UserData {
  id?: number
  name: string
  email?: string
  birthday: Date | string
  location: string
  description: string
  rating: number
  volunteerHours: number
  avatar?: string
  role: 'USER' | 'ORG_REPRESENTATIVE' | 'ADMIN'
  organisationId?: number | null
  organizationName?: string | null
}

export interface Organization {
  id: number
  name: string
  description?: string
  address?: string
}

export interface OrganizationRequest {
  requestType: 'EXISTING' | 'NEW'
  organizationId?: number
  organizationName?: string
  organizationDescription?: string
  organizationAddress?: string
}
