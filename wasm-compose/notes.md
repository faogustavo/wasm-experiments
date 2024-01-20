-> Bundle size is enormous
    -> 8mb for skiko.wasm
    -> 4.8mb for composeApp.wasm

```
.rw-r--r-- 8.0M faogustavo staff 20 Jan 16:57  58c97d5c0661070cab69.wasm
.rw-r--r-- 327k faogustavo staff 20 Jan 16:57  366.js
.rw-r--r-- 738k faogustavo staff 20 Jan 16:57  366.js.map
.rw-r--r--  15k faogustavo staff 20 Jan 16:57  composeApp.js
.rw-r--r--  46k faogustavo staff 20 Jan 16:57  composeApp.js.map
.rw-r--r-- 4.8M faogustavo staff 20 Jan 16:57  composeApp.wasm
.rw-rw-r--  304 faogustavo staff 20 Jan 16:57  index.html
drwxr-xr-x    - faogustavo staff 20 Jan 16:57  META-INF
.rw-r--r-- 416k faogustavo staff 20 Jan 16:57  skiko.js
.rw-r--r-- 464k faogustavo staff 20 Jan 16:57  skiko.mjs
.rwxr-xr-x 8.0M faogustavo staff 20 Jan 16:57  skiko.wasm
```

-> The debug step is a bit different:

```kotlin
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        addAll(rootProject.subprojects.map { File(it.rootDir, it.name).toString() }) // This
                    }
                }
            }
        }
        binaries.executable()
    }
```

-> With `applyBinaryen()`, we are not able to debug the code on browser
-> Compose is not debuggable at all
-> You can only debug Kotlin code that is not part of a "@Composable" function
    -> ViewModels, for example, work fine
    -> Methods/Classes called from composable work fine
