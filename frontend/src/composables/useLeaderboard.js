import { ref, onMounted } from 'vue'
import * as leaderboardService from '@/services/leaderboard'
import { useAuth } from './useAuth'

/**
 * Composable для управления лидербордом
 * Лидерборд всегда отсортирован по количеству часов (в порядке убывания)
 */
export function useLeaderboard() {
  const { token, user } = useAuth()
  
  // Состояние
  const entries = ref([])
  const currentUserRank = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const limit = ref(50)

  /**
   * Загрузить данные лидербордов
   */
  const fetchLeaderboard = async () => {
    try {
      isLoading.value = true
      error.value = null

      // МОКИРОВАННЫЕ ДАННЫЕ для демонстрации
      // В реальном приложении раскомментируйте следующие строки:
      // const response = await leaderboardService.getLeaderboard(limit.value, token.value)

      // Мок-данные
      const mockLeaderboard = [
        { rank: 1, userId: 101, fullName: 'Виктор Петров', score: 1337, rating: 5 },
        { rank: 2, userId: 102, fullName: 'Александра Иванова', score: 1200, rating: 4.8 },
        { rank: 3, userId: 103, fullName: 'Дмитрий Сидоров', score: 980, rating: 4.5 },
        { rank: 4, userId: 1, fullName: 'Тестовый Пользователь', score: 42, rating: 4.8 },
        { rank: 5, userId: 104, fullName: 'Мария Кузнецова', score: 850, rating: 4.7 },
        { rank: 6, userId: 105, fullName: 'Павел Морозов', score: 720, rating: 4.3 },
        { rank: 7, userId: 106, fullName: 'Екатерина Волкова', score: 650, rating: 4.9 },
        { rank: 8, userId: 107, fullName: 'Иван Соколов', score: 580, rating: 4.2 },
        { rank: 9, userId: 108, fullName: 'Оксана Лебедева', score: 520, rating: 4.6 },
        { rank: 10, userId: 109, fullName: 'Сергей Новиков', score: 450, rating: 4.4 },
      ]

      const mockCurrentUserRank = user.value
        ? {
            rank: 4,
            userId: user.value.id,
            fullName: user.value.name,
            score: user.value.volunteerHours,
          }
        : null

      entries.value = mockLeaderboard.map(entry => ({
        ...entry,
        isCurrentUser: user.value && entry.userId === user.value.id,
      }))

      currentUserRank.value = mockCurrentUserRank

      // Для реального API используйте:
      // const response = await leaderboardService.getLeaderboard(limit.value, token.value)
      // entries.value = response.leaderboard.map(entry => ({
      //   ...entry,
      //   isCurrentUser: user.value && entry.userId === user.value.id,
      // }))
      // currentUserRank.value = response.currentUserRank
    } catch (err) {
      console.error('Ошибка при загрузке лидербордов:', err)
      error.value = err.message || 'Ошибка при загрузке лидербордов'
    } finally {
      isLoading.value = false
    }
  }

  // Загружаем данные при монтировании компонента
  onMounted(fetchLeaderboard)

  return {
    entries,
    currentUserRank,
    isLoading,
    error,
    limit,
    fetchLeaderboard,
  }
}
