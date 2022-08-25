import android.util.Log
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

open class TCPclass {
    var input : InputStream? = null
    var output : OutputStream? = null

    open fun readBytes() : ByteArray?{
        try{
            return input?.readBytes()
        }catch(e : Exception){
            Log.d("infox",e.toString())
            return null
        }
    }

    open fun writeBytes(b : ByteArray){
        try{
            output?.write(b)
        }catch(e : Exception) {
            Log.d("infox",e.toString())
        }
    }

    open fun SendFile(name : String){
        var file = File(name)
        writeBytes(file.readBytes())
    }

    open fun SaveFile(name : String){
        var file = File(name)
        var bytes = readBytes()
        if(bytes!=null) {
            file.writeBytes(bytes)
        }
    }
}