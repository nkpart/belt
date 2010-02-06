package belt

trait Request {
  val underlying: scalaz.http.request.Request[Stream]
}


