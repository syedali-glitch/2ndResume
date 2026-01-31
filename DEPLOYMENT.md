# Resume Architect - Build and Deploy Guide

## 📦 GitHub Repository
**URL**: https://github.com/syedali-glitch/2ndResume  
**Branch**: master  
**Status**: ✅ Live (58 files, 9,212 lines)

---

## 🚀 Quick Start (Android Studio)

### 1. Clone the Repository
```bash
git clone https://github.com/syedali-glitch/2ndResume.git
cd 2ndResume
```

### 2. Open in Android Studio
1. Open Android Studio
2. File → Open
3. Select the cloned folder
4. Wait for Gradle sync
5. Click Build → Make Project

### 3. Run on Device/Emulator
1. Connect your Android device or start emulator
2. Click Run ▶️ button
3. Select your device
4. App will install and launch

---

## 📱 Build APK (Without Android Studio)

### Prerequisites
- **Java JDK 17** or newer
- **ANDROID_HOME** environment variable set to Android SDK location

### Steps

#### 1. Create local.properties
Create a file named `local.properties` in the root directory:
```properties
sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
```
Replace with your actual SDK path.

#### 2. Build Debug APK
```bash
gradlew assembleDebug
```
**Output**: `app/build/outputs/apk/debug/app-debug.apk`

#### 3. Build Release APK
```bash
gradlew assembleRelease
```
**Output**: `app/build/outputs/apk/release/app-release.apk`

---

## 🔧 Installing Gradle Wrapper (If Missing)

If `gradlew.bat` doesn't work or is missing:

### Option 1: Using Gradle (if installed globally)
```bash
gradle wrapper --gradle-version 8.2
```

### Option 2: Download Gradle manually
1. Download Gradle 8.2 from https://gradle.org/releases/
2. Extract to C:\Gradle
3. Add to PATH: `C:\Gradle\gradle-8.2\bin`
4. Run: `gradle wrapper`

### Option 3: Use Android Studio
1. Open project in Android Studio
2. Studio will automatically create wrapper files
3. Sync Gradle

---

## 📦 Build Output Locations

After successful build:

### Debug APK
```
app/
└── build/
    └── outputs/
        └── apk/
            └── debug/
                └── app-debug.apk         (~15-20 MB)
```

### Release APK
```
app/
└── build/
    └── outputs/
        └── apk/
            └── release/
                └── app-release.apk      (~8-12 MB, optimized)
```

---

## 📱 Install APK on Device

### Via ADB
```bash
# Install debug
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release
adb install app/build/outputs/apk/release/app-release.apk

# Reinstall (if already installed)
adb install -r app-debug.apk
```

### Via File Transfer
1. Copy APK to device
2. Enable "Install from Unknown Sources" in device settings
3. Tap APK file to install

---

## 🔑 Signing APK for Production

### Create Keystore
```bash
keytool -genkey -v -keystore resume-architect.keystore ^
  -alias resumearchitect ^
  -keyalg RSA ^
  -keysize 2048 ^
  -validity 10000
```

### Configure Signing in app/build.gradle.kts
Add before `buildTypes`:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file("../resume-architect.keystore")
        storePassword = "your_store_password"
        keyAlias = "resumearchitect"
        keyPassword = "your_key_password"
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        // ... existing config
    }
}
```

### Better: Use keystore.properties
Create `keystore.properties`:
```properties
storeFile=resume-architect.keystore
storePassword=your_store_password
keyAlias=resumearchitect
keyPassword=your_key_password
```

Then in build.gradle.kts:
```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}
```

---

## 🧹 Clean Build

If you encounter build errors:
```bash
# Clean all build artifacts
gradlew clean

# Rebuild everything
gradlew clean assembleDebug

# Refresh dependencies
gradlew clean build --refresh-dependencies
```

---

## 📊 Current Build Configuration

```
Application ID: com.resumearchitect
Version Code: 1
Version Name: 1.0.0

Min SDK: 26 (Android 8.0)
Target SDK: 34 (Android 14)
Compile SDK: 34

Optimizations (Release):
- ProGuard: Enabled
- R8: Enabled
- Resource Shrinking: Enabled
```

---

## 🐛 Troubleshooting

### "SDK location not found"
- Create `local.properties` with SDK path
- Set ANDROID_HOME environment variable

### "gradlew: command not found"
- Run `gradle wrapper` to generate files
- Or use Android Studio

### Build fails with "OutOfMemoryError"
Add to `gradle.properties`:
```
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
```

### Dependencies not downloading
```bash
gradlew clean build --refresh-dependencies
```

### Java version mismatch
- Ensure JDK 17 is installed
- Check with: `java -version`
- Set JAVA_HOME if needed

---

## 🎯 Recommended Workflow

### For Development
```bash
# Clean build
gradlew clean

# Build debug APK
gradlew assembleDebug

# Install on connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### For Testing
```bash
# Build release (test production optimizations)
gradlew assembleRelease

# Install
adb install app/build/outputs/apk/release/app-release.apk
```

### For Production
```bash
# Create signed bundle for Play Store
gradlew bundleRelease

# Or signed APK
gradlew assembleRelease

# Files at:
# - app/build/outputs/bundle/release/app-release.aab
# - app/build/outputs/apk/release/app-release.apk
```

---

## 📤 GitHub Actions CI/CD (Future)

Add `.github/workflows/build.yml` for automated builds:
```yaml
name: Android CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: ./gradlew assembleRelease
```

---

## 📝 Quick Reference Commands

```bash
# Build
gradlew assembleDebug          # Debug APK
gradlew assembleRelease        # Release APK
gradlew bundleRelease          # AAB for Play Store

# Clean
gradlew clean                  # Remove build artifacts

# Install
gradlew installDebug           # Install debug on device
gradlew installRelease         # Install release on device

# Test
gradlew test                   # Run unit tests
gradlew connectedAndroidTest   # Run instrumented tests

# Analyze
gradlew lint                   # Run lint checks
gradlew dependencyUpdates      # Check for updates
```

---

## 🎉 Status

✅ **Repository**: Live on GitHub  
✅ **Build Config**: Optimized and ready  
✅ **Dependencies**: All configured  
✅ **ProGuard**: Rules in place  
✅ **Gradle Wrapper**: Created  

**Ready to build!** 🚀

---

**Repository**: https://github.com/syedali-glitch/2ndResume  
**Last Updated**: January 31, 2026  
**Version**: 1.0.0
