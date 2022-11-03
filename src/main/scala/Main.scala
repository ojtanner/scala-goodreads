import goodreads.{Book, Goodreads, Series, CSVReader}
import goodreads.reader.{CSVReader => EffectReader}
import goodreads.MarkdownWriter
import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import cats.implicits._

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

  override def run: IO[Unit] = EffectReader.parseCsv("goodreads_library_export.csv")
  
}
