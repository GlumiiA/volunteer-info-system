<script setup>
import { Badge, Button, Tag } from 'primevue'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

/** @typedef {import('@/types/event'.EventEntry)} EventEntry */

const props = defineProps({
  title: {
    type: String,
    default: 'Мероприятия'
  }
})

const router = useRouter()

/** @type EventEntry[] */
const events = defineModel()

const hasEvents = computed(() => events.value && events.value.length > 0)

const handleEventClick = (event) => {
  router.push({ name: 'entry-view', params: { id: event.id } })
}

const formatDate = (dateString) => {
  if (!dateString) return 'Не указано'
  const date = new Date(dateString)
  return date.toLocaleDateString('ru-RU', { 
    day: 'numeric', 
    month: 'long', 
    year: 'numeric' 
  })
}

const getEventStatus = (event) => {
  const now = new Date()
  const start = new Date(event.dateStart)
  const end = new Date(event.dateEnd)
  
  if (end < now) return 'completed'
  if (start > now) return 'upcoming'
  return 'active'
}

const getStatusLabel = (status) => {
  const labels = {
    completed: 'Завершено',
    upcoming: 'Предстоит',
    active: 'В процессе'
  }
  return labels[status] || 'Неизвестно'
}

const getStatusSeverity = (status) => {
  const severities = {
    completed: 'secondary',
    upcoming: 'info',
    active: 'success'
  }
  return severities[status] || 'info'
}
</script>

<template>
  <div class="events-container">
    <div v-if="hasEvents" class="events-grid">
      <div v-for="event in events" :key="event.id" class="event-card">
        <div class="event-header">
          <h4 class="event-title">{{ event.title }}</h4>
          <Tag 
            :value="getStatusLabel(getEventStatus(event))" 
            :severity="getStatusSeverity(getEventStatus(event))"
          />
        </div>
        
        <p v-if="event.description" class="event-description">
          {{ event.description }}
        </p>
        
        <div class="event-details">
          <div class="event-detail-item">
            <i class="pi pi-calendar"></i>
            <div>
              <div class="detail-label">Начало</div>
              <div class="detail-value">{{ formatDate(event.dateStart) }}</div>
            </div>
          </div>
          
          <div class="event-detail-item">
            <i class="pi pi-calendar-times"></i>
            <div>
              <div class="detail-label">Окончание</div>
              <div class="detail-value">{{ formatDate(event.dateEnd) }}</div>
            </div>
          </div>
          
          <div v-if="event.address" class="event-detail-item">
            <i class="pi pi-map-marker"></i>
            <div>
              <div class="detail-label">Адрес</div>
              <div class="detail-value">{{ event.address }}</div>
            </div>
          </div>
          
          <div v-if="event.workHours" class="event-detail-item">
            <i class="pi pi-clock"></i>
            <div>
              <div class="detail-label">Часов работы</div>
              <div class="detail-value">{{ event.workHours }}</div>
            </div>
          </div>
        </div>
        
        <div class="event-footer">
          <div v-if="event.volunteersRequired" class="volunteers-info">
            <i class="pi pi-users"></i>
            <span>{{ event.volunteersRequired }} волонтеров</span>
          </div>
          <Button 
            label="Подробнее" 
            size="small" 
            outlined 
            icon="pi pi-arrow-right" 
            iconPos="right"
            @click="handleEventClick(event)"
          />
        </div>
      </div>
    </div>
    
    <div v-else class="empty-state">
      <i class="pi pi-calendar-times empty-icon"></i>
      <h3>Нет мероприятий</h3>
      <p>У вас пока нет мероприятий в этой категории</p>
    </div>
  </div>
</template>

<style scoped>
.events-container {
  padding: var(--space-l) 0;
}

.events-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: var(--space-l);
}

.event-card {
  background: var(--surface-card);
  border-radius: 12px;
  padding: var(--space-xl);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  border: 1px solid var(--surface-border);
  display: flex;
  flex-direction: column;
  gap: var(--space-l);
}

.event-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.event-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: var(--space-m);
}

.event-title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-color);
  flex: 1;
  line-height: 1.4;
}

.event-description {
  margin: 0;
  color: var(--text-color-secondary);
  line-height: 1.6;
  font-size: 0.938rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.event-details {
  display: flex;
  flex-direction: column;
  gap: var(--space-m);
  padding: var(--space-l);
  background: var(--surface-ground);
  border-radius: 8px;
}

.event-detail-item {
  display: flex;
  align-items: flex-start;
  gap: var(--space-m);
}

.event-detail-item i {
  color: var(--primary-color);
  font-size: 1.125rem;
  margin-top: 2px;
}

.detail-label {
  font-size: 0.75rem;
  color: var(--text-color-secondary);
  text-transform: uppercase;
  font-weight: 600;
  letter-spacing: 0.5px;
  margin-bottom: var(--space-xs);
}

.detail-value {
  font-size: 0.938rem;
  color: var(--text-color);
  font-weight: 500;
}

.event-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--space-l);
  border-top: 1px solid var(--surface-border);
}

.volunteers-info {
  display: flex;
  align-items: center;
  gap: var(--space-s);
  color: var(--text-color-secondary);
  font-size: 0.875rem;
  font-weight: 600;
}

.volunteers-info i {
  color: var(--primary-color);
}

.empty-state {
  text-align: center;
  padding: var(--space-2xl);
  color: var(--text-color-secondary);
}

.empty-icon {
  font-size: 4rem;
  color: var(--surface-400);
  margin-bottom: var(--space-l);
}

.empty-state h3 {
  margin: 0 0 var(--space-s);
  color: var(--text-color);
  font-size: 1.5rem;
}

.empty-state p {
  margin: 0;
  font-size: 1rem;
}

@media (max-width: 768px) {
  .events-grid {
    grid-template-columns: 1fr;
  }
  
  .event-footer {
    flex-direction: column;
    gap: var(--space-m);
    align-items: stretch;
  }
}
</style>
