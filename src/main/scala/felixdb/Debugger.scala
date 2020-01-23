package felixdb

object Debugger extends App {

  while (true) {
    val connector = Connector.findAttachingConnector()
    val vm = Connector.attach(connector)
    val bp = Breakpoint(33)
    new Events(vm, List(bp)).start()
  }

}
