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
import { useToast } from 'primevue/usetoast'
import * as adminService from '@/services/admin'

const { user } = useAuth()
const toast = useToast()

// Данные
const organizationRequests = ref([])
const organizations = ref([])
const users = ref([])
const loading = ref(false)

// Диалоги
const showOrgDialog = ref(false)
const showEditOrgDialog = ref(false)
const selectedOrg = ref(null)
const newOrg = ref({
  name: '',
  description: '',
  address: '',
})

// Загрузка данных
const loadData = async () => {
  loading.value = true
  try {
    await Promise.all([
      loadOrganizationRequests(),
      loadOrganizations(),
      loadUsers(),
    ])
  } catch (error) {
    console.error('Error loading admin data:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось загрузить данные',
      life: 3000,
    })
  } finally {
    loading.value = false
  }
}

const loadOrganizationRequests = async () => {
  try {
    organizationRequests.value = await adminService.getOrganizationRequests('ALL')
    // Convert createdAt string to Date if needed
    organizationRequests.value = organizationRequests.value.map(req => ({
      ...req,
      createdAt: req.createdAt ? new Date(req.createdAt) : null,
    }))
  } catch (error) {
    console.error('Error loading organization requests:', error)
    throw error
  }
}

const loadOrganizations = async () => {
  try {
    organizations.value = await adminService.getOrganizations()
  } catch (error) {
    console.error('Error loading organizations:', error)
    throw error
  }
}

const loadUsers = async () => {
  try {
    users.value = await adminService.getUsers()
  } catch (error) {
    console.error('Error loading users:', error)
    throw error
  }
}

onMounted(() => {
  loadData()
})

// Обработчики запросов на статус ПрОрг
const approveRequest = async (requestId) => {
  try {
    await adminService.approveOrganizationRequest(requestId)
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Запрос одобрен',
      life: 3000,
    })
    // Reload all data to reflect changes
    await loadData()
  } catch (error) {
    console.error('Error approving request:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось одобрить запрос',
      life: 3000,
    })
  }
}

const rejectRequest = async (requestId) => {
  try {
    await adminService.rejectOrganizationRequest(requestId)
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Запрос отклонен',
      life: 3000,
    })
    // Reload all data to reflect changes
    await loadData()
  } catch (error) {
    console.error('Error rejecting request:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось отклонить запрос',
      life: 3000,
    })
  }
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
  try {
    const org = await adminService.createOrganization(newOrg.value)
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Организация создана',
      life: 3000,
    })
    showOrgDialog.value = false
    await loadOrganizations()
  } catch (error) {
    console.error('Error creating organization:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось создать организацию',
      life: 3000,
    })
  }
}

const updateOrganization = async () => {
  try {
    await adminService.updateOrganization(selectedOrg.value.id, selectedOrg.value)
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Организация обновлена',
      life: 3000,
    })
    showEditOrgDialog.value = false
    await loadOrganizations()
  } catch (error) {
    console.error('Error updating organization:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось обновить организацию',
      life: 3000,
    })
  }
}

const deleteOrganization = async (orgId) => {
  if (confirm('Вы уверены, что хотите удалить эту организацию?')) {
    try {
      await adminService.deleteOrganization(orgId)
      toast.add({
        severity: 'success',
        summary: 'Успешно',
        detail: 'Организация удалена',
        life: 3000,
      })
      // Reload all data to reflect changes (users might be affected)
      await loadData()
    } catch (error) {
      console.error('Error deleting organization:', error)
      toast.add({
        severity: 'error',
        summary: 'Ошибка',
        detail: error.message || 'Не удалось удалить организацию',
        life: 3000,
      })
    }
  }
}

// Управление ролями пользователей
const changeUserRole = async (userId, newRole) => {
  try {
    // For ORG_REPRESENTATIVE role, organization ID is required
    // For now, we'll show an error if trying to set ORG_REPRESENTATIVE without organization
    // Users should use the organization request approval flow instead
    if (newRole === 'ORG_REPRESENTATIVE') {
      toast.add({
        severity: 'warn',
        summary: 'Предупреждение',
        detail: 'Для назначения представителя организации используйте одобрение запроса на статус ПрОрг',
        life: 5000,
      })
      return
    }
    
    // For USER role, no organization ID needed
    await adminService.updateUserRole(userId, newRole, null)
    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Роль пользователя изменена',
      life: 3000,
    })
    await loadUsers()
  } catch (error) {
    console.error('Error changing user role:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось изменить роль пользователя',
      life: 3000,
    })
  }
}

const getRequestTypeName = (type) => {
  if (!type) return 'Неизвестно'
  const typeStr = typeof type === 'string' ? type : type.toString()
  return typeStr === 'EXISTING' ? 'Существующая организация' : 'Новая организация'
}

const getStatusSeverity = (status) => {
  if (!status) return 'info'
  const statusStr = typeof status === 'string' ? status : status.toString()
  switch (statusStr) {
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
  if (!status) return 'Неизвестно'
  const statusStr = typeof status === 'string' ? status : status.toString()
  switch (statusStr) {
    case 'PENDING':
      return 'Ожидает'
    case 'APPROVED':
      return 'Одобрено'
    case 'REJECTED':
      return 'Отклонено'
    default:
      return statusStr
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
              <Column header="Организация">
                <template #body="slotProps">
                  {{ slotProps.data.organizationName || 'Не указано' }}
                </template>
              </Column>
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
                @click="openNewOrgDialog"
                size="small"
              >
                <i class="pi pi-plus" />
                <span>Добавить организацию</span>
              </Button>
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
                      severity="info"
                      size="small"
                      @click="openEditOrgDialog(slotProps.data)"
                      v-tooltip="'Редактировать'"
                    >
                      <i class="pi pi-pencil" />
                    </Button>
                    <Button
                      severity="danger"
                      size="small"
                      @click="deleteOrganization(slotProps.data.id)"
                      v-tooltip="'Удалить'"
                    >
                      <i class="pi pi-trash" />
                    </Button>
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
                      v-if="slotProps.data.role !== 'USER' && slotProps.data.role !== 'ADMIN'"
                      label="→ Пользователь"
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
