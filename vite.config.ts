import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    outDir: './build',
  },
  plugins: [react()],
  server: {
    host: true,
    port: 8000, // This is the port which we will use in docker
  },
});
