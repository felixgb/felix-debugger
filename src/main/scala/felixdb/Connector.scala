package felixdb

import com.sun.jdi.connect.ListeningConnector
import com.sun.jdi.{Bootstrap, VirtualMachine}
import scala.collection.JavaConverters._

object Connector {

  def findAttachingConnector(): ListeningConnector =
    Bootstrap.virtualMachineManager()
      .listeningConnectors.asScala
      .find(_.name == "com.sun.jdi.SocketListen")
      .getOrElse {
        throw new Exception("can't find connector")
      }

  def attach(ac: ListeningConnector): VirtualMachine = {
    val args = ac.defaultArguments
    args.get("port").setValue("5005")
    ac.accept(args)
  }

}
