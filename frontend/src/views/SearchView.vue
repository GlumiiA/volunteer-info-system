<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Button } from 'primevue'
import EventCard from '@/components/EventCard.vue'
import EventFilters from '@/components/EventFilters.vue'
import cogImage from '@/assets/images/cog.png'
import pawImage from '@/assets/images/paw.png'

const API_BASE_URL = 'http://localhost:8080/api/v1'
const router = useRouter()

const allEvents = ref([])
const isLoading = ref(false)
const error = ref(null)

// Фильтры
const filters = ref({
  title: '',
  dateFrom: null,
  dateTo: null,
  address: '',
  organisationId: null,
  type: null,
})

// Заглушка списка организаций
const organizations = ref([
  { label: 'Все организации', value: null },
  { label: 'Спортивный фонд "Движение"', value: 1 },
  { label: 'Благотворительный фонд "Помощь"', value: 2 },
  { label: 'Волонтерский центр ИТМО', value: 3 },
])

// Типы заявок
const eventTypes = ref([
  { label: 'Все типы', value: null },
  { label: 'Массовые', value: 'MASS' },
  { label: 'Индивидуальные', value: 'INDIVIDUAL' },
])

// Фильтрованные заявки
const filteredEvents = computed(() => {
  let result = allEvents.value

  // Фильтр по названию
  if (filters.value.title && filters.value.title.trim() !== '') {
    const searchTerm = filters.value.title.toLowerCase()
    result = result.filter((event) => event.title.toLowerCase().includes(searchTerm))
  }

  // Фильтр по дате начала (от)
  if (filters.value.dateFrom) {
    const fromDate = new Date(filters.value.dateFrom)
    fromDate.setHours(0, 0, 0, 0)
    result = result.filter((event) => new Date(event.dateStart) >= fromDate)
  }

  // Фильтр по дате окончания (до)
  if (filters.value.dateTo) {
    const toDate = new Date(filters.value.dateTo)
    toDate.setHours(23, 59, 59, 999)
    result = result.filter((event) => new Date(event.dateEnd) <= toDate)
  }

  // Фильтр по адресу
  if (filters.value.address && filters.value.address.trim() !== '') {
    const searchAddress = filters.value.address.toLowerCase()
    result = result.filter(
      (event) => event.address && event.address.toLowerCase().includes(searchAddress),
    )
  }

  // Фильтр по организации
  if (filters.value.organisationId) {
    result = result.filter((event) => event.organisationId === filters.value.organisationId)
  }

  // Фильтр по типу заявки
  if (filters.value.type) {
    result = result.filter((event) => event.type === filters.value.type)
  }

  return result
})

// Сброс фильтров
const resetFilters = () => {
  filters.value = {
    title: '',
    dateFrom: null,
    dateTo: null,
    address: '',
    organisationId: null,
    type: null,
  }
}

// Навигация к созданию новой заявки
const handleCreateEntry = () => {
  router.push({ name: 'entry-create' })
}

// Загрузка всех заявок (массовые + индивидуальные)
const loadAllEvents = async () => {
  isLoading.value = true
  error.value = null

  try {
    // Заглушка: GET /events/mass с параметрами фильтрации
    // const params = new URLSearchParams()
    // if (filters.value.title) params.append('title', filters.value.title)
    // if (filters.value.dateFrom) params.append('dateFrom', filters.value.dateFrom.toISOString().split('T')[0])
    // if (filters.value.dateTo) params.append('dateTo', filters.value.dateTo.toISOString().split('T')[0])
    // if (filters.value.address) params.append('address', filters.value.address)
    // if (filters.value.organisationId) params.append('organisationId', filters.value.organisationId)
    // const massEventsResponse = await fetch(`${API_BASE_URL}/events/mass?${params}`)
    // const massEvents = await massEventsResponse.json()

    // Заглушка: GET /events/individual с параметрами фильтрации
    // const individualEventsResponse = await fetch(`${API_BASE_URL}/events/individual?${params}`)
    // const individualEvents = await individualEventsResponse.json()

    // Имитация задержки сети
    await new Promise((resolve) => setTimeout(resolve, 500))

    // Временные mock данные (до подключения API)
    const mockMassEvents = [
      {
        id: 1,
        type: 'MASS',
        organisationId: 1,
        title: 'Помощь в организации городского марафона',
        description:
          'Требуются добровольцы для помощи в организации городского благотворительного марафона. Обязанности: помощь на регистрации участников, работа на пунктах питания.',
        volunteersRequired: 20,
        ageRestriction: 18,
        dateStart: '2026-03-15T09:00:00',
        dateEnd: '2026-03-15T18:00:00',
        address: 'Центральный парк, г. Санкт-Петербург',
        workHours: 8,
        headerImage: cogImage,
      },
      {
        id: 2,
        type: 'MASS',
        organisationId: 2,
        title: 'Уборка приюта для животных',
        description:
          'Нужна помощь в уборке и уходе за животными в приюте. Задачи: уборка вольеров, кормление, прогулки с собаками. Приветствуется любовь к животным!',
        volunteersRequired: 5,
        ageRestriction: 16,
        dateStart: '2026-02-20T10:00:00',
        dateEnd: '2026-02-20T16:00:00',
        address: 'Приют "Добрые руки", ул. Садовая 25',
        workHours: 6,
        headerImage: pawImage,
      },
    ]

    const mockIndividualEvents = [
      {
        id: 3,
        type: 'INDIVIDUAL',
        organisationId: null,
        title: 'Помощь пожилому человеку',
        description:
          'Нужен волонтер для помощи пожилому человеку с покупками и уборкой дома. Задачи: сходить в магазин, провести легкую уборку, пообщаться.',
        volunteersRequired: 1,
        ageRestriction: 18,
        dateStart: '2026-02-10T14:00:00',
        dateEnd: '2026-02-10T17:00:00',
        address: 'ул. Пушкина, д. 12, кв. 45',
        headerImage: cogImage,
      },
    ]

    // Объединение массовых и индивидуальных заявок
    const events = [...mockMassEvents, ...mockIndividualEvents]

    // Сортировка по дате начала (ближайшие сначала)
    events.sort((a, b) => new Date(a.dateStart) - new Date(b.dateStart))

    allEvents.value = events
  } catch (err) {
    console.error('Ошибка загрузки заявок:', err)
    error.value = 'Не удалось загрузить заявки. Попробуйте позже.'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadAllEvents()
})
</script>

<template>
  <div class="search-view">
    <!-- Заголовок с кнопкой создания -->
    <div class="search-header">
      <h1>Поиск заявок</h1>
      <Button @click="handleCreateEntry" severity="success">
        <i class="pi pi-plus" />
        <span>Создать заявку</span>
      </Button>
    </div>

    <!-- Панель фильтров -->
    <EventFilters
      :filters="filters"
      :organizations="organizations"
      :eventTypes="eventTypes"
      @update:filters="filters = $event"
      @reset="resetFilters"
      class="filters-wrapper"
    />

    <!-- Индикатор загрузки -->
    <div v-if="isLoading" class="loading-container">
      <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
      <p>Загрузка заявок...</p>
    </div>

    <!-- Сообщение об ошибке -->
    <div v-else-if="error" class="error-container">
      <i class="pi pi-exclamation-triangle" style="font-size: 2rem; color: var(--red-500)"></i>
      <p>{{ error }}</p>
      <button @click="loadAllEvents" class="retry-button">Повторить попытку</button>
    </div>

    <!-- Список заявок -->
    <div v-else-if="filteredEvents.length > 0" class="search-results">
      <div class="results-header">
        <p>
          Найдено заявок: <strong>{{ filteredEvents.length }}</strong>
        </p>
      </div>
      <div class="search-container">
        <div class="search-container__col" v-for="event in filteredEvents" :key="event.id">
          <EventCard :event="event" />
        </div>
      </div>
    </div>

    <!-- Пустое состояние с фильтрами -->
    <div v-else-if="allEvents.length > 0" class="empty-container">
      <i class="pi pi-search" style="font-size: 3rem; color: var(--surface-400)"></i>
      <p>По заданным фильтрам ничего не найдено</p>
      <Button @click="resetFilters" severity="secondary">
        <i class="pi pi-filter-slash" />
        <span>Сбросить фильтры</span>
      </Button>
    </div>

    <!-- Пустое состояние без данных -->
    <div v-else class="empty-container">
      <i class="pi pi-inbox" style="font-size: 3rem; color: var(--surface-400)"></i>
      <p>Заявки не найдены</p>
    </div>
  </div>
</template>

<style scoped>
.search-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-l);
  padding: var(--space-l) 0;
  min-height: 60vh;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 60%;
  margin-bottom: var(--space-m);
}

.search-header h1 {
  font-family: 'Nunito', sans-serif;
  margin: 0;
  font-size: 2rem;
}

/* Панель фильтров */
.filters-wrapper {
  width: 60%;
  margin-bottom: var(--space-m);
}

/* Результаты поиска */
.search-results {
  width: 60%;
}

.results-header {
  margin-bottom: var(--space-m);
  padding: var(--space-s);
  background: var(--surface-50);
  border-radius: var(--border-radius);
  text-align: center;
}

.results-header p {
  margin: 0;
  font-size: 1rem;
  color: var(--text-color-secondary);
}

.search-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: var(--space-l);
}

.loading-container,
.error-container,
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-m);
  padding: var(--space-xl);
  color: var(--text-color-secondary);
}

.error-container {
  color: var(--red-500);
}

.retry-button {
  padding: var(--space-s) var(--space-l);
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s;
}

.retry-button:hover {
  background: var(--primary-600);
}

@media (max-width: 1200px) {
  .search-header,
  .filters-wrapper,
  .search-results {
    width: 80%;
  }
}

@media (max-width: 768px) {
  .search-header,
  .filters-wrapper,
  .search-results {
    width: 90%;
  }

  .search-header {
    flex-direction: column;
    gap: var(--space-m);
    align-items: flex-start;
  }

  .filters-grid {
    grid-template-columns: 1fr;
  }

  .search-container {
    grid-template-columns: 1fr;
  }
}
</style>
