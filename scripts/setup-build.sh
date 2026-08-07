#!/usr/bin/env bash
set -euo pipefail

if ! command -v bb >/dev/null 2>&1; then
  curl -fsSL https://raw.githubusercontent.com/babashka/babashka/master/install | bash -s -- --dir "$HOME/.local/bin"
  export PATH="$HOME/.local/bin:$PATH"
fi

if command -v bun >/dev/null 2>&1; then
  bun install
  bun run build
else
  npm ci
  npm run build
fi
