<script setup>
import { Button, DatePicker, InputText, Rating, Textarea } from 'primevue'
import { ref, computed } from 'vue'
import { useAuth } from '@/composables/useAuth'
import OrganizationRequestForm from './OrganizationRequestForm.vue'

/** @typedef {import('@/types/user').UserData} UserData */

/** @type {import('vue').Ref<UserData|null>} */
const userData = defineModel()

const { user } = useAuth()

const isEditing = ref(false)
const showOrgRequestDialog = ref(false)

// Определяем роль на основе userData, а не глобального user
const isOrgRepresentative = computed(() => userData.value?.role === 'ORG_REPRESENTATIVE')
const isUser = computed(() => userData.value?.role === 'USER')

const toggleEditing = () => {
  isEditing.value = !isEditing.value
}

const handleRequestSubmitted = () => {
  // Можно добавить логику обновления данных пользователя
  console.log('Organization request submitted')
}
</script>

<template>
  <div class="profile-container">
    <!-- Основная информация -->
    <div class="info-card">
      <h3 class="card-title">
        <i class="pi pi-id-card"></i>
        Основная информация
      </h3>
      <div class="info-grid">
        <div class="info-field">
          <label class="field-label">
            <i class="pi pi-user"></i>
            Имя пользователя
          </label>
          <InputText
            v-model="userData.name"
            placeholder="Введите ваше имя"
            :readonly="!isEditing"
            class="field-input"
          />
        </div>

        <div class="info-field">
          <label class="field-label">
            <i class="pi pi-calendar"></i>
            День рождения
          </label>
          <DatePicker
            v-model="userData.birthday"
            placeholder="Выберите дату"
            :readonly="!isEditing"
            class="field-input"
            dateFormat="dd.mm.yy"
          />
        </div>

        <div class="info-field">
          <label class="field-label">
            <i class="pi pi-map-marker"></i>
            Место жительства
          </label>
          <InputText
            v-model="userData.location"
            placeholder="Ваш город"
            :readonly="!isEditing"
            class="field-input"
          />
        </div>
      </div>

      <div class="info-field full-width">
        <label class="field-label">
          <i class="pi pi-align-justify"></i>
          О себе
        </label>
        <Textarea
          v-model="userData.description"
          placeholder="Расскажите о себе, ваших интересах и навыках..."
          :readonly="!isEditing"
          auto-resize
          rows="4"
          class="field-input"
        />
      </div>

      <div class="button-group">
        <template v-if="isEditing">
          <Button @click="toggleEditing" severity="success">
            <i class="pi pi-check"></i>
            Сохранить
          </Button>
          <Button @click="toggleEditing" severity="secondary" outlined>
            <i class="pi pi-times"></i>
            Отмена
          </Button>
        </template>
        <template v-else>
          <Button @click="toggleEditing">
            <i class="pi pi-pencil"></i>
            Редактировать профиль
          </Button>
        </template>
      </div>
    </div>

    <!-- Информация об организации для ПрОрг -->
    <div v-if="isOrgRepresentative && userData.organizationName" class="info-card org-card">
      <h3 class="card-title">
        <i class="pi pi-building"></i>
        Организация
      </h3>
      <div class="org-details">
        <div class="org-name-section">
          <i class="pi pi-briefcase org-icon"></i>
          <div>
            <div class="org-name">{{ userData.organizationName }}</div>
            <div class="org-role">Представитель организации</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Достижения и рейтинг -->
    <div class="info-card achievements-card">
      <h3 class="card-title">
        <i class="pi pi-trophy"></i>
        Достижения
      </h3>
      <div class="achievements-grid">
        <div class="achievement-item">
          <div class="achievement-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
            <i class="pi pi-star-fill"></i>
          </div>
          <div class="achievement-info">
            <div class="achievement-label">Рейтинг</div>
            <div class="achievement-value">
              <Rating v-model="userData.rating" readonly :cancel="false" />
              <span class="rating-number">{{ userData.rating?.toFixed(1) || 'N/A' }}</span>
            </div>
          </div>
        </div>
        <div class="achievement-item">
          <div class="achievement-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
            <i class="pi pi-clock"></i>
          </div>
          <div class="achievement-info">
            <div class="achievement-label">Волонтерских часов</div>
            <div class="achievement-value">{{ userData.volunteerHours || 0 }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Кнопка запроса статуса ПрОрг для обычных пользователей -->
    <div v-if="isUser" class="info-card org-request-card">
      <div class="org-request-content">
        <div class="org-request-icon">
          <i class="pi pi-briefcase"></i>
        </div>
        <div class="org-request-text">
          <h4>Стать представителем организации</h4>
          <p>Получите возможность создавать массовые мероприятия от имени организации</p>
        </div>
      </div>
      <Button 
        @click="showOrgRequestDialog = true" 
        severity="info"
        size="large"
      >
        <i class="pi pi-send"></i>
        Подать запрос
      </Button>
    </div>

    <OrganizationRequestForm 
      v-model:visible="showOrgRequestDialog"
      @request-submitted="handleRequestSubmitted"
    />
  </div>
</template>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  gap: var(--space-l);
}

.info-card {
  background: var(--surface-card);
  border-radius: 12px;
  padding: var(--space-xl);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.2s;
}

.info-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.card-title {
  display: flex;
  align-items: center;
  gap: var(--space-s);
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0 0 var(--space-l);
  color: var(--text-color);
}

.card-title i {
  color: var(--primary-color);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--space-l);
  margin-bottom: var(--space-l);
}

.info-field {
  display: flex;
  flex-direction: column;
  gap: var(--space-s);
}

.info-field.full-width {
  grid-column: 1 / -1;
}

.field-label {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-weight: 600;
  color: var(--text-color-secondary);
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.field-input {
  width: 100%;
}

.button-group {
  display: flex;
  gap: var(--space-m);
  justify-content: flex-start;
  padding-top: var(--space-l);
  border-top: 1px solid var(--surface-border);
}

/* Организация */
.org-card {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border: 2px solid var(--primary-color);
}

.org-details {
  display: flex;
  flex-direction: column;
  gap: var(--space-m);
}

.org-name-section {
  display: flex;
  align-items: center;
  gap: var(--space-l);
}

.org-icon {
  font-size: 2.5rem;
  color: var(--primary-color);
}

.org-name {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
  margin-bottom: var(--space-xs);
}

.org-role {
  font-size: 0.875rem;
  color: var(--text-color-secondary);
  font-weight: 500;
}

/* Достижения */
.achievements-card {
  background: linear-gradient(135deg, rgba(240, 147, 251, 0.05) 0%, rgba(245, 87, 108, 0.05) 100%);
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-l);
}

.achievement-item {
  display: flex;
  align-items: center;
  gap: var(--space-l);
  padding: var(--space-l);
  background: var(--surface-ground);
  border-radius: 8px;
  transition: transform 0.2s;
}

.achievement-item:hover {
  transform: translateX(4px);
}

.achievement-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
  flex-shrink: 0;
}

.achievement-info {
  flex: 1;
}

.achievement-label {
  font-size: 0.75rem;
  color: var(--text-color-secondary);
  text-transform: uppercase;
  font-weight: 600;
  letter-spacing: 0.5px;
  margin-bottom: var(--space-xs);
}

.achievement-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-color);
  display: flex;
  align-items: center;
  gap: var(--space-s);
}

.rating-number {
  font-size: 1.25rem;
  color: var(--text-color-secondary);
}

/* Запрос организации */
.org-request-card {
  background: linear-gradient(135deg, rgba(79, 172, 254, 0.1) 0%, rgba(0, 242, 254, 0.1) 100%);
  border: 2px dashed var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-xl);
}

.org-request-content {
  display: flex;
  align-items: center;
  gap: var(--space-l);
  flex: 1;
}

.org-request-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  flex-shrink: 0;
}

.org-request-text h4 {
  margin: 0 0 var(--space-xs);
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--text-color);
}

.org-request-text p {
  margin: 0;
  color: var(--text-color-secondary);
  font-size: 0.875rem;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }

  .achievements-grid {
    grid-template-columns: 1fr;
  }

  .org-request-card {
    flex-direction: column;
    text-align: center;
  }

  .org-request-content {
    flex-direction: column;
  }

  .button-group {
    flex-direction: column;
  }
}
</style>
