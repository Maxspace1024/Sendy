import android.util.Log
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

class TCPclient : TCPclass() {
    private var sock : Socket? = null

    fun startSocket(addr : String , port : Int){
        if(sock == null){
            try{
                sock = Socket(addr,port)
                input = sock?.getInputStream()
                output = sock?.getOutputStream()
            }catch(e : Exception){
                Log.d("infox",e.toString())
            }
        }
        else{
            return
        }
    }

    fun closeSocket(){
        sock?.close()
        sock == null
    }
}