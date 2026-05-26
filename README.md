# GameVault 🎮

Android app to browse and search PC & mobile games, powered by the [RAWG API](https://rawg.io/apidocs).

## Features
- Browse top-rated games and new releases
- Filter by **All / PC+Console / Mobile**
- Search games with real-time debounced search
- Game detail view with rating, platforms, genres

## Build via GitHub (no Android Studio needed)

### 1. Get a free RAWG API key
1. Go to [rawg.io/apidocs](https://rawg.io/apidocs)
2. Sign up for a free account
3. Copy your API key

### 2. Push to GitHub
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/GameVault.git
git push -u origin main
```

### 3. Add API key as a GitHub Secret
1. Go to your repo → **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Name: `RAWG_API_KEY`
4. Value: your RAWG API key

### 4. Trigger the build
- Push any commit, **or**
- Go to **Actions** tab → **Build APK** → **Run workflow**

### 5. Download APK
1. Go to **Actions** tab
2. Click the latest successful run
3. Scroll to **Artifacts** at the bottom
4. Download **GameVault-debug** → install the `.apk` on your phone

> Enable "Install from unknown sources" on your Android device first.

## Project Structure
```
app/src/main/java/com/gamevault/app/
├── data/
│   ├── api/         RawgApiService (Retrofit)
│   ├── model/       Game data classes
│   └── repository/  GameRepository
├── di/              Hilt dependency injection
├── ui/
│   ├── home/        HomeFragment + ViewModel + Adapter
│   ├── search/      SearchFragment + ViewModel
│   └── detail/      DetailFragment + ViewModel
├── GameVaultApp.kt
└── MainActivity.kt
```
