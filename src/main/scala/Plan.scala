package lssn

import unfiltered.request._
import unfiltered.response._
import QParams._

import highchair.meta.FilterOps._
import com.google.appengine.api.datastore.DatastoreServiceFactory

/** unfiltered plan */
class Plan extends unfiltered.filter.Plan {
  
  implicit val dss = DatastoreServiceFactory.getDatastoreService
  
  val URL = """\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]""".r
  
  def intent = {
    
    case POST(Path("/shrink", Params(params, _))) => {
      val expected = for {
        url <- lookup("url")
          .is(required("URL is required."))
          .is(pred
            { s:String => !URL.unapplySeq(s).isEmpty }
            { s => "%s is not a valid URL.".format(s) })
      } yield {
        val shortUrl = 
          ShortUrl find {
            ShortUrl.url === url.get
          } match {
            case Nil      => ShortUrl.put(ShortUrl(None, url.get, 0, new java.util.Date))
            case u :: Nil => u
          }
          
        val sxgId = shortUrl.key.map { k =>
          Sexagesimal.toSxg(k.getId)
        } getOrElse(error("Failed"))
        
        ResponseString(sxgId)
      }
      
      expected(params) orFail { fails =>
        BadRequest ~> ResponseString(
          fails map { fail => fail.name + ":" + fail.error } mkString ","
        )
      }
      
    }
    
    case Path(Seg(x :: Nil), _) => {
      val key = ShortUrl.keyFor(Sexagesimal.toNumber(x))
      
      ShortUrl.get(key).map { s =>
        ShortUrl.put(s.copy(clicks = s.clicks + 1))
      } match {
        case None     => NotFound
        case Some(s)  => Redirect(s.url)
      }
    }
    
  }
}
