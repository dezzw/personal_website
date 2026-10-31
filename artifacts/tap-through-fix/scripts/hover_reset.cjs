const C = require('./common.cjs');
const fs = require('fs');

const URL = process.env.URL || C.MAIN_URL;
const TAG = process.env.TAG || 'site';

(async () => {
  const b = await C.browser();
  const { ctx, page } = await C.ctx(b, 'desk', 'no-preference');
  await C.enter(page, URL, 'desk');
  const card = page.locator('[data-card-id="nix"]');
  await card.scrollIntoViewIfNeeded();
  await card.hover();
  await page.waitForTimeout(250);
  const scaleHover = await card.evaluate((el) => getComputedStyle(el).transform);
  await card.click();
  await page.waitForTimeout(500);
  await page.keyboard.press('Escape');
  await page.waitForTimeout(900);
  await page.mouse.move(10, 10);
  await page.waitForTimeout(200);
  const scaleAfter = await card.evaluate((el) => getComputedStyle(el).transform);
  const out = {
    URL,
    TAG,
    scaleHover,
    scaleAfter,
    resetOk: scaleAfter === 'none' || scaleAfter === 'matrix(1, 0, 0, 1, 0, 0)',
  };
  fs.writeFileSync(
    `/workspace/artifacts/tap-through-fix/data/hover_reset_${TAG}.json`,
    JSON.stringify(out, null, 2),
  );
  console.log(out);
  await ctx.close();
  await b.close();
})();
