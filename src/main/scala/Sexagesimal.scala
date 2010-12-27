package lssn

/**
 * http://tantek.pbworks.com/w/page/19402946/NewBase60
 * Tantek Çelik http://tantek.com
 */
object Sexagesimal {
  val SXG_CHARS = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ_abcdefghijkmnopqrstuvwxyz"
  
  def toSxg(num: Long) = {
    def conv(n: Long, s: String): String = n match {
      case x if x <= 0  => s
      case x if x > 0   => conv((x - (x % 60L)) / 60L, SXG_CHARS((x % 60L).toInt) + s) 
    }
    conv(num, "")
  }
  
  def toNumber(sxgValue: String) = {
    def conv(sxg: String, v: Long): Long = sxg.headOption match {
      case None     => v
      case Some(c)  => conv(sxg.drop(1), ((60 * v) + convertChar(c)))
    }
    conv(sxgValue, 0)
  }
  
  /** Map a SXG (ASCII) character to a decimal value. */
  def convertChar(c: Int) = c match {
    case _ if c >= 48 && c <= 57    => c - 48
    case _ if c >= 65 && c <= 72    => c - 55
    case _ if c == 73 || c == 108   => 1      // typo capital I, lowercase l to 1
    case _ if c >= 74 && c <= 78    => c - 56
    case _ if c == 79               => 0      // error correct typo capital O to 0
    case _ if c >= 80 && c <= 90    => c - 57
    case _ if c == 95               => 34     // underscore
    case _ if c >= 97 && c <= 107   => c - 62
    case _ if c >= 109 && c <= 122  => c - 63
    case _                          => 0      // treat all other noise as 0
  }
}
