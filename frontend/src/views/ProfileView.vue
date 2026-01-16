<script setup>
import ProfileEvents from '@/components/profile/ProfileEvents.vue'
import ProfileInfo from '@/components/profile/ProfileInfo.vue'
import { TabList, TabPanel, TabPanels, Tabs, Tab } from 'primevue'
import { ref } from 'vue'

/** @typedef {import('@/types/user').UserData} UserData */
/** @typedef {import('@/types/event'.EventEntry)} EventEntry */

/** @type {import('vue').Ref<UserData|null>} */
const userData = ref({
  username: 'Timofey',
  birthday: '08/09/2005',
  location: 'Novosibirsk',
  description: 'Sample description',
  rating: 4.8,
  volunteerHours: 120,
})

/** @type {import('vue').Ref<EventEntry|null>} */
const participatedEvents = ref([])

/** @type {import('vue').Ref<EventEntry|null>} */
const organizedEvents = ref([])
</script>

<template>
  <Tabs value="0">
    <TabList>
      <Tab value="0">О волонтере</Tab>
      <Tab value="1">Волонтерская книжка</Tab>
      <Tab value="2">Организованные мероприятия</Tab>
    </TabList>
    <TabPanels>
      <TabPanel value="0">
        <ProfileInfo v-model="userData" />
      </TabPanel>
      <TabPanel value="1">
        <ProfileEvents v-model="participatedEvents" />
      </TabPanel>
      <TabPanel value="2">
        <ProfileEvents v-model="organizedEvents" />
      </TabPanel>
    </TabPanels>
  </Tabs>
</template>
