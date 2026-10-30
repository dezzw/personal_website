#!/usr/bin/env node
import { spawnSync } from 'node:child_process';
import { createHash } from 'node:crypto';
import fs from 'node:fs';
import path from 'node:path';

const BB_VERSION = '1.13.229';
const BB_SHA256 =
  '64fb984991c6e68f44e8a9391176a9bb411f9b9ae9959a6bca5307c75a7414da';
const BB_TAR = `babashka-${BB_VERSION}-linux-amd64-static.tar.gz`;
const BB_URL = `https://github.com/babashka/babashka/releases/download/v${BB_VERSION}/${BB_TAR}`;

function bbOnPath() {
  const checker = process.platform === 'win32' ? 'where' : 'which';
  return spawnSync(checker, ['bb'], { encoding: 'utf8' }).status === 0;
}

function runBuild(bbExecutable) {
  return spawnSync(bbExecutable, ['build.clj'], { stdio: 'inherit' });
}

async function ensureCfPagesBb() {
  const cacheDir = path.join(process.cwd(), 'node_modules', '.cache', 'bb');
  const bbBin = path.join(cacheDir, 'bb');
  if (fs.existsSync(bbBin)) {
    return bbBin;
  }

  fs.mkdirSync(cacheDir, { recursive: true });
  const tarPath = path.join(cacheDir, BB_TAR);

  const res = await fetch(BB_URL);
  if (!res.ok) {
    console.error(`Failed to download Babashka ${BB_VERSION}: HTTP ${res.status}`);
    process.exit(1);
  }

  const buf = Buffer.from(await res.arrayBuffer());
  const hash = createHash('sha256').update(buf).digest('hex');
  if (hash !== BB_SHA256) {
    console.error(
      `Babashka ${BB_VERSION} SHA256 mismatch: expected ${BB_SHA256}, got ${hash}`,
    );
    process.exit(1);
  }

  fs.writeFileSync(tarPath, buf);
  const extract = spawnSync('tar', ['-xzf', tarPath, '-C', cacheDir], {
    stdio: 'inherit',
  });
  if (extract.status !== 0) {
    process.exit(extract.status ?? 1);
  }
  if (!fs.existsSync(bbBin)) {
    console.error(`Expected Babashka binary at ${bbBin} after extract`);
    process.exit(1);
  }
  fs.chmodSync(bbBin, 0o755);
  console.log(`Downloaded and verified Babashka ${BB_VERSION} for Cloudflare Pages`);
  return bbBin;
}

async function main() {
  let bbExecutable = 'bb';
  if (!bbOnPath()) {
    if (process.env.CF_PAGES === '1') {
      bbExecutable = await ensureCfPagesBb();
    } else {
      console.error(
        'Babashka (bb) is not on PATH. Install Babashka (see README) or run on Cloudflare Pages.',
      );
      process.exit(1);
    }
  }

  const result = runBuild(bbExecutable);
  if (result.status !== 0) {
    console.error('bb build.clj failed');
    process.exit(result.status ?? 1);
  }
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
