<script setup>
import { Button, Dialog, Select, InputText, Textarea, RadioButton } from 'primevue'
import { ref, watch, onMounted } from 'vue'
import { getOrganizations, submitOrganizationRequest } from '@/services/organizations'
import { useAuth } from '@/composables/useAuth'
import { useToast } from 'primevue/usetoast'

/** @typedef {import('@/types/user').Organization} Organization */
/** @typedef {import('@/types/user').OrganizationRequest} OrganizationRequest */

const emit = defineEmits(['request-submitted'])
const { token } = useAuth()
const toast = useToast()

const visible = defineModel('visible', { type: Boolean, default: false })

/** @type {import('vue').Ref<'EXISTING' | 'NEW'>} */
const requestType = ref('EXISTING')

/** @type {import('vue').Ref<Organization[]>} */
const organizations = ref([])

/** @type {import('vue').Ref<Organization | null>} */
const selectedOrganization = ref(null)

/** @type {import('vue').Ref<string>} */
const newOrgName = ref('')

/** @type {import('vue').Ref<string>} */
const newOrgDescription = ref('')

/** @type {import('vue').Ref<string>} */
const newOrgAddress = ref('')

const isSubmitting = ref(false)

onMounted(async () => {
  try {
    organizations.value = await getOrganizations()
  } catch (error) {
    console.error('Error loading organizations:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: 'Не удалось загрузить список организаций',
      life: 3000
    })
  }
})

const resetForm = () => {
  requestType.value = 'EXISTING'
  selectedOrganization.value = null
  newOrgName.value = ''
  newOrgDescription.value = ''
  newOrgAddress.value = ''
}

watch(visible, (newValue) => {
  if (!newValue) {
    resetForm()
  }
})

const isFormValid = () => {
  if (requestType.value === 'EXISTING') {
    return selectedOrganization.value !== null
  } else {
    return newOrgName.value.trim().length > 0
  }
}

const submitRequest = async () => {
  if (!isFormValid()) {
    toast.add({
      severity: 'warn',
      summary: 'Предупреждение',
      detail: 'Пожалуйста, заполните все обязательные поля',
      life: 3000
    })
    return
  }

  isSubmitting.value = true

  try {
    /** @type {OrganizationRequest} */
    const request = {
      requestType: requestType.value
    }

    if (requestType.value === 'EXISTING') {
      request.organizationId = selectedOrganization.value.id
    } else {
      request.organizationName = newOrgName.value
      request.organizationDescription = newOrgDescription.value
      request.organizationAddress = newOrgAddress.value
    }

    await submitOrganizationRequest(request, token.value)

    toast.add({
      severity: 'success',
      summary: 'Успешно',
      detail: 'Запрос на получение статуса представителя организации отправлен. Ожидайте рассмотрения администратором.',
      life: 5000
    })

    visible.value = false
    emit('request-submitted')
  } catch (error) {
    console.error('Error submitting organization request:', error)
    toast.add({
      severity: 'error',
      summary: 'Ошибка',
      detail: error.message || 'Не удалось отправить запрос',
      life: 3000
    })
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <Dialog
    v-model:visible="visible"
    modal
    header="Запрос на статус представителя организации"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
  >
    <div class="form-container">
      <p class="form-description">
        Выберите существующую организацию или создайте новую. Ваш запрос будет рассмотрен
        администратором.
      </p>

      <div class="radio-group">
        <div class="radio-item">
          <RadioButton v-model="requestType" inputId="existing" value="EXISTING" />
          <label for="existing">Существующая организация</label>
        </div>
        <div class="radio-item">
          <RadioButton v-model="requestType" inputId="new" value="NEW" />
          <label for="new">Новая организация</label>
        </div>
      </div>

      <div v-if="requestType === 'EXISTING'" class="form-field">
        <label for="organization">Организация *</label>
        <Select
          id="organization"
          v-model="selectedOrganization"
          :options="organizations"
          optionLabel="name"
          placeholder="Выберите организацию"
          class="w-full"
        >
          <template #option="slotProps">
            <div class="org-option">
              <div class="org-name">{{ slotProps.option.name }}</div>
              <div class="org-details" v-if="slotProps.option.description">
                {{ slotProps.option.description }}
              </div>
            </div>
          </template>
        </Select>
        <small v-if="selectedOrganization?.address" class="org-address">
          Адрес: {{ selectedOrganization.address }}
        </small>
      </div>

      <div v-else class="new-org-fields">
        <div class="form-field">
          <label for="orgName">Название организации *</label>
          <InputText
            id="orgName"
            v-model="newOrgName"
            placeholder="Введите название"
            class="w-full"
          />
        </div>

        <div class="form-field">
          <label for="orgDescription">Описание организации</label>
          <Textarea
            id="orgDescription"
            v-model="newOrgDescription"
            placeholder="Введите описание"
            rows="3"
            class="w-full"
          />
        </div>

        <div class="form-field">
          <label for="orgAddress">Адрес организации</label>
          <InputText
            id="orgAddress"
            v-model="newOrgAddress"
            placeholder="Введите адрес"
            class="w-full"
          />
        </div>
      </div>

      <div class="form-actions">
        <Button
          @click="submitRequest"
          :loading="isSubmitting"
          :disabled="!isFormValid()"
        >
          <i class="pi pi-send" />
          <span>Отправить запрос</span>
        </Button>
        <Button
          label="Отмена"
          severity="secondary"
          @click="visible = false"
          :disabled="isSubmitting"
        />
      </div>
    </div>
  </Dialog>
</template>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  gap: var(--space-l);
  padding: var(--space-l);
  background: var(--surface-ground);
  border-radius: var(--border-radius-lg);
}

.form-description {
  color: var(--text-color-secondary);
  margin: 0;
  font-size: 1rem;
  line-height: 1.6;
  padding: var(--space-m);
  background: var(--surface-card);
  border-radius: var(--border-radius);
  border-left: 4px solid var(--primary-color);
}

.radio-group {
  display: flex;
  gap: var(--space-xl);
  padding: var(--space-m);
  background: var(--surface-card);
  border-radius: var(--border-radius);
}

.radio-item {
  display: flex;
  align-items: center;
  gap: var(--space-s);
  padding: var(--space-s) var(--space-m);
  border-radius: var(--border-radius);
  transition: background-color var(--transition-fast);
}

.radio-item:hover {
  background: var(--surface-hover);
}

.radio-item label {
  cursor: pointer;
  user-select: none;
  font-weight: 500;
  font-size: 0.938rem;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: var(--space-s);
}

.form-field label {
  font-weight: 700;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--text-color-secondary);
  display: flex;
  align-items: center;
  gap: var(--space-xs);
}

.new-org-fields {
  display: flex;
  flex-direction: column;
  gap: var(--space-l);
  padding: var(--space-l);
  background: var(--surface-card);
  border-radius: var(--border-radius);
  border: 2px dashed var(--primary-color);
}

.org-option {
  display: flex;
  flex-direction: column;
  padding: var(--space-m);
  border-radius: var(--border-radius);
  transition: background-color var(--transition-fast);
}

.org-option:hover {
  background: var(--surface-50);
}

.org-name {
  font-weight: 700;
  font-size: 1rem;
  color: var(--text-color);
  margin-bottom: var(--space-xs);
}

.org-details {
  font-size: 0.875rem;
  color: var(--text-color-secondary);
  line-height: 1.5;
}

.org-address {
  color: var(--text-color-secondary);
  font-size: 0.875rem;
  font-style: italic;
  margin-top: var(--space-s);
  padding: var(--space-s) var(--space-m);
  background: var(--surface-ground);
  border-radius: var(--border-radius);
  display: inline-flex;
  align-items: center;
  gap: var(--space-xs);
}

.org-address::before {
  content: '\f3c5';
  font-family: 'primeicons';
  color: var(--primary-color);
}

.form-actions {
  display: flex;
  gap: var(--space-m);
  margin-top: var(--space-l);
  padding-top: var(--space-l);
  border-top: 1px solid var(--surface-border);
}

.form-actions .p-button {
  flex: 1;
  justify-content: center;
}

.w-full {
  width: 100%;
}

:deep(.p-dialog-header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: var(--space-xl);
  border-radius: var(--border-radius-lg) var(--border-radius-lg) 0 0;
}

:deep(.p-dialog-title) {
  font-weight: 700;
  font-size: 1.5rem;
}

:deep(.p-dialog-header-icons button) {
  color: white !important;
}

:deep(.p-dialog-header-icons button:hover) {
  background: rgba(255, 255, 255, 0.2) !important;
}

:deep(.p-dialog-content) {
  padding: 0;
}

@media (max-width: 768px) {
  .radio-group {
    flex-direction: column;
    gap: var(--space-s);
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style>
