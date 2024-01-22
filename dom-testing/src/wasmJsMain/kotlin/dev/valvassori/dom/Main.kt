package dev.valvassori.dom

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.InputEvent

fun main() {
    document.querySelector("#input")
        ?.addEventListener("input") {
            it as InputEvent
            val target = it.target as HTMLInputElement
            document.querySelector("#message")?.innerHTML = "Hello ${target.value}"
        }
}

/**
 * const wasmModule = (await this.dom_testing).default;
 * wasmModule.manipulateDOM()
 */
@JsExport
fun manipulateDOM() {
    val h1 = document.createElement("h1").apply {
        textContent = "Hello from Kotlin/Wasm!"
    }

    document.querySelector("#message")?.appendChild(h1)
}
