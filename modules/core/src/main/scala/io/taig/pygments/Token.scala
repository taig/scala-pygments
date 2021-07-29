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
      case object Attribute extends Variant
      case object Class extends Variant
      case object Function extends Variant
      case object Other extends Variant
    }
  }

  final case class Keyword(variant: Option[Keyword.Variant]) extends Token

  object Keyword {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Declaration extends Variant
      case object Type extends Variant
    }
  }

  final case class Literal(variant: Literal.Variant) extends Token

  object Literal {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      final case class String(variant: Option[String.Variant]) extends Variant

      object String {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          final case object Double extends Variant
        }
      }
    }
  }

  case object Operator extends Token

  case object Punctuation extends Token

  case object Text extends Token

  val parse: String => Option[Token] = value =>
    PartialFunction.condOpt(value.substring(6)) {
      case "Comment.Single"        => Comment(Comment.Variant.Single)
      case "Name"                  => Name(None)
      case "Name.Attribute"        => Name(Some(Name.Variant.Attribute))
      case "Name.Class"            => Name(Some(Name.Variant.Class))
      case "Name.Function"         => Name(Some(Name.Variant.Function))
      case "Name.Other"            => Name(Some(Name.Variant.Other))
      case "Keyword"               => Keyword(None)
      case "Keyword.Declaration"   => Keyword(Some(Keyword.Variant.Declaration))
      case "Keyword.Type"          => Keyword(Some(Keyword.Variant.Type))
      case "Literal.String"        => Literal(Literal.Variant.String(None))
      case "Literal.String.Double" => Literal(Literal.Variant.String(Some(Literal.Variant.String.Variant.Double)))
      case "Operator"              => Operator
      case "Punctuation"           => Punctuation
      case "Text"                  => Text
    }
}
