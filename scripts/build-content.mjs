#!/usr/bin/env node
/**
 * Node fallback for content generation when Babashka is unavailable.
 * Keeps Cloudflare Pages builds working without a bb install step.
 */
import fs from 'node:fs';
import path from 'node:path';
import { parseEDNString } from 'edn-data';

const root = process.cwd();
const contentDir = path.join(root, 'content');
const publicDir = path.join(root, 'public');

function ensureDir(dir) {
  fs.mkdirSync(dir, { recursive: true });
}

function escapeHtml(text) {
  return text
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;');
}

function inlineFormat(text) {
  return escapeHtml(text).replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
}

function orgBodyToHtml(lines) {
  const html = [];
  let inList = false;

  for (const rawLine of lines) {
    const line = rawLine.trim();
    if (!line) continue;

    if (line.startsWith('* ')) {
      if (inList) {
        html.push('</ul>');
        inList = false;
      }
      html.push(`<h2>${inlineFormat(line.slice(2))}</h2>`);
    } else if (line.startsWith('- ')) {
      if (!inList) {
        html.push('<ul>');
        inList = true;
      }
      html.push(`<li>${inlineFormat(line.slice(2))}</li>`);
    } else {
      if (inList) {
        html.push('</ul>');
        inList = false;
      }
      html.push(`<p>${inlineFormat(line)}</p>`);
    }
  }

  if (inList) html.push('</ul>');
  return html.join('\n');
}

function parseOrgFile(filePath) {
  const text = fs.readFileSync(filePath, 'utf8');
  const lines = text.split('\n');
  const slug = path.basename(filePath, '.org');

  const title =
    lines
      .find((line) => line.startsWith('#+TITLE:'))
      ?.slice('#+TITLE:'.length)
      .trim() ?? slug;

  const date =
    lines
      .find((line) => line.startsWith('#+DATE:'))
      ?.slice('#+DATE:'.length)
      .trim() ?? null;

  const bodyLines = lines.filter(
    (line) => !line.startsWith('#+') && line.trim() !== '',
  );

  return { slug, title, date, html: orgBodyToHtml(bodyLines) };
}

function keywordize(value) {
  if (Array.isArray(value)) {
    return value.map(keywordize);
  }
  if (value && typeof value === 'object') {
    return Object.fromEntries(
      Object.entries(value).map(([key, val]) => [
        key.startsWith(':') ? key.slice(1) : key,
        keywordize(val),
      ]),
    );
  }
  return value;
}

function readEdnFile(filePath) {
  const raw = fs.readFileSync(filePath, 'utf8');
  return keywordize(parseEDNString(raw));
}

function writeJson(filePath, data) {
  fs.writeFileSync(filePath, JSON.stringify(data, null, 2));
}

function buildBlog() {
  const blogDir = path.join(contentDir, 'blog');
  const outDir = path.join(publicDir, 'blog');
  ensureDir(outDir);

  if (!fs.existsSync(blogDir)) return [];

  const posts = fs
    .readdirSync(blogDir)
    .filter((name) => name.endsWith('.org'))
    .map((name) => parseOrgFile(path.join(blogDir, name)))
    .sort((a, b) => a.slug.localeCompare(b.slug));

  for (const post of posts) {
    writeJson(path.join(outDir, `${post.slug}.json`), post);
  }

  writeJson(path.join(outDir, 'manifest.json'), {
    posts: posts.map(({ slug, title, date }) => ({ slug, title, date })),
  });

  return posts;
}

function buildSiteData() {
  const outDir = path.join(publicDir, 'data');
  ensureDir(outDir);

  for (const file of ['projects.edn', 'experience.edn']) {
    const data = readEdnFile(path.join(contentDir, 'data', file));
    writeJson(path.join(outDir, file.replace('.edn', '.json')), data);
  }
}

function buildSeo(posts) {
  fs.writeFileSync(
    path.join(publicDir, 'robots.txt'),
    'User-agent: *\nAllow: /\n\nSitemap: https://www.dezzw.com/sitemap.xml\n',
  );

  const urls = ['/', '/blog', ...posts.map((post) => `/blog/${post.slug}`)];
  const sitemap = [
    '<?xml version="1.0" encoding="UTF-8"?>',
    '<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">',
    ...urls.map(
      (url) => `  <url><loc>https://www.dezzw.com${url}</loc></url>`,
    ),
    '</urlset>',
    '',
  ].join('\n');

  fs.writeFileSync(path.join(publicDir, 'sitemap.xml'), sitemap);
}

ensureDir(publicDir);
const posts = buildBlog();
buildSiteData();
buildSeo(posts);
console.log('Built content with Node fallback');
