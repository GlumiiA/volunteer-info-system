<script setup>
import { DataTable, Column, Rating } from 'primevue'
import { ref } from 'vue'

/**
 * @typedef {import('@/types/leaderboard').LeaderboardEntry} LeaderboardEntry
 */

//  Replace example data with null
/**
 * @type {import('vue').Ref<LeaderboardEntry|null>}
 */
const entries = ref([
  {
    place: 1,
    name: 'Timofey',
    hours: 1337,
    rating: 4,
  },
])
</script>

<template>
  <DataTable :value="entries">
    <Column field="place" header="Место" header-class="column-header" body-class="column-body" />
    <Column field="name" header="Волонтер" header-class="column-header" body-class="column-body" />
    <Column field="hours" header="Часы" header-class="column-header" body-class="column-body" />
    <Column field="rating" header="Рейтинг" header-class="column-header" body-class="column-body">
      <template #body="{ data }">
        <Rating v-model="data.rating" readonly />
      </template>
    </Column>
  </DataTable>
</template>

<style scoped>
:deep(.column-header) {
  font-family: Nunito, sans-serif;
  font-size: large;
}

:deep(.column-body) {
  font-family: Nunito, sans-serif;
}
</style>
