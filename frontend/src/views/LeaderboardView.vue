<script setup>
import LeaderboardTable from '@/components/LeaderboardTable.vue'
</script>

<template>
  <div class="leaderboard-page">
    <LeaderboardTable />
  </div>
</template>

<style scoped>
.leaderboard-page {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

@media (max-width: 768px) {
  .leaderboard-page {
    padding: 1rem 0.5rem;
  }
}
</style>
