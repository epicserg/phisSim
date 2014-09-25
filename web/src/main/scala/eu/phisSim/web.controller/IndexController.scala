package eu.phisSim.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMethod, RequestMapping}


@Controller
@RequestMapping(Array( "/hi"))
class IndexController {

  @RequestMapping( method = Array(RequestMethod.GET))
  def index(model:Model)={
    "index"
  }
}
