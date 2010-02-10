import belt.{Belt, Response}
import scalaz._
import http.response.{Location, MovedPermanently}
import Scalaz._

package object belt {
  val OK = http.response.OK // TODO rest of these
  
  type SRequest = scalaz.http.request.Request[Stream]
  type SResponse = scalaz.http.response.Response[Stream] 
  
  private[belt] def loadApplication(cl: String): Option[Belt] = {
    val userClass = Class forName cl
    if (classOf[Belt] isAssignableFrom userClass) {
      some(userClass.newInstance.asInstanceOf[Belt])
    } else {
      none
    }
  }

  def redirect(to: String): Response = {
    new Response {
      val status = MovedPermanently
      val headers = List((Location, to))
      def apply(response: SResponse): SResponse = response
    }
  }
}
