
import axon._

object X extends App {

  // All of this is to support the simple XML document I described in our office.

  // TODO macros will probably replace all 3 of these with 3 lines of code!

  case class Complex(name: String, nested: Seq[MarkupLike]) extends MarkupLike

  object Complex {

    // We cannot have overloading involving vararg and Seq because
    // they are the same after type erasure.
    // We need these overloaded versions for specific number of arguments.
    // Without them, we would have to write `Complex(Seq(...))` or,
    // if we have `apply` with a vararg instead, `Complex { (...): _* }`.
    // The latter is definitely uglier.

    def apply(n1: Any): Complex = apply(Seq(n1))

    def apply(n1: Any, n2: Any): Complex = apply(Seq(n1, n2))

    def apply(n1: Any, n2: Any, n3: Any): Complex = apply(Seq(n1, n2, n3))

    def apply(nested: Seq[Any]): Complex =
      new Complex("complex", MarkupLike.unbundle(nested))
  }

  case class Real(name: String, nested: Seq[MarkupLike]) extends MarkupLike

  object Real {

    def apply(nested: Any*) =
      new Real("real", MarkupLike.unbundle(nested))
  }

  case class Imag(name: String, nested: Seq[MarkupLike]) extends MarkupLike

  object Imag {

    def apply(nested: Any*) =
      new Real("imaginary", MarkupLike.unbundle(nested))
  }

  // This is the composite object style

  val myAppMarkup = Complex(
    Real("value" -> 2.5),
    Imag("value" -> 3.5),
    Imag("yahoo" -> 3.5)
  )

  // This is JSON style

  val myAltMarkup = Complex {
    Real {
      "value" -> 2.5
    } ~
    Imag {
      "value" -> 3.5
    } ~
    Imag {
      "yahoo" -> 3.5
    }
  }

  // TODO: This just prints the AST. We will have a toXML or what not.

  println(myAppMarkup)
  println(myAltMarkup)
  assert { myAppMarkup == myAltMarkup }
  println(myAppMarkup.toXML)
  println(myAltMarkup.toXML)
}
