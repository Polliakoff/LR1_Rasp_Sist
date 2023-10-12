import java.net._
import java.util.Scanner

object ChatClient {
  @throws[Exception]
  def main(arg: Array[String]): Unit = {

    new Thread(() => {
      try {
        var buffer: Array[Byte] = null
        var packet: DatagramPacket = null
        var str: String = null

        System.out.println("-------> LISTENING FOR SERVER INPUT <--------")
        var udp_socket = new MulticastSocket(1502)
        var address = InetAddress.getByName("233.0.0.1")
        udp_socket.joinGroup(address)

        while (true) {
          buffer = new Array[Byte](256)
          packet = new DatagramPacket(buffer, buffer.length)
          udp_socket.receive(packet)
          str = new String(packet.getData)
          System.out.println("-------> MESSAGE GET: " + str.trim)
        }

      } catch {
        case e: Exception =>
      }
    }).start()

    try {
      val socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999)
      val os = socket.getOutputStream
      val sc = new Scanner(System.in)
      var msg = ""

      System.out.println("-------> READING MESSAGE INPUT <--------")
      while (msg != "110") {
        msg = sc.nextLine()
        os.write(msg.getBytes)
        System.out.println("-------> MESSAGE READ")
      }
      os.close()
      socket.close()
    } catch {
      case e: Exception =>
    }
  }
}