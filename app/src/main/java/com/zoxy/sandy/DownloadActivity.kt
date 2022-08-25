package com.zoxy.sandy

import TCPclient
import TCPserv
import UDPgram
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.util.Log
import com.zoxy.sandy.databinding.ActivityDownloadBinding
import java.io.File
import java.lang.Exception
import java.net.*
import java.nio.charset.Charset

class DownloadActivity : AppCompatActivity() {
    private lateinit var b : ActivityDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_download)
        b = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnDownload.setOnClickListener {
            Thread{
                val wifi = getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifi.connectionInfo
                val dhcpInfo = wifi.dhcpInfo
                var localIP = Formatter.formatIpAddress(info.ipAddress)
                Log.d("infox","${Formatter.formatIpAddress(dhcpInfo.ipAddress)}")


                var t = TCPserv()
                t.startServerSocket(9999)

                var u = UDPgram(12)
                if(u.request(InetAddress.getByName(localIP),Integer.parseInt(b.etInputCode.text.toString()))==1){
                    Log.d("infox", "requests succ")
                    t.accept()

                    try {

                        var bytes = t.readBytes()
                        if (bytes != null) {
                            Log.d("infox","size:: ${bytes.size}")
                            var file =
                                File(Environment.getExternalStorageDirectory().path + "/Download/Sendy/temp.jpeg")
                            file.delete()
                            file.writeBytes(bytes)
                            //Log.d("infox","${bytes.toString(Charsets.UTF_8)}")

                        } else {
                            Log.d("infox", "null bytes")
                        }
                    }catch(e :Exception){
                        Log.d("infox",e.toString())
                    }

                }
                else {
                    Log.d("infox", "requests fail")
                }
                t.closeServerSocket()


            }.start()
        }
    }
}