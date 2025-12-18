# SimpleQualityOfLife - Documentation

**SimpleQualityOfLife** is a modular Fabric mod for Minecraft 1.21+ that focuses on utility tweaks, farming improvements, and server administration tools.

## 📥 Installation & Dependencies

To use SimpleQualityOfLife, install the following:
1.  **Fabric Loader**
2.  **Fabric API**
3.  **Cloth Config API** (Required for configuration)
4.  **Mod Menu** (Recommended for in-game settings)

## 📖 Features & Configuration

All features can be configured via `cloth-config` (in-game) or the `config/simplequalityoflife.json` file.

### 🎮 Gameplay Tweaks
| Feature | Description | Config Key |
| :--- | :--- | :--- |
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

### 🏷️ Nametag Features
Renaming mobs with specific suffixes triggers special effects.

* **Mute Mobs:** Suffixes `_mute`, `_shhh` (Configurable list: `qOL.nametagMuteSuffixes`).
* **Forever Baby:** Suffixes `_baby`, `_small` (Configurable list: `qOL.nametagBabySuffixes`).

### ⚙️ Server / Vaults
| Feature | Description | Config Key |
| :--- | :--- | :--- |
| **Vault Cooldown** | Sets the cooldown (in Minecraft days) for looting Vaults. | `qOL.vaultCooldownDays` |

## 💻 Commands
Admin commands to change settings without editing files manually. Requires Level 4 OP.

### General Configuration
* `/simplequalityoflife tweaks autowalk <true|false>`
* `/simplequalityoflife tweaks farmlandProtect <true|false>`
* `/simplequalityoflife tweaks hoeHarvest <true|false>`
* `/simplequalityoflife tweaks ladderSpeed <value>`
* `/simplequalityoflife tweaks sharpnessCut <true|false>`

### Nametag Management
* `/simplequalityoflife tweaks muteSuffixes list`
* `/simplequalityoflife tweaks muteSuffixes add <suffix>`
* `/simplequalityoflife tweaks muteSuffixes remove <suffix>`
* *(Same commands apply to `babySuffixes`)*

### Vault Management
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