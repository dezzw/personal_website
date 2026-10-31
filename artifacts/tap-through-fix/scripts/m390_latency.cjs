const C = require('./common.cjs');
const fs = require('fs');

const URL = process.env.URL || C.MAIN_URL;
const TAG = process.env.TAG || 'site';
const CARDS = ['nix', 'contact', 'info', 'projects', 'edu'];

(async () => {
  const b = await C.browser();
  const { ctx, page } = await C.ctx(b, 'm390', 'reduce');
  await page.addInitScript(() => {
    window.__probeT0 = 0;
    document.addEventListener(
      'pointerdown',
      (e) => {
        if (e.target.closest('[data-card-id]') === e.target.closest('[role=button], [data-card-id]'))
          window.__probeT0 = performance.now();
      },
      true,
    );
    document.addEventListener(
      'keydown',
      (e) => {
        if (['Enter', ' '].includes(e.key) && e.target.closest('[data-card-id]'))
          window.__probeT0 = performance.now();
      },
      true,
    );
  });
  await C.enter(page, URL, 'm390');
  const rows = [];
  for (const cardId of CARDS) {
    for (const input of ['point', 'enter', 'space']) {
      const samples = [];
      for (let i = 0; i < 5; i++) {
        await page.locator(`[data-card-id="${cardId}"]`).evaluate((e) =>
          e.scrollIntoView({ block: 'center' }),
        );
        await page.waitForTimeout(400);
        const ms = await page.evaluate(
          async ({ cardId, input }) => {
            window.__probeT0 = 0;
            const card = document.querySelector(`[data-card-id="${cardId}"]`);
            const done = new Promise((resolve) => {
              const obs = new MutationObserver(() => {
                if (document.querySelector('[role=dialog]')) {
                  obs.disconnect();
                  resolve(performance.now() - window.__probeT0);
                }
              });
              obs.observe(document.body, { childList: true, subtree: true });
              setTimeout(() => {
                obs.disconnect();
                resolve(null);
              }, 2000);
            });
            if (input === 'point') {
              const r = card.getBoundingClientRect();
              let x = r.left + 30;
              const y = r.top + r.height / 2;
              for (let dx = 30; dx < r.width - 20; dx += 8) {
                const el = document.elementFromPoint(r.left + dx, y);
                if (el && card.contains(el) && !el.closest('a,button')) {
                  x = r.left + dx;
                  break;
                }
              }
              window.__probeT0 = performance.now();
              card.dispatchEvent(
                new PointerEvent('pointerdown', {
                  bubbles: true,
                  button: 0,
                  clientX: x,
                  clientY: y,
                  pointerType: 'touch',
                }),
              );
            } else {
              card.focus();
              window.__probeT0 = performance.now();
              card.dispatchEvent(
                new KeyboardEvent('keydown', {
                  key: input === 'enter' ? 'Enter' : ' ',
                  bubbles: true,
                }),
              );
            }
            return await done;
          },
          { cardId, input },
        );
        samples.push(ms);
        await page.keyboard.press('Escape');
        await page.waitForTimeout(450);
      }
      rows.push({ cardId, input, median: C.median(samples), samples });
    }
  }
  const out = { URL, TAG, rows };
  fs.writeFileSync(
    `/workspace/artifacts/tap-through-fix/data/m390_latency_${TAG}.json`,
    JSON.stringify(out, null, 2),
  );
  console.log(TAG, rows.filter((r) => r.input === 'point').map((r) => r.cardId + ':' + r.median?.toFixed(1)).join(' '));
  await ctx.close();
  await b.close();
})();
