package eu.phisSim.shared.messages

import eu.phisSim.shared.model.PhysicalObject


case class CalculationJob(allObjects: List[PhysicalObject], objectsToCalculate: List[PhysicalObject])