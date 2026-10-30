import { readFileSync, writeFileSync } from 'node:fs';
import { join } from 'node:path';
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
    closeBundle() {
      const outDir = join(process.cwd(), 'dist');
      const indexHtml = join(outDir, 'index.html');
      const indexSource = readFileSync(indexHtml, 'utf8');
      const robotsMeta = '    <meta name="robots" content="noindex" />\n';
      const notFoundHtml = indexSource.replace(
        '<meta name="viewport"',
        `${robotsMeta}    <meta name="viewport"`,
      );
      writeFileSync(join(outDir, '404.html'), notFoundHtml);
    },
  };
}

export default defineConfig({
  base: '/',
  plugins: [react(), siteBuildPlugin()],
  build: {
    outDir: './dist',
  },
  server: {
    host: true,
    port: 8000,
  },
});
