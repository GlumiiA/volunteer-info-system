<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import {
  Panel,
  Card,
  Button,
  InputText,
  Textarea,
  InputNumber,
  Calendar,
  Dropdown,
  FileUpload,
  Message,
  Toast,
  SelectButton,
} from 'primevue'

import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const toast = useToast()
const { user, isOrgRepresentative } = useAuth()

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

// Доступные типы заявок в зависимости от роли
const availableEntryTypes = computed(() => {
  if (isOrgRepresentative.value) {
    return [
      { label: 'От себя (индивидуальная заявка)', value: 'INDIVIDUAL' },
      { label: 'От организации (корпоративная заявка)', value: 'MASS' },
    ]
  }
  return [{ label: 'От себя (индивидуальная заявка)', value: 'INDIVIDUAL' }]
})

// Получить название организации по ID
const getOrganizationName = () => {
  const org = organizations.value.find((o) => o.value === user.value?.organisationId)
  return org ? org.label : 'вашей организации'
}

// Заглушка данных для создания
const entryForm = ref({
  type: 'INDIVIDUAL',
  title: '',
  description: '',
  volunteersRequired: 1,
  ageRestriction: 0,
  dateStart: null,
  dateEnd: null,
  address: '',
  workHours: null,
  headerImage: null,
  organisationId: null,
})

// Заглушка списка организаций
const organizations = ref([
  { label: 'Спортивный фонд "Движение"', value: 1 },
  { label: 'Благотворительный фонд "Помощь"', value: 2 },
  { label: 'Волонтерский центр ИТМО', value: 3 },
])

// Состояние формы
const isSubmitting = ref(false)
const errors = ref({})

// Инициализация при монтировании
onMounted(() => {
  if (isOrgRepresentative.value) {
    entryForm.value.organisationId = user.value?.organisationId
  }
})

const validateForm = () => {
  errors.value = {}

  if (!entryForm.value.title || entryForm.value.title.trim() === '') {
    errors.value.title = 'Название обязательно'
  }

  if (!entryForm.value.description || entryForm.value.description.trim() === '') {
    errors.value.description = 'Описание обязательно'
  }

  if (!entryForm.value.volunteersRequired || entryForm.value.volunteersRequired < 1) {
    errors.value.volunteersRequired = 'Укажите количество волонтеров (минимум 1)'
  }

  if (entryForm.value.ageRestriction === null || entryForm.value.ageRestriction < 0) {
    errors.value.ageRestriction = 'Укажите возрастное ограничение'
  }

  if (!entryForm.value.dateStart) {
    errors.value.dateStart = 'Укажите дату начала'
  }

  if (!entryForm.value.dateEnd) {
    errors.value.dateEnd = 'Укажите дату окончания'
  }

  if (
    entryForm.value.dateStart &&
    entryForm.value.dateEnd &&
    entryForm.value.dateEnd < entryForm.value.dateStart
  ) {
    errors.value.dateEnd = 'Дата окончания не может быть раньше даты начала'
  }

  if (entryForm.value.type === 'MASS') {
    if (!entryForm.value.organisationId) {
      errors.value.organisationId = 'Выберите организацию для корпоративной заявки'
    }
    if (!entryForm.value.address || entryForm.value.address.trim() === '') {
      errors.value.address = 'Адрес обязателен для корпоративной заявки'
    }
    if (!entryForm.value.workHours || entryForm.value.workHours <= 0) {
      errors.value.workHours = 'Укажите количество часов работы'
    }
  }

  return Object.keys(errors.value).length === 0
}

const handleSubmit = async () => {
  if (!validateForm()) {
    toast.add({
      severity: 'warn',
      summary: 'Проверьте форму',
      detail: 'Не все обязательные поля заполнены корректно',
      life: 3000,
    })
    return
  }

  // Проверка роли для корпоративных заявок
  if (entryForm.value.type === 'MASS' && !isOrgRepresentative.value) {
    toast.add({
      severity: 'error',
      summary: 'Доступ запрещен',
      detail: 'Только представители организаций могут создавать корпоративные заявки',
      life: 3000,
    })
    return
  }

  isSubmitting.value = true

  try {
    // Определение endpoint в зависимости от типа заявки
    const endpoint =
      entryForm.value.type === 'MASS' ? `${API_BASE_URL}/events/mass` : `${API_BASE_URL}/events/individual`

    // Подготовка данных для отправки
    const requestData = {
      title: entryForm.value.title,
      description: entryForm.value.description,
      dateStart: entryForm.value.dateStart?.toISOString(),
      dateEnd: entryForm.value.dateEnd?.toISOString(),
      volunteersRequired: entryForm.value.volunteersRequired,
      ageRestriction: entryForm.value.ageRestriction,
    }

    // Дополнительные поля только для корпоративных заявок
    if (entryForm.value.type === 'MASS') {
      requestData.address = entryForm.value.address
      requestData.workHours = entryForm.value.workHours
      // organisationId будет взят из токена авторизации на бэкенде
    }

    // Получение токена из localStorage
    const authToken = localStorage.getItem('auth_token')
    if (!authToken) {
      throw new Error('Требуется авторизация для создания заявки')
    }

    // Отправка запроса к API
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`,
      },
      body: JSON.stringify(requestData),
    })

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || `HTTP ${response.status}: ${response.statusText}`)
    }

    const savedEntry = await response.json()

    console.log('Заявка создана:', savedEntry)

    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Заявка успешно создана',
      life: 3000,
    })

    // Переход к странице просмотра созданной заявки
    router.push({ 
      name: 'entry-view', 
      params: { 
        id: savedEntry.id,
        type: entryForm.value.type.toLowerCase()
      } 
    })
  } catch (err) {
    console.error('Ошибка создания заявки:', err)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось создать заявку. Попробуйте еще раз.',
      life: 3000,
    })
  } finally {
    isSubmitting.value = false
  }
}

const handleCancel = () => {
  router.back()
}

const handleFileUpload = (event) => {
  console.log('Загрузка файла:', event.files)
  // Здесь будет логика загрузки изображения на сервер
}

// Смена роли для тестирования
</script>

<template>
  <Toast />
  <Panel class="entry-create-panel">
    <div class="entry-create-header">
      <h1>Создание заявки</h1>
      <p v-if="isOrgRepresentative" class="role-badge">
        <i class="pi pi-building"></i> Представитель организации
      </p>
      <p v-else class="role-badge">
        <i class="pi pi-user"></i> Пользователь
      </p>
    </div>

    <Card>
      <template #content>
        <form @submit.prevent="handleSubmit" class="entry-form">
          <!-- Тип заявки -->
          <div class="form-field">
            <label for="type" class="form-label">От чьего имени публиковать заявку *</label>
            <Dropdown
              id="type"
              v-model="entryForm.type"
              :options="availableEntryTypes"
              optionLabel="label"
              optionValue="value"
              placeholder="Выберите тип заявки"
              class="w-full"
            />
            <small v-if="entryForm.type === 'INDIVIDUAL'" class="field-hint">
              Заявка будет опубликована от вашего имени
            </small>
            <small v-else-if="entryForm.type === 'MASS' && isOrgRepresentative" class="field-hint">
              Заявка будет опубликована от имени организации "{{ getOrganizationName() }}"
            </small>
            <small v-else-if="!isOrgRepresentative" class="field-hint">
              Корпоративные заявки доступны только для представителей организаций
            </small>
          </div>

          <!-- Название -->
          <div class="form-field">
            <label for="title" class="form-label">Название *</label>
            <InputText
              id="title"
              v-model="entryForm.title"
              placeholder="Введите название заявки"
              class="w-full"
              :class="{ 'p-invalid': errors.title }"
            />
            <small v-if="errors.title" class="p-error">{{ errors.title }}</small>
          </div>

          <!-- Описание -->
          <div class="form-field">
            <label for="description" class="form-label">Описание *</label>
            <Textarea
              id="description"
              v-model="entryForm.description"
              placeholder="Опишите заявку подробно"
              rows="6"
              class="w-full"
              :class="{ 'p-invalid': errors.description }"
            />
            <small v-if="errors.description" class="p-error">{{ errors.description }}</small>
          </div>

          <!-- Даты -->
          <div class="form-row">
            <div class="form-field">
              <label for="dateStart" class="form-label">Дата начала *</label>
              <Calendar
                id="dateStart"
                v-model="entryForm.dateStart"
                dateFormat="yy-mm-dd"
                placeholder="Выберите дату"
                class="w-full"
                :class="{ 'p-invalid': errors.dateStart }"
              />
              <small v-if="errors.dateStart" class="p-error">{{ errors.dateStart }}</small>
            </div>

            <div class="form-field">
              <label for="dateEnd" class="form-label">Дата окончания *</label>
              <Calendar
                id="dateEnd"
                v-model="entryForm.dateEnd"
                dateFormat="yy-mm-dd"
                placeholder="Выберите дату"
                class="w-full"
                :class="{ 'p-invalid': errors.dateEnd }"
              />
              <small v-if="errors.dateEnd" class="p-error">{{ errors.dateEnd }}</small>
            </div>
          </div>

          <!-- Адрес (только для корпоративных заявок) -->
          <div class="form-field" v-if="entryForm.type === 'MASS'">
            <label for="address" class="form-label">Адрес проведения *</label>
            <InputText
              id="address"
              v-model="entryForm.address"
              placeholder="Укажите адрес проведения мероприятия"
              class="w-full"
              :class="{ 'p-invalid': errors.address }"
            />
            <small v-if="errors.address" class="p-error">{{ errors.address }}</small>
          </div>

          <!-- Количество волонтеров и возраст -->
          <div class="form-row">
            <div class="form-field">
              <label for="volunteersRequired" class="form-label">Требуется волонтеров *</label>
              <InputNumber
                id="volunteersRequired"
                v-model="entryForm.volunteersRequired"
                :min="1"
                placeholder="0"
                class="w-full"
                :class="{ 'p-invalid': errors.volunteersRequired }"
              />
              <small v-if="errors.volunteersRequired" class="p-error">{{
                errors.volunteersRequired
              }}</small>
            </div>

            <div class="form-field">
              <label for="ageRestriction" class="form-label">Возрастное ограничение *</label>
              <InputNumber
                id="ageRestriction"
                v-model="entryForm.ageRestriction"
                :min="0"
                placeholder="0"
                suffix="+"
                class="w-full"
                :class="{ 'p-invalid': errors.ageRestriction }"
              />
              <small v-if="errors.ageRestriction" class="p-error">{{
                errors.ageRestriction
              }}</small>
            </div>
          </div>

          <!-- Часы работы (только для корпоративных заявок) -->
          <div class="form-field" v-if="entryForm.type === 'MASS'">
            <label for="workHours" class="form-label">Часов работы *</label>
            <InputNumber
              id="workHours"
              v-model="entryForm.workHours"
              :min="0"
              :step="0.5"
              placeholder="0"
              suffix=" ч."
              class="w-full"
              :class="{ 'p-invalid': errors.workHours }"
            />
            <small class="field-hint"
              >Количество часов, которые волонтер отработает в рамках мероприятия</small
            >
            <small v-if="errors.workHours" class="p-error">{{ errors.workHours }}</small>
          </div>

          <!-- Загрузка изображения -->
          <div class="form-field">
            <label class="form-label">Изображение заголовка</label>
            <FileUpload
              mode="basic"
              accept="image/*"
              :maxFileSize="5000000"
              chooseLabel="Выбрать изображение"
              @select="handleFileUpload"
              :auto="true"
              customUpload
            />
            <small class="field-hint">Максимальный размер: 5 МБ</small>
          </div>

          <!-- Информационное сообщение -->
          <Message severity="info" :closable="false">
            * - обязательные для заполнения поля
          </Message>

          <!-- Кнопки действий -->
          <div class="form-actions">
            <Button
              type="submit"
              :loading="isSubmitting"
              :disabled="isSubmitting"
            >
              <i class="pi pi-check" />
              <span>Создать заявку</span>
            </Button>
            <Button
              type="button"
              severity="secondary"
              outlined
              @click="handleCancel"
            >
              <i class="pi pi-times" />
              <span>Отмена</span>
            </Button>
          </div>
        </form>
      </template>
    </Card>
  </Panel>
</template>

<style scoped>
.entry-create-panel {
  max-width: 900px;
  margin: 0 auto;
}


.entry-create-header {
  margin-bottom: var(--space-l);
}

.entry-create-header h1 {
  margin: 0 0 var(--space-s) 0;
  font-family: 'Nunito', sans-serif;
  font-weight: 700;
  font-size: 2rem;
  color: var(--text-color);
}

.role-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-s);
  padding: var(--space-xs) var(--space-m);
  background: var(--primary-50);
  color: var(--primary-700);
  border-radius: var(--border-radius);
  font-size: 0.9rem;
  font-family: 'Roboto Flex', sans-serif;
  margin: 0;
}

.entry-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-l);
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: var(--space-s);
}

.form-label {
  font-family: 'Roboto Flex', sans-serif;
  font-weight: 500;
  font-size: 0.95rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-l);
}

.field-hint {
  font-family: 'Roboto Flex', sans-serif;
  font-size: 0.85rem;
  color: #666;
}

.form-actions {
  display: flex;
  gap: var(--space-m);
  margin-top: var(--space-l);
}

.w-full {
  width: 100%;
}

.p-error {
  color: #e24c4c;
  font-size: 0.85rem;
  font-family: 'Roboto Flex', sans-serif;
}

.p-invalid {
  border-color: #e24c4c;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
