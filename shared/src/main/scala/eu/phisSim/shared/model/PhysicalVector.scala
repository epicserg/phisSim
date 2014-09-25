package eu.phisSim.shared.model

import eu.phisSim.shared.wrapper.Apfloat

case class PhysicalVector(x:Apfloat,y:Apfloat,z:Apfloat){

  def getSuperPositionWith(vector:PhysicalVector):PhysicalVector ={
    PhysicalVector(vector.x+this.x,vector.y+this.y,vector.z+this.z)
  }
}

