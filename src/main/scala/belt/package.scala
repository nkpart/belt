import scalaz._
import Scalaz._

import scalaz.http.response.{Response => SzResponse}
package object belt {
  type SRequest = scalaz.http.request.Request[Stream]
  type SResponse = scalaz.http.response.Response[Stream] 
  
  def strap(f: Request => Response): SRequest => SResponse = request => {
    val response = f(new Request { val underlying = request })
    val headers = response.headers map (t2 => (t2._1, t2._2.charsNelErr("empty header wtf")))
    SzResponse.statusResponse(response.status, headers, response.body)(request)
  }
}