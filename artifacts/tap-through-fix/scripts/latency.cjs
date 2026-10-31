const C = require('./common.cjs');
const fs = require('fs');

const URL = process.env.URL || C.MAIN_URL;
const TAG = process.env.TAG || 'site';
const OUT = process.env.OUT || `/workspace/artifacts/tap-through-fix/data/latency_${TAG}.json`;
const CARDS = ['nix', 'contact', 'info', 'projects', 'edu'];

(async () => {
  const bundle = await C.bundleFingerprint(URL);
  const b = await C.browser();
  const { ctx, page } = await C.ctx(b, 'desk', 'no-preference');
  await C.enter(page, URL, 'desk');
  const rows = [];
  for (const cardId of CARDS) {
    for (const input of ['point', 'enter', 'space']) {
      const samples = [];
      for (let i = 0; i < 5; i++) {
        await page.locator(`[data-card-id="${cardId}"]`).evaluate((e) =>
          e.scrollIntoView({ block: 'center' }),
        );
        await page.waitForTimeout(300);
        const ms = await page.evaluate(
          ({ cardId, input }) =>
            new Promise((resolve) => {
              const card = document.querySelector(`[data-card-id="${cardId}"]`);
              const obs = new MutationObserver(() => {
                if (document.querySelector('[role=dialog]')) {
                  obs.disconnect();
                  resolve(window.__openMs ?? null);
                }
              });
              obs.observe(document.body, { childList: true, subtree: true });
              window.__openMs = null;
              const t0 = performance.now();
              const mark = () => {
                window.__openMs = performance.now() - t0;
              };
              if (input === 'point') {
                const r = card.getBoundingClientRect();
                let x = r.left + 20;
                const y = r.top + r.height / 2;
                for (let dx = 20; dx < r.width - 20; dx += 10) {
                  const el = document.elementFromPoint(r.left + dx, y);
                  if (el && card.contains(el) && !el.closest('a,button')) {
                    x = r.left + dx;
                    break;
                  }
                }
                card.addEventListener(
                  'pointerdown',
                  () => mark(),
                  { once: true, capture: true },
                );
                card.dispatchEvent(
                  new PointerEvent('pointerdown', {
                    bubbles: true,
                    cancelable: true,
                    button: 0,
                    clientX: x,
                    clientY: y,
                    pointerType: 'mouse',
                  }),
                );
              } else {
                card.addEventListener(
                  'keydown',
                  () => mark(),
                  { once: true, capture: true },
                );
                card.focus();
                card.dispatchEvent(
                  new KeyboardEvent('keydown', {
                    key: input === 'enter' ? 'Enter' : ' ',
                    bubbles: true,
                    cancelable: true,
                  }),
                );
              }
              setTimeout(() => {
                obs.disconnect();
                resolve(window.__openMs);
              }, 1500);
            }),
          { cardId, input },
        );
        samples.push(ms);
        await page.keyboard.press('Escape');
        await page.waitForTimeout(500);
      }
      rows.push({ cardId, input, median: C.median(samples), samples });
    }
  }
  fs.writeFileSync(OUT, JSON.stringify({ URL, bundle, TAG, rows }, null, 2));
  console.log(TAG, rows.map((r) => `${r.cardId}/${r.input}:${r.median?.toFixed(1)}`).join(' '));
  await ctx.close();
  await b.close();
})();
