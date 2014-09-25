
import java.math.{ MathContext, BigDecimal => BigDec }
import eu.phisSim.shared.wrapper.Apfloat
import org.apfloat.ApfloatMath

object Test extends App{
  /*
  val a = 1::2::Nil
  val b = 3::4::Nil

  val f = for{
    c <- a
    d <- b
  }yield (c,d)

  println(f)

  println(getHa(ha))

  def ha(a: Int) :Int={
    return a+1
  }

  def getHa(closure : (Int)=> Int) : Int = {
    return closure(1)
  }


  for{
    c <- a
    d <- b
  }(println("a" + c + " "+ d ))
    */

  testApfloat()
  
  def testApfloat():Unit={
    val a  = Apfloat("2.0")
    println(a+a*a )

  }
}
