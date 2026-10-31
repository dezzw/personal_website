const C = require('./common.cjs');
const fs = require('fs');
const URL = process.env.URL || C.MAIN_URL;
const N = +(process.env.N || 20);
const RMQ = process.env.RMQ || 'reduce';
const OUT = process.env.OUT || `/workspace/artifacts/tap-through-fix/data/noentry_${process.env.TAG || 'site'}.json`;

(async () => {
  const bundle = await C.bundleFingerprint(URL);
  const b = await C.browser();
  const { ctx, page, ev } = await C.ctx(b, 'm390', RMQ);
  const R = [];
  for (let i = 0; i < N; i++) {
    await page.goto(URL + '/', { waitUntil: 'networkidle' });
    const eb = await page.getByText('Enter Site').first().boundingBox();
    await C.press(page, 'm390', eb.x + eb.width / 2, eb.y + eb.height / 2);
    await page.waitForSelector('[data-card-id="contact"]', { timeout: 15000 });
    await page.waitForTimeout(100);
    const evMark = ev.length;
    await C.press(page, 'm390', 198, 612);
    await page.waitForTimeout(400);
    const tail = ev.slice(evMark);
    const popup = tail.some((e) => e.k === 'popup');
    const mainNav = tail.some((e) => e.k === 'framenavigated' && !String(e.url).includes('pages.dev'));
    R.push({ i, popup, mainNav, through: popup || mainNav });
  }
  fs.writeFileSync(OUT, JSON.stringify({ URL, bundle, N, RMQ, through: R.filter((r) => r.through).length, R }, null, 2));
  console.log('through', R.filter((r) => r.through).length, '/', N);
  await ctx.close();
  await b.close();
})();
