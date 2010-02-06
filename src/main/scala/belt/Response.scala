package belt

import scalaz.http._
import scalaz.http.response._
import scalaz.http.response.{Response => _}

trait Response {
  val status: Status
  val headers: List[(ResponseHeader, String)]
  val body: Stream[Byte]
}
