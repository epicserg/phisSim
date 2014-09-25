package eu.phisSim.core.actors

import akka.actor.{ActorRef, Props, Actor}
import akka.routing.{RoundRobinPool}
import eu.phisSim.shared.messages.{CycleState, CalculationJob, StartCalculation, JobCalculated}
import eu.phisSim.shared.model.PhysicalObject

class ManagerActor(
                    val listener: ActorRef,
                    private var objectsMap: List[PhysicalObject],
                    val nrOfWorkers: Int
                    ) extends Actor {
  val objectCount = objectsMap.size;
  val maxJobSize = (objectCount / nrOfWorkers) + 1;

  var cycleNumber: Long = 0L;
  var jobsNrToBeDone: Integer = nrOfWorkers;
  var continueCalculation: Boolean = true;


  val workerRouter = context.actorOf(
    Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

  def receive = {
    case StartCalculation =>
      startNewCycle
    case JobCalculated(result) =>
      appendResultsAndCycle(result)
  }

  def appendResultsAndCycle(calculatedObjects:List[PhysicalObject])={
    objectsMap=calculatedObjects:::objectsMap;
    jobsNrToBeDone-=1
    if(jobsNrToBeDone==0){
      startNewCycle()
    }
  }

  def startNewCycle() = {
    cycleNumber=cycleNumber+1
    jobsNrToBeDone=nrOfWorkers
    val jobs = splitJobs()
    jobs foreach {job => workerRouter ! job}
    listener ! CycleState(objectsMap,cycleNumber)
    objectsMap=Nil
  }

  private def splitJobs(): Array[CalculationJob] = {
    def nextIndex(currentIndex: Int): Int = {
      val next = currentIndex + 1
      if (next == nrOfWorkers) {
        return 0;
      }
      return next;
    }

    val jobs: Array[List[PhysicalObject]] = new Array[List[PhysicalObject]](nrOfWorkers);

    for (i <- 0 until nrOfWorkers) {
      jobs(i) = Nil
    }
    var currentJobIndex: Int = 0;
    objectsMap foreach {
      physicalObject => jobs(currentJobIndex) = physicalObject :: jobs(currentJobIndex);
        currentJobIndex = nextIndex(currentJobIndex);
    }
    return jobs.map{list => CalculationJob(objectsMap,list)}
  }

}
