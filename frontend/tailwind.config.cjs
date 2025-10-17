module.exports = {
    content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    theme: {
        extend: {
            boxShadow: { card: '0 4px 24px rgba(0,0,0,0.06)' },
            colors: { ink: { 50: '#f6f7fb', 100: '#eef0f7' } },
        },
    },
    plugins: [],
}
