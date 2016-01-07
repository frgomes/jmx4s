jmx4s
=====

Simplistic JMX for Scala.


Known issues
------------

Apparently, it does not work with case classes.


Installation
------------

Only Scala 2.11 version is supported. Binaries are available at JCenter.

.. code-block:: scala

  libraryDependencies += "info.rgomes.jmx4s" %% "jmx4s" % "0.1.0"


Example
-------

You can instrument your classes like so:

.. code-block:: scala

  package mydomain.myapp
  
  import info.rgomes.jmx4s.{Manageable, JMX}
  
  object Zoo {
    def main(args: Array[String]) {
      val zoo = Seq(
        new Animal("elephant", "Dumbo"),
        new Animal("dog",      "Pluto"),
        new Animal("cat",      "Tom"),
        new Animal("mouse",    "Jerry"))
  
      val secs = if(args.size > 0) args(0).toInt else 60
      Thread.sleep(secs*1000)
    }
  }
  
  
  class Animal(species: String, name: String) extends JMX {
    @Manageable
    override def toString(): String = s"${name} (${species})"
  
    @Manageable
    def say: String =
      species match {
        case "lion" => "roar!"
        case "dog"  => "bark! bark!"
        case "cat"  => "mehou"
        case _      => "(just dont say anything)"
      }
  
    @Manageable
    def weight: Float =
      species match {
        case "elephant" => 2000
        case "lion" => 500
        case "dog" => 15
        case "cat" => 4
        case _ => Float.NaN
      }
  }
