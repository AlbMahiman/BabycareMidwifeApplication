package com.example.midwivesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.midwivesapp.databinding.ActivityQrScannerBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import com.google.android.gms.vision.Detector.Detections
import com.google.firebase.database.DatabaseReference

class QrScanner : AppCompatActivity() {
    // Initialize variables
    private lateinit var binding: ActivityQrScannerBinding
    private lateinit var user: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    var functionRunningStatus = "false"
    private var scannedValue = ""
    private var lastScannedUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity and set the content view
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Handle click events for home and back buttons
        binding.btnHome.setOnClickListener {
            var intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener {
            var intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Check if camera permission is granted, otherwise request it
        if (ContextCompat.checkSelfPermission(
                this@QrScanner, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

        // Create and start the QR code scanner animation
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@QrScanner, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }

    private fun setupControls() {
        // Create a BarcodeDetector and a CameraSource
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) // Enable autofocus
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    // Start camera preview after a 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        // Set up the barcode detection processor
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                // Release resources when the scanner is closed
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    // Get the scanned QR code value
                    scannedValue = barcodes.valueAt(0).rawValue

                    runOnUiThread {
                        if (!scannedValue.contains(".")) {
                            if (functionRunningStatus == "false") {
                                // Check the scanned QR code
                                checkQRCode(scannedValue)
                            }
                        } else {
                            binding.qrStatusText.text = "Invalid Qr code"
                        }
                    }
                } else {
                    Toast.makeText(this@QrScanner, "value- else", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun checkQRCode(scannedValue: String) {
        // Lock the function to prevent multiple scans
        functionRunningStatus = "true"

        // Get a reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("Mother")

        // Check if the scanned QR code corresponds to a valid user
        databaseReference.child(scannedValue).get().addOnSuccessListener {
            if (it.exists()) {
                // Stop the camera source
                cameraSource.stop()

                // Clear the QR status text
                binding.qrStatusText.text = ""

                // Scanned QR code belongs to a user
                // Perform necessary actions here

                // Start the ViewMother activity
                var intent = Intent(this, ViewMother::class.java).also {
                    it.putExtra("motherId", scannedValue)
                }
                startActivity(intent)
            } else {
                // Invalid QR code
                binding.qrStatusText.text = "Invalid QR code"
                functionRunningStatus = "false"
            }
        }
    }

    private fun askForCameraPermission() {
        // Request camera permission from the user
        ActivityCompat.requestPermissions(
            this@QrScanner,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, set up the camera controls
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
