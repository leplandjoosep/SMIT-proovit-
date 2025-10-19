import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'
import VinylSection from '../../views/VinylSection.vue'
import axios from 'axios'

const mockAxios = vi.mocked(axios)

describe('VinylSection', () => {
  const mockVinyls = [
    {
      id: '1',
      title: 'Dark Side of the Moon',
      artist: 'Pink Floyd',
      releaseYear: 1973,
      acquiredFrom: 'Discogs',
      acquiredDate: '2023-01-15',
      location: 'Riiul A1'
    }
  ]

  const mockSearchResponse = {
    content: mockVinyls,
    totalSize: 1,
    pageable: { size: 12, number: 0, sort: {}, mode: 'OFFSET' }
  }

  beforeEach(() => {
    vi.clearAllMocks()
    mockAxios.get.mockImplementation((url) => {
      if (url.includes('/search')) {
        return Promise.resolve({ data: mockSearchResponse })
      } else if (url.includes('/artists')) {
        return Promise.resolve({ data: ['Pink Floyd', 'The Beatles'] })
      } else {
        return Promise.resolve({ data: mockVinyls })
      }
    })
  })

  const createWrapper = () => {
    const router = createRouter({
      history: createWebHistory(),
      routes: [{ path: '/', component: VinylSection }]
    })

    return mount(VinylSection, {
      global: { plugins: [router] }
    })
  }

  it('renders vinyl section with search and add functionality', () => {
    const wrapper = createWrapper()
    
    expect(wrapper.text()).toContain('Vinüülid')
    expect(wrapper.text()).toContain('Lisa plaat')
    expect(wrapper.find('input[placeholder*="Otsi"]').exists()).toBe(true)
    expect(wrapper.find('select').exists()).toBe(true)
  })

  it('loads and displays vinyl records', async () => {
    const wrapper = createWrapper()
    
    await wrapper.vm.$nextTick()
    await new Promise(resolve => setTimeout(resolve, 100))
    
    expect(mockAxios.get).toHaveBeenCalled()
    expect(wrapper.text()).toContain('Dark Side of the Moon')
  })

  it('performs search when query changes', async () => {
    const wrapper = createWrapper()
    
    await wrapper.vm.$nextTick()
    
    const searchInput = wrapper.find('input[placeholder*="Otsi"]')
    await searchInput.setValue('pink')
    
    await new Promise(resolve => setTimeout(resolve, 500))
    
    expect(mockAxios.get).toHaveBeenCalledWith(
      expect.stringContaining('/search'),
      expect.objectContaining({
        params: expect.objectContaining({ q: 'pink' })
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
