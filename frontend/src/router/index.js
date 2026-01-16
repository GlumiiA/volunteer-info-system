import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/search',
      name: 'search',
      component: () => import('../views/SearchView.vue'),
    },
    {
      path: '/leaderboard',
      name: 'leaderboard',
      component: () => import('../views/LeaderboardView.vue'),
    },
    {
      path: '/entry-view',
      name: 'entry-view',
      component: () => import('../views/EntryView.vue'),
    },
    {
      path: '/entry-edit',
      name: 'entry-edit',
      component: () => import('../views/EntryEdit.vue'),
    },
    {
      path: '/profile-view',
      name: 'profile-view',
      component: () => import('../views/ProfileView.vue'),
    },
  ],
})

export default router
