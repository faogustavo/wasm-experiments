import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun App(viewModel: ViewModel) {
    MaterialTheme {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val geoIp: GeoIp? by viewModel.data.collectAsState()

            Button(onClick = { viewModel.fetchData() }) {
                Text("Check my IP location")
            }

            if (geoIp != null) {
                Text("Your IP is from ${geoIp?.name}")
            }
        }
    }
}
