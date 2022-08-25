import android.util.Log
import java.net.*
import java.nio.ByteBuffer
import kotlin.collections.*

class UDPgram(var bufsize : Int) {
    private val baordPort = 9998
    var remoteIP = InetAddress.getByName("0.0.0.0")
    private var datasock : DatagramSocket? = null

    var verifyCode = 0

    fun request(localIP : InetAddress, code : Int) : Int{
        val datasock = DatagramSocket()
        var request = localIP.address + intToBytes( datasock.localPort ) + intToBytes(code)
        val datapack = DatagramPacket(request,request.size, InetAddress.getByName("255.255.255.255"),baordPort)
        datasock.send(datapack)

        var buf = ByteArray(bufsize)
        var datapack2 = DatagramPacket(buf,buf.size)
        datasock.receive(datapack2)

        var verifystat = bytesToInt(datapack2.data.take(4).toByteArray())

        datasock.close()

        return verifystat
    }

    fun listenRequest() : Int{
        val buf = ByteArray(bufsize)

        val datasock = DatagramSocket(baordPort)
        val datapack = DatagramPacket(buf, buf.size)
        datasock.receive(datapack)

        var data = datapack.data
        val ip = InetAddress.getByAddress(data.take(4).toByteArray())
        val port = bytesToInt(data.take(8).takeLast(4).toByteArray())
        val requestCode = bytesToInt(data.takeLast(4).toByteArray())
        Log.d("infox","receive: ${ip}:${port}_${requestCode}")

        var verifystat = 0
        if(verifyCode == requestCode)
            verifystat = 1
        else
            verifystat = 0
        var reply = intToBytes(verifystat)
        var dp2 = DatagramPacket(reply,reply.size,ip,port)
        datasock.soTimeout = 1000
        datasock.send(dp2)

        datasock.close()

        remoteIP = ip
        return verifystat
    }

    fun forceCloseSock(){
        datasock?.close()
        datasock = null
    }

    companion object {


        fun intToBytes(i: Int): ByteArray =
            ByteBuffer.allocate(Int.SIZE_BYTES).putInt(i).array()

        fun bytesToInt(bytes: ByteArray): Int =
            ByteBuffer.wrap(bytes).int
    }
}