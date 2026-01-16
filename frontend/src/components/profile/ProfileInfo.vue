<script setup>
import { Button, DatePicker, InputText, Rating, Textarea } from 'primevue'
import { ref } from 'vue'

/** @typedef {import('@/types/user').UserData} UserData */

/** @type {import('vue').Ref<UserData|null>} */
const userData = defineModel()

const isEditing = ref(false)

const toggleEditing = () => {
  isEditing.value = !isEditing.value
}
</script>

<template>
  <div class="profile-container">
    <span class="profile-username">
      <i class="pi pi-user" />
      <InputText
        class="input-element"
        placeholder="Имя пользователя"
        v-model="userData.username"
        :readonly="!isEditing"
      />
    </span>
    <span class="profile-birthday">
      <i class="pi pi-calendar" />
      <DatePicker
        class="input-element"
        placeholder="День рождения"
        v-model="userData.birthday"
        :readonly="!isEditing"
      />
    </span>
    <span class="profile-location">
      <i class="pi pi-building" />
      <InputText
        class="input-element"
        placeholder="Место жительства"
        v-model="userData.location"
        :readonly="!isEditing"
      />
    </span>
    <span class="profile-description">
      <i class="pi pi-align-justify" />
      <Textarea
        class="input-element"
        placeholder="О себе"
        v-model="userData.description"
        auto-resize
        :readonly="!isEditing"
      />
    </span>
    <span class="profile-rating">
      Рейтинг:
      <Rating class="input-element" v-model="userData.rating" readonly />
    </span>
    <span class="profile-volunteer-hours"> Отработано часов: {{ userData.volunteerHours }} </span>
    <div class="edit-buttons">
      <template v-if="isEditing">
        <Button @click="toggleEditing" size="small">
          <i class="pi pi-check" />
          <span>Сохранить</span>
        </Button>
        <Button @click="toggleEditing" size="small" severity="danger">
          <i class="pi pi-times" />
          <span>Отмена</span>
        </Button>
      </template>
      <template v-else>
        <Button @click="toggleEditing" size="small">
          <i class="pi pi-pencil" />
          <span>Редактировать</span>
        </Button>
      </template>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  justify-self: center;
  justify-items: center;
}

.profile-container > * {
  margin-bottom: var(--space-m);
}

.profile-container i {
  margin-right: var(--space-s);
}

.edit-buttons {
  display: flex;
  flex-direction: row;
}

.edit-buttons .p-button {
  margin-right: var(--space-s);
}

span {
  font-family: Nunito, sans-serif;
}
</style>
