package io.taig.pygments

sealed abstract class Token extends Product with Serializable

object Token {
  final case class Comment(variant: Comment.Variant) extends Token

  object Comment {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Single extends Variant
    }
  }

  final case class Name(variant: Option[Name.Variant]) extends Token

  object Name {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Class extends Variant
      case object Function extends Variant
    }
  }

  case object Keyword extends Token

  final case class Literal(variant: Literal.Variant) extends Token

  object Literal {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object String extends Variant
    }
  }

  case object Operator extends Token

  case object Punctuation extends Token

  case object Text extends Token

  val parse: String => Option[Token] = value =>
    PartialFunction.condOpt(value.substring(6)) {
      case "Comment.Single" => Comment(Comment.Variant.Single)
      case "Name"           => Name(None)
      case "Name.Class"     => Name(Some(Name.Variant.Class))
      case "Name.Function"  => Name(Some(Name.Variant.Function))
      case "Keyword"        => Keyword
      case "Literal.String" => Literal(Literal.Variant.String)
      case "Operator"       => Operator
      case "Punctuation"    => Punctuation
      case "Text"           => Text
    }
}
