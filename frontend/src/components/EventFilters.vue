<script setup>
import { InputText, Calendar, Dropdown, Panel, Button } from 'primevue'

const props = defineProps({
  filters: {
    type: Object,
    required: true,
  },
  organizations: {
    type: Array,
    required: true,
  },
  eventTypes: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['update:filters', 'reset'])

const updateFilter = (key, value) => {
  emit('update:filters', { ...props.filters, [key]: value })
}

const handleReset = () => {
  emit('reset')
}
</script>

<template>
  <Panel header="Фильтры поиска" :toggleable="true" class="filters-panel">
    <div class="filters-grid">
      <div class="filter-field">
        <label for="filter-title">Название</label>
        <InputText
          id="filter-title"
          :modelValue="filters.title"
          @update:modelValue="updateFilter('title', $event)"
          placeholder="Поиск по названию..."
        />
      </div>

      <div class="filter-field">
        <label for="filter-date-from">Дата начала (от)</label>
        <Calendar
          id="filter-date-from"
          :modelValue="filters.dateFrom"
          @update:modelValue="updateFilter('dateFrom', $event)"
          dateFormat="dd.mm.yy"
          placeholder="Выберите дату"
          showIcon
        />
      </div>

      <div class="filter-field">
        <label for="filter-date-to">Дата начала (до)</label>
        <Calendar
          id="filter-date-to"
          :modelValue="filters.dateTo"
          @update:modelValue="updateFilter('dateTo', $event)"
          dateFormat="dd.mm.yy"
          placeholder="Выберите дату"
          showIcon
        />
      </div>

      <div class="filter-field">
        <label for="filter-address">Адрес</label>
        <InputText
          id="filter-address"
          :modelValue="filters.address"
          @update:modelValue="updateFilter('address', $event)"
          placeholder="Поиск по адресу..."
        />
      </div>

      <div class="filter-field">
        <label for="filter-organisation">Организация</label>
        <Dropdown
          id="filter-organisation"
          :modelValue="filters.organisationId"
          @update:modelValue="updateFilter('organisationId', $event)"
          :options="organizations"
          optionLabel="label"
          optionValue="value"
          placeholder="Все организации"
          :filter="true"
          :showClear="true"
        />
      </div>

      <div class="filter-field">
        <label for="filter-type">Тип заявки</label>
        <Dropdown
          id="filter-type"
          :modelValue="filters.type"
          @update:modelValue="updateFilter('type', $event)"
          :options="eventTypes"
          optionLabel="label"
          optionValue="value"
          placeholder="Все типы"
          :showClear="true"
        />
      </div>

      <div class="filter-actions">
        <Button
          label="Сбросить фильтры"
          @click="handleReset"
          severity="secondary"
          icon="pi pi-filter-slash"
        />
      </div>
    </div>
  </Panel>
</template>

<style scoped>
.filters-panel {
  width: 100%;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-m);
  align-items: end;
}

.filter-field {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.filter-field label {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--text-color);
}

.filter-actions {
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
}

@media (max-width: 768px) {
  .filters-grid {
    grid-template-columns: 1fr;
  }
}
</style>
