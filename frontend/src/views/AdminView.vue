<script setup>
import { ref, onMounted } from 'vue'
import {
  Card,
  TabView,
  TabPanel,
  DataTable,
  Column,
  Button,
  Dialog,
  InputText,
  Textarea,
} from 'primevue'
import { useAuth } from '@/composables/useAuth'

const { user } = useAuth()

// Заглушки для данных
const organizationRequests = ref([])
const organizations = ref([])
const users = ref([])

// Диалоги
const showOrgDialog = ref(false)
const showEditOrgDialog = ref(false)
const selectedOrg = ref(null)
const newOrg = ref({
  name: '',
  description: '',
  address: '',
})

// Загрузка данных (заглушки)
onMounted(async () => {
  // Мок-данные для запросов на статус ПрОрг
  organizationRequests.value = [
    {
      id: 1,
      userId: 3,
      user: {
        id: 3,
        name: 'Иван Петров',
        email: 'ivan@example.com',
      },
      requestType: 'EXISTING',
      organizationId: 1,
      organizationName: 'Красный Крест',
      status: 'PENDING',
      createdAt: new Date('2026-01-10'),
    },
    {
      id: 2,
      userId: 4,
      user: {
        id: 4,
        name: 'Мария Иванова',
        email: 'maria@example.com',
      },
      requestType: 'NEW',
      organizationName: 'Помощь животным',
      organizationDescription: 'Организация по защите бездомных животных',
      organizationAddress: 'ул. Ленина, 25',
      status: 'PENDING',
      createdAt: new Date('2026-01-12'),
    },
  ]

  // Мок-данные для организаций
  organizations.value = [
    {
      id: 1,
      name: 'Красный Крест',
      description: 'Международная организация помощи',
      address: 'Невский проспект, 100',
    },
    {
      id: 2,
      name: 'Волонтеры Победы',
      description: 'Патриотическое волонтерское движение',
      address: 'ул. Маршала Жукова, 15',
    },
  ]

  // Мок-данные для пользователей
  users.value = [
    {
      id: 1,
      name: 'Тестовый Пользователь',
      email: 'test@example.com',
      role: 'USER',
      organisationId: null,
    },
    {
      id: 2,
      name: 'Представитель Организации',
      email: 'org@example.com',
      role: 'ORG_REPRESENTATIVE',
      organisationId: 1,
    },
  ]
})

// Обработчики запросов на статус ПрОрг
const approveRequest = async (requestId) => {
  console.log('Одобрение запроса:', requestId)
  const request = organizationRequests.value.find((r) => r.id === requestId)
  if (request) {
    request.status = 'APPROVED'
  }
  // TODO: Реальный API вызов
}

const rejectRequest = async (requestId) => {
  console.log('Отклонение запроса:', requestId)
  const request = organizationRequests.value.find((r) => r.id === requestId)
  if (request) {
    request.status = 'REJECTED'
  }
  // TODO: Реальный API вызов
}

// Управление организациями
const openNewOrgDialog = () => {
  newOrg.value = { name: '', description: '', address: '' }
  showOrgDialog.value = true
}

const openEditOrgDialog = (org) => {
  selectedOrg.value = { ...org }
  showEditOrgDialog.value = true
}

const createOrganization = async () => {
  console.log('Создание организации:', newOrg.value)
  organizations.value.push({
    id: organizations.value.length + 1,
    ...newOrg.value,
  })
  showOrgDialog.value = false
  // TODO: Реальный API вызов
}

const updateOrganization = async () => {
  console.log('Обновление организации:', selectedOrg.value)
  const index = organizations.value.findIndex((o) => o.id === selectedOrg.value.id)
  if (index !== -1) {
    organizations.value[index] = { ...selectedOrg.value }
  }
  showEditOrgDialog.value = false
  // TODO: Реальный API вызов
}

const deleteOrganization = async (orgId) => {
  if (confirm('Вы уверены, что хотите удалить эту организацию?')) {
    console.log('Удаление организации:', orgId)
    organizations.value = organizations.value.filter((o) => o.id !== orgId)
    // TODO: Реальный API вызов
  }
}

// Управление ролями пользователей
const changeUserRole = async (userId, newRole) => {
  console.log('Изменение роли пользователя:', userId, newRole)
  const userObj = users.value.find((u) => u.id === userId)
  if (userObj) {
    userObj.role = newRole
  }
  // TODO: Реальный API вызов
}

const getRequestTypeName = (type) => {
  return type === 'EXISTING' ? 'Существующая организация' : 'Новая организация'
}

const getStatusSeverity = (status) => {
  switch (status) {
    case 'PENDING':
      return 'warning'
    case 'APPROVED':
      return 'success'
    case 'REJECTED':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusName = (status) => {
  switch (status) {
    case 'PENDING':
      return 'Ожидает'
    case 'APPROVED':
      return 'Одобрено'
    case 'REJECTED':
      return 'Отклонено'
    default:
      return status
  }
}

const getRoleName = (role) => {
  switch (role) {
    case 'USER':
      return 'Пользователь'
    case 'ORG_REPRESENTATIVE':
      return 'Представитель организации'
    case 'ADMIN':
      return 'Администратор'
    default:
      return role
  }
}
</script>

<template>
  <div class="admin-panel">
    <Card>
      <template #title>
        <div class="title-container">
          <i class="pi pi-shield" style="font-size: 1.5rem; margin-right: 0.5rem"></i>
          Панель администратора
        </div>
      </template>
      <template #subtitle> Добро пожаловать, {{ user?.name }} </template>
      <template #content>
        <TabView>
          <!-- Вкладка: Запросы на статус ПрОрг -->
          <TabPanel header="Запросы на статус ПрОрг">
            <DataTable :value="organizationRequests" stripedRows paginator :rows="10" dataKey="id">
              <Column field="id" header="ID" style="width: 5%"></Column>
              <Column field="user.name" header="Пользователь"></Column>
              <Column field="user.email" header="Email"></Column>
              <Column header="Тип запроса">
                <template #body="slotProps">
                  {{ getRequestTypeName(slotProps.data.requestType) }}
                </template>
              </Column>
              <Column field="organizationName" header="Организация"></Column>
              <Column header="Статус">
                <template #body="slotProps">
                  <Button
                    :label="getStatusName(slotProps.data.status)"
                    :severity="getStatusSeverity(slotProps.data.status)"
                    size="small"
                    text
                  />
                </template>
              </Column>
              <Column header="Действия" style="width: 15%">
                <template #body="slotProps">
                  <div v-if="slotProps.data.status === 'PENDING'" class="action-buttons">
                    <Button
                      severity="success"
                      size="small"
                      @click="approveRequest(slotProps.data.id)"
                      v-tooltip="'Одобрить'"
                    >
                      <i class="pi pi-check" />
                    </Button>
                    <Button
                      severity="danger"
                      size="small"
                      @click="rejectRequest(slotProps.data.id)"
                      v-tooltip="'Отклонить'"
                    >
                      <i class="pi pi-times" />
                    </Button>
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>

          <!-- Вкладка: Управление организациями -->
          <TabPanel header="Организации">
            <div class="table-header">
              <Button
                label="Добавить организацию"
                icon="pi pi-plus"
                @click="openNewOrgDialog"
                size="small"
              />
            </div>
            <DataTable :value="organizations" stripedRows paginator :rows="10" dataKey="id">
              <Column field="id" header="ID" style="width: 5%"></Column>
              <Column field="name" header="Название"></Column>
              <Column field="description" header="Описание"></Column>
              <Column field="address" header="Адрес"></Column>
              <Column header="Действия" style="width: 12%">
                <template #body="slotProps">
                  <div class="action-buttons">
                    <Button
                      icon="pi pi-pencil"
                      severity="info"
                      size="small"
                      @click="openEditOrgDialog(slotProps.data)"
                      v-tooltip="'Редактировать'"
                    />
                    <Button
                      icon="pi pi-trash"
                      severity="danger"
                      size="small"
                      @click="deleteOrganization(slotProps.data.id)"
                      v-tooltip="'Удалить'"
                    />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>

          <!-- Вкладка: Управление пользователями -->
          <TabPanel header="Пользователи">
            <DataTable :value="users" stripedRows paginator :rows="10" dataKey="id">
              <Column field="id" header="ID" style="width: 5%"></Column>
              <Column field="name" header="Имя"></Column>
              <Column field="email" header="Email"></Column>
              <Column header="Роль">
                <template #body="slotProps">
                  {{ getRoleName(slotProps.data.role) }}
                </template>
              </Column>
              <Column field="organisationId" header="ID организации" style="width: 12%"></Column>
              <Column header="Действия" style="width: 15%">
                <template #body="slotProps">
                  <div class="action-buttons">
                    <Button
                      v-if="slotProps.data.role !== 'ORG_REPRESENTATIVE'"
                      label="→ ПрОрг"
                      severity="info"
                      size="small"
                      @click="changeUserRole(slotProps.data.id, 'ORG_REPRESENTATIVE')"
                    />
                    <Button
                      v-if="slotProps.data.role !== 'USER'"
                      label="→ АП"
                      severity="secondary"
                      size="small"
                      @click="changeUserRole(slotProps.data.id, 'USER')"
                    />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
        </TabView>
      </template>
    </Card>

    <!-- Диалог создания организации -->
    <Dialog
      v-model:visible="showOrgDialog"
      header="Создать организацию"
      :modal="true"
      :style="{ width: '500px' }"
    >
      <div class="dialog-form">
        <div class="form-field">
          <label for="org-name">Название</label>
          <InputText
            id="org-name"
            v-model="newOrg.name"
            class="w-full"
            placeholder="Введите название организации"
          />
        </div>
        <div class="form-field">
          <label for="org-description">Описание</label>
          <Textarea
            id="org-description"
            v-model="newOrg.description"
            rows="3"
            class="w-full"
            placeholder="Введите описание"
          />
        </div>
        <div class="form-field">
          <label for="org-address">Адрес</label>
          <InputText
            id="org-address"
            v-model="newOrg.address"
            class="w-full"
            placeholder="Введите адрес"
          />
        </div>
      </div>
      <template #footer>
        <Button label="Отмена" severity="secondary" @click="showOrgDialog = false" />
        <Button label="Создать" @click="createOrganization" />
      </template>
    </Dialog>

    <!-- Диалог редактирования организации -->
    <Dialog
      v-model:visible="showEditOrgDialog"
      header="Редактировать организацию"
      :modal="true"
      :style="{ width: '500px' }"
    >
      <div v-if="selectedOrg" class="dialog-form">
        <div class="form-field">
          <label for="edit-org-name">Название</label>
          <InputText id="edit-org-name" v-model="selectedOrg.name" class="w-full" />
        </div>
        <div class="form-field">
          <label for="edit-org-description">Описание</label>
          <Textarea
            id="edit-org-description"
            v-model="selectedOrg.description"
            rows="3"
            class="w-full"
          />
        </div>
        <div class="form-field">
          <label for="edit-org-address">Адрес</label>
          <InputText id="edit-org-address" v-model="selectedOrg.address" class="w-full" />
        </div>
      </div>
      <template #footer>
        <Button label="Отмена" severity="secondary" @click="showEditOrgDialog = false" />
        <Button label="Сохранить" @click="updateOrganization" />
      </template>
    </Dialog>
  </div>
</template>

<style scoped>
.admin-panel {
  max-width: 1400px;
  margin: 0 auto;
  padding: var(--space-m);
}

.title-container {
  display: flex;
  align-items: center;
}

.table-header {
  margin-bottom: var(--space-m);
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.dialog-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-m);
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-field label {
  font-weight: 600;
  font-size: 0.9rem;
}

.w-full {
  width: 100%;
}
</style>
