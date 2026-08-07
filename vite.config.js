import { execSync } from 'node:child_process';
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

function run(command) {
  execSync(command, { stdio: 'inherit' });
}

function siteBuildPlugin() {
  return {
    name: 'site-build',
    buildStart() {
      run('node scripts/prebuild.mjs');
      run('npx squint compile');
    },
  };
}

export default defineConfig({
  plugins: [react(), siteBuildPlugin()],
  build: {
    outDir: './build',
  },
  server: {
    host: true,
    port: 8000,
  },
});
