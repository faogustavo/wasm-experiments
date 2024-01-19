package dev.valvassori.helloWorld

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import org.w3c.fetch.Response
import kotlin.js.Promise
import kotlin.wasm.WasmExport

private val scope = MainScope()

fun main() {
    println("Wasm Started!")
}

// This function is not exported
fun testMyFun() {
    println("My Fun")
}

@WasmExport
fun testMyFunWithWasmExport() {
    println("My Fun with Wasm Export")
}

@WasmExport
fun wasmExportSum(a: Int, b: Int): Int {
    return a + b
}

@JsExport
fun hello(name: String) {
    println("Hello $name!")
}

@JsExport
fun callback(callback: (String) -> Unit) {
    callback("Hello from Kotlin!")
}

@JsExport
@JsName("renamedFun")
fun jsExportWithJsName() {
    println("Renamed fun!")
}

@WasmExport
@JsName("renamedFunWithWasmExport")
fun wasmExportWithJsName() {
    println("Renamed fun!")
}

@JsExport
@JsName("dev_valvassori_helloWorld_testJsNameWithDots")
fun testJsNameWithDots() {
    println("Test JsName with dots")
}

@JsExport
fun createPerson() = Person("Gustavo", 28)

external class Person(name: String, age: Int) {
    val name: String
    val age: Int
}

//@JsExport // Doesn't work
data class KtPerson(val name: String, val age: Int)

@JsExport
fun setMessage(message: String) {
    document.querySelector("#message")?.innerHTML = message
}

@JsExport
fun runFetch(): Promise<*> = scope.promise {
    window.fetch("https://get.geojs.io/v1/ip/country.json")
        .await<Response>()
        .json()
        .await<GeoIp>()
}

external class GeoIp : JsAny {
    val country: String
}

@JsExport
fun debuggerFunWithoutCoroutines(): Int {
    val sum = 5 + 3
    val div = 9 / 3
    val mul = 2 * 4
    val sub = 10 - 5

    return (sum + div + mul + sub)
}

@JsExport
fun fibonacci(n: Int): Int {
    if (n <= 1) {
        return n
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2)
    }
}

@JsExport
fun debuggerWithKotlinType() {
    val person = KtPerson("Gustavo", 28)

    val halfAge = person.age / 2
    val firstTwoLetters = person.name.take(2)

    println("Half age: $halfAge")
    println("First two letters: $firstTwoLetters")
}
