package felixdb

object DebuggerServer extends cask.MainRoutes {

  @cask.websocket("/debugger")
  def debugger(): cask.WebsocketResult =
    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text(txt) =>
          channel.send(cask.Ws.Text("this is a thing"))
      }
    }

  val debugThread = new Thread {
    override def run(): Unit =
      while (true) {
        val connector = Connector.findAttachingConnector()
        val vm = Connector.attach(connector)
        val bp = Breakpoint(33)
        new Events(vm, List(bp)).start()
      }
  }

  debugThread.start()

  initialize()

}
