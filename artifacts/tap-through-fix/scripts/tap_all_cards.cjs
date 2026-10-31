const C = require('./common.cjs');
const fs = require('fs');

const URL = process.env.URL || C.MAIN_URL;
const TAG = process.env.TAG || 'site';
const N = +(process.env.N || 5);
const N_CONTACT = +(process.env.N_CONTACT || 20);
const RMQ = process.env.RMQ || 'reduce';
const OUT = process.env.OUT || `/workspace/artifacts/tap-through-fix/data/tap_all_${TAG}_${RMQ}.json`;
const CARDS = ['nix', 'contact', 'info', 'projects', 'edu'];

async function blankTap(page, cardId) {
  return page.evaluate((id) => {
    const w = document.querySelector(`[data-card-id="${id}"]`);
    w.scrollIntoView({ block: 'center' });
    const r = w.getBoundingClientRect();
    for (let x = Math.ceil(r.left + 8); x < r.right - 8; x += 12) {
      for (let y = Math.ceil(r.top + 8); y < r.bottom - 8; y += 12) {
        const e = document.elementFromPoint(x, y);
        if (!e || !w.contains(e) || e.closest('a,button')) continue;
        if (e.tagName === 'H2' || e.tagName === 'P') continue;
        return [Math.round(x), Math.round(y)];
      }
    }
    return null;
  }, cardId);
}

(async () => {
  const bundle = await C.bundleFingerprint(URL);
  const b = await C.browser();
  const { ctx, page, ev } = await C.ctx(b, 'm390', RMQ);
  await C.enter(page, URL, 'm390');
  const agg = {};
  for (const cardId of CARDS) {
    const reps = cardId === 'contact' ? N_CONTACT : N;
    let through = 0;
    let opened = 0;
    for (let i = 0; i < reps; i++) {
      await page.waitForTimeout(400);
      const pt = await blankTap(page, cardId);
      if (!pt) continue;
      const evMark = ev.length;
      await C.press(page, 'm390', pt[0], pt[1]);
      await page.waitForTimeout(350);
      const tail = ev.slice(evMark);
      const hit =
        tail.some((e) => e.k === 'popup') ||
        tail.some((e) => e.k === 'framenavigated' && !String(e.url).includes('pages.dev'));
      if (hit) through++;
      if (await page.evaluate(() => !!document.querySelector('[role=dialog]'))) {
        opened++;
        await page.keyboard.press('Escape');
        await page.waitForTimeout(600);
      }
    }
    agg[cardId] = { n: reps, through, opened };
  }
  fs.writeFileSync(OUT, JSON.stringify({ URL, bundle, TAG, RMQ, agg }, null, 2));
  console.log(TAG, agg);
  await ctx.close();
  await b.close();
})();
