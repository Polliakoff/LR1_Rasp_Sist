import java.net._
import java.util.concurrent.CopyOnWriteArrayList
import scala.jdk.CollectionConverters._

object ChatServer {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val message_buffer = new CopyOnWriteArrayList[String]().asScala

    System.out.println("-------> SERVER RUNNING <--------")

    new Thread(() => {
      try {
        val address = InetAddress.getByName("233.0.0.1")
        var packet: DatagramPacket = null
        val socket = new DatagramSocket
        while (true) {
          Thread.sleep(10000)
          System.out.println("-------> COMMENCING UDP SEND OUT")
          if (message_buffer.nonEmpty){
            for (m <- message_buffer) {
              val conversion_array: Array[Byte] = m.getBytes("UTF-8")
              packet = new DatagramPacket(conversion_array, conversion_array.length, address, 1502)
              socket.send(packet)
            }
            message_buffer.clear()
          }
        }
      } catch {
        case e: Exception =>
      }
    }).start()

    try {
      val serverSocket = new ServerSocket(9999)
      while (true) {
        val accept = serverSocket.accept
        val ip = accept.getInetAddress.getHostAddress
        val port = accept.getPort
        val user = ip + ":" + port
        System.out.println("-------> USER JOINED:" + user)
        new Thread(() => {
          try {
            val is = accept.getInputStream
            val arr = new Array[Byte](1024)
            var len = 0
            while (len != -1) {
              len = is.read(arr)
              val msg = new String(arr, 0, len)
              val full_message = user + ":" + msg
              message_buffer.addOne(full_message)
              System.out.println("-------> MESSAGE READ")
            }
            is.close()
            accept.close()
          } catch {
            case e: Exception =>
          }
        }).start()
      }
    } catch {
      case e: Exception =>
    }

  }
}