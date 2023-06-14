package info.fekri.androidxml

import android.Manifest.permission.RECORD_AUDIO
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.WindowCompat
import info.fekri.androidxml.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null
    private var isBackSelected: Boolean = true

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            // request camera permission
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                requestPermissions()
            }

            // Set up the listeners for take photo and video capture buttons
            imageCaptureButton.setOnClickListener { takePhoto() }
            videoCaptureButton.setOnClickListener { captureVideo() }
            changeCameraViewButton.setOnClickListener {
                isBackSelected = !isBackSelected

                if (isBackSelected) {
                    startCamera(CameraSelector.DEFAULT_BACK_CAMERA)
                } else {
                    startCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
                }
            }

            cameraExecutor = Executors.newSingleThreadExecutor()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return
        binding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Android_xml-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@MainActivity,
                        RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        binding.videoCaptureButton.apply {
                            setIconResource(R.drawable.ic_puase_circle)
                            isEnabled = true
                        }
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "vide capture succeed: " +
                                    "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                            Log.d("MainActivity", msg)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(
                                "MainActivity", "Video capture ends with error: " +
                                        "${recordEvent.error}"
                            )
                        }
                        binding.videoCaptureButton.apply {
                            setIconResource(R.drawable.ic_video_camera)
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Android_xml-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val message = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", message)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(
                        "MainActivity",
                        "Photo capture was failed: ${exception.message}",
                        exception
                    )
                }
            }
        )

    }

    /**
     * "[CameraSelector.DEFAULT_BACK_CAMERA] = Background camera"
     *
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     *
     * [startCamera] is used to start the camera. The function startCamera takes an optional parameter cameraSelector which is set to [CameraSelector.DEFAULT_BACK_CAMERA] by default.
     *
     * The function starts by creating a [ProcessCameraProvider] instance using the `getInstance()` method. The `addListener()` method is then called on the `cameraProviderFuture` object to add a listener that will be notified when the camera provider is ready.
     *
     * Once the camera provider is ready, the function creates a [Preview] object using the `Preview.Builder()` method. The preview is then set up to display on the screen using the `setSurfaceProvider()` method.
     *
     * Next, a [Recorder] object is created using the `Recorder.Builder()` method. The quality of the video capture is set using the `setQualitySelector()` method with the highest quality and fallback strategy set to higher quality than SD. A [VideoCapture] object is then created using the `VideoCapture.withOutput()` method with the recorder as an argument.
     *
     * An [ImageCapture] object is also created using the `ImageCapture.Builder()` method.
     *
     * Finally, the use cases are bound to the camera using the `bindToLifecycle()` method of the camera provider object. If there is an exception during this process, it will be caught and logged.
     * */
    private fun startCamera(cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(
                    QualitySelector.from(
                        Quality.HIGHEST,
                        FallbackStrategy.higherQualityThan(Quality.SD)
                    )
                )
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider
                    .bindToLifecycle(this, cameraSelector, preview, imageCapture, videoCapture)
            } catch (exc: Exception) {
                Log.e("MainScreen", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

}