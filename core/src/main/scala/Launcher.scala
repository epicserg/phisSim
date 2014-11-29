
import akka.actor.{Props, ActorSystem}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import eu.phisSim.core.actors.{ManagerActor, ListenerActor}
import eu.phisSim.shared.messages.StartCalculation
import eu.phisSim.shared.model.{PhysicalVector, PhysicalObject}


/**
 * This starts the cluster seed.
 */
object Launcher extends App {

  calculate(4,getObjects())




  def calculate(nrOfWorkers: Int, initialObjects: List[PhysicalObject]) {


    val actorSystem = ActorSystem("ClusterSystem", ConfigFactory.load())

     Cluster(actorSystem)
      val managerProps = Props(new ManagerActor(initialObjects))
      val manager = actorSystem.actorOf(managerProps, name = "manager")

  }

  private def getObjects(): List[PhysicalObject] = {
    val point1 = PhysicalVector(1L, 1L, 1L)
    val point2 = PhysicalVector(0L, 0L, 0L)
    val point3 = PhysicalVector(0L, 0L, 0L)
    val point4 = PhysicalVector(0L, 0L, 0L)

    val velocity1 = PhysicalVector(1L, 1L, 1L)
    val velocity2 = PhysicalVector(1L, 1L, 1L)
    val velocity3 = PhysicalVector(1L, 1L, 1L)
    val velocity4 = PhysicalVector(1L, 1L, 1L)

    val object1 = PhysicalObject(1L, point1, velocity1)
    val object2 = PhysicalObject(2L, point2, velocity2)
    val object3 = PhysicalObject(3L, point3, velocity3)
    val object4 = PhysicalObject(4L,point4,velocity4)

    object1::object2::object3::object4::Nil

  }
}
