package felixdb

import java.util.concurrent.LinkedBlockingQueue
import upickle.default.write

object DebuggerServer extends cask.MainRoutes {

  val messages = new LinkedBlockingQueue[DebugMessage]()

  @cask.websocket("/debugger")
  def debugger(): cask.WebsocketResult =
    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("start") =>
          while (true) {
            val msg = messages.take()
            channel.send(cask.Ws.Text(write(msg)))
          }
      }
    }

  val debugThread = new Thread {
    override def run(): Unit =
      while (true) {
        val connector = Connector.findAttachingConnector()
        val vm = Connector.attach(connector)
        val bp = Breakpoint(67)
        new Events(vm, List(bp), messages).start()
      }
  }

  debugThread.start()

  initialize()

}
