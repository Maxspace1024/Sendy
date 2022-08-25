import android.util.Log
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class TCPserv : TCPclass() {
    private var sock: ServerSocket? = null
    private var client: Socket? = null

    fun startServerSocket(port: Int) {
        if (sock == null) {
            try {
                sock = ServerSocket(port)
            } catch (e: Exception) {
                Log.d("infox","error : " + e.stackTraceToString())
                closeServerSocket()
            }
        }
    }

    fun accept(){
        try {
            client = sock?.accept()
            input = client?.getInputStream()
            output = client?.getOutputStream()
        } catch (e: Exception) {
            Log.d("infox","error : " + e.stackTraceToString())
            closeServerSocket()
        }
    }


    fun closeServerSocket() {
        sock?.close()
        sock == null
    }
}