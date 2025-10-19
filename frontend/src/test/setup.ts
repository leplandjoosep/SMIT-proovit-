import { vi } from 'vitest'

// Mock axios
vi.mock('axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn()
  }
}))

// Mock window.scrollTo
Object.defineProperty(window, 'scrollTo', {
  value: () => {},
  writable: true
})

// Mock window.confirm
Object.defineProperty(window, 'confirm', {
  value: () => true,
  writable: true
})
