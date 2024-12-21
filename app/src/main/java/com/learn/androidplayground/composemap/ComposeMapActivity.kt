package com.learn.androidplayground.composemap

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.learn.androidplayground.R
import com.learn.androidplayground.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.flow.filter
import java.util.Locale

/**
 * @author Partho
 * @since 19 Dec,2024
 */
class ComposeMapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.8103, 90.4125), 18f) // Dhaka, Bangladesh
    }
    val markerPosition = remember { mutableStateOf(LatLng(23.8103, 90.4125)) }
    val address = remember { mutableStateOf("") }

    // Listener for camera idle
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .filter { !it } // Trigger when map stops moving
            .collect {
                markerPosition.value = cameraPositionState.position.target
                getAddressFromLatLng(context, markerPosition.value) { fetchedAddress ->
                    address.value = fetchedAddress
                }
            }
    }

    Column {
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_marker), // Center marker icon
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
        Text(
            text = "Address: ${address.value}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

fun getAddressFromLatLng(context: Context, latLng: LatLng, onAddressFetched: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(
            latLng.latitude,
            latLng.longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        onAddressFetched(addresses[0].getAddressLine(0) ?: "No address found")
                    } else {
                        onAddressFetched("No address found")
                    }
                }

                override fun onError(errorMessage: String?) {
                    onAddressFetched("Unable to fetch address: $errorMessage")
                }
            }
        )
    } else {
        // Use the synchronous method for older APIs
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                onAddressFetched(addresses[0].getAddressLine(0) ?: "No address found")
            } else {
                onAddressFetched("No address found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onAddressFetched("Unable to fetch address")
        }
    }
}

