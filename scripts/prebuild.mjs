#!/usr/bin/env node
import { spawnSync } from 'node:child_process';

const bb = spawnSync('bb', ['build.clj'], { stdio: 'inherit' });

if (bb.status === 0) {
  process.exit(0);
}

console.warn('Babashka unavailable or failed; falling back to Node content build.');
const node = spawnSync('node', ['scripts/build-content.mjs'], { stdio: 'inherit' });
process.exit(node.status ?? 1);
