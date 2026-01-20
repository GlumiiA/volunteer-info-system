<script setup>
import ProfileEvents from '@/components/profile/ProfileEvents.vue'
import ProfileInfo from '@/components/profile/ProfileInfo.vue'
import { TabList, TabPanel, TabPanels, Tabs, Tab, Button, FileUpload } from 'primevue'
import { ref, computed, onMounted, watch } from 'vue'
import { useAuth } from '@/composables/useAuth'
import { useToast } from 'primevue'
import * as userService from '@/services/user'
import { useRouter } from 'vue-router'

/** @typedef {import('@/types/user').UserData} UserData */
/** @typedef {import('@/types/event').EventEntry} EventEntry */

const { user, isOrgRepresentative, isAuthenticated } = useAuth()
const toast = useToast()
const router = useRouter()

/** @type {import('vue').Ref<UserData|null>} */
const userData = ref(null)

/** @type {import('vue').Ref<EventEntry[]>} */
const participatedEvents = ref([])

/** @type {import('vue').Ref<EventEntry[]>} */
const organizedEvents = ref([])

const isLoadingEvents = ref(false)

// Загружаем данные пользователя
const loadUserData = () => {
  if (user.value) {
    userData.value = { ...user.value }
  }
}

// Загружаем организованные мероприятия
const loadOrganizedEvents = async () => {
  if (!isAuthenticated.value) {
    return
  }

  isLoadingEvents.value = true
  try {
    const eventsData = await userService.getUserEvents('all')
    
    // Combine mass and individual events
    const allEvents = []
    
    if (eventsData.massEvents && Array.isArray(eventsData.massEvents)) {
      eventsData.massEvents.forEach((event) => {
        allEvents.push({
          id: event.id,
          type: 'MASS',
          title: event.title,
          description: event.description || '',
          dateStart: event.dateStart,
          dateEnd: event.dateEnd,
          address: event.address || null,
          workHours: event.workHours || null,
          volunteersRequired: event.volunteersRequired || 0,
          ageRestriction: event.ageRestriction || null,
        })
      })
    }
    
    if (eventsData.individualEvents && Array.isArray(eventsData.individualEvents)) {
      eventsData.individualEvents.forEach((event) => {
        allEvents.push({
          id: event.id,
          type: 'INDIVIDUAL',
          title: event.title,
          description: event.description || '',
          dateStart: event.dateStart,
          dateEnd: event.dateEnd,
          address: null,
          workHours: null,
          volunteersRequired: event.volunteersRequired || 0,
          ageRestriction: event.ageRestriction || null,
        })
      })
    }
    
    organizedEvents.value = allEvents
  } catch (error) {
    console.error('Failed to load organized events:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось загрузить организованные мероприятия',
      life: 3000,
    })
  } finally {
    isLoadingEvents.value = false
  }
}

// Загружаем волонтерскую книжку
const loadVolunteerBook = async () => {
  if (!isAuthenticated.value) {
    return
  }

  try {
    const volunteerBook = await userService.getUserVolunteerBook()
    
    if (volunteerBook.entries && Array.isArray(volunteerBook.entries)) {
      participatedEvents.value = volunteerBook.entries.map((entry) => ({
        id: entry.eventId,
        type: 'MASS', // Volunteer book entries are from mass events
        title: entry.event?.title || `Мероприятие #${entry.eventId}`,
        description: entry.event?.description || '',
        dateStart: entry.event?.dateStart || null,
        dateEnd: entry.event?.dateEnd || null,
        address: entry.event?.address || null,
        workHours: entry.event?.workHours || null,
        volunteersRequired: null,
        ageRestriction: null,
      }))
    }
  } catch (error) {
    console.error('Failed to load volunteer book:', error)
    // Don't show error toast for volunteer book - it's optional
  }
}

onMounted(() => {
  loadUserData()
  if (isAuthenticated.value) {
    loadOrganizedEvents()
    loadVolunteerBook()
  }
})

// Следим за изменениями user
watch(user, (newUser) => {
  if (newUser) {
    userData.value = { ...newUser }
    // Reload events when user changes
    if (isAuthenticated.value) {
      loadOrganizedEvents()
      loadVolunteerBook()
    }
  }
}, { immediate: true })

// Функция для загрузки аватара
const handleAvatarUpload = (event) => {
  const file = event.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      if (userData.value) {
        userData.value.avatar = e.target.result
        console.log('Avatar uploaded:', userData.value.avatar)
      }
    }
    reader.readAsDataURL(file)
  }
}

// Функция для удаления аватара
const removeAvatar = () => {
  if (userData.value) {
    userData.value.avatar = null
  }
}

const isOrgMode = computed(() => {
  return userData.value?.role === 'ORG_REPRESENTATIVE' || isOrgRepresentative.value
})
</script>

<template>
  <div class="profile-view" v-if="userData">
    <!-- Заголовок профиля -->
    <div class="profile-header">
      <div class="header-background"></div>
      <div class="header-content">
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <img v-if="userData.avatar" :src="userData.avatar" alt="Avatar" class="avatar-image" />
            <div v-else class="avatar-placeholder">
              <i class="pi pi-user" style="font-size: 3rem"></i>
            </div>
            <!-- Кнопка удаления аватара -->
            <button v-if="userData.avatar" @click="removeAvatar" class="remove-avatar-btn" title="Удалить аватар">
              <i class="pi pi-times"></i>
            </button>
          </div>
          <!-- Загрузка аватара -->
          <FileUpload 
            mode="basic" 
            accept="image/*" 
            :maxFileSize="1000000"
            @select="handleAvatarUpload"
            :auto="true"
            chooseLabel="Загрузить аватар"
            class="avatar-upload"
          />
        </div>
        <div class="header-info">
          <h1 class="profile-name">{{ userData.name }}</h1>
          <p class="profile-email">{{ userData.email }}</p>
          <div class="profile-badges">
            <span v-if="isOrgMode" class="badge badge-org">
              <i class="pi pi-briefcase"></i>
              Представитель организации
            </span>
            <span v-else class="badge badge-volunteer">
              <i class="pi pi-heart"></i>
              Волонтер
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Статистика -->
    <div class="stats-container">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
          <i class="pi pi-clock"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ userData.volunteerHours || 0 }}</div>
          <div class="stat-label">Отработано часов</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
          <i class="pi pi-star-fill"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ userData.rating?.toFixed(1) || 'N/A' }}</div>
          <div class="stat-label">Рейтинг</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
          <i class="pi pi-calendar-plus"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ participatedEvents.length }}</div>
          <div class="stat-label">Мероприятий</div>
        </div>
      </div>
    </div>

    <!-- Вкладки -->
    <Tabs value="0" class="profile-tabs">
      <TabList>
        <Tab value="0">
          <i class="pi pi-user"></i>
          О волонтере
        </Tab>
        <Tab value="1">
          <i class="pi pi-book"></i>
          Волонтерская книжка
        </Tab>
        <Tab value="2">
          <i class="pi pi-calendar"></i>
          {{ isOrgMode ? 'Мероприятия организации' : 'Организованные мероприятия' }}
        </Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0">
          <ProfileInfo v-model="userData" />
        </TabPanel>
        <TabPanel value="1">
          <ProfileEvents v-model="participatedEvents" title="Волонтерская книжка" />
        </TabPanel>
        <TabPanel value="2">
          <ProfileEvents v-model="organizedEvents" title="Организованные мероприятия" />
        </TabPanel>
      </TabPanels>
    </Tabs>
  </div>
  <div v-else class="loading-message">
    <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
    <p>Загрузка профиля...</p>
  </div>
</template>

<style scoped>
.profile-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-l);
}

.profile-header {
  position: relative;
  margin-bottom: var(--space-2xl);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.header-background {
  height: 180px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header-content {
  position: relative;
  padding: 0 var(--space-xl) var(--space-xl);
  background: var(--surface-card);
  display: flex;
  gap: var(--space-xl);
  align-items: flex-start;
}

.avatar-section {
  margin-top: -60px;
}

.avatar-wrapper {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  border: 6px solid var(--surface-card);
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  background: var(--surface-50);
  position: relative;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.remove-avatar-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.9);
  border: 2px solid white;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  z-index: 10;
}

.remove-avatar-btn:hover {
  background: rgb(220, 38, 38);
  transform: scale(1.1);
}

.avatar-upload {
  margin-top: var(--space-m);
}

.avatar-upload :deep(.p-button) {
  font-size: 0.875rem;
  padding: var(--space-s) var(--space-m);
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.avatar-upload :deep(.p-button:hover) {
  background: linear-gradient(135deg, #5568d3 0%, #653a8b 100%);
}

.header-info {
  flex: 1;
  padding-top: var(--space-m);
}

.profile-name {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 var(--space-xs);
  color: var(--text-color);
}

.profile-email {
  color: var(--text-color-secondary);
  margin: 0 0 var(--space-m);
  font-size: 1rem;
}

.profile-badges {
  display: flex;
  gap: var(--space-s);
  margin-bottom: var(--space-m);
}

.badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-xs);
  padding: var(--space-xs) var(--space-m);
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
}


.badge-org {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.badge-volunteer {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-l);
  margin-bottom: var(--space-2xl);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--space-l);
  padding: var(--space-l);
  background: var(--surface-card);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  line-height: 1;
  margin-bottom: var(--space-xs);
}

.stat-label {
  font-size: 0.875rem;
  color: var(--text-color-secondary);
  font-weight: 500;
}

.profile-tabs :deep(.p-tablist-tab-list) {
  background: var(--surface-card);
  border-radius: 12px;
  padding: var(--space-xs);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.profile-tabs :deep(.p-tab) {
  display: flex;
  align-items: center;
  gap: var(--space-s);
  padding: var(--space-m) var(--space-l);
  border-radius: 8px;
  transition: all 0.2s;
}

.profile-tabs :deep(.p-tab:hover) {
  background: var(--surface-50);
}

.profile-tabs :deep(.p-tab[data-p-active="true"]) {
  background: var(--primary-color);
  color: white;
}

.loading-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-m);
  padding: var(--space-2xl);
  color: var(--text-color-secondary);
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .avatar-section {
    margin-top: -70px;
  }

  .stats-container {
    grid-template-columns: 1fr;
  }
}
</style>
