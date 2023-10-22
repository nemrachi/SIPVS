import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8082/api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // maybe for testing of their service
      '/posts': {
        target: "http://test.ditec.sk/",
        secure: true,
        rewrite: (path) => path.replace(/^\/posts/, ''),
      },
    }
  },
})
