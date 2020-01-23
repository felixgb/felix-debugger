package felixdb

import com.sun.jdi._
import com.sun.jdi.event._
import collection.JavaConverters._
import java.util.concurrent.LinkedBlockingQueue

final case class Breakpoint(lineNumber: Int)

class Events(vm: VirtualMachine, breakpoints: List[Breakpoint], messages: LinkedBlockingQueue[String]) {

  lazy val eventRequestManager = vm.eventRequestManager()

  def enableClassPrepareEvents(): Unit = {
    val cpr = eventRequestManager.createClassPrepareRequest()
    cpr.addSourceNameFilter("*MainSpec.scala")
    cpr.enable()
  }

  def setBreakpoints(classPrepareEvent: ClassPrepareEvent): Unit =
    classPrepareEvent.referenceType() match {
      case classType: ClassType =>
        breakpoints.foreach { bp =>
          classType.locationsOfLine(bp.lineNumber).asScala match {
            case Seq(location) =>
              eventRequestManager.createBreakpointRequest(location).enable()
          }
        }
      case unknown => throw new Exception(s"unknown type: ${unknown}")
    }

  def displayVaribles(locatable: LocatableEvent): Unit = {
    val stackFrame = locatable.thread.frame(0)
    val visibleValues = stackFrame.getValues(stackFrame.visibleVariables()).asScala
    messages.add(s"visible at ${stackFrame.location}:")
    visibleValues.foreach { case (varible, value) =>
      messages.add(s"${varible.name} ==> ${value}")
    }
  }

  def eventSets(): Iterator[EventSet] =
    new Iterator[EventSet] {
      def next(): EventSet =
        vm.eventQueue().remove()
      def hasNext: Boolean =
        true
    }

  def start(): Unit = {
    enableClassPrepareEvents()

    try {
      for {
        eventSet <- eventSets()
        event <- eventSet.eventIterator().asScala
      } {
        event match {
          case cpe: ClassPrepareEvent =>
            setBreakpoints(cpe)
          case bpe: BreakpointEvent =>
            displayVaribles(bpe)
          case ev =>
            println(s"unhandled event: ${ev}")
        }
        vm.resume()
      }
    } catch {
      case disconnected: VMDisconnectedException =>
        println("vm disconnected")
    }
  }

}
