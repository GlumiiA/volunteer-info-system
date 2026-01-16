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
      path: '/auth',
      name: 'auth',
      component: () => import('../views/AuthView.vue'),
    },
    {
      path: '/auth/callback',
      name: 'auth-callback',
      component: () => import('../views/AuthCallbackView.vue'),
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
      path: '/entry/new',
      name: 'entry-create',
      component: () => import('../views/EntryCreate.vue'),
    },
    {
      path: '/entry/:id',
      name: 'entry-view',
      component: () => import('../views/EntryView.vue'),
    },
    {
      path: '/entry/:id/edit',
      name: 'entry-edit',
      component: () => import('../views/EntryEdit.vue'),
    },
    {
      path: '/profile-view',
      name: 'profile-view',
      component: () => import('../views/ProfileView.vue'),
    },
    {
      path: '/profile-edit',
      name: 'profile-edit',
      component: () => import('../views/ProfileEdit.vue'),
    },
  ],
})

export default router
