package lssn.response

import unfiltered.request.Jsonp.Wrapper
import unfiltered.response._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer.compact


case class Json(content: JValue) extends ChainResponse(JsonContent ~> ResponseString(compact(render(content))))

case class JsonpResponse(cb: Wrapper, content: JValue) extends ChainResponse(JsonContent ~> ResponseString(cb.wrap(compact(render(content)))))
