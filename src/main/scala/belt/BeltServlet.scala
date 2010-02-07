package belt

import scalaz._
import Scalaz._

class BeltServlet extends javax.servlet.http.HttpServlet {

  import scalaz.http.servlet.HttpServletRequest._
  import scalaz.http.servlet.HttpServletResponse._  
  var belt: SRequest => SResponse = null
  
  override final def init {
    val loadedApp = loadApplication("belt.Application")
    val beltApp = loadedApp.err("Could not load belt.Application")
    belt = beltApp.strap
  }
  
  override final def service(request: javax.servlet.http.HttpServletRequest, response: javax.servlet.http.HttpServletResponse) {
    request.asRequest[Stream].foreach(r => {
      val res = belt(r)
      response.respond[Stream](res)
    })
  }
}
