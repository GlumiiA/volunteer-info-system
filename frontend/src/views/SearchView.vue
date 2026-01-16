<script setup>
import EventCard from '@/components/EventCard.vue'

const event = {
  title: 'Car washing',
  description:
    'Требуются добровольцы для отмывки машины. Все необходимые инструменты есть на месте, достаточно ваше желание поучаствовать. Оставляю свои контакты, если появятся дальнейшие вопросы: sample@email.com',
  volunteersRequired: 2,
  ageRestriction: 16,
  dateStart: new Date(2025, 11, 3).toISOString().split('T')[0],
  dateEnd: new Date(2025, 11, 3).toISOString().split('T')[0],
  address: 'Pososi street, -1337 house',
  headerImage: 'src/assets/images/cog.png',
}
</script>

<template>
  <div class="search-container">
    <div class="search-container__col">
      <EventCard :event="event" />
    </div>
    <div class="search-container__col"></div>
    <div class="search-container__col"></div>
  </div>
</template>

<style scoped>
.search-container {
  justify-self: center;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  width: 60%;
}
</style>
