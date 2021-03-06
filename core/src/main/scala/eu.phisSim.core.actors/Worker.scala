package eu.phisSim.core.actors

import akka.actor.Actor
import eu.phisSim.shared.messages.{CalculationJob, JobCalculated}
import eu.phisSim.shared.model.{PhysicalObject, PhysicalVector}
import eu.phisSim.shared.wrapper.Apfloat

import scala.annotation.tailrec


class Worker extends Actor {

   private val dt = Apfloat("0.00001")

   def receive = {
     case CalculationJob(allObjects, objectsToCalculate) =>
       val calculatedObjects: List[PhysicalObject] = calculateRec(allObjects, objectsToCalculate)
       sender ! JobCalculated(calculatedObjects)
   }

   private def calculate(allObjects: List[PhysicalObject],
                         objectsToCalculate: List[PhysicalObject]): List[PhysicalObject] = {
     var result: List[PhysicalObject] = Nil
     for (physicalObject <- objectsToCalculate) {
       result = calculateObject(allObjects, physicalObject) :: result
     }
     result
   }

   private def calculateRec(all: List[PhysicalObject],
                            objectsToCalculate: List[PhysicalObject]): List[PhysicalObject] = {
     @tailrec
     def calc(all: List[PhysicalObject],
              remaining: List[PhysicalObject],
              result: List[PhysicalObject]): List[PhysicalObject] = {
       if (remaining eq Nil) {
         return result
       }
       val newObject = calculateObject(all, remaining.head)
       calc(all, remaining.tail, newObject :: result)
     }

     calc(all, objectsToCalculate, Nil)
   }

   private def calculateObject(allObjects: List[PhysicalObject],
                               physicalObject: PhysicalObject): PhysicalObject = {
     val acceleration = getSummedAcceleration(allObjects, physicalObject)
     val newVelocity = PhysicalVector(
       physicalObject.velocity.x + dt * acceleration.x,
       physicalObject.velocity.y + dt * acceleration.y,
       physicalObject.velocity.z + dt * acceleration.z
     )
     val newPosition = PhysicalVector(
       physicalObject.position.x + dt * newVelocity.x,
       physicalObject.position.y + dt * newVelocity.y,
       physicalObject.position.z + dt * newVelocity.z

     )
     PhysicalObject(physicalObject.mass, newPosition, newVelocity)
   }

   private def getSummedAcceleration(allObjects: List[PhysicalObject],
                                     objectToCalculate: PhysicalObject): PhysicalVector = {
     var acceleration: PhysicalVector = PhysicalVector(0, 0, 0)
     for (otherObject <- allObjects) {
       val acc = getAccleration(objectToCalculate, otherObject);
       acceleration = acceleration.getSuperPositionWith(acc);
     }
     return acceleration
   }

   private def getAccleration(observedObject: PhysicalObject, otherObject: PhysicalObject): PhysicalVector = {
     val distanceSqr: Apfloat = getDistanceSquared(observedObject.position, otherObject.position)
     if (distanceSqr == 0) {
       return PhysicalVector(0, 0, 0)
     }
     val scalarAcc: Apfloat = otherObject.mass / (distanceSqr * distanceSqr.root(2))
     return PhysicalVector(scalarAcc * (observedObject.position.x - otherObject.position.x),
       scalarAcc * (observedObject.position.y - otherObject.position.y),
       scalarAcc * (observedObject.position.z - otherObject.position.z))
   }

   private def getDistanceSquared(o1: PhysicalVector, o2: PhysicalVector): Apfloat = {
     ((o1.x - o2.x) ^ (2L)) + ((o1.y - o2.y) ^ (2L)) + ((o1.z - o2.z) ^ (2L))
   }
 }
