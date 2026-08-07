#!/usr/bin/env bash
set -euo pipefail

if command -v bun >/dev/null 2>&1; then
  bun install
  bun run build
else
  npm ci
  npm run build
fi
