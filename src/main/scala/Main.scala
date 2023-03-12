import goodreads.{Book, Goodreads}
import cats.effect.IOApp
import cats.effect.IO
import cats.effect.std.Console
import cats.implicits._
import goodreads.reader.CLIReader

/*
object Main extends App() {
  val goodreads: Goodreads = new Goodreads()

  val books: List[Book] = goodreads.encodeBooksFromCsv()

  val allSeries = goodreads.booksToSeriesOfBooks(books);
  val uncompletedSeries = goodreads.getUncompletedSeries(books)
  val completedSeries = goodreads.getCompletedSeries(books)
  val unreadSeries = goodreads.getUnreadSeries(books)

  // println(goodreads.lineToBook(line))
  // books.foreach(println)

  //allSeries.foreach(println)
  // uncompletedSeries.foreach()
  // completedSeries.foreach(Series.print)
  // unreadSeries.foreach(Series.print)

  MarkdownWriter.write(uncompletedSeries, "uncompletedSeriesOliver.md")
}
*/

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {

    /*
    val books: IO[List[Book]] = Goodreads.encodeBooksFromCsv("goodreads_library_export.csv")

    for {
      bookList <- books
      _ <- bookList.traverse(IO.println)
    } yield IO.unit
     */

    val input: IO[String] = CLIReader.readUserInput()

    input.flatMap(Console[IO].println)
  }
  
}
