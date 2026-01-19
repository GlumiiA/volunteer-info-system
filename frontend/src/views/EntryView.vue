<script setup>
import { ref, onMounted } from 'vue'
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
} from 'primevue'
import { useToast } from 'primevue'
import cogImage from '@/assets/images/cog.png'
import pawImage from '@/assets/images/paw.png'

const router = useRouter()
const route = useRoute()
const toast = useToast()

// Заглушки данных для разных заявок
const entriesData = {
  1: {
    entry: {
      id: 1,
      type: 'MASS',
      title: 'Помощь в организации городского марафона',
      description:
        'Требуются добровольцы для помощи в организации городского благотворительного марафона. ' +
        'Обязанности: помощь на регистрации участников, работа на пунктах питания, сопровождение бегунов. ' +
        'Все необходимое оборудование будет предоставлено. Приветствуется опыт волонтерской работы, но не обязателен.',
      volunteersRequired: 20,
      volunteersRegistered: 12,
      ageRestriction: 18,
      dateStart: '2026-03-15',
      dateEnd: '2026-03-15',
      address: 'Центральный парк, г. Санкт-Петербург',
      hoursToWork: 6,
      headerImage: cogImage,
      organizationName: 'Спортивный фонд "Движение"',
      status: 'ACTIVE',
    },
    author: {
      id: 101,
      name: 'Иванов Иван Иванович',
      avatar: null,
    },
    participants: [
      { id: 1, name: 'Петрова Анна', avatar: null, status: 'APPROVED' },
      { id: 2, name: 'Сидоров Петр', avatar: null, status: 'APPROVED' },
      { id: 3, name: 'Кузнецова Мария', avatar: null, status: 'PENDING' },
    ],
    isAuthor: true,
    isParticipant: false,
  },
  2: {
    entry: {
      id: 2,
      type: 'MASS',
      title: 'Уборка приюта для животных',
      description:
        'Нужна помощь в уборке и уходе за животными в приюте. ' +
        'Задачи: уборка вольеров, кормление, прогулки с собаками. ' +
        'Приветствуется любовь к животным! Не требуется опыт работы с животными, мы все покажем и расскажем.',
      volunteersRequired: 5,
      volunteersRegistered: 2,
      ageRestriction: 16,
      dateStart: '2026-02-20',
      dateEnd: '2026-02-20',
      address: 'Приют "Добрые руки", ул. Садовая 25',
      hoursToWork: 4,
      headerImage: pawImage,
      organizationName: 'Благотворительный фонд "Помощь животным"',
      status: 'ACTIVE',
    },
    author: {
      id: 102,
      name: 'Смирнова Елена Петровна',
      avatar: null,
    },
    participants: [
      { id: 4, name: 'Алексеев Дмитрий', avatar: null, status: 'APPROVED' },
      { id: 5, name: 'Васильева Ольга', avatar: null, status: 'APPROVED' },
    ],
    isAuthor: false,
    isParticipant: false,
  },
  3: {
    entry: {
      id: 3,
      type: 'INDIVIDUAL',
      title: 'Помощь пожилому человеку',
      description:
        'Нужен волонтер для помощи пожилому человеку с покупками и уборкой дома. ' +
        'Задачи: сходить в магазин за продуктами, провести легкую уборку в квартире, пообщаться. ' +
        'Моя бабушка будет очень рада компании!',
      volunteersRequired: 1,
      volunteersRegistered: 0,
      ageRestriction: 18,
      dateStart: '2026-02-10',
      dateEnd: '2026-02-10',
      address: 'ул. Пушкина, д. 12, кв. 45',
      hoursToWork: null,
      headerImage: cogImage,
      organizationName: null,
      status: 'ACTIVE',
    },
    author: {
      id: 103,
      name: 'Новикова Мария Александровна',
      avatar: null,
    },
    participants: [],
    isAuthor: false,
    isParticipant: false,
  },
}

// Текущие данные
const entry = ref({})
const author = ref({})
const participants = ref([])
const isAuthor = ref(false)
const isParticipant = ref(false)
const participantStatus = ref(null) // 'PENDING', 'APPROVED', 'REJECTED'
const userRole = ref('VOLUNTEER') // VOLUNTEER, ORGANIZATION, ADMIN
const isLoading = ref(true)

// Текущий пользователь (заглушка)
const currentUser = ref({
  id: 999,
  name: 'Текущий Пользователь',
})

// Загрузка данных по ID
const loadEntryData = () => {
  isLoading.value = true
  const entryId = parseInt(route.params.id)
  const data = entriesData[entryId]

  if (data) {
    entry.value = data.entry
    author.value = data.author
    participants.value = [...data.participants]
    isAuthor.value = data.isAuthor
    isParticipant.value = data.isParticipant
    isLoading.value = false
  } else {
    // Если заявка не найдена, перенаправляем на поиск
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
// В будущем: POST /events/mass/{id}/participate или POST /events/individual/{id}/participate
const handleJoin = async () => {
  try {
    // Заглушка API запроса
    console.log(`Подача заявки на участие в мероприятии ${entry.value.id}`)
    // const response = await fetch(`/api/v1/events/${entry.value.type === 'MASS' ? 'mass' : 'individual'}/${entry.value.id}/participate`, {
    //   method: 'POST',
    //   headers: {
    //     'Authorization': `Bearer ${token}`,
    //     'Content-Type': 'application/json'
    //   }
    // })

    // Имитация успешной подачи заявки
    setTimeout(() => {
      isParticipant.value = true
      participantStatus.value = 'PENDING'

      // Добавляем текущего пользователя в список участников со статусом PENDING
      participants.value.push({
        id: currentUser.value.id,
        name: currentUser.value.name,
        avatar: null,
        status: 'PENDING',
      })

      entry.value.volunteersRegistered += 1

      toast.add({
        severity: 'success',
        summary: 'Заявка отправлена',
        detail:
          'Ваша заявка на участие успешно отправлена. Ожидайте подтверждения от организатора.',
        life: 3000,
      })
    }, 500)
  } catch (error) {
    console.error('Ошибка при подаче заявки:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось отправить заявку. Попробуйте позже.',
      life: 3000,
    })
  }
}

// Отозвать заявку на участие
const handleLeave = async () => {
  try {
    // Заглушка API запроса
    console.log(`Отзыв заявки на участие в мероприятии ${entry.value.id}`)
    // const response = await fetch(`/api/v1/events/${entry.value.type === 'MASS' ? 'mass' : 'individual'}/${entry.value.id}/participate/${requestId}`, {
    //   method: 'DELETE',
    //   headers: {
    //     'Authorization': `Bearer ${token}`
    //   }
    // })

    // Имитация успешного отзыва заявки
    setTimeout(() => {
      isParticipant.value = false
      participantStatus.value = null

      // Удаляем текущего пользователя из списка участников
      const index = participants.value.findIndex((p) => p.id === currentUser.value.id)
      if (index > -1) {
        participants.value.splice(index, 1)
        entry.value.volunteersRegistered -= 1
      }

      toast.add({
        severity: 'info',
        summary: 'Заявка отозвана',
        detail: 'Ваша заявка на участие отозвана.',
        life: 3000,
      })
    }, 500)
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

const handleApproveParticipant = (participantId) => {
  console.log('Одобрить участника:', participantId)
}

const handleRejectParticipant = (participantId) => {
  console.log('Отклонить участника:', participantId)
}

const handleDelete = () => {
  console.log('Удалить заявку')
  router.push({ name: 'search' })
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
          <Button v-if="isAuthor" @click="handleDelete" severity="danger" outlined>
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
            v-if="!isAuthor && isParticipant && entry.status === 'ACTIVE'"
            label="Отозвать заявку"
            icon="pi pi-user-minus"
            @click="handleLeave"
            outlined
          >
            <i class="pi pi-user-minus" />
            <span>Отозвать заявку</span>
          </Button>
        </div>
      </div>

      <Card class="entry-details">
        <template #header>
          <img v-if="entry.headerImage" class="entry-image" alt="header" :src="entry.headerImage" />
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
                  <div class="detail-value">{{ entry.dateStart }} - {{ entry.dateEnd }}</div>
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
            <h3>Участники</h3>
            <div v-if="participants.length === 0" class="no-participants">
              <p style="color: var(--text-color-secondary); font-style: italic">
                Пока нет заявок на участие
              </p>
            </div>
            <Accordion v-else>
              <AccordionPanel>
                <template #header>
                  <div class="participants-header">
                    <i class="pi pi-users" />
                    <span>Список участников ({{ participants.length }})</span>
                  </div>
                </template>
                <template #default>
                  <div class="participants-list">
                    <Card
                      v-for="participant in participants"
                      :key="participant.id"
                      class="participant-card"
                    >
                      <template #content>
                        <div class="participant-info">
                          <Avatar :label="participant.name[0]" shape="circle" />
                          <span class="participant-name">{{ participant.name }}</span>
                          <Chip
                            :label="getParticipantStatusLabel(participant.status)"
                            :class="'status-' + participant.status.toLowerCase()"
                          />
                          <div class="participant-actions">
                            <Button
                              v-if="participant.status === 'PENDING'"
                              icon="pi pi-check"
                              @click="handleApproveParticipant(participant.id)"
                              rounded
                              text
                              severity="success"
                              title="Одобрить"
                            />
                            <Button
                              v-if="participant.status === 'PENDING'"
                              icon="pi pi-times"
                              @click="handleRejectParticipant(participant.id)"
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
</style>
