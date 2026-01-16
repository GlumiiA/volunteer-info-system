export interface LeaderboardEntry {
  rank: number
  userId: number
  fullName: string
  score: number
  rating?: number
  avatar?: string
  isCurrentUser?: boolean
}

export interface LeaderboardResponse {
  leaderboard: LeaderboardEntry[]
  currentUserRank?: {
    rank: number
    userId: number
    fullName: string
    score: number
  }
}
