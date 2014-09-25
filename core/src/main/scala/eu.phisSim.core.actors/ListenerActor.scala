package eu.phisSim.core.actors

import akka.actor.Actor
import eu.phisSim.shared.messages.CycleState
import eu.phisSim.shared.model.PhysicalObject


class ListenerActor extends Actor {
  def receive = {
    case CycleState(state:List[PhysicalObject],cycleNumber:Long)=>
      println("recieved message : "+cycleNumber+ " : state " +state.toList)
  }
}


