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

      const response = await leaderboardService.getLeaderboard(limit.value, token.value)
      
      entries.value = response.leaderboard.map(entry => ({
        ...entry,
        isCurrentUser: user.value && entry.userId === user.value.id,
      }))
      
      currentUserRank.value = response.currentUserRank || null
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
