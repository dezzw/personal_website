const C = require('./common.cjs');
const fs = require('fs');
const TAG = process.env.TAG || 'base';
const N = +(process.env.N || 20);
const RMQ = process.env.RMQ || 'reduce';
const URL = process.env.URL || C.MAIN_URL;
const OUT =
  process.env.OUT ||
  `/workspace/artifacts/tap-through-fix/data/taprepro_${TAG}_${RMQ}.json`;

(async () => {
  const bundle = await C.bundleFingerprint(URL);
  const b = await C.browser();
  const { ctx, page, ev } = await C.ctx(b, 'm390', RMQ);
  await C.enter(page, URL, 'm390');
  const R = [];
  const dlgH = () =>
    page.evaluate(() => {
      const d = document.querySelector('[role=dialog]');
      return d ? (d.querySelector('h2') || {}).textContent || 'dialog' : null;
    });
  for (let i = 0; i < N; i++) {
    const c = page.locator('[data-card-id="contact"]');
    await c.evaluate((e) => e.scrollIntoView({ block: 'center' }));
    await page.waitForTimeout(500);
    const pts = [
      [192, 606],
      [362, 736],
    ];
    for (const [x, y] of pts) {
      const evMark = ev.length;
      await C.press(page, 'm390', x, y);
      let opened = null;
      let at = null;
      const t = Date.now();
      for (let k = 0; k < 30; k++) {
        await page.waitForTimeout(100);
        opened = await dlgH();
        if (opened) {
          at = Date.now() - t;
          break;
        }
      }
      await page.waitForTimeout(300);
      const tail = ev.slice(evMark);
      const popup = tail.some((e) => e.k === 'popup');
      const mainNav = tail.some((e) => e.k === 'framenavigated' && !String(e.url).includes('pages.dev'));
      R.push({ i, x, y, opened, at, popup, mainNav, through: popup || mainNav });
      if (opened) {
        await page.keyboard.press('Escape');
        await page.waitForTimeout(1000);
      }
    }
  }
  fs.writeFileSync(
    OUT,
    JSON.stringify({ URL, bundle, TAG, RMQ, R, through: R.filter((r) => r.through).length }, null, 2),
  );
  console.log('through', R.filter((r) => r.through).length, '/', R.length);
  await ctx.close();
  await b.close();
})();
