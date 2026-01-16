<script setup>
import { DataTable, Column, Rating, Skeleton } from 'primevue'
import { useLeaderboard } from '@/composables/useLeaderboard'
import { useAuth } from '@/composables/useAuth'

/**
 * @typedef {import('@/types/leaderboard').LeaderboardEntry} LeaderboardEntry
 */

const {
  entries,
  currentUserRank,
  isLoading,
  error,
  fetchLeaderboard,
} = useLeaderboard()

const { user } = useAuth()

/**
 * Проверить, текущий ли это пользователь
 */
const isCurrentUser = (entry) => {
  return user.value && entry.userId === user.value.id
}
</script>

<template>
  <div class="leaderboard-wrapper">
    <!-- Заголовок -->
    <div class="leaderboard-header">
      <h2>Лидерборд</h2>
      <p class="subtitle">Топ волонтеров по отработанным часам</p>
    </div>

    <!-- Информация о позиции текущего пользователя -->
    <div v-if="user && currentUserRank" class="current-user-info">
      <div class="info-card">
        <span class="info-label">Ваша позиция:</span>
        <span class="info-rank">#{{ currentUserRank.rank }}</span>
        <span class="info-score">{{ currentUserRank.score }} часов</span>
      </div>
    </div>

    <!-- Сообщение об ошибке -->
    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <!-- Таблица лидеров -->
    <div v-if="isLoading" class="skeleton-table">
      <div v-for="i in 5" :key="i" class="skeleton-row">
        <Skeleton width="3rem" />
        <Skeleton width="100%" />
      </div>
    </div>

    <DataTable
      v-else
      :value="entries"
      :rows="10"
      paginator
      responsive-layout="scroll"
      class="leaderboard-table"
    >
      <!-- Колонка: Место -->
      <Column
        field="rank"
        header="Место"
        header-class="column-header"
        body-class="column-body rank-column"
        style="width: 12%"
      >
        <template #body="{ data }">
          <div class="rank-cell">
            <span v-if="data.rank <= 3" class="medal" :class="`medal-${data.rank}`"></span>
            <span class="rank-number">{{ data.rank }}</span>
          </div>
        </template>
      </Column>

      <!-- Колонка: Волонтер -->
      <Column
        field="fullName"
        header="Волонтер"
        header-class="column-header"
        body-class="column-body"
        style="width: 40%"
      >
        <template #body="{ data }">
          <div class="volunteer-info" :class="{ 'current-user': isCurrentUser(data) }">
            <span class="name">{{ data.fullName }}</span>
            <span v-if="isCurrentUser(data)" class="current-badge">Вы</span>
          </div>
        </template>
      </Column>

      <!-- Колонка: Часы -->
      <Column
        field="score"
        header="Часы"
        header-class="column-header"
        body-class="column-body"
        style="width: 20%"
      >
        <template #body="{ data }">
          <span class="hours">{{ data.score }}</span>
        </template>
      </Column>

      <!-- Колонка: Рейтинг -->
      <Column
        field="rating"
        header="Рейтинг"
        header-class="column-header"
        body-class="column-body"
        style="width: 28%"
      >
        <template #body="{ data }">
          <Rating
            v-if="data.rating"
            :model-value="Math.round(data.rating)"
            readonly
            :cancel="false"
          />
          <span v-else class="no-rating">—</span>
        </template>
      </Column>

      <!-- Шаблон для пустого стола -->
      <template #empty>
        <div class="empty-message">
          Лидерборд пока пуст
        </div>
      </template>
    </DataTable>
  </div>
</template>

<style scoped>
.leaderboard-wrapper {
  padding: 2rem;
  font-family: Nunito, sans-serif;
}

.leaderboard-header {
  margin-bottom: 1.5rem;
  text-align: center;
}

.leaderboard-header h2 {
  margin: 0 0 0.5rem;
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--text-color);
}

.subtitle {
  margin: 0;
  font-size: 0.9rem;
  color: var(--text-color-secondary);
}

/* Информация о текущем пользователе */
.current-user-info {
  margin-bottom: 1.5rem;
}

.info-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 1.25rem;
  background: var(--surface-card);
  border: 1px solid var(--surface-border);
  border-radius: 6px;
  box-shadow: var(--card-shadow, 0 1px 3px rgba(0, 0, 0, 0.1));
}

.info-label {
  font-size: 0.9rem;
  color: var(--text-color-secondary);
}

.info-rank {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--primary-color);
}

.info-score {
  font-size: 0.95rem;
  color: var(--text-color);
  margin-left: auto;
}

/* Сообщение об ошибке */
.error-message {
  padding: 1rem;
  margin-bottom: 1rem;
  background: var(--red-50);
  border-left: 3px solid var(--red-500);
  border-radius: 4px;
  color: var(--red-700);
  font-size: 0.9rem;
}

/* Таблица */
.leaderboard-table {
  background: var(--surface-card);
  border: 1px solid var(--surface-border);
  border-radius: 6px;
  overflow: hidden;
}

:deep(.column-header) {
  background: var(--surface-100);
  font-weight: 600;
  font-size: 0.875rem;
  color: var(--text-color);
  border-bottom: 1px solid var(--surface-border);
  padding: 0.75rem 1rem;
  text-align: left;
}

:deep(.column-body) {
  padding: 0.875rem 1rem;
  vertical-align: middle;
}

:deep(.p-datatable .p-datatable-tbody > tr:hover) {
  background-color: var(--surface-hover);
}

:deep(.p-datatable .p-datatable-tbody > tr) {
  border-bottom: 1px solid var(--surface-border);
}

/* Ранг */
.rank-column {
  text-align: left;
}

.rank-cell {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.medal {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: inline-block;
  position: relative;
  flex-shrink: 0;
}

.medal::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 14px;
  height: 14px;
  border-radius: 50%;
}

.medal-1 {
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  box-shadow: 0 2px 4px rgba(255, 215, 0, 0.3);
}

.medal-1::before {
  background: radial-gradient(circle, #fff, transparent);
}

.medal-2 {
  background: linear-gradient(135deg, #c0c0c0, #e8e8e8);
  box-shadow: 0 2px 4px rgba(192, 192, 192, 0.3);
}

.medal-2::before {
  background: radial-gradient(circle, #fff, transparent);
}

.medal-3 {
  background: linear-gradient(135deg, #cd7f32, #e8a87c);
  box-shadow: 0 2px 4px rgba(205, 127, 50, 0.3);
}

.medal-3::before {
  background: radial-gradient(circle, #fff, transparent);
}

.rank-number {
  font-weight: 600;
  color: var(--text-color);
  font-size: 0.95rem;
}

/* Информация о волонтере */
.volunteer-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.volunteer-info.current-user .name {
  font-weight: 600;
  color: var(--primary-color);
}

.name {
  font-size: 0.9rem;
  color: var(--text-color);
}

.current-badge {
  display: inline-block;
  background: var(--primary-color);
  color: var(--primary-color-text, #ffffff);
  padding: 0.125rem 0.5rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 600;
}

/* Часы */
.hours {
  font-size: 0.9rem;
  color: var(--text-color);
  font-weight: 500;
}

/* Рейтинг */
.no-rating {
  color: var(--text-color-secondary);
  font-size: 0.875rem;
}

/* Пустая таблица */
.empty-message {
  padding: 2rem;
  text-align: center;
  color: var(--text-color-secondary);
  font-size: 0.9rem;
}

/* Скелетон */
.skeleton-table {
  background: var(--surface-card);
  border: 1px solid var(--surface-border);
  border-radius: 6px;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.skeleton-row {
  display: flex;
  gap: 1rem;
}

/* Адаптивность */
@media (max-width: 768px) {
  .leaderboard-wrapper {
    padding: 1rem;
  }

  .leaderboard-header h2 {
    font-size: 1.5rem;
  }

  .info-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .info-score {
    margin-left: 0;
  }

  :deep(.p-datatable) {
    font-size: 0.85rem;
  }

  :deep(.column-body) {
    padding: 0.75rem 0.5rem;
  }

  .medal {
    width: 16px;
    height: 16px;
  }

  .medal::before {
    width: 10px;
    height: 10px;
  }
}
</style>
