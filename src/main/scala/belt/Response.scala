package belt

import scalaz.http._
import scalaz.http.response._

trait Response {
  val status: Status
  val headers: List[(ResponseHeader, String)]

  def apply(response: SResponse): SResponse

  def apply(f: SResponse => SResponse): Response = new Response {
    val status = Response.this.status
    val headers = Response.this.headers
    def apply(response: SResponse) = f(Response.this.apply(response))
  }
}

object Response {
  def apply(s: Status, hs: (ResponseHeader, String)*) = new Response {
    val status = s
    val headers = hs.toList
    def apply(response: SResponse): SResponse = response
  }
}
