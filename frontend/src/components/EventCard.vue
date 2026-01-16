<script setup>
import { Card } from 'primevue'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

/**
 * @typedef {import('@/types/event').EventEntry} EventEntry
 * @typedef {import('@/types/event').EventType} EventType
 */

const router = useRouter()

const props = defineProps({
  event: {
    type: Object,
    required: true,
  },
})

const shortenedDescription = computed(() => {
  const maxLength = 100
  return props.event.description.substring(0, maxLength).replace(/\s\S*$/, '') + '...'
})

const handleClick = () => {
  router.push({ name: 'entry-view', params: { id: props.event.id } })
}
</script>

<template>
  <Card class="event-card" @click="handleClick">
    <template #header>
      <img class="event-header" alt="header" :src="event.headerImage" />
    </template>
    <template #title>
      <span class="event-title">
        {{ event.title }}
      </span>
    </template>
    <template #content>
      <div class="event-content">
        <div class="event-description">{{ shortenedDescription }}</div>
        <div class="event-date">
          <i class="pi pi-calendar" />
          <span>{{ event.dateStart }}-{{ event.dateEnd }}</span>
        </div>
        <div class="event-address" v-if="event.address">
          <i class="pi pi-building" />
          <span>{{ event.address }}</span>
        </div>
      </div>
    </template>
  </Card>
</template>

<style scoped>
.event-card {
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.event-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.event-header {
  display: block;
  margin: auto;
  width: 50%;
}

.event-title,
.event-description,
.event-date,
.event-address {
  font-family: 'Roboto Flex', sans-serif;
}

.event-description,
.event-date,
.event-address {
  margin-bottom: var(--space-m);
}

.event-date > i,
.event-address > i {
  margin-right: var(--space-s);
}
</style>
