<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Panel,
  Card,
  Button,
  Tag,
  Avatar,
  Divider,
  Chip,
  Accordion,
  AccordionPanel,
  Dialog,
} from 'primevue'
import { useToast } from 'primevue'
import { useAuth } from '@/composables/useAuth'
import * as eventsService from '@/services/events'
import * as userService from '@/services/user'
import cogImage from '@/assets/images/cog.png'
import pawImage from '@/assets/images/paw.png'

const router = useRouter()
const route = useRoute()
const toast = useToast()
const { user, token, isAuthenticated } = useAuth()

// Текущие данные
const entry = ref({})
const entryType = ref(null) // 'MASS' or 'INDIVIDUAL'
const author = ref({})
const organization = ref(null)
const participants = ref([])
const requests = ref([])
const isAuthor = ref(false)
const isParticipant = ref(false)
const participantStatus = ref(null) // 'PENDING', 'APPROVED', 'REJECTED'
const isLoading = ref(true)
const showDeleteDialog = ref(false)

// Format date for display
const formatDate = (dateString) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('ru-RU', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    })
  } catch {
    return dateString
  }
}

// Format datetime for display
const formatDateTime = (dateString) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return date.toLocaleString('ru-RU', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  } catch {
    return dateString
  }
}

// Get header image based on event type
const headerImage = computed(() => {
  if (entryType.value === 'MASS') {
    return cogImage
  }
  return pawImage
})

// Load organization info for mass events
const loadOrganization = async (organizationId) => {
  if (!organizationId) return
  try {
    const org = await eventsService.getOrganization(organizationId)
    if (org) {
      organization.value = org
    }
  } catch (error) {
    console.error('Failed to load organization:', error)
  }
}

// Load participants (for event authors and to check current user status)
const loadParticipants = async () => {
  if (!entry.value.id || !entryType.value) return
  try {
    const result = await eventsService.getEventParticipants(entryType.value.toLowerCase(), entry.value.id)
    if (result) {
      // Always update count from API response
      if (result.totalCount !== undefined) {
        entry.value.volunteersRegistered = result.totalCount
      }
      
      // Load participant list if available
      if (result.participants) {
        participants.value = result.participants.map((p) => ({
          id: p.id,
          name: p.name || p.fullName || 'Неизвестный пользователь',
          avatar: p.avatar || p.avatarUrl,
          status: 'APPROVED',
        }))
      } else {
        participants.value = []
      }
    }
  } catch (error) {
    // If user doesn't have permission (not author), participants list will be empty
    // This is expected for non-authors, but we still try to get the count
    if (isAuthor.value) {
      console.error('Failed to load participants:', error)
    }
    participants.value = []
    // Don't reset count on error - keep previous value
  }
}

// Load requests (for event authors)
const loadRequests = async () => {
  if (!isAuthor.value || !entry.value.id || !entryType.value) return
  try {
    const requestsData = await eventsService.getEventRequests(entryType.value.toLowerCase(), entry.value.id)
    if (requestsData && Array.isArray(requestsData)) {
      requests.value = requestsData.map((req) => ({
        id: req.requestId,
        userId: req.user?.id,
        name: req.user?.name || req.user?.fullName || 'Неизвестный пользователь',
        avatar: req.user?.avatar,
        status: req.status,
        applicationDate: req.applicationDate,
      }))
      // Also update participants list with pending requests
      const pendingRequests = requestsData.filter((req) => req.status === 'PENDING')
      pendingRequests.forEach((req) => {
        if (req.user) {
          participants.value.push({
            id: req.user.id,
            name: req.user.name || req.user.fullName || 'Неизвестный пользователь',
            avatar: req.user.avatar,
            status: 'PENDING',
          })
        }
      })
    }
  } catch (error) {
    console.error('Failed to load requests:', error)
  }
}

// Check if current user is participant
const checkParticipantStatus = async () => {
  if (!isAuthenticated.value || !entry.value.id || !entryType.value || !user.value) {
    isParticipant.value = false
    participantStatus.value = null
    return
  }

  try {
    // First check if user is in the participants list (for approved participants)
    if (participants.value && participants.value.length > 0) {
      const userParticipant = participants.value.find((p) => p.id === user.value.id)
      if (userParticipant) {
        isParticipant.value = true
        participantStatus.value = userParticipant.status || 'APPROVED'
        return
      }
    }

    // Check if user has a request/participation for this event
    const requestsData = await eventsService.getEventRequests(entryType.value.toLowerCase(), entry.value.id)
    if (requestsData && Array.isArray(requestsData)) {
      const userRequest = requestsData.find((req) => req.user?.id === user.value?.id)
      if (userRequest) {
        isParticipant.value = true
        participantStatus.value = userRequest.status
        return
      }
    }

    // User is not a participant
    isParticipant.value = false
    participantStatus.value = null
  } catch (error) {
    // If user doesn't have permission, they're not a participant
    console.error('Error checking participant status:', error)
    isParticipant.value = false
    participantStatus.value = null
  }
}

// Загрузка данных по ID
const loadEntryData = async () => {
  isLoading.value = true
  const entryId = parseInt(route.params.id)

  if (isNaN(entryId)) {
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Неверный ID заявки',
      life: 3000,
    })
    router.push({ name: 'search' })
    return
  }

  try {
    // Try to fetch as mass event first
    let eventData = await eventsService.getMassEvent(entryId)

    if (eventData) {
      entryType.value = 'MASS'
      entry.value = {
        id: eventData.id,
        type: 'MASS',
        title: eventData.title,
        description: eventData.description || '',
        volunteersRequired: eventData.volunteersRequired || 0,
        volunteersRegistered: 0, // Will be updated from participants/requests
        ageRestriction: eventData.ageRestriction || null,
        dateStart: eventData.dateStart,
        dateEnd: eventData.dateEnd,
        address: eventData.address || null,
        hoursToWork: eventData.workHours || null,
        organizationId: eventData.organisationId,
        organizationName: null, // Will be loaded separately
        status: 'ACTIVE',
      }

      // Load organization info
      if (eventData.organisationId) {
        await loadOrganization(eventData.organisationId)
        if (organization.value) {
          entry.value.organizationName = organization.value.name
        }
      }

      // Check if current user is the organization representative (author)
      if (isAuthenticated.value && user.value) {
        isAuthor.value = user.value.role === 'ORG_REPRESENTATIVE' &&
                         user.value.organisationId === eventData.organisationId
      }

      // Load organization representative as author for mass events
      if (eventData.organisationId && organization.value) {
        // For mass events, author is the organization
        author.value = {
          id: organization.value.id,
          name: organization.value.name || 'Организация',
          avatar: null,
        }
      } else if (isAuthenticated.value && user.value && user.value.organisationId === eventData.organisationId) {
        // Fallback: use current user if they're the org rep
        author.value = {
          id: user.value.id,
          name: user.value.name || user.value.username || 'Организатор',
          avatar: user.value.avatarUrl || null,
        }
      }

      // Load participants and requests if user is author
      if (isAuthor.value) {
        await Promise.all([loadParticipants(), loadRequests()])
        // Count is updated in loadParticipants from API response
      } else {
        // For non-authors, also load participants to check if current user is approved
        // This will also update the count from API
        await loadParticipants()
        // Check if user is participant
        await checkParticipantStatus()
      }
    } else {
      // Try to fetch as individual event
      eventData = await eventsService.getIndividualEvent(entryId)

      if (eventData) {
        entryType.value = 'INDIVIDUAL'
        entry.value = {
          id: eventData.id,
          type: 'INDIVIDUAL',
          title: eventData.title,
          description: eventData.description || '',
          volunteersRequired: eventData.volunteersRequired || 1,
          volunteersRegistered: 0, // Will be updated from participants/requests
          ageRestriction: eventData.ageRestriction || null,
          dateStart: eventData.dateStart,
          dateEnd: eventData.dateEnd,
          address: null, // Individual events don't have address in backend
          hoursToWork: null,
          organizationId: eventData.organisationId,
          organizationName: null,
          creatorUserId: eventData.creatorUserId,
          status: 'ACTIVE',
        }

        // Load organization info if exists
        if (eventData.organisationId) {
          await loadOrganization(eventData.organisationId)
          if (organization.value) {
            entry.value.organizationName = organization.value.name
          }
        }

        // Check if current user is the creator (author)
        if (isAuthenticated.value && user.value) {
          isAuthor.value = eventData.creatorUserId === user.value.id ||
                          (user.value.role === 'ORG_REPRESENTATIVE' &&
                           user.value.organisationId === eventData.organisationId)
        }

        // Load participants and requests if user is author
        if (isAuthor.value) {
          await Promise.all([loadParticipants(), loadRequests()])
          // Count is updated in loadParticipants from API response
        } else {
          // For non-authors, also load participants to check if current user is approved
          // This will also update the count from API
          await loadParticipants()
          // Check if user is participant
          await checkParticipantStatus()
        }

        // Try to get author info (for individual events, author is the creator)
        if (eventData.creatorUserId) {
          try {
            const authorUser = await userService.getUserById(eventData.creatorUserId)
            author.value = {
              id: authorUser.id,
              name: authorUser.name || authorUser.username || `Пользователь #${eventData.creatorUserId}`,
              avatar: authorUser.avatarUrl || null,
            }
          } catch (error) {
            console.error('Failed to load author info:', error)
            // Fallback to showing ID
            author.value = {
              id: eventData.creatorUserId,
              name: `Пользователь #${eventData.creatorUserId}`,
              avatar: null,
            }
          }
        }
      } else {
        // Event not found
        toast.add({
          severity: 'error',
          summary: 'Ошибка',
          detail: 'Заявка не найдена',
          life: 3000,
        })
        router.push({ name: 'search' })
        return
      }
    }

    isLoading.value = false
  } catch (error) {
    console.error('Failed to load entry:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось загрузить заявку. Попробуйте позже.',
      life: 3000,
    })
    router.push({ name: 'search' })
  }
}

onMounted(() => {
  loadEntryData()
})

const handleEdit = () => {
  router.push({ name: 'entry-edit', params: { id: entry.value.id } })
}

// Подать заявку на участие
const handleJoin = async () => {
  if (!isAuthenticated.value) {
    toast.add({
      severity: 'warn',
      summary: 'Требуется авторизация',
      detail: 'Пожалуйста, войдите в систему для подачи заявки',
      life: 3000,
    })
    router.push({ name: 'auth' })
    return
  }

  try {
    await eventsService.participateInEvent(entryType.value.toLowerCase(), entry.value.id)
    
    isParticipant.value = true
    participantStatus.value = 'PENDING'

    // Reload requests to show the new request
    if (isAuthor.value) {
      await loadRequests()
      // Count will be updated when participants are reloaded
    }

    toast.add({
      severity: 'success',
      summary: 'Заявка отправлена',
      detail: 'Ваша заявка на участие успешно отправлена. Ожидайте подтверждения от организатора.',
      life: 3000,
    })
  } catch (error) {
    console.error('Ошибка при подаче заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось отправить заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

// Отозвать заявку на участие
const handleLeave = async () => {
  if (!isAuthenticated.value) {
    return
  }

  try {
    // Find the user's request ID
    const userRequest = requests.value.find((req) => req.userId === user.value.id)
    if (!userRequest) {
      toast.add({
        severity: 'error',
        summary: 'Ошибка',
        detail: 'Заявка не найдена',
        life: 3000,
      })
      return
    }

    // TODO: Implement DELETE /events/{type}/{eventId}/participate/{requestId}
    // For now, just update the local state
    isParticipant.value = false
    participantStatus.value = null

    // Remove from requests if visible
    const index = requests.value.findIndex((req) => req.userId === user.value.id)
    if (index > -1) {
      requests.value.splice(index, 1)
    }

    // Remove from participants if visible
    const participantIndex = participants.value.findIndex((p) => p.id === user.value.id)
    if (participantIndex > -1) {
      participants.value.splice(participantIndex, 1)
      entry.value.volunteersRegistered = Math.max(0, entry.value.volunteersRegistered - 1)
    }

    toast.add({
      severity: 'info',
      summary: 'Заявка отозвана',
      detail: 'Ваша заявка на участие отозвана.',
      life: 3000,
    })
  } catch (error) {
    console.error('Ошибка при отзыве заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось отозвать заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

const handleApproveParticipant = async (requestId) => {
  try {
    const response = await fetch(
      `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'}/events/${entryType.value.toLowerCase()}/${entry.value.id}/requests/${requestId}/accept`,
      {
        method: 'POST',
        headers: getHeaders(true),
        credentials: 'include',
      }
    )

    if (!response.ok) {
      if (response.status === 403) {
        throw new Error('У вас нет прав для одобрения заявок на это мероприятие')
      }
      if (response.status === 400) {
        const errorText = await response.text()
        throw new Error(errorText || 'Достигнуто максимальное количество волонтеров')
      }
      const errorText = await response.text()
      throw new Error(errorText || `Failed to approve: ${response.statusText}`)
    }

    // Reload requests and participants
    await Promise.all([loadRequests(), loadParticipants()])
    // Count is updated in loadParticipants from API response

    toast.add({
      severity: 'success',
      summary: 'Заявка одобрена',
      detail: 'Заявка на участие успешно одобрена.',
      life: 3000,
    })
    
    // If current user is not the author, re-check their participant status
    if (!isAuthor.value) {
      await checkParticipantStatus()
    }
  } catch (error) {
    console.error('Ошибка при одобрении заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось одобрить заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

const handleRejectParticipant = async (requestId) => {
  try {
    const response = await fetch(
      `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'}/events/${entryType.value.toLowerCase()}/${entry.value.id}/requests/${requestId}/reject`,
      {
        method: 'POST',
        headers: getHeaders(true),
        credentials: 'include',
      }
    )

    if (!response.ok) {
      if (response.status === 403) {
        throw new Error('У вас нет прав для отклонения заявок на это мероприятие')
      }
      const errorText = await response.text()
      throw new Error(errorText || `Failed to reject: ${response.statusText}`)
    }

    // Reload requests and participants
    await Promise.all([loadRequests(), loadParticipants()])
    // Count is updated in loadParticipants from API response

    toast.add({
      severity: 'info',
      summary: 'Заявка отклонена',
      detail: 'Заявка на участие отклонена.',
      life: 3000,
    })
    
    // If current user is not the author, re-check their participant status
    if (!isAuthor.value) {
      await checkParticipantStatus()
    }
  } catch (error) {
    console.error('Ошибка при отклонении заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось отклонить заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

// Get headers helper
const getHeaders = (includeAuth = false) => {
  const headers = {
    'Content-Type': 'application/json',
  }
  if (includeAuth && token.value) {
    headers['Authorization'] = `Bearer ${token.value}`
  }
  return headers
}

const openDeleteDialog = () => {
  showDeleteDialog.value = true
}

const handleDelete = async () => {
  showDeleteDialog.value = false

  if (!isAuthenticated.value || !token.value) {
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Требуется авторизация',
      life: 3000,
    })
    return
  }

  try {
    if (!entry.value.id || !entryType.value) {
      throw new Error('Invalid entry data')
    }

    await eventsService.deleteEvent(entryType.value.toLowerCase(), entry.value.id)

    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Заявка успешно удалена',
      life: 3000,
    })

    // Redirect to search page after deletion
    router.push({ name: 'search' })
  } catch (error) {
    console.error('Ошибка при удалении заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось удалить заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

const cancelDelete = () => {
  showDeleteDialog.value = false
}

const getStatusLabel = (status) => {
  const labels = {
    ACTIVE: 'Активна',
    COMPLETED: 'Завершена',
    CANCELLED: 'Отменена',
  }
  return labels[status] || status
}

const getStatusSeverity = (status) => {
  const severities = {
    ACTIVE: 'success',
    COMPLETED: 'secondary',
    CANCELLED: 'danger',
  }
  return severities[status] || 'info'
}

const getParticipantStatusLabel = (status) => {
  const labels = {
    APPROVED: 'Одобрен',
    PENDING: 'Ожидает',
    REJECTED: 'Отклонен',
  }
  return labels[status] || status
}
</script>

<template>
  <Panel class="entry-view-panel">
    <div class="back-button-container">
      <Button @click="router.push({ name: 'search' })" text>
        <i class="pi pi-arrow-left" />
        <span>Назад к заявкам</span>
      </Button>
    </div>

    <div v-if="isLoading" class="loading-container">
      <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      <p>Загрузка...</p>
    </div>

    <template v-else>
      <div class="entry-header">
        <div class="entry-header-top">
          <h1>{{ entry.title }}</h1>
          <Tag :value="getStatusLabel(entry.status)" :severity="getStatusSeverity(entry.status)" />
        </div>
        <div class="entry-actions">
          <Button v-if="isAuthor" @click="handleEdit" outlined>
            <i class="pi pi-pencil" />
            <span>Редактировать</span>
          </Button>
          <Button v-if="isAuthor" @click="openDeleteDialog" severity="danger" outlined>
            <i class="pi pi-trash" />
            <span>Удалить</span>
          </Button>
          <Button
            v-if="!isAuthor && !isParticipant && entry.status === 'ACTIVE'"
            @click="handleJoin"
          >
            <i class="pi pi-user-plus" />
            <span>Подать заявку</span>
          </Button>
          <Button
            v-if="!isAuthor && isParticipant && participantStatus === 'PENDING' && entry.status === 'ACTIVE'"
            label="Отозвать заявку"
            icon="pi pi-user-minus"
            @click="handleLeave"
            outlined
          >
            <i class="pi pi-user-minus" />
            <span>Отозвать заявку</span>
          </Button>
          <Tag
            v-if="!isAuthor && isParticipant && participantStatus === 'APPROVED'"
            severity="success"
            value="Вы участвуете"
          />
          <Tag
            v-if="!isAuthor && isParticipant && participantStatus === 'REJECTED'"
            severity="danger"
            value="Заявка отклонена"
          />
        </div>
      </div>

      <Card class="entry-details">
        <template #header>
          <img v-if="headerImage" class="entry-image" alt="header" :src="headerImage" />
        </template>
        <template #content>
          <div class="detail-section">
            <h3>Описание</h3>
            <p>{{ entry.description }}</p>
          </div>

          <Divider />

          <div class="detail-section">
            <h3>Информация о мероприятии</h3>
            <div class="detail-grid">
              <div class="detail-item">
                <i class="pi pi-calendar" />
                <div>
                  <div class="detail-label">Дата проведения</div>
                  <div class="detail-value">{{ formatDate(entry.dateStart) }} - {{ formatDate(entry.dateEnd) }}</div>
                </div>
              </div>

              <div class="detail-item" v-if="entry.address">
                <i class="pi pi-map-marker" />
                <div>
                  <div class="detail-label">Адрес</div>
                  <div class="detail-value">{{ entry.address }}</div>
                </div>
              </div>

              <div class="detail-item">
                <i class="pi pi-users" />
                <div>
                  <div class="detail-label">Волонтеров</div>
                  <div class="detail-value">
                    {{ entry.volunteersRegistered }} / {{ entry.volunteersRequired }}
                  </div>
                </div>
              </div>

              <div class="detail-item">
                <i class="pi pi-id-card" />
                <div>
                  <div class="detail-label">Возрастное ограничение</div>
                  <div class="detail-value">{{ entry.ageRestriction }}+</div>
                </div>
              </div>

              <div class="detail-item" v-if="entry.hoursToWork">
                <i class="pi pi-clock" />
                <div>
                  <div class="detail-label">Часов работы</div>
                  <div class="detail-value">{{ entry.hoursToWork }} ч.</div>
                </div>
              </div>

              <div class="detail-item" v-if="entry.organizationName">
                <i class="pi pi-building" />
                <div>
                  <div class="detail-label">Организация</div>
                  <div class="detail-value">{{ entry.organizationName }}</div>
                </div>
              </div>
            </div>
          </div>

          <Divider />

          <div class="detail-section">
            <h3>Организатор</h3>
            <div class="author-info">
              <Avatar :label="author.name[0]" shape="circle" size="large" />
              <span class="author-name">{{ author.name }}</span>
            </div>
          </div>

          <Divider v-if="isAuthor" />

          <div class="detail-section" v-if="isAuthor">
            <h3>Заявки на участие</h3>
            <div v-if="requests.length === 0" class="no-participants">
              <p style="color: var(--text-color-secondary); font-style: italic">
                Пока нет заявок на участие
              </p>
            </div>
            <Accordion v-else>
              <AccordionPanel>
                <template #header>
                  <div class="participants-header">
                    <i class="pi pi-users" />
                    <span>Список заявок ({{ requests.length }})</span>
                  </div>
                </template>
                <template #default>
                  <div class="participants-list">
                    <Card
                      v-for="request in requests"
                      :key="request.id"
                      class="participant-card"
                    >
                      <template #content>
                        <div class="participant-info">
                          <Avatar :label="request.name ? request.name[0] : '?'" shape="circle" />
                          <span class="participant-name">{{ request.name }}</span>
                          <Chip
                            :label="getParticipantStatusLabel(request.status)"
                            :class="'status-' + request.status.toLowerCase()"
                          />
                          <div class="participant-actions">
                            <Button
                              v-if="request.status === 'PENDING'"
                              icon="pi pi-check"
                              @click="handleApproveParticipant(request.id)"
                              rounded
                              text
                              severity="success"
                              title="Одобрить"
                            />
                            <Button
                              v-if="request.status === 'PENDING'"
                              icon="pi pi-times"
                              @click="handleRejectParticipant(request.id)"
                              rounded
                              text
                              severity="danger"
                              title="Отклонить"
                            />
                          </div>
                        </div>
                      </template>
                    </Card>
                  </div>
                </template>
              </AccordionPanel>
            </Accordion>
          </div>
        </template>
      </Card>
    </template>

    <!-- Delete Confirmation Dialog -->
    <Dialog
      v-model:visible="showDeleteDialog"
      modal
      header="Подтверждение удаления"
      :style="{ width: '450px' }"
      :closable="true"
    >
      <div class="delete-dialog-content">
        <p>
          Вы уверены, что хотите удалить заявку <strong>"{{ entry.title }}"</strong>?
        </p>
        <p style="color: #e24c4c; font-size: 0.9rem; margin-top: 1rem;">
          Это действие нельзя отменить.
        </p>
      </div>
      <template #footer>
        <Button label="Отмена" icon="pi pi-times" @click="cancelDelete" text />
        <Button label="Удалить" icon="pi pi-trash" @click="handleDelete" severity="danger" />
      </template>
    </Dialog>
  </Panel>
</template>

<style scoped>
.entry-view-panel {
  max-width: 1200px;
  margin: 0 auto;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-xxl);
  gap: var(--space-m);
}

.loading-container p {
  font-family: 'Roboto Flex', sans-serif;
  color: #666;
}

.back-button-container {
  margin-bottom: var(--space-m);
}

.entry-header {
  margin-bottom: var(--space-l);
}

.entry-header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-m);
}

.entry-header h1 {
  font-family: 'Nunito', sans-serif;
  margin: 0;
}

.entry-actions {
  display: flex;
  gap: var(--space-m);
}

.entry-details {
  margin-top: var(--space-l);
}

.entry-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
}

.detail-section {
  margin-bottom: var(--space-l);
}

.detail-section h3 {
  font-family: 'Nunito', sans-serif;
  margin-bottom: var(--space-m);
}

.detail-section p {
  font-family: 'Roboto Flex', sans-serif;
  line-height: 1.6;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-l);
}

.detail-item {
  display: flex;
  gap: var(--space-m);
  align-items: flex-start;
}

.detail-item i {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin-top: 4px;
}

.detail-label {
  font-family: 'Roboto Flex', sans-serif;
  font-size: 0.9rem;
  color: #666;
  margin-bottom: var(--space-xs);
}

.detail-value {
  font-family: 'Roboto Flex', sans-serif;
  font-weight: 500;
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--space-m);
}

.author-name {
  font-family: 'Roboto Flex', sans-serif;
  font-weight: 500;
  font-size: 1.1rem;
}

.no-participants {
  padding: var(--space-l);
  text-align: center;
}

.participants-header {
  display: flex;
  align-items: center;
  gap: var(--space-m);
  font-family: 'Nunito', sans-serif;
  font-weight: 600;
  font-size: 1.1rem;
}

.participants-header i {
  color: var(--primary-color);
}

.participants-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-m);
}

.participant-card {
  padding: 0;
}

.participant-info {
  display: flex;
  align-items: center;
  gap: var(--space-m);
}

.participant-name {
  font-family: 'Roboto Flex', sans-serif;
  flex-grow: 1;
}

.participant-actions {
  display: flex;
  gap: var(--space-s);
}

.status-approved {
  background-color: #d4edda;
  color: #155724;
}

.status-pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-rejected {
  background-color: #f8d7da;
  color: #721c24;
}

.delete-dialog-content {
  font-family: 'Roboto Flex', sans-serif;
}

.delete-dialog-content p {
  margin: 0;
  line-height: 1.6;
}
</style>
