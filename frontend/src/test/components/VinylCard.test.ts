import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import VinylCard from '../../components/vinyl/VinylCard.vue'
import axios from 'axios'

vi.mocked(axios.delete).mockResolvedValue({ data: {} })

describe('VinylCard', () => {
  const mockVinyl = {
    id: '1',
    title: 'Test Album',
    artist: 'Test Artist',
    releaseYear: 2020,
    acquiredFrom: 'Test Store',
    acquiredDate: '2023-01-15',
    location: 'Riiul A1',
    notes: 'Test notes'
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders vinyl information correctly', () => {
    const wrapper = mount(VinylCard, {
      props: { vinyl: mockVinyl }
    })

    expect(wrapper.text()).toContain('Test Album')
    expect(wrapper.text()).toContain('Test Artist')
    expect(wrapper.text()).toContain('2020')
    expect(wrapper.text()).toContain('Test Store')
  })

  it('emits edit event when edit button is clicked', async () => {
    const wrapper = mount(VinylCard, {
      props: { vinyl: mockVinyl }
    })

    const editButton = wrapper.find('[title="Muuda"]')
    await editButton.trigger('click')

    expect(wrapper.emitted('edit')).toBeTruthy()
    expect(wrapper.emitted('edit')?.[0]).toEqual([mockVinyl])
  })

  it('emits deleted event when delete button is clicked', async () => {
    const wrapper = mount(VinylCard, {
      props: { vinyl: mockVinyl }
    })

    const deleteButton = wrapper.find('[title="Kustuta"]')
    await deleteButton.trigger('click')

    expect(axios.delete).toHaveBeenCalledWith('/api/vinyl/1')
    expect(wrapper.emitted('deleted')).toBeTruthy()
  })
})
