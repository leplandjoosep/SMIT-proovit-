import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'
import BikeSection from '../../views/BikeSection.vue'
import axios from 'axios'

const mockAxios = vi.mocked(axios)

describe('BikeSection', () => {
  const mockParts = [
    {
      id: '1',
      name: 'Test Tire',
      brand: 'Maxxis',
      category: 'TIRE',
      quantity: 3,
      location: 'Garage A1'
    }
  ]

  const mockSearchResponse = {
    content: mockParts,
    totalSize: 1,
    pageable: { size: 12, number: 0, sort: {}, mode: 'OFFSET' }
  }

  beforeEach(() => {
    vi.clearAllMocks()
    mockAxios.get.mockImplementation((url) => {
      if (url.includes('/search')) {
        return Promise.resolve({ data: mockSearchResponse })
      } else if (url.includes('/categories')) {
        return Promise.resolve({ data: ['TIRE', 'BRAKE'] })
      } else {
        return Promise.resolve({ data: mockParts })
      }
    })
  })

  const createWrapper = () => {
    const router = createRouter({
      history: createWebHistory(),
      routes: [{ path: '/', component: BikeSection }]
    })

    return mount(BikeSection, {
      global: { plugins: [router] }
    })
  }

  it('renders bike section with search and add functionality', () => {
    const wrapper = createWrapper()
    
    expect(wrapper.text()).toContain('Rattaosad')
    expect(wrapper.text()).toContain('Lisa osa')
    expect(wrapper.find('input[placeholder*="Otsi"]').exists()).toBe(true)
    expect(wrapper.find('select').exists()).toBe(true)
  })

  it('loads and displays bike parts', async () => {
    const wrapper = createWrapper()
    
    await wrapper.vm.$nextTick()
    await new Promise(resolve => setTimeout(resolve, 100))
    
    expect(mockAxios.get).toHaveBeenCalled()
    expect(wrapper.text()).toContain('Test Tire')
  })

  it('performs search when query changes', async () => {
    const wrapper = createWrapper()
    
    await wrapper.vm.$nextTick()
    
    const searchInput = wrapper.find('input[placeholder*="Otsi"]')
    await searchInput.setValue('tire')
    
    await new Promise(resolve => setTimeout(resolve, 500))
    
    expect(mockAxios.get).toHaveBeenCalledWith(
      expect.stringContaining('/search'),
      expect.objectContaining({
        params: expect.objectContaining({ q: 'tire' })
      })
    )
  })

  it('shows pagination controls', async () => {
    const wrapper = createWrapper()
    
    await wrapper.vm.$nextTick()
    await new Promise(resolve => setTimeout(resolve, 100))
    
    expect(wrapper.text()).toContain('Lehekülg')
    expect(wrapper.text()).toContain('Eelmine')
    expect(wrapper.text()).toContain('Järgmine')
  })
})
