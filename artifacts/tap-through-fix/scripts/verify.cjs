/**
 * Full verification: tap-through, noentry, contact buttons, latency, hover-reset.
 * Usage: URL=https://... node verify.js [out.json]
 */
const C = require('./common.cjs');
const fs = require('fs');
const path = require('path');

const URL = process.env.URL || C.MAIN_URL;
const TAG = process.env.TAG || 'site';
const OUT = process.argv[2] || path.join(__dirname, '../data', `verify_${TAG}.json`);

const CARDS = ['nix', 'contact', 'info', 'projects', 'edu'];
const N_CONTACT = +(process.env.N_CONTACT || 20);
const N_OTHER = +(process.env.N_OTHER || 5);
const N_NOENTRY = +(process.env.N_NOENTRY || 20);

async function findBlankPoint(page, cardId) {
  return page.evaluate((id) => {
    const w = document.querySelector(`[data-card-id="${id}"]`);
    if (!w) return null;
    w.scrollIntoView({ block: 'center' });
    const r = w.getBoundingClientRect();
    const candidates = [];
    for (let x = Math.ceil(r.left + 8); x < r.right - 8; x += 14) {
      for (let y = Math.ceil(r.top + 8); y < r.bottom - 8; y += 14) {
        const e = document.elementFromPoint(x, y);
        if (!e || !w.contains(e)) continue;
        if (e.closest('a, button')) continue;
        const t = (e.textContent || '').trim();
        if (t.length > 2 && e === w) continue;
        if (e.tagName === 'H2' || e.tagName === 'P') continue;
        candidates.push([Math.round(x), Math.round(y)]);
      }
    }
    return candidates[0] || null;
  }, cardId);
}

async function settleCard(page, cardId) {
  const c = page.locator(`[data-card-id="${cardId}"]`);
  await c.evaluate((e) => e.scrollIntoView({ block: 'center' }));
  await page.waitForTimeout(500);
}

function classifyTapThrough(ev, afterT) {
  const tail = ev.filter((e) => e.t >= afterT);
  const popup = tail.some((e) => e.k === 'popup');
  const mainNav = tail.some(
    (e) => e.k === 'framenavigated' && e.url && !e.url.includes('pages.dev'),
  );
  const extNav = tail.some(
    (e) => e.k === 'navRequest' && e.same && /github|linkedin|mailto|resume/.test(e.url),
  );
  const clickLink = tail.some(
    (e) => e.k === 'click' && e.href && !e.defaultPrevented,
  );
  return { popup, mainNav, extNav, clickLink, through: popup || mainNav || extNav || clickLink };
}

async function tapBlankSuite(page, dev, ev, nByCard) {
  const rows = [];
  for (const cardId of CARDS) {
    const n = cardId === 'contact' ? nByCard.contact : nByCard.other;
    for (let i = 0; i < n; i++) {
      await settleCard(page, cardId);
      const pt = await findBlankPoint(page, cardId);
      if (!pt) {
        rows.push({ cardId, i, err: 'no blank point' });
        continue;
      }
      const evMark = ev.length;
      await C.press(page, dev, pt[0], pt[1]);
      let opened = false;
      for (let k = 0; k < 25; k++) {
        await page.waitForTimeout(80);
        opened = await page.evaluate(() => !!document.querySelector('[role=dialog]'));
        if (opened) break;
      }
      await page.waitForTimeout(350);
      const through = classifyTapThrough(ev.slice(evMark), 0);
      rows.push({ cardId, i, pt, opened, ...through });
      if (opened) {
        await page.keyboard.press('Escape');
        await page.waitForTimeout(600);
      }
    }
  }
  return rows;
}

async function noentrySuite(page, dev, ev, n) {
  const rows = [];
  for (let i = 0; i < n; i++) {
    await page.goto(URL + '/', { waitUntil: 'networkidle' });
    const eb = await page.getByText('Enter Site').first().boundingBox();
    await C.press(page, dev, eb.x + eb.width / 2, eb.y + eb.height / 2);
    await page.waitForSelector('[data-card-id="contact"]', { timeout: 15000 });
    await page.waitForTimeout(80);
    const pt =
      (await findBlankPoint(page, 'contact')) ||
      (await page.evaluate(() => {
        const w = document.querySelector('[data-card-id="contact"]');
        const r = w.getBoundingClientRect();
        return [Math.round(r.left + r.width / 2), Math.round(r.top + r.height * 0.75)];
      }));
    const evMark = ev.length;
    await C.press(page, dev, pt[0], pt[1]);
    await page.waitForTimeout(400);
    rows.push({ i, pt, ...classifyTapThrough(ev.slice(evMark), 0) });
  }
  return rows;
}

async function contactButtons(page, dev, ev) {
  const labels = ['Email', 'GitHub', 'LinkedIn', 'Resume'];
  const rows = [];
  await settleCard(page, 'contact');
  for (const label of labels) {
    const link = page.locator('[data-card-id="contact"] a').filter({ hasText: label });
    const href = await link.getAttribute('href');
    const evBefore = ev.length;
    await link.tap();
    await page.waitForTimeout(300);
    const tail = ev.slice(evBefore);
    const dialog = await page.evaluate(() => !!document.querySelector('[role=dialog]'));
    const nav = tail.some((e) => e.k === 'navRequest' || e.k === 'popup');
    rows.push({ label, href, dialogOpened: dialog, navigated: nav });
    if (dialog) {
      await page.keyboard.press('Escape');
      await page.waitForTimeout(400);
    } else {
      await page.goto(URL + '/', { waitUntil: 'networkidle' });
      await C.enter(page, URL, dev, 2500);
    }
  }
  return rows;
}

async function openLatency(page, dev, input, cardId, n) {
  const samples = [];
  for (let i = 0; i < n; i++) {
    await settleCard(page, cardId);
    const ms = await page.evaluate(
      async ({ input, cardId }) => {
        const card = document.querySelector(`[data-card-id="${cardId}"]`);
        const t0 = performance.now();
        const done = new Promise((resolve) => {
          const obs = new MutationObserver(() => {
            if (document.querySelector('[role=dialog]')) {
              obs.disconnect();
              resolve(performance.now() - t0);
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
          let x = r.left + r.width / 2,
            y = r.top + r.height / 2;
          for (let dx = 0; dx < r.width; dx += 12) {
            const e = document.elementFromPoint(r.left + dx, r.top + r.height / 2);
            if (e && card.contains(e) && !e.closest('a,button')) {
              x = r.left + dx;
              break;
            }
          }
          card.dispatchEvent(new PointerEvent('pointerdown', { bubbles: true, button: 0, clientX: x, clientY: y }));
        } else {
          card.focus();
          card.dispatchEvent(new KeyboardEvent('keydown', { key: input === 'enter' ? 'Enter' : ' ', bubbles: true }));
        }
        return await done;
      },
      { input, cardId },
    );
    samples.push(ms);
    await page.keyboard.press('Escape');
    await page.waitForTimeout(500);
  }
  return { cardId, input, median: C.median(samples), samples };
}

async function hoverReset(page) {
  await settleCard(page, 'nix');
  const scaleBefore = await page.evaluate(() => {
    const el = document.querySelector('[data-card-id="nix"]');
    const t = getComputedStyle(el).transform;
    return t;
  });
  await page.locator('[data-card-id="nix"]').hover();
  await page.waitForTimeout(200);
  await page.locator('[data-card-id="nix"]').click();
  await page.waitForTimeout(400);
  await page.keyboard.press('Escape');
  await page.waitForTimeout(800);
  const scaleAfter = await page.evaluate(() => {
    const el = document.querySelector('[data-card-id="nix"]');
    return getComputedStyle(el).transform;
  });
  return { scaleBefore, scaleAfter, resetOk: scaleAfter === 'none' || scaleAfter === scaleBefore };
}

(async () => {
  const bundle = await C.bundleFingerprint(URL);
  const b = await C.browser();
  const result = { url: URL, tag: TAG, bundle, at: new Date().toISOString() };

  for (const rm of ['reduce', 'no-preference']) {
    const { ctx, page, ev } = await C.ctx(b, 'm390', rm);
    await C.enter(page, URL, 'm390', 2500);
    const tapBlank = await tapBlankSuite(page, 'm390', ev, {
      contact: N_CONTACT,
      other: N_OTHER,
    });
    const agg = {};
    for (const id of CARDS) {
      const sub = tapBlank.filter((r) => r.cardId === id && !r.err);
      agg[id] = {
        n: sub.length,
        through: sub.filter((r) => r.through).length,
        opened: sub.filter((r) => r.opened).length,
      };
    }
    result[`m390_${rm}_tapBlank`] = { agg, rows: tapBlank };
    await ctx.close();

    const { ctx: ctx2, page: page2, ev: ev2 } = await C.ctx(b, 'm390', rm);
    const noentry = await noentrySuite(page2, 'm390', ev2, N_NOENTRY);
    result[`m390_${rm}_noentry`] = {
      through: noentry.filter((r) => r.through).length,
      n: noentry.length,
      rows: noentry,
    };
    await ctx2.close();
  }

  {
    const { ctx, page, ev } = await C.ctx(b, 'm390', 'reduce');
    await C.enter(page, URL, 'm390', 2500);
    result.contactButtons = await contactButtons(page, 'm390', ev);
    await ctx.close();
  }

  {
    const { ctx, page } = await C.ctx(b, 'desk', 'no-preference');
    await C.enter(page, URL, 'desk', 2500);
    const lat = [];
    for (const cardId of CARDS) {
      for (const input of ['point', 'enter', 'space']) {
        lat.push(await openLatency(page, 'desk', input, cardId, 5));
      }
    }
    result.desk_latency = lat;
    result.hoverReset = await hoverReset(page);
    await ctx.close();
  }

  fs.mkdirSync(path.dirname(OUT), { recursive: true });
  fs.writeFileSync(OUT, JSON.stringify(result, null, 2));
  console.log('Wrote', OUT);
  console.log(JSON.stringify(result, (k, v) => (k === 'rows' || k === 'samples' ? undefined : v), 2));
  await b.close();
})();
