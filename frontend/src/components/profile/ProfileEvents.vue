<script setup>
import { Divider } from 'primevue'

/** @typedef {import('@/types/event'.EventEntry)} EventEntry */

/** @type EventEntry[] */
const events = defineModel()
</script>

<template>
  <div class="event-container">
    <div class="event-list" v-for="event in events" :key="event.id">
      <h4 class="event-header">{{ event.title }}</h4>
      <div class="event-date">
        <i class="pi pi-calendar" />
        <span>{{ event.dateStart }}</span>
      </div>
      <Divider />
    </div>
  </div>
</template>

<style scoped>
.event-header {
  font-family: Nunito, sans-serif;
  font-weight: bolder;
}

span {
  font-family: Nunito, sans-serif;
}

.event-date > i {
  margin-right: var(--space-s);
}
</style>
