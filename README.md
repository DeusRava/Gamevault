# GameVault 🎮

Android app to compare game prices across countries and stores — powered by the [RAWG API](https://rawg.io/apidocs), [Steam Store API](https://store.steampowered.com/api/), and [IsThereAnyDeal](https://isthereanydeal.com/).

## Features
- 🔍 Browse top-rated games and new releases
- 🌍 **Cross-country price comparison** — Steam prices across 15 regions (US, UK, DE, TR, AR, RU, BR, IN, AU, JP, KZ, UA, PL, CN, MX)
- 🏆 **Best deal highlighted** — instantly see the cheapest region
- 🎮 **Multi-store** — Steam + Epic Games Store prices side by side
- 🔎 Search with real-time debounced search + platform filter (All / PC / Mobile)
- ⭐ **Favorites** — save games locally, persisted across sessions
- 🎛️ Filter price table by store (All / Steam / EGS)

## APIs Used
| Store | API | Notes |
|-------|-----|-------|
| Steam | `store.steampowered.com/api/appdetails` | Official — per-country `cc` param |
| Epic Games | IsThereAnyDeal v1 | No official EGS public API |
| Game metadata | RAWG.io | Game details, platforms, genres |

## Build via GitHub (no Android Studio needed)

### 1. Get a free RAWG API key
1. Go to [rawg.io/apidocs](https://rawg.io/apidocs) and sign up
2. Copy your API key

### 2. Push to GitHub
```bash
git init && git add . && git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/GameVault.git
git push -u origin main
```

### 3. Add secret
**Settings → Secrets → Actions → New repository secret**
- Name: `RAWG_API_KEY`
- Value: your RAWG key

### 4. Download APK
**Actions tab → latest run → Artifacts → GameVault-debug**

> Enable "Install from unknown sources" on Android first.

## Project Structure
```
app/src/main/java/com/gamevault/app/
├── data/
│   ├── api/           RawgApiService, SteamApiService, ItadApiService
│   ├── db/            Room database + FavoriteDao
│   ├── model/         Game, Price, FavoriteGame data classes
│   └── repository/    GameRepository, PriceRepository, FavoriteRepository
├── di/                Hilt DI (NetworkModule — 3 Retrofit instances)
├── ui/
│   ├── home/          Browse popular & new releases
│   ├── search/        Real-time search with platform filter
│   ├── favorites/     Saved games (Room-backed)
│   └── detail/        Game info + price comparison table
├── GameVaultApp.kt
└── MainActivity.kt
```
