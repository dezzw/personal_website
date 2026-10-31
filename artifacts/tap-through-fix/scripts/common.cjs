const { chromium } = require('playwright-core');
const MAIN_URL = 'https://3b640435.personal-website-3s7.pages.dev';

exports.MAIN_URL = MAIN_URL;

exports.DEV = {
  desk: { viewport: { width: 1280, height: 800 } },
  m390: {
    viewport: { width: 390, height: 844 },
    deviceScaleFactor: 3,
    isMobile: true,
    hasTouch: true,
    userAgent:
      'Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1',
  },
};

exports.browser = () =>
  chromium.launch({
    executablePath: '/usr/bin/google-chrome',
    headless: true,
    args: ['--no-sandbox'],
  });

exports.ctx = async (browser, dev, rm = 'no-preference') => {
  const ctx = await browser.newContext({ ...exports.DEV[dev], reducedMotion: rm });
  const ev = [];
  const t0 = Date.now();
  const T = () => Date.now() - t0;
  await ctx.exposeBinding('__qaRec', (src, x) => {
    ev.push({ t: T(), ...x });
  });
  await ctx.addInitScript(() => {
    const rec = (x) => {
      try {
        window.__qaRec(x);
      } catch (e) {}
    };
    const desc = (e) => {
      if (!e || !e.closest) return null;
      const a = e.closest('a,button,[role=button]');
      return a
        ? a.tagName +
            (a.getAttribute('data-card-id') ? '#' + a.getAttribute('data-card-id') : '') +
            ' ' +
            (a.getAttribute('href') || '') +
            ' "' +
            (a.textContent || '').trim().slice(0, 20) +
            '"'
        : e.tagName;
    };
    new MutationObserver((ms) => {
      for (const m of ms)
        for (const n of m.addedNodes) {
          if (n.nodeType !== 1) continue;
          const d = n.matches?.('[role=dialog]') ? n : n.querySelector?.('[role=dialog]');
          if (d) rec({ k: 'dialogOpen', h2: (d.querySelector('h2') || {}).textContent || null });
        }
    }).observe(document, { childList: true, subtree: true });
    window.addEventListener(
      'click',
      (e) =>
        rec({
          k: 'click',
          target: desc(e.target),
          href: e.target.closest?.('a')?.href || null,
          defaultPrevented: e.defaultPrevented,
        }),
      false,
    );
    window.addEventListener(
      'pointerdown',
      (e) => rec({ k: 'pointerdown', ptype: e.pointerType, target: desc(e.target) }),
      false,
    );
  });
  const page = await ctx.newPage();
  ctx.on('request', (r) => {
    if (r.isNavigationRequest())
      ev.push({
        t: T(),
        k: 'navRequest',
        url: r.url(),
        same: (() => {
          try {
            return r.frame().page() === page;
          } catch {
            return false;
          }
        })(),
      });
  });
  ctx.on('page', (p) => {
    if (p !== page) ev.push({ t: T(), k: 'popup', url: p.url() });
  });
  page.on('framenavigated', (f) => {
    if (f === page.mainFrame()) ev.push({ t: T(), k: 'framenavigated', url: f.url() });
  });
  await ctx.route(/^https:\/\/(www\.)?(linkedin\.com|github\.com)\//, (r) =>
    r.fulfill({ status: 200, contentType: 'text/html', body: '<title>stub</title>' }),
  );
  return { ctx, page, ev, T };
};

exports.enter = async (page, base, dev, waitMs = 2500) => {
  await page.goto(base + '/', { waitUntil: 'networkidle' });
  const eb = await page.getByText('Enter Site').first().boundingBox();
  if (dev === 'desk') await page.mouse.click(eb.x + eb.width / 2, eb.y + eb.height / 2);
  else await page.touchscreen.tap(eb.x + eb.width / 2, eb.y + eb.height / 2);
  await page.waitForFunction(
    () =>
      document.querySelector('[data-card-id=projects]') &&
      document.querySelector('[data-card-id=projects]').textContent.includes('Security Tooling'),
    null,
    { timeout: 15000 },
  );
  if (waitMs) await page.waitForTimeout(waitMs);
};

exports.press = async (page, dev, x, y) => {
  if (dev === 'desk') await page.mouse.click(x, y);
  else await page.touchscreen.tap(x, y);
};

exports.bundleFingerprint = async (base) => {
  const res = await fetch(base + '/');
  const html = await res.text();
  const m = html.match(/assets\/index-[a-f0-9]+\.js/);
  return m ? m[0] : null;
};

exports.median = (arr) => {
  const a = arr.filter((x) => x != null && !Number.isNaN(x)).sort((x, y) => x - y);
  if (!a.length) return null;
  const mid = Math.floor(a.length / 2);
  return a.length % 2 ? a[mid] : (a[mid - 1] + a[mid]) / 2;
};
