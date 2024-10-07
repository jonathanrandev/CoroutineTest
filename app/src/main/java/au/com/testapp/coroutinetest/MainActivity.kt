package au.com.testapp.coroutinetest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.testapp.coroutinetest.ble.BluetoothScanner
import au.com.testapp.coroutinetest.ui.theme.CoroutineTestTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothScanner = BluetoothScanner.getInstance(this.applicationContext)
        enableEdgeToEdge()
        setContent {
            CoroutineTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                        ) {
                            Button(onClick = {
                                val intent =
                                    Intent(this@MainActivity, TestForegroundService::class.java)
                                startForegroundService(intent)
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Start Foreground Service")
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text("Use these 2 buttons to enable scanning BLE every 10 seconds. Internally checks if running before starting scan.")
                            Button(onClick = {
                                bluetoothScanner.startRepeatingScan()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Start repeated BLE Scanning (Good)")
                            }

                            Button(onClick = {
                                bluetoothScanner.stopRepeatingScan()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Stop repeated BLE Scanning (Good)")
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {

                            Text("Use these buttons to cause a problem with the allowed number of BLE scan callbacks. No checks, no safety.")
                            Button(onClick = {
                                bluetoothScanner.startScanningBad()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Start BLE Scanning (Bad)")
                            }

                            Button(onClick = {
                                bluetoothScanner.stopScanningBad()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Stop BLE Scanning (Bad)")
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Hello! Please go to the permission settings on device and grant all permissions for this app before you press any buttons here.",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoroutineTestTheme {
        Greeting()
    }
}