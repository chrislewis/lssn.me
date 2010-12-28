package lssn

import response._

import unfiltered.request._
import unfiltered.response._
import QParams._

import highchair.meta.FilterOps._
import com.google.appengine.api.datastore.DatastoreServiceFactory

import net.liftweb.json._
import Extraction.decompose
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST._

/** unfiltered plan */
class Plan extends unfiltered.filter.Plan {
  
  implicit val formats = net.liftweb.json.DefaultFormats
  implicit val dss = DatastoreServiceFactory.getDatastoreService
  
  object Validation {
    val URL = """\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]""".r
    
    def shrink[A](valid: String => A) =
      for {
        url <- lookup("url")
          .is(required("URL is required."))
          .is(pred
            { s:String => !URL.unapplySeq(s).isEmpty }
            { s => "%s is not a valid URL.".format(s) })
      } yield valid(url.get)
  }
  
  def intent = {
    
    case POST(Path("/lessen", Params(params, r))) => {
      val expected = Validation.shrink { url =>
        val shortUrl = 
          ShortUrl find {
            ShortUrl.url === url
          } match {
            case Nil      => ShortUrl.put(ShortUrl(None, url, 0, new java.util.Date))
            case u :: Nil => u
          }
          
        val sxgId = shortUrl.key.map { k =>
          Sexagesimal.toSxg(k.getId)
        } getOrElse(error("Failed"))
        
        Json(decompose(Map(("shortUrl" -> sxgId))))
      }
      
      expected(params) orFail { fails =>
        BadRequest ~> Json(
          decompose(fails map { fail => fail.name + ":" + fail.error } mkString ",")
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
