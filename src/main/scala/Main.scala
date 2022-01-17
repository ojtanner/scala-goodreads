import goodreads.{Book, Goodreads}
import scala.collection.immutable.Map

object Main extends App() {

  val goodreads: Goodreads = new Goodreads()

  val books: List[Book] = goodreads.encodeBooksFromCsv()

  val completedSeries = goodreads.booksToSeriesOfBooks(books)

  completedSeries.foreach(println)
}
