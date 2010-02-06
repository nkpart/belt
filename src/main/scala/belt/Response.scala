package belt

import scalaz.http._
import scalaz.http.response._

trait Response {
  val status: Status
  val headers: List[(ResponseHeader, String)]

  def apply(response: SResponse): SResponse
}
