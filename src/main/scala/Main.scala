import cats.effect.{ExitCode, IO}
import cats.implicits._
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp
import goodreads.reader.CLIReader.command

object Main extends CommandIOApp(
  name = "A name", header = "A header", helpFlag = true, version = "0.1.0"
) {

  override def main: Opts[IO[ExitCode]] =
    command.map {
      result => IO.println(result).as(ExitCode.Success)
    }
}

/*
val books: IO[List[Book]] = Goodreads.encodeBooksFromCsv("goodreads_library_export.csv")

books.flatMap(_.traverse(IO.println))

val input: IO[String] = CLIReader.readUserInput()

input.flatMap(choice => {
  choice match {
    case "1" => books.flatMap(_.traverse((book: Book) => IO.println(show"$book"))) >> IO.unit
    case "2" =>
      val allSeries: IO[Map[String, Series]] = books.map(Goodreads.booksToSeriesOfBooks)
      allSeries.flatMap(_.values.toList.traverse(IO.println)) >> IO.unit
    case "3" =>
      val completedSeries: IO[List[Series]] = books.map(books => Goodreads.getCompletedSeries(books))
      completedSeries.flatMap(_.traverse(IO.println)) >> IO.unit
    case "4" =>
      val uncompletedSeries: IO[List[Series]] = books.map(books => Goodreads.getUncompletedSeries(books))
      uncompletedSeries.flatMap(_.traverse(IO.println)) >> IO.unit
    case _ => IO.defer(IO.println("Wrong input"))
  }
})

 */

