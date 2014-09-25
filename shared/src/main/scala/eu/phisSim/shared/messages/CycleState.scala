package eu.phisSim.shared.messages

import eu.phisSim.shared.model.PhysicalObject


case class CycleState(state:List[PhysicalObject],cycleNumber:Long)
