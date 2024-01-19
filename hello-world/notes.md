# Wasm initial tests

-> To run on browser, we use the wasmjs target:

```kotlin
wasmJs {
    binaries.executable()
    browser()
}
```

-> To start the server you can run the `./gradlew wasmJsBrowserProductionRun --continuous` command

-> The output is a .js file that instantiate the wasm module.

-> This JS file needs to be imported in your HTML file to load the wasm module. If the module has a main function, it 
will be called on startup.

-> Other functions can be accessed from the module export that is added to the window.

```js
// const wasmInstance = (await this.<module-name>).default
const wasmInstance = (await window.hello_world).default
```

-> To export a function, you can use either `@WasmExport` or `@JsExport`.

```kotlin
@WasmExport
fun testMyFunWithWasmExport() {
    println("My Fun with Wasm Export")
}

@JsExport
fun hello(name: String) {
    println("Hello $name!")
}
```
-> Non-exported functions will not be accessible from JS.

-> The main function is automatically exported, so it can be called from JS. It is renamed to `_initialize` to warn people
not to call it (underscore is a way of saying "private" in JS).

-> Wasm only support exporting functions and primitive types. Classes, enums, object, interfaces, etc will not work!

```kotlin
// This will throw an error as it is not supported by wasm
@JsExport // or @WasmExport
data class KtPerson(val name: String, val age: Int)
// Error: Type KtPerson cannot be used in external function return. Only external, primitive, string and function types are supported in Kotlin/Wasm JS interop.
```

-> Returns are allowed and follow the same rule as above

-> All functions are top level and name conflicts will crash the build.

```kotlin
package dev.valvassori.helloWorld

@WasmExport
fun testMyFunWithWasmExport() {
    println("My Fun with Wasm Export")
}
```

```kotlin
package dev.valvassori.helloWorld.submodule

@JsExport
fun testMyFunWithWasmExport() {
    println("My Fun with Wasm Export")
}
```

> Uncaught (in promise) CompileError: wasm validation error: at offset 5240: duplicate export

-> `@JsName` can be used with `@JsExport` to rename public functions.
-> `@JsName` **cannot** be used with `@WasmExport` to rename public functions.
-> `@JsName` **cannot** contain dots, so we can "namespace" our functions. But you can use underscores.

```kotlin
// Works fine
@JsExport
@JsName("renamedFun")
fun jsExportWithJsName() {
    println("Renamed fun!")
}

@JsExport
@JsName("my_package_renamedFunWithWasmExport")
fun wasmExportWithJsNameNamespaced() {
    println("Renamed fun!")
}

// Does not work
@WasmExport
@JsName("renamedFunWithWasmExport")
fun wasmExportWithJsName() {
    println("Renamed fun!")
}
```

-> You can use classes defined on JS on Kotlin code, but not Kotlin classes on JS.

```js
class Person {
  constructor(name, age) {
    this.name = name;
		this.age = age
  }
}
```

```kotlin
external class Person(name: String, age: Int) {
    val name: String
    val age: Int
}

@JsExport
fun createPerson() = Person("Gustavo", 28)
```

```js
wasmInstance.createPerson()
// Object { name: "Gustavo", age: 28 }
```

-> JS Apis can be used on Wasm side:

```kotlin
@JsExport
fun setMessage(message: String) {
    document.querySelector("#message")?.innerHTML = message
}
```

-> Coroutines work just fine. You can return promises but you can't export suspend functions

```kotlin
private val scope = MainScope()

@JsExport
fun runFetch(): Promise<*> = scope.promise {
    window.fetch("https://get.geojs.io/v1/ip/country.json")
        .await<Response>()
        .json()
        .await<GeoIp>()
}
```

```js
const wasmInstance = (await window.hello_world).default;
await wasmInstance.runFetch();
```

-> You can define "structs" from JS objects using the "external class" syntax.

```kotlin
private val scope = MainScope()

external class GeoIp : JsAny {
    val country: String
}

@JsExport
fun runFetch() {
    scope.launch {
        val result = window.fetch("https://get.geojs.io/v1/ip/country.json")
            .await<Response>()
            .json()
            .await<GeoIp>()

        println(result.country)
    }
}
```

> Note: If this class doesn't exist on JS side, don't create constructor, functions or extensions for it. It will during
> runtime.

-> To debug wasm, you need to include your source codes in the static dev server:

```kotlin
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(project.rootDir.path)
                    }
                }
            }
        }
    }
```

-> WasmAnalizer (https://wa2.dev/) can be used to inspect the wasm module.
-> Binaryen (https://github.com/WebAssembly/binaryen) can be used to improve bundle size

```kotlin
    wasmJs {
        binaries.executable()
        browser {
            applyBinaryen()
        }
    }
```

-> https://github.com/vshymanskyy/awesome-wasm-tools?tab=readme-ov-file
-> https://github.com/mbasso/awesome-wasm
