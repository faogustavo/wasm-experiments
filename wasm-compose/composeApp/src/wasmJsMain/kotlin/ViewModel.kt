import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.w3c.fetch.Response


class ViewModel {
    private val scope = MainScope()
    private val _data = MutableStateFlow<GeoIp?>(null)

    val data: StateFlow<GeoIp?> = _data.asStateFlow()

    fun fetchData() {
        scope.launch {
            val geoIpStruct = window.fetch("https://get.geojs.io/v1/ip/country.json")
                .await<Response>()
                .json()
                .await<GeoIpJsStruct>()

            _data.value = GeoIp(geoIpStruct)
        }
    }
}

data class GeoIp(
    val name: String
) {
    constructor(struct: GeoIpJsStruct) : this(struct.name)
}

external class GeoIpJsStruct : JsAny {
    val name: String
}
