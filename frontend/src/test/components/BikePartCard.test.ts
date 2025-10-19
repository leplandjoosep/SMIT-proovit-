import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import BikePartCard from '../../components/part/BikePartCard.vue'
import axios from 'axios'

vi.mocked(axios.get).mockResolvedValue({ data: [] })
vi.mocked(axios.post).mockResolvedValue({ data: {} })

describe('BikePartCard', () => {
  const mockPart = {
    id: '1',
    name: 'Test Tire',
    brand: 'Maxxis',
    category: 'TIRE',
    quantity: 3,
    location: 'Garage A1',
    notes: 'Test notes'
  }

  const mockLoans = [
    {
      id: 'loan1',
      borrowerName: 'John Doe',
      borrowedAt: '2023-01-01T10:00:00Z',
      dueAt: '2023-01-08T10:00:00Z',
      returnedAt: null
    }
  ]

  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(axios.get).mockResolvedValue({ data: mockLoans })
  })

  it('renders bike part information correctly', async () => {
    const wrapper = mount(BikePartCard, {
      props: { part: mockPart }
    })

    await wrapper.vm.$nextTick()

    expect(wrapper.text()).toContain('Test Tire')
    expect(wrapper.text()).toContain('Maxxis')
    expect(wrapper.text()).toContain('TIRE')
    expect(wrapper.text()).toContain('Kogus:3/3')
  })

  it('emits edit event when edit button is clicked', async () => {
    const wrapper = mount(BikePartCard, {
      props: { part: mockPart }
    })

    const editButton = wrapper.find('[title="Muuda"]')
    await editButton.trigger('click')

    expect(wrapper.emitted('edit')).toBeTruthy()
    expect(wrapper.emitted('edit')?.[0]).toEqual([mockPart])
  })

  it('emits deleted event when delete button is clicked', async () => {
    const wrapper = mount(BikePartCard, {
      props: { part: mockPart }
    })

    const deleteButton = wrapper.find('[title="Kustuta"]')
    await deleteButton.trigger('click')

    expect(axios.delete).toHaveBeenCalledWith('/api/part/1')
    expect(wrapper.emitted('deleted')).toBeTruthy()
  })

  it('calculates available quantity correctly', async () => {
    const partWithLoans = { ...mockPart, quantity: 5 }
    
    vi.mocked(axios.get).mockResolvedValue({
      data: [
        { id: 'loan1', borrowerName: 'John', borrowedAt: '2023-01-01', returnedAt: null },
        { id: 'loan2', borrowerName: 'Jane', borrowedAt: '2023-01-02', returnedAt: null }
      ]
    })

    const wrapper = mount(BikePartCard, {
      props: { part: partWithLoans }
    })

    await wrapper.vm.$nextTick()
    await new Promise(resolve => setTimeout(resolve, 100))

    expect(wrapper.text()).toContain('Kogus:3/5')
  })
})
