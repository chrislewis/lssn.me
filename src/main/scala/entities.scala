package lssn

import highchair._
import com.google.appengine.api.datastore.Key
import java.util.Date

case class ShortUrl(
  val key: Option[Key],
  val url: String,
  val clicks: Long,
  val shrunkOn: Date
) extends Entity[ShortUrl]

object ShortUrl extends Kind[ShortUrl] {
  val url = property[String]("url")
  val clicks = property[Long]("clicks")
  val shrunkOn = property[Date]("shrunkOn")
  val * = url ~ clicks ~ shrunkOn
}
