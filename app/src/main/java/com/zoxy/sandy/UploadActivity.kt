package com.zoxy.sandy

import TCPclient
import TCPserv
import UDPgram
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.zoxy.sandy.databinding.ActivityUploadBinding
import java.io.File
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset

class UploadActivity : AppCompatActivity() {
    private lateinit var b : ActivityUploadBinding

    private val PICK_FILE: Int = 111

    private var code = 0L

    private var urilist = ArrayList<Uri>()

    var u = UDPgram(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        b = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnSelect.setOnClickListener {
            code = Math.round(Math.random() * 10000) %10000
            u.verifyCode = code.toInt()
            b.tvRandCode.text = code.toString()


            var intent = Intent()
            intent.setType("file/*")
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            var types = arrayOf("file/*","image/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES,types)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,PICK_FILE)

        }

        Thread{
            if(u.listenRequest()==1){
                try {
                    val t = TCPclient()
                    t.startSocket(u.remoteIP.hostAddress, 9999)


                   var file : File? = null
                    if (urilist.get(0) != null) {
                        var path = urilist.get(0)?.path
                        if (path!!.contains("/document/raw:"))
                            path = path.replace("/document/raw:", "")
                        file = File(path)
                    }

                    var bs = file?.readBytes()
                    Log.d("infox", "size: ${bs?.size}")
                    t.writeBytes(bs!!)


                    //t.writeBytes("asdf".toByteArray(Charsets.UTF_8))

                    t.closeSocket()
                }catch ( e : Exception){
                    Log.d("infox",e.toString())
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        u.forceCloseSock()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_FILE && resultCode == RESULT_OK && data != null){
            urilist.clear()
            var clipsData = data.clipData
            if(clipsData == null){
                urilist.add(data.data!!)
                b.ivPreview.setImageURI(data.data)
                b.tvSelections.text = data.data.toString()
            }
            else{
                var csize = clipsData.itemCount
                b.tvSelections.text = ""
                for( i in 0..csize-1){
                    var uri = clipsData.getItemAt(i).uri
                    urilist.add(uri)
                    b.tvSelections.text = "${b.tvSelections.text}\n${uri.path}"
                }
            }

        }
        else{
            Log.d("infox","no selections")
        }
    }
}