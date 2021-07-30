package io.taig.pygments

sealed abstract class Token extends Product with Serializable

object Token {
  final case class Comment(variant: Option[Comment.Variant]) extends Token

  object Comment {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Multiline extends Variant
      case object Single extends Variant
      case object Preproc extends Variant
      case object PreprocFile extends Variant
    }
  }

  final case class Name(variant: Option[Name.Variant]) extends Token

  object Name {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Attribute extends Variant

      case class Builtin(variant: Option[Builtin.Variant]) extends Variant

      object Builtin {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          case object Pseudo extends Variant
        }
      }

      case object Class extends Variant

      case object Decorator extends Variant

      case object Function extends Variant

      case object Tag extends Variant

      case object Variable extends Variant

      case object Other extends Variant
    }
  }

  final case class Keyword(variant: Option[Keyword.Variant]) extends Token

  object Keyword {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Declaration extends Variant
      case object Namespace extends Variant
      case object Pseudo extends Variant
      case object Reserved extends Variant
      case object Type extends Variant
    }
  }

  final case class Literal(variant: Literal.Variant) extends Token

  object Literal {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      final case class Number(variant: Option[Number.Variant]) extends Variant

      object Number {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          final case object Integer extends Variant
        }
      }

      final case class String(variant: Option[String.Variant]) extends Variant

      object String {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          final case object Double extends Variant
          final case object Escape extends Variant
          final case object Single extends Variant
        }
      }
    }
  }

  case class Operator(variant: Option[Operator.Variant]) extends Token

  object Operator {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Word extends Variant
    }
  }

  case object Other extends Token

  case object Punctuation extends Token

  case class Text(variant: Option[Text.Variant]) extends Token

  object Text {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Whitespace extends Variant
    }
  }

  val parse: String => Option[Token] = value =>
    PartialFunction.condOpt(value.substring(6)) {
      case "Comment"                => Comment(None)
      case "Comment.Multiline"      => Comment(Some(Comment.Variant.Multiline))
      case "Comment.Preproc"        => Comment(Some(Comment.Variant.Preproc))
      case "Comment.PreprocFile"    => Comment(Some(Comment.Variant.PreprocFile))
      case "Comment.Single"         => Comment(Some(Comment.Variant.Single))
      case "Name"                   => Name(None)
      case "Name.Attribute"         => Name(Some(Name.Variant.Attribute))
      case "Name.Builtin"           => Name(Some(Name.Variant.Builtin(None)))
      case "Name.Builtin.Pseudo"    => Name(Some(Name.Variant.Builtin(Some(Name.Variant.Builtin.Variant.Pseudo))))
      case "Name.Class"             => Name(Some(Name.Variant.Class))
      case "Name.Decorator"         => Name(Some(Name.Variant.Decorator))
      case "Name.Function"          => Name(Some(Name.Variant.Function))
      case "Name.Tag"               => Name(Some(Name.Variant.Tag))
      case "Name.Variable"          => Name(Some(Name.Variant.Variable))
      case "Name.Other"             => Name(Some(Name.Variant.Other))
      case "Keyword"                => Keyword(None)
      case "Keyword.Declaration"    => Keyword(Some(Keyword.Variant.Declaration))
      case "Keyword.Namespace"      => Keyword(Some(Keyword.Variant.Namespace))
      case "Keyword.Pseudo"         => Keyword(Some(Keyword.Variant.Pseudo))
      case "Keyword.Reserved"       => Keyword(Some(Keyword.Variant.Reserved))
      case "Keyword.Type"           => Keyword(Some(Keyword.Variant.Type))
      case "Literal.Number.Integer" => Literal(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Integer)))
      case "Literal.String"         => Literal(Literal.Variant.String(None))
      case "Literal.String.Double"  => Literal(Literal.Variant.String(Some(Literal.Variant.String.Variant.Double)))
      case "Literal.String.Escape"  => Literal(Literal.Variant.String(Some(Literal.Variant.String.Variant.Escape)))
      case "Literal.String.Single"  => Literal(Literal.Variant.String(Some(Literal.Variant.String.Variant.Single)))
      case "Operator"               => Operator(None)
      case "Operator.Word"          => Operator(Some(Operator.Variant.Word))
      case "Other"                  => Other
      case "Punctuation"            => Punctuation
      case "Text"                   => Text(None)
      case "Text.Whitespace"        => Text(Some(Text.Variant.Whitespace))
    }
}
