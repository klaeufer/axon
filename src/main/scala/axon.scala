package object axon {

  trait MarkupLike extends Product {

    def name: String

    def nested: Seq[MarkupLike]

    val unbundle = MarkupLike.unbundle _

    /** Returns a sequence containing the receiver and the argument. */
    def ~(that: MarkupLike): Seq[MarkupLike] = Seq(this, that)

    protected def toXML(builder: StringBuilder): Unit = {
      builder ++= "<"
      builder ++= name
      nested foreach {
        case Attribute(k, v) =>
          builder ++= " \""
          builder ++= k
          builder ++= "\"=\""
          builder ++= v.toString
          builder ++= "\""
        case _ =>
      }
      builder ++= ">"
      nested foreach {
        case Attribute(_, _) =>
        case Text(text) => builder ++= text
        case children: Seq[MarkupLike] => children foreach { _.toXML(builder) }
        case child: MarkupLike => child.toXML(builder)
        case unknown => builder ++= "unknown child: " ; builder ++= unknown.toString
      }
      builder ++= "</"
      builder ++= name
      builder ++= ">"
    }

    def toXML(): String = {
      val result = StringBuilder.newBuilder
      toXML(result)
      result.toString
    }
  }

  object MarkupLike {

    /** Injects specific element structure into a single item in our DSL. */
    def apply(item: Any): MarkupLike = item match {
      case (key: String, value: String) => Attribute(key, value)
      case (key: String, value: Int) => Attribute(key, value)
      case (key: String, value: Double) => Attribute(key, value)
      case text: String => Text(text)
      case other: MarkupLike => other
      case _ => new Text("Error matching " + item.toString)
    }

    /** Injects specific element structure into items in our DSL. */
    def unbundle(nested: Seq[Any]): Seq[MarkupLike] = nested map apply
  }

  // We want to support common leaf attribute uses where T = String, Int, Double

  case class Attribute[T](key: String, value: T) extends MarkupLike {
    override def name = key
    override def nested = Seq.empty
  }

  // for XML character data

  case class Text(text: String) extends MarkupLike {
    override def name = "CDATA"
    override def nested = Seq.empty
  }

  /** Provides `~` to sequences of `MarkupLike` as an extension method. */
  implicit class MarkupLikeSeq(val self: Seq[MarkupLike]) extends AnyVal {
    def ~(that: MarkupLike): Seq[MarkupLike] = self :+ that
  }

  implicit def markupLikeSeq(self: MarkupLike): Seq[MarkupLike] = Seq(self)
}
