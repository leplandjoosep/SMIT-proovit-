# Frontend Test Setup

## Running Tests

```bash
# Run all tests
pnpm test

# Run tests in watch mode
pnpm test --watch

# Run tests with UI
pnpm test:ui
```

## Test Configuration

- **Framework**: Vitest
- **Test Environment**: JSDOM
- **Vue Testing**: @vue/test-utils
- **TypeScript**: Configured in `tsconfig.test.json`

## Mocking

- **Axios**: Automatically mocked in `setup.ts`
- **Window APIs**: `scrollTo` and `confirm` are mocked
- **Vue Router**: Created in individual tests as needed
