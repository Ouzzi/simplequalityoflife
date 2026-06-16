# SimpleQualityOfLife — Wiki & Documentation

**SimpleQualityOfLife** is a modular Fabric mod for Minecraft **1.21.11** that focuses on utility tweaks, movement improvements, farming enhancements, and server administration tools. Almost every feature can be toggled individually, so you can run exactly the subset you want.

> This document is the full reference (features, config keys, and commands). For the short store description, see `readme_modrinth.md`.

## 📥 Installation & Dependencies

| Dependency | Required | Purpose |
| :--- | :--- | :--- |
| **Fabric Loader** (`>=0.18.3`) | ✅ | Mod loader |
| **Fabric API** | ✅ | Core hooks (events, keybinds, commands) |
| **Cloth Config API** | ✅ | Configuration backend |
| **Mod Menu** | ⭐ Recommended | In-game config screen |

`environment: "*"` — works on the client, on dedicated servers, and in singleplayer. Most server-side features still apply when only the server has the mod.

## ⚙️ How to configure

All options live in `config/simplequalityoflife.json` and can be edited:
- **In-game** via Mod Menu → SimpleQualityOfLife (requires Cloth Config + Mod Menu), or
- **On a server** via the `/simplequalityoflife` admin commands (see below), or
- by editing the JSON file directly.

The global option `frostWalkerWalkOnPowderSnow` sits at the top level; everything else lives under the `qOL` object (config keys below are written as `qOL.<key>`).

---

## ✨ Features

### 🎮 Movement
| Feature | Description | Config Key (default) |
| :--- | :--- | :--- |
| **Manual Crawling** | Press **P** (default keybind) or run `/crawl` to drop into a crawling pose. Press **Jump** to stand back up. | N/A (keybind) |
| **Frost Walker on Powder Snow** | Boots with **Frost Walker** stop you from sinking into Powder Snow — no leather boots required. | `frostWalkerWalkOnPowderSnow` (true) |
| **Auto-Walk** | Toggle automatic forward movement with a keybind (**R** by default). Must be enabled in config first. | `qOL.enableAutowalk` (false) |
| **Faster Ladder Climbing** | Adjust the upward climb speed on ladders (vanilla is ~0.2). | `qOL.ladderClimbingSpeed` (0.4) |
| **Ladder Slide** | Slide down ladders quickly instead of crawling down. Speed and trigger are configurable. | `qOL.enableFastLadderSlide` (true), `qOL.ladderSlideSpeed` (0.8) |
| **Slide Activation Mode** | `CAMERA` = slide while sneaking and looking down; `ALWAYS` = slide while looking down unless sneaking. | `qOL.ladderSlideActivation` (CAMERA) |

### 🌾 Farming & World Interaction
| Feature | Description | Config Key (default) |
| :--- | :--- | :--- |
| **Farmland Protection** | Wearing **Feather Falling** boots prevents you from trampling farmland, even when jumping onto it. | `qOL.preventFarmlandTrampleWithFeatherFalling` (true) |
| **Hoe Harvest & Replant** | Right-click a fully grown crop with a hoe to harvest **and** replant it in one action. Costs 1 seed (taken from the drops) and 1 durability. Works on wheat, carrots, potatoes, beetroot, nether wart and cocoa. | `qOL.enableHoeHarvest` (true) |
| **Fill Furnace with Lava** | Right-click a furnace, blast furnace or smoker with a **lava bucket** to drop it straight into the fuel slot and get an empty bucket back — no need to open the GUI. Only triggers when the fuel slot is empty. | `qOL.enableFurnaceLavaFill` (true) |
| **Sharpness Cuts Grass** | Weapons with **Sharpness III+** clear grass, flowers and similar vegetation around a target when you attack. (Swords/axes only; hold sneak to disable; also removes the block outline so it doesn't block your hits.) | `qOL.sharpnessCutsGrass` (true) |
| **Full Durability Bonus** | Tools/weapons at or above a durability threshold gain a mining-speed and attack-damage multiplier — a reward for keeping your gear repaired. | `qOL.enableFullDurabilityBonus` (true), `qOL.fullDurabilityThreshold` (0.80), `qOL.fullDurabilityBonusMultiplier` (1.5) |

### 🏷️ Mobs & Nametags
| Feature | Description | Config Key (default) |
| :--- | :--- | :--- |
| **Mute by Entity Type** | Silence whole entity types by id (e.g. `minecraft:cow`). | `qOL.mutedEntities` (empty list) |
| **Mute by Nametag Suffix** | Rename a mob so its name ends with one of these suffixes to silence it. | `qOL.nametagMuteSuffixes` (`_mute`, `_shhh`) |
| **Forever Baby** | Rename a baby animal so its name ends with one of these suffixes to stop it from growing up. | `qOL.nametagBabySuffixes` (`_baby`, `_small`) |
| **Piglins Ignore Gold Trims** | Piglins won't aggro at players wearing armor with **gold trim**. | `qOL.piglinsIgnoreGoldTrims` (true) |
| **Piglins Ignore Gold Tools** | Piglins won't aggro at players holding a golden tool/weapon in either hand. | `qOL.piglinsIgnoreGoldTools` (true) |

### 🌧️ Weather
| Feature | Description | Config Key (default) |
| :--- | :--- | :--- |
| **Disable Weather (Server)** | Stops rain/thunder from starting and clears it when active. Also skips the vanilla weather tick to save CPU. | `qOL.disableWeather` (false) |
| **Rain Disturbance (Client)** | Scales rain particles and rain sound down. `100` = vanilla, `0` = silent/no particles, values in between thin it out. | `qOL.clientRainParticleDensity` (20) |

### 🏛️ Vaults
| Feature | Description | Config Key (default) |
| :--- | :--- | :--- |
| **Vault Cooldown** | Sets how many Minecraft days must pass before a player can loot the same vault again. The per-player loot times are saved with the vault block. | `qOL.vaultCooldownDays` (100) |

---

## 💻 Commands

### Everyone
* `/crawl` — toggle the crawling state (same as the keybind, default **P**).

### Admin (permission level 4 / server owner)
All configuration commands are grouped under `/simplequalityoflife`. Changes are saved to the config file immediately.

**Vaults**
```
/simplequalityoflife vaults cooldown <days>
```

**Feature toggles** (`<true|false>`)
```
/simplequalityoflife tweaks fullDurabilityBonus <true|false>
/simplequalityoflife tweaks autowalk <true|false>
/simplequalityoflife tweaks farmlandProtect <true|false>
/simplequalityoflife tweaks frostWalkerSnow <true|false>
/simplequalityoflife tweaks hoeHarvest <true|false>
/simplequalityoflife tweaks furnaceLava <true|false>
/simplequalityoflife tweaks sharpnessCut <true|false>
/simplequalityoflife tweaks ladderSpeed <value>
```

**Nametag suffix lists** (same sub-commands for both `muteSuffixes` and `babySuffixes`)
```
/simplequalityoflife tweaks muteSuffixes list
/simplequalityoflife tweaks muteSuffixes add <suffix>
/simplequalityoflife tweaks muteSuffixes remove <suffix>
/simplequalityoflife tweaks muteSuffixes clear

/simplequalityoflife tweaks babySuffixes list
/simplequalityoflife tweaks babySuffixes add <suffix>
/simplequalityoflife tweaks babySuffixes remove <suffix>
/simplequalityoflife tweaks babySuffixes clear
```

> Not every config option has a command — options like ladder slide, durability threshold/multiplier, piglin toggles, weather and rain density are set through Mod Menu or the config file.

## 🏗️ Building from Source

1. Clone the repository.
2. From the project directory run the build:
   * Windows: `gradlew build`
   * Linux/macOS: `./gradlew build`
3. The compiled `.jar` is written to `build/libs/`.

## ⚖️ License

Licensed under **CC0 1.0 Universal**. You are free to use, modify, and distribute this software without restriction.

---

![Growup Prevention](https://cdn.modrinth.com/data/cached_images/112cea39f1f83db1ebff79b03649f9ad3dff1ca5.png)

![Mute Mobs](https://cdn.modrinth.com/data/cached_images/2ecdf358a8b19478a3573a7c2d74ba69d3858d81.png)

![Feather Falling Prevents Destruction](https://cdn.modrinth.com/data/cached_images/427d03fdfdd11cc661d9bd5aa003fff7f2be91eb.png)

![Ladder Speed](https://cdn.modrinth.com/data/cached_images/a80130592d19294197920bec466956ccc92f0aad.png)

![Replant With Hoe](https://cdn.modrinth.com/data/cached_images/b557b46032e298fbb606249bb6df8994b1cac0d4.png)

![Sharpness Cuts Through Grass](https://cdn.modrinth.com/data/cached_images/a01611f6d33c106a9c05864e9dd0bc28844e448a.png)
