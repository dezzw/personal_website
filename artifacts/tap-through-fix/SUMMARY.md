# Mobile tap-through fix — verification summary

| | **main** | **PR #20 head** |
|---|---|---|
| **Deployment URL** | https://3b640435.personal-website-3s7.pages.dev | https://d6e8495a.personal-website-3s7.pages.dev |
| **JS bundle** | `assets/index-231acdfe.js` | `assets/index-cd2cfbcc.js` |
| **Git SHA** | `c84d534` (base) | `c65b9e31de440e8b4400da7cdf34bc9f70fc57b4` |

## Fix (1 file)

`src/pages/dashboard.cljs`: after opening a card from non-interactive `pointerdown`, register a one-shot capture `click` listener on `document` (500ms cleanup) that `preventDefault`s + `stopPropagation`s the synthesized click that would otherwise hit dialog links.

---

## 1. Contact blank tap (Vera repro coordinates)

Script: `scripts/taprepro_orig.cjs` — m390 touch, scroll contact to center, wait 500ms, tap `(192,606)` and `(362,736)` per rep, detect popup / main-frame navigation.

| reduced motion | main tap-through | PR tap-through | n |
|---|---|---|---|
| `reduce` | **30 / 60** (LinkedIn popup) | **0 / 60** | 30 reps × 2 points |
| `no-preference` | **20 / 40** | **0 / 40** | 20 reps × 2 points |

Raw: `data/taprepro_main_reduce.json`, `data/taprepro_main_nopref.json`, `data/taprepro_pr_reduce.json`, `data/taprepro_pr_nopref.json`

---

## 2. All cards — blank-area tap (auto blank point)

Script: `scripts/tap_all_cards.cjs` — m390, contact **n=20**, other cards **n=5**, tap-through = popup or off-site `framenavigated`.

| card | RM reduce main | RM reduce PR | RM no-pref main | RM no-pref PR |
|---|---|---|---|---|
| nix | 0/5 | 0/5 | 0/5 | 0/5 |
| contact | 0/20 | 0/20 | 0/20 | 0/20 |
| info | 0/5 | 0/5 | 0/5 | 0/5 |
| projects | 0/5 | 0/5 | 0/5 | 0/5 |
| edu | 0/5 | 0/5 | 0/5 | 0/5 |

Note: auto blank points differ from Vera’s fixed contact coords; contact ghost-click is covered by §1.

Raw: `data/tap_all_main_reduce.json`, `data/tap_all_pr_reduce.json`, `data/tap_all_main_nopref.json`, `data/tap_all_pr_nopref.json`

---

## 3. Enter Site → immediate contact tap

Script: `scripts/noentry.cjs` and `scripts/verify.cjs` — tap `(198,612)` ~100ms after contact mounts.

| | main | PR |
|---|---|---|
| through (popup / nav), n=20, RM reduce | 0/20 | 0/20 |
| through, n=20, RM no-preference | 0/20 | 0/20 |

Headless run did not reproduce the historical main-only full-page GitHub navigation (18/20 in Vera’s notes); contact coordinate repro in §1 is the reliable signal.

Raw: `data/noentry_main.json`, `data/noentry_pr.json`, `data/verify_*_noentry` inside `verify_main.json` / `verify_pr.json`

---

## 4. Collapsed contact buttons (PR #17)

Script: `scripts/verify.cjs` — tap each of 4 links on closed contact card; expect navigation, **no** dialog.

| button | main | PR |
|---|---|---|
| Email | navigated, no dialog | navigated, no dialog |
| GitHub | navigated, no dialog | navigated, no dialog |
| LinkedIn | navigated, no dialog | navigated, no dialog |
| Resume | navigated, no dialog | navigated, no dialog |

---

## 5. Open latency medians (ms, n=5 per cell)

Scripts: `scripts/latency.cjs` (desktop dispatch probe), `scripts/m390_latency.cjs` (m390 reduce, pointerdown/keydown → dialog mount).

### Desktop (`desk`, no-preference)

| card | input | main | PR |
|---|---|---|---|
| nix | point | 0.3 | 0.2 |
| nix | enter | 0.0 | 0.0 |
| nix | space | 0.1 | 0.0 |
| contact | point | 0.1 | 0.2 |
| contact | enter | 0.0 | 0.0 |
| contact | space | 0.0 | 0.1 |
| info | point | 0.2 | 0.2 |
| info | enter | 0.1 | 0.1 |
| info | space | 0.1 | 0.1 |
| projects | point | 0.2 | 0.3 |
| projects | enter | 0.0 | 0.0 |
| projects | space | 0.0 | 0.0 |
| edu | point | 0.2 | 0.1 |
| edu | enter | 0.1 | 0.0 |
| edu | space | 0.1 | 0.0 |

Raw: `data/latency_main.json`, `data/latency_pr.json`

### Mobile m390 (`reduce`, point only)

| card | main | PR |
|---|---|---|
| nix | 5.4 | 6.6 |
| contact | 5.1 | 5.3 |
| info | 6.7 | 6.0 |
| projects | 5.2 | 5.3 |
| edu | 5.6 | 5.2 |

Raw: `data/m390_latency_main.json`, `data/m390_latency_pr.json`

---

## 6. Hover scale after close (nix, desktop)

Script: `scripts/hover_reset.cjs` — hover card → open → Esc → move mouse away → read transform.

| | scale after hover | scale after close | reset to 1? |
|---|---|---|---|
| main | 1.02 | 1.02 | no (still hovered scale in this run) |
| PR | 1.02 | **none (1.0)** | **yes** |

Raw: `data/hover_reset_main.json`, `data/hover_reset_pr.json`

---

## Scripts

| Script | Purpose |
|---|---|
| `scripts/common.cjs` | Playwright context, stubs, helpers |
| `scripts/taprepro_orig.cjs` | Vera contact fixed-point ghost-click repro |
| `scripts/tap_all_cards.cjs` | All-card blank tap matrix |
| `scripts/noentry.cjs` | Post–Enter Site contact tap |
| `scripts/verify.cjs` | Combined suite |
| `scripts/latency.cjs` | Desktop open latency |
| `scripts/m390_latency.cjs` | Mobile open latency |
| `scripts/hover_reset.cjs` | Post-close scale check |

## Build

```bash
export PATH="$PWD/node_modules/.cache/bb:$PATH"
CF_PAGES=1 node scripts/prebuild.mjs && bun run build
```

Passed locally on PR head.
