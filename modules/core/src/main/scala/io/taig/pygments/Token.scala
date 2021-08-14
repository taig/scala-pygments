package io.taig.pygments

sealed abstract class Token extends Product with Serializable

object Token {
  final case class Comment(variant: Option[Comment.Variant]) extends Token

  object Comment {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Hashbang extends Variant
      case object Multiline extends Variant
      case object Single extends Variant
      case object Preproc extends Variant
      case object PreprocFile extends Variant
      case object Special extends Variant

      val All: Set[Variant] = Set(Hashbang, Multiline, Single, Preproc, PreprocFile, Special)
    }

    val All: Set[Comment] = Set(Comment(None)) ++ Variant.All.map(variant => Comment(Some(variant)))
  }

  case object Error extends Token

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

          val All: Set[Variant] = Set(Pseudo)
        }

        val All: Set[Builtin] = Set(Builtin(None)) ++ Variant.All.map(variant => Builtin(Some(variant)))
      }

      case object Class extends Variant

      case object Constant extends Variant

      case object Entity extends Variant

      case object Decorator extends Variant

      case object Exception extends Variant

      final case class Function(variant: Option[Function.Variant]) extends Variant

      object Function {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          case object Magic extends Variant

          val All: Set[Variant] = Set(Magic)
        }

        val All: Set[Function] = Set(Function(None)) ++ Variant.All.map(variant => Function(Some(variant)))
      }

      case object Label extends Variant

      case object Namespace extends Variant

      case object Property extends Variant

      case object Tag extends Variant

      final case class Variable(variant: Option[Variable.Variant]) extends Variant

      object Variable {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          case object Class extends Variant
          case object Global extends Variant
          case object Instance extends Variant
          case object Magic extends Variant

          val All: Set[Variant] = Set(Class, Global, Instance, Magic)
        }

        val All: Set[Variable] = Set(Variable(None)) ++ Variant.All.map(variant => Variable(Some(variant)))
      }

      case object Other extends Variant

      val All: Set[Variant] = Set(
        Attribute,
        Class,
        Constant,
        Entity,
        Decorator,
        Exception,
        Label,
        Namespace,
        Property,
        Tag,
        Other
      ) ++ Builtin.All ++ Function.All ++ Variable.All
    }

    val All: Set[Name] = Set(Name(None)) ++ Variant.All.map(variant => Name(Some(variant)))
  }

  final case class Keyword(variant: Option[Keyword.Variant]) extends Token

  object Keyword {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Constant extends Variant
      case object Declaration extends Variant
      case object Namespace extends Variant
      case object Pseudo extends Variant
      case object Reserved extends Variant
      case object Type extends Variant

      val All: Set[Variant] = Set(Constant, Declaration, Namespace, Pseudo, Reserved, Type)
    }

    val All: Set[Keyword] = Set(Keyword(None)) ++ Variant.All.map(variant => Keyword(Some(variant)))
  }

  final case class Literal(variant: Option[Literal.Variant]) extends Token

  object Literal {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      final case class Number(variant: Option[Number.Variant]) extends Variant

      object Number {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          case object Bin extends Variant

          case object Float extends Variant

          case object Hex extends Variant

          case class Integer(variant: Option[Integer.Variant]) extends Variant

          object Integer {
            sealed abstract class Variant extends Product with Serializable

            object Variant {
              case object Long extends Variant

              val All: Set[Variant] = Set(Long)
            }

            val All: Set[Integer] = Set(Integer(None)) ++ Variant.All.map(variant => Integer(Some(variant)))
          }

          case object Oct extends Variant

          val All: Set[Variant] = Set(Bin, Float, Hex, Oct) ++ Integer.All
        }

        val All: Set[Number] = Set(Number(None)) ++ Variant.All.map(variant => Number(Some(variant)))
      }

      final case class String(variant: Option[String.Variant]) extends Variant

      object String {
        sealed abstract class Variant extends Product with Serializable

        object Variant {
          case object Affix extends Variant
          case object Backtick extends Variant
          case object Char extends Variant
          case object Delimiter extends Variant
          case object Doc extends Variant
          case object Double extends Variant
          case object Escape extends Variant
          case object Heredoc extends Variant
          case object Other extends Variant
          case object Interpol extends Variant
          case object Regex extends Variant
          case object Single extends Variant
          case object Symbol extends Variant

          val All: Set[Variant] =
            Set(Affix, Backtick, Char, Delimiter, Doc, Double, Escape, Heredoc, Other, Interpol, Regex, Single, Symbol)
        }

        val All: Set[String] = Set(String(None)) ++ Variant.All.map(variant => String(Some(variant)))
      }

      val All: Set[Variant] = Number.All ++ String.All
    }

    val All: Set[Literal] = Set(Literal(None)) ++ Variant.All.map(variant => Literal(Some(variant)))
  }

  case class Operator(variant: Option[Operator.Variant]) extends Token

  object Operator {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Word extends Variant

      val All: Set[Variant] = Set(Word)
    }

    val All: Set[Operator] = Set(Operator(None)) ++ Variant.All.map(variant => Operator(Some(variant)))
  }

  case object Other extends Token

  case object Punctuation extends Token

  case class Text(variant: Option[Text.Variant]) extends Token

  object Text {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      case object Whitespace extends Variant

      val All: Set[Variant] = Set(Whitespace)
    }

    val All: Set[Text] = Set(Text(None)) ++ Variant.All.map(variant => Text(Some(variant)))
  }

  val All: Set[Token] = Comment.All ++ Set(Error) ++ Name.All ++ Keyword.All ++ Literal.All ++ Operator.All ++ Set(
    Other,
    Punctuation
  ) ++ Text.All

  val parse: String => Option[Token] = value =>
    PartialFunction.condOpt(value.substring(6)) {
      // format: off
      case "Comment"                     => Comment(None)
      case "Comment.Hashbang"            => Comment(Some(Comment.Variant.Hashbang))
      case "Comment.Multiline"           => Comment(Some(Comment.Variant.Multiline))
      case "Comment.Preproc"             => Comment(Some(Comment.Variant.Preproc))
      case "Comment.PreprocFile"         => Comment(Some(Comment.Variant.PreprocFile))
      case "Comment.Single"              => Comment(Some(Comment.Variant.Single))
      case "Comment.Special"             => Comment(Some(Comment.Variant.Special))
      case "Error"                       => Error
      case "Keyword"                     => Keyword(None)
      case "Keyword.Constant"            => Keyword(Some(Keyword.Variant.Constant))
      case "Keyword.Declaration"         => Keyword(Some(Keyword.Variant.Declaration))
      case "Keyword.Namespace"           => Keyword(Some(Keyword.Variant.Namespace))
      case "Keyword.Pseudo"              => Keyword(Some(Keyword.Variant.Pseudo))
      case "Keyword.Reserved"            => Keyword(Some(Keyword.Variant.Reserved))
      case "Keyword.Type"                => Keyword(Some(Keyword.Variant.Type))
      case "Literal"                     => Literal(None)
      case "Literal.Number"              => Literal(Some(Literal.Variant.Number(None)))
      case "Literal.Number.Bin"          => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Bin))))
      case "Literal.Number.Float"        => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Float))))
      case "Literal.Number.Hex"          => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Hex))))
      case "Literal.Number.Integer"      => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Integer(None)))))
      case "Literal.Number.Integer.Long" => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Integer(Some(Literal.Variant.Number.Variant.Integer.Variant.Long))))))
      case "Literal.Number.Oct"          => Literal(Some(Literal.Variant.Number(Some(Literal.Variant.Number.Variant.Oct))))
      case "Literal.String"              => Literal(Some(Literal.Variant.String(None)))
      case "Literal.String.Affix"        => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Affix))))
      case "Literal.String.Backtick"     => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Backtick))))
      case "Literal.String.Char"         => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Char))))
      case "Literal.String.Delimiter"    => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Delimiter))))
      case "Literal.String.Doc"          => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Doc))))
      case "Literal.String.Double"       => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Double))))
      case "Literal.String.Escape"       => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Escape))))
      case "Literal.String.Heredoc"      => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Heredoc))))
      case "Literal.String.Interpol"     => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Interpol))))
      case "Literal.String.Other"        => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Other))))
      case "Literal.String.Regex"        => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Regex))))
      case "Literal.String.Single"       => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Single))))
      case "Literal.String.Symbol"       => Literal(Some(Literal.Variant.String(Some(Literal.Variant.String.Variant.Symbol))))
      case "Name"                        => Name(None)
      case "Name.Attribute"              => Name(Some(Name.Variant.Attribute))
      case "Name.Builtin"                => Name(Some(Name.Variant.Builtin(None)))
      case "Name.Builtin.Pseudo"         => Name(Some(Name.Variant.Builtin(Some(Name.Variant.Builtin.Variant.Pseudo))))
      case "Name.Class"                  => Name(Some(Name.Variant.Class))
      case "Name.Constant"               => Name(Some(Name.Variant.Constant))
      case "Name.Decorator"              => Name(Some(Name.Variant.Decorator))
      case "Name.Entity"                 => Name(Some(Name.Variant.Entity))
      case "Name.Exception"              => Name(Some(Name.Variant.Exception))
      case "Name.Function"               => Name(Some(Name.Variant.Function(None)))
      case "Name.Function.Magic"         => Name(Some(Name.Variant.Function(Some(Name.Variant.Function.Variant.Magic))))
      case "Name.Label"                  => Name(Some(Name.Variant.Label))
      case "Name.Namespace"              => Name(Some(Name.Variant.Namespace))
      case "Name.Other"                  => Name(Some(Name.Variant.Other))
      case "Name.Property"               => Name(Some(Name.Variant.Property))
      case "Name.Tag"                    => Name(Some(Name.Variant.Tag))
      case "Name.Variable"               => Name(Some(Name.Variant.Variable(None)))
      case "Name.Variable.Class"         => Name(Some(Name.Variant.Variable(Some(Name.Variant.Variable.Variant.Class))))
      case "Name.Variable.Global"        => Name(Some(Name.Variant.Variable(Some(Name.Variant.Variable.Variant.Global))))
      case "Name.Variable.Instance"      => Name(Some(Name.Variant.Variable(Some(Name.Variant.Variable.Variant.Instance))))
      case "Name.Variable.Magic"         => Name(Some(Name.Variant.Variable(Some(Name.Variant.Variable.Variant.Magic))))
      case "Operator"                    => Operator(None)
      case "Operator.Word"               => Operator(Some(Operator.Variant.Word))
      case "Other"                       => Other
      case "Punctuation"                 => Punctuation
      case "Text"                        => Text(None)
      case "Text.Whitespace"             => Text(Some(Text.Variant.Whitespace))
      // format: on
    }
}
