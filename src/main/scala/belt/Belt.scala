package belt

import scalaz._
import Scalaz._
import scalaz.http.response.{Response => SzResponse}

trait Belt {
  def service(f: Request): Response

  def strap: SRequest => SResponse = implicit request => {
    val response = service(Request(request))
    val headers = response.headers map (t2 => (t2._1, t2._2.charsNelErr("empty header wtf")))
    val emptySR = SzResponse.emptyStatusResponse[Stream,Stream](response.status, headers)
    response(emptySR)
  }
}
