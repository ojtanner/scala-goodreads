package goodreads.reader

import cats.data.Validated
import cats.implicits._
import com.monovore.decline.Opts

import java.nio.file.Path

/**
 * What does the user need ?
 *
 * Command
 * -------
 *
 * Options:
 *
 * (optional) --file / -f : specify the file location. Default will search in cwd.
 *
 * Arguments:
 *
 * [books | series] : choose subcommand
 *
 * Subcommand books
 * ----------------
 *
 * Options:
 *
 * (optional) --output-format / -o : specify the output format. Default will be unformatted string.
 *
 * Arguments:
 *
 * [list] : lists all books in the exported csv
 *
 * Subcommand series
 * -----------------
 *
 * Options:
 *
 * (optional) --output-format / -o : specify the output format. Default will be unformatted string.
 *
 * Flags:
 *
 * (optional) --no-books : does not print the books of each series
 *
 * (optional) --finished : lists only finished series
 *
 * (optional) --unfinished : lists only unfinished series
 *
 * Arguments:
 *
 * [list] : lists all series in the exported csv
 *
 */
object CLIReader {

  private val fileOption: Opts[Path] = Opts.option[Path](
    long = "file",
    short = "f",
    metavar = "path",
    help = "Point to the export. Looks for TODO in current working directory of none is provided."
  )

  private val outputFormatOption: Opts[() => OutputFormat] =
    Opts.option[String](
      long = "output-format",
      short = "o",
      metavar = "output format",
      help = "Specify the output format. Defaults to plaintext."
    ).mapValidated {
      case "plaintext" | "txt" => Validated.valid(PlaintextFormat)
      case "markdown" | "md" => Validated.valid(MarkdownFormat)
      case _ => Validated.invalidNel("Invalid argument. Must be plaintext or markdown.")
    }.withDefault(PlaintextFormat)

  // Split into Book Subcommand and Series Subcommand
  private val booksOrSeriesArgument: Opts[() => SubcommandType] =
    Opts.argument[String]("book or series")
      .mapValidated {
        case "books" => Validated.valid(BookSubcommand)
        case "series" => Validated.valid(SeriesSubcommand)
        case _ => Validated.invalidNel("Invalid argument. Must be books or series")
      }

  val command: Opts[String] = (fileOption, outputFormatOption, booksOrSeriesArgument).mapN { (file, format, argument) => s"Got $file , $format and $argument" }
}

sealed trait SubcommandType

case class BookSubcommand() extends SubcommandType

case class SeriesSubcommand() extends SubcommandType

sealed trait OutputFormat

case class MarkdownFormat() extends OutputFormat

case class PlaintextFormat() extends OutputFormat
