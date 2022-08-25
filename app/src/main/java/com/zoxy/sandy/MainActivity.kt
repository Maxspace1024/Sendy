package com.zoxy.sandy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.zoxy.sandy.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var b : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        var sendyDir = File(Environment.getExternalStorageDirectory().path+"/Download/Sendy")
        if(!sendyDir.exists())
            sendyDir.mkdir()

        b.navUpload.setOnClickListener {
            startActivity(Intent(applicationContext,UploadActivity::class.java))
        }

        b.navDownload.setOnClickListener {
            startActivity(Intent(applicationContext,DownloadActivity::class.java))
        }
    }
}