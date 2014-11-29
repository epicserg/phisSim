package eu.phisSim.core.actors

import akka.actor.{RootActorPath, ActorRef, Props, Actor}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{UnreachableMember, MemberUp, InitialStateAsEvents, MemberEvent}
import akka.cluster.Member
import akka.cluster.routing.{ClusterRouterPoolSettings, ClusterRouterPool}
import akka.routing.{ConsistentHashingPool, RoundRobinPool, FromConfig}
import eu.phisSim.shared.messages._
import eu.phisSim.shared.model.PhysicalObject

class ManagerActor(
                    private var objectsMap: List[PhysicalObject]
                    ) extends Actor {

  var activeWorkers: Int = 0;

  /**
   * Nr of workers that have just joined the cluster.
   * When the next cycle starts, they will become active workers
   */
  var idleWorkers: Int = 0;

  /**
   * the number of jobs which must be done before starting the next cycle.
   * When the number reachs 0 a new cycle can start.
   */
  var jobsNrToBeDone: Integer = 0;


  val objectCount = objectsMap.size;
  //val maxJobSize = (objectCount / nrOfWorkers) + 1;

  var cycleNumber: Long = 0L;


  var continueCalculation: Boolean = true;

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberUp], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  val workerRouter = context.actorOf(FromConfig.props(Props[Worker]),
    name = "workerRouter")


  def receive = {
    case MemberUp(m) =>
      if (m.roles.contains("worker")) {
        activeWorkers = activeWorkers + 1
        if (activeWorkers == 1) {
          startNewCycle()
        }
      }
    case JobCalculated(result) =>
      appendResultsAndCycle(result)
  }


  def appendResultsAndCycle(calculatedObjects: List[PhysicalObject]) = {
    objectsMap = calculatedObjects ::: objectsMap;
    jobsNrToBeDone -= 1
    if (jobsNrToBeDone == 0) {
      startNewCycle()
    }
  }

  def startNewCycle() = {
    cycleNumber = cycleNumber + 1
    activeWorkers=activeWorkers+idleWorkers
    idleWorkers=0
    val jobs = splitJobs(activeWorkers)
    jobs foreach {job => workerRouter ! job}
    objectsMap = Nil
  }

  private def splitJobs(jobCount:Int): Array[CalculationJob] = {
    def nextIndex(currentIndex: Int,jobCount:Int): Int = {
      val next = currentIndex + 1
      if (next == jobCount) {
        return 0;
      }
      return next;
    }

    val jobs: Array[List[PhysicalObject]] = new Array[List[PhysicalObject]](jobCount);

    for (i <- 0 until jobCount) {
      jobs(i) = Nil
    }
    var currentJobIndex: Int = 0;
    objectsMap foreach {
      physicalObject => jobs(currentJobIndex) = physicalObject :: jobs(currentJobIndex);
        currentJobIndex = nextIndex(currentJobIndex,jobCount);
    }
    return jobs.map { list => CalculationJob(objectsMap, list)}
  }

}
