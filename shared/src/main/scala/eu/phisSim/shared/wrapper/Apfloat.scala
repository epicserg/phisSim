package eu.phisSim.shared.wrapper

import org.apfloat.{ApfloatMath, Apfloat => JApfloat}

/**
 * A class with companion object to simplify Apfloat use in scala code.
 */
object Apfloat {

  val DefaultPrecision = 100L

  def apply(jApfloat: JApfloat): Apfloat = {
    new Apfloat(jApfloat)
  }

  def apply(number: String): Apfloat = {
    apply(new JApfloat(number, DefaultPrecision))
  }

  def apply(number: Long): Apfloat = {
    apply(new JApfloat(number, DefaultPrecision))
  }

  implicit def javaApfloatToCurrentAppfloat(x: JApfloat): Apfloat = apply(x)

  implicit def longToApfloat(x: Long): Apfloat = apply(x)
}
@SerialVersionUID(100L)
class Apfloat(val apfloat: JApfloat)extends Serializable {

  def +(that: Apfloat): Apfloat = {
    apfloat.add(that.apfloat)
  }

  def *(that: Apfloat): Apfloat = {
    apfloat.multiply(that.apfloat)
  }

  def /(that: Apfloat): Apfloat = {
    apfloat.divide(that.apfloat)
  }

  def -(that: Apfloat): Apfloat = {
    apfloat.subtract(that.apfloat)
  }

  def ^(power: Long): Apfloat = {
    ApfloatMath.pow(apfloat, power)
  }

  def root(n: Int): Apfloat = {
    ApfloatMath.root(apfloat, n)
  }

  def ==(that: Long): Boolean = {
    apfloat.equals(new JApfloat(that))
  }

  override def toString(): String = {
    apfloat.toString()
  }

}
