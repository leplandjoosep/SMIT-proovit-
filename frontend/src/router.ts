import { createRouter, createWebHistory } from 'vue-router'

export const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', redirect: '/vinyls' },
        { path: '/vinyls', component: () => import('./views/VinylSection.vue') },
        { path: '/parts',  component: () => import('./views/BikeSection.vue') },
    ],
})

export default router
