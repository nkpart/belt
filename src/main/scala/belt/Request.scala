package belt

import scalaz._
import http.request.Method
import Scalaz._

trait Request {
  val underlying: scalaz.http.request.Request[Stream]

  val extraParams: Map[String, String]

  def apply(s: String): Option[String] = extraParams.get(s) orElse ((underlying !| s) ∘ (_.mkString))

  def log {
    println("%s\t%s" format (underlying.method, underlying.path.list.mkString))
  }

  def methodHax(methodField: String = "_method"): Request = {
    val method = (apply(methodField) >>= (scalaz.http.Slinky.StringMethod _))
    method ∘ {(method: Method) => Request(underlying(method), extraParams)} | this
  }

  def addParam(key: String, value: String) = {
    Request(underlying, extraParams + ((key, value)))
  }

  lazy val method = underlying.method
  lazy val parts = underlying.parts

  // Matches paths of this form:
  //   /a/:b/c
  // Any part preceded by a : will be written into the query string.
  //   ie. If constructed with /a/:b/c and hit with /a/5/c, (request ! "b") will be "5"
  def parsePath(s: String): Option[Request] = {
    val partsFormat = s.stripPrefix("/").stripSuffix("/").split("/")

    if (parts.size == partsFormat.size) {
      val checks = partsFormat.toList zip parts map {
        case (a, b) =>
          if (a.startsWith(":")) {
            val k = a.stripPrefix(":")
            some(some((k, b)))
          } else {
            (a == b).option(none)
          }
      }
      if (checks ∀ (_.isDefined)) {
        some(checks.foldl(this, (r: Request, check: Option[Option[(String, String)]]) => check match {
          case Some(Some((k, v))) => r.addParam(k, v)
          case Some(None) => r
          case None => r // should never reach here, all should be defined
        }))
      } else {
        none
      }
    } else {
      none
    }
  }
}

object Request {
  def apply(r: scalaz.http.request.Request[Stream]): Request = apply(r, Map())

  def apply(r: scalaz.http.request.Request[Stream], overrides: Map[String, String]): Request = new Request {
    val underlying = r
    val extraParams = overrides
  }
}


