package felixdb

import java.util.concurrent.LinkedBlockingQueue

object DebuggerServer extends cask.MainRoutes {

  val messages = new LinkedBlockingQueue[String]()

  @cask.websocket("/debugger")
  def debugger(): cask.WebsocketResult =
    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("start") =>
          while (true) {
            val msg = messages.take()
            channel.send(cask.Ws.Text(msg))
          }
      }
    }

  val debugThread = new Thread {
    override def run(): Unit =
      while (true) {
        val connector = Connector.findAttachingConnector()
        val vm = Connector.attach(connector)
        val bp = Breakpoint(33)
        new Events(vm, List(bp), messages).start()
      }
  }

  debugThread.start()

  initialize()

}
