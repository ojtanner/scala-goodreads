import cats.effect.{IO, IOApp}
import cats.implicits._
import goodreads.Implicits._
import goodreads.{Book, Goodreads, Series}
import goodreads.reader.CLIReader

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {

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
  }
  
}
