# SimpleQualityOfLife - Documentation

**SimpleQualityOfLife** is a modular Fabric mod for Minecraft 1.21+ that focuses on utility tweaks, movement improvements, farming enhancements, and server administration tools.

## 📥 Installation & Dependencies

To use SimpleQualityOfLife, install the following:
1.  **Fabric Loader**
2.  **Fabric API**
3.  **Cloth Config API** (Required for configuration)
4.  **Mod Menu** (Recommended for in-game settings)

## 📖 Features & Configuration

All features can be configured via `cloth-config` (in-game) or the `config/simplequalityoflife.json` file.

### 🎮 Gameplay & Movement Tweaks
| Feature | Description | Config Key |
| :--- | :--- | :--- |
| **Manual Crawling** | Press **C** (default) or type `/crawl` to drop into a crawling pose. Jump to stand up. | N/A (Keybind) |
| **Frost Walker Fix** | Frost Walker boots prevent sinking into Powder Snow (no leather boots needed). | `qOL.frostWalkerWalkOnPowderSnow` |
| **Auto-Walk** | Toggles automatic forward movement via keybind. | `qOL.enableAutowalk` |
| **Ladder Climb Speed** | Multiplier for climbing up ladders (Vanilla ~0.2). | `qOL.ladderClimbingSpeed` |
| **Ladder Slide** | Allows sliding down ladders faster. | `qOL.enableFastLadderSlide`, `qOL.ladderSlideSpeed` |
| **Slide Activation** | How to trigger the slide (Camera angle or Always). | `qOL.ladderSlideActivation` |

### 🌾 Farming & Interaction
| Feature | Description | Config Key |
| :--- | :--- | :--- |
| **Farmland Protection** | *Feather Falling* boots prevent farmland trampling. | `qOL.preventFarmlandTrampleWithFeatherFalling` |
| **Hoe Harvesting** | Right-click crops with a hoe to harvest and replant. | `qOL.enableHoeHarvest` |
| **Sharpness Cut** | Sharpness weapons break grass/flowers on swing. | `qOL.sharpnessCutsGrass` |
| **Full Durability Bonus** | Tools with full durability gain extra efficiency or damage. | `qOL.fullDurabilityBonus.*` |

### 🏷️ Nametag Features
Renaming mobs with specific suffixes triggers special effects.

* **Mute Mobs:** Suffixes `_mute`, `_shhh` (Configurable list: `qOL.nametagMuteSuffixes`).
* **Forever Baby:** Suffixes `_baby`, `_small` (Configurable list: `qOL.nametagBabySuffixes`).

### ⚙️ Server / Vaults
| Feature | Description | Config Key |
| :--- | :--- | :--- |
| **Vault Cooldown** | Sets the cooldown (in Minecraft days) for looting Vaults. | `qOL.vaultCooldownDays` |

## 💻 Commands

### General Commands (All Players)
* `/crawl`: Toggles the crawling state. Can also be triggered via the configured keybind (Default: `C`).

### Admin Configuration (Level 4 OP)
Admin commands to change settings without editing files manually.

#### General Configuration
* `/simplequalityoflife tweaks frostWalkerSnow <true|false>`
* `/simplequalityoflife tweaks autowalk <true|false>`
* `/simplequalityoflife tweaks farmlandProtect <true|false>`
* `/simplequalityoflife tweaks hoeHarvest <true|false>`
* `/simplequalityoflife tweaks ladderSpeed <value>`
* `/simplequalityoflife tweaks sharpnessCut <true|false>`

#### Nametag Management
* `/simplequalityoflife tweaks muteSuffixes list`
* `/simplequalityoflife tweaks muteSuffixes add <suffix>`
* `/simplequalityoflife tweaks muteSuffixes remove <suffix>`
* *(Same commands apply to `babySuffixes`)*

#### Vault Management
* `/simplequalityoflife vaults cooldown <days>`: Sets the global vault cooldown.

## 🏗️ Building from Source

1.  Clone the repository.
2.  Navigate to the project directory.
3.  Run the build command:
    * Windows: `gradlew build`
    * Linux/macOS: `./gradlew build`
4.  The compiled `.jar` file will be in `build/libs/`.

## ⚖️ License
This project is licensed under the **CC0 1.0 Universal** license. You are free to use, modify, and distribute this software without restriction.


![Growup Prevention](https://cdn.modrinth.com/data/cached_images/112cea39f1f83db1ebff79b03649f9ad3dff1ca5.png)

![Mute Mobs](https://cdn.modrinth.com/data/cached_images/2ecdf358a8b19478a3573a7c2d74ba69d3858d81.png)

![Feather Falling Prevents Destruction](https://cdn.modrinth.com/data/cached_images/427d03fdfdd11cc661d9bd5aa003fff7f2be91eb.png)

![Ladder Speed](https://cdn.modrinth.com/data/cached_images/a80130592d19294197920bec466956ccc92f0aad.png)

![Replant With Hoe](https://cdn.modrinth.com/data/cached_images/b557b46032e298fbb606249bb6df8994b1cac0d4.png)

![Sharpness Cutts Trough Grass](https://cdn.modrinth.com/data/cached_images/a01611f6d33c106a9c05864e9dd0bc28844e448a.png)