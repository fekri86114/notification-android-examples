package info.fekri.androidxml.ui

import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.WindowCompat
import info.fekri.androidxml.R
import info.fekri.androidxml.databinding.ActivityMainBinding
import info.fekri.androidxml.ext.CHANNEL_ID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // show notification when clicked on button
        binding.btnOpenNotification.setOnClickListener { view ->


            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New message!")
                .setContentText("You got a new message from Mohammad Reza Fekri.")
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(
                            BitmapFactory.decodeResource(resources, R.drawable.img_person)
                        )
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify((1..100).random(), notification)

        }

    }

}