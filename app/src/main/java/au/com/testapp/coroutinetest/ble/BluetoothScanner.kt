package au.com.testapp.coroutinetest.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import timber.log.Timber

class BluetoothScanner private constructor(context: Context) {

    companion object {
        private var _instance: BluetoothScanner? = null

        fun getInstance(context: Context): BluetoothScanner {
            if (_instance == null) {
                _instance = BluetoothScanner(context)
            }
            return _instance!!
        }
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var isScanning = false
    private val scanPeriod: Long = 10_000 // 10 seconds
    private val handler = Handler(Looper.getMainLooper())

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.d("Device found: ${result.device.name} - ${result.device.address}")
        }
    }

    init {
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Timber.e("Bluetooth is not available")
        } else {
            bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
        }
    }

    fun startRepeatingScan() {
        Timber.d("Starting BLE scan on repeat")
        if (isScanning) {
            Timber.d("Scanner already running")
            return
        }
        handler.post(scanRunnable)
    }

    private val scanRunnable = object : Runnable {
        override fun run() {
            if (isScanning) {
                stopScan()
                handler.post(this)
            } else {
                startScan()
                // Schedule the next scan after the scan period
                handler.postDelayed(this, scanPeriod)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (bluetoothLeScanner != null && !isScanning) {
            Timber.d("Starting BLE scan")
            bluetoothLeScanner?.startScan(scanCallback)
            isScanning = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        if (bluetoothLeScanner != null && isScanning) {
            Timber.d("Stopping BLE scan")
            bluetoothLeScanner?.stopScan(scanCallback)
            isScanning = false
        }
    }

    fun stopRepeatingScan() {
        handler.removeCallbacks(scanRunnable)
        stopScan()
    }

    @SuppressLint("MissingPermission")
    fun startScanningBad() {
        if (bluetoothLeScanner != null) {
            Timber.d("Starting BLE scan - stupidly")
            bluetoothLeScanner?.startScan(scanCallback)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScanningBad() {
        if (bluetoothLeScanner != null) {
            Timber.d("Stopping BLE scan - stupidly")
            bluetoothLeScanner?.stopScan(scanCallback)
        }
    }
}
