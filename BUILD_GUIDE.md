# Resume Architect APK Build Guide

## 🚀 Quick Build Instructions

### Option 1: Using Gradle (Command Line)
```bash
# Navigate to project directory
cd "e:\Resume Architect"

# Build debug APK
gradlew assembleDebug

# Build release APK (production)
gradlew assembleRelease

# Build both debug and release
gradlew build
```

### Option 2: Using Android Studio
1. Open Android Studio
2. File → Open → Select "e:\Resume Architect"
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. Or: Build → Generate Signed Bundle / APK → APK → Create new keystore

### Option 3: Using Gradle Wrapper (if available)
```bash
# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build APK
./gradlew assembleRelease
```

---

## 📦 Build Outputs

### Debug APK
**Location**: `app/build/outputs/apk/debug/app-debug.apk`
- Debuggable build
- Application ID: `com.resumearchitect.debug`
- Version: 1.0.0-debug
- **Use for**: Testing and development

### Release APK
**Location**: `app/build/outputs/apk/release/app-release.apk`
- Optimized build
- ProGuard enabled (code minification)
- Resource shrinking enabled
- Application ID: `com.resumearchitect`
- Version: 1.0.0
- **Use for**: Production deployment

### Android App Bundle (AAB)
```bash
gradlew bundleRelease
```
**Location**: `app/build/outputs/bundle/release/app-release.aab`
- **Recommended for**: Google Play Store submission
- Smaller download sizes for users
- Dynamic delivery

---

## 🔧 Build Configuration

### Current Setup
- **Compile SDK**: 34 (Android 14)
- **Target SDK**: 34
- **Min SDK**: 26 (Android 8.0)
- **Version Code**: 1
- **Version Name**: 1.0.0
- **JVM Target**: 17

### Optimizations Enabled (Release)
- ✅ **Code minification** (ProGuard)
- ✅ **Resource shrinking**
- ✅ **Optimized ProGuard rules**
- ✅ **R8 optimization**

---

## 🔑 Signing the APK (for Production)

### Create Keystore
```bash
keytool -genkey -v -keystore resume-architect-release.keystore ^
  -alias resume-architect ^
  -keyalg RSA ^
  -keysize 2048 ^
  -validity 10000
```

### Sign APK
```bash
jarsigner -verbose -sigalg SHA256withRSA ^
  -digestalg SHA-256 ^
  -keystore resume-architect-release.keystore ^
  app-release.apk resume-architect
```

### Zipalign (Optimize)
```bash
zipalign -v 4 app-release.apk app-release-aligned.apk
```

---

## 📊 APK Size Expectations

### Debug APK
- **Unoptimized**: ~15-20 MB
- Includes debugging symbols

### Release APK
- **Optimized**: ~8-12 MB
- ProGuard minified
- Resources shrunk

### AAB (Bundle)
- **Size**: ~6-10 MB
- On-device APK: ~5-8 MB (varies)

---

## 🧪 Testing the APK

### Install Debug APK
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Install Release APK
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### Reinstall (if already installed)
```bash
adb install -r app-debug.apk
```

### Uninstall
```bash
adb uninstall com.resumearchitect
# or
adb uninstall com.resumearchitect.debug
```

---

## ⚠️ Prerequisites

Ensure you have:
- ✅ Java JDK 17 or newer
- ✅ Android SDK installed
- ✅ Gradle (or Android Studio with embedded Gradle)
- ✅ ANDROID_HOME environment variable set

### Check Prerequisites
```bash
# Check Java version
java -version

# Check if gradlew is executable
gradlew --version

# Check Android SDK
echo %ANDROID_HOME%
```

---

## 🐛 Troubleshooting

### "gradlew: command not found"
The Gradle wrapper files might not be in the repository. Use Android Studio to sync Gradle or download wrapper files:
```bash
gradle wrapper
```

### "SDK location not found"
Create `local.properties` in project root:
```
sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
```

### Build fails due to missing dependencies
```bash
gradlew clean
gradlew build --refresh-dependencies
```

### OutOfMemoryError during build
Add to `gradle.properties`:
```
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

---

## 📝 Build Variants

The project has 2 build types:

### Debug
- Debuggable
- No obfuscation
- Fast build
- Emulator & device testing

### Release
- Not debuggable
- ProGuard enabled
- Code optimized
- Production deployment

---

## 🚀 CI/CD (Future)

For automated builds, consider:
- **GitHub Actions** - `.github/workflows/build.yml`
- **Bitrise** - Mobile-focused CI/CD
- **CircleCI** - Fast Android builds
- **Jenkins** - Self-hosted option

---

## 📱 Distribution

### Internal Testing
1. Build debug APK
2. Share via email/drive
3. Install on test devices

### Google Play (Beta)
1. Build signed AAB
2. Upload to Play Console
3. Create internal/closed test track

### Production
1. Build signed AAB with production keystore
2. Upload to Play Console production track
3. Submit for review

---

## 🎯 Quick Reference

```bash
# Clean build
gradlew clean

# Build debug APK
gradlew assembleDebug

# Build release APK
gradlew assembleRelease

# Build AAB for Play Store
gradlew bundleRelease

# Run all tests
gradlew test

# Install on connected device
gradlew installDebug

# Uninstall from device
gradlew uninstallDebug
```

---

## 📂 Build Artifacts Location

After successful build:
```
app/
├── build/
│   ├── outputs/
│   │   ├── apk/
│   │   │   ├── debug/
│   │   │   │   └── app-debug.apk
│   │   │   └── release/
│   │   │       └── app-release.apk (unsigned)
│   │   └── bundle/
│   │       └── release/
│   │           └── app-release.aab
│   └── intermediates/
│       └── ... (build artifacts)
```

---

**Last Updated**: January 31, 2026  
**Version**: 1.0.0  
**Build System**: Gradle 8.2.2 + Android Gradle Plugin 8.2.2
