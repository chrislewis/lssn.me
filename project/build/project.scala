import sbt._

class Project(info: ProjectInfo) extends AppengineProject(info) {
  val uf_version = "0.2.3"
  
  val lift_json = "net.liftweb" %% "lift-json" % "2.2-RC4" 
  
  // unfiltered
  lazy val uff = "net.databinder" %% "unfiltered-filter" % uf_version
  lazy val ufj = "net.databinder" %% "unfiltered-jetty" % uf_version
  lazy val ufjs = "net.databinder" %% "unfiltered-json" % uf_version

  // testing
  lazy val uf_spec = "net.databinder" %% "unfiltered-spec" % uf_version % "test"
  lazy val specs = "org.scala-tools.testing" %% "specs" %"1.6.6" % "test"
}
