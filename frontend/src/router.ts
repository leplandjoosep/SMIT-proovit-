import { createRouter, createWebHistory } from 'vue-router'

export const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', redirect: '/vinyls' },
        { path: '/vinyls', component: () => import('./views/VinylsPage.vue') },
        { path: '/parts', component: () => import('./views/PartsPage.vue') },
        { path: '/loans', component: () => import('./views/LoansPage.vue') },
    ],
})

export default router
