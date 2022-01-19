import goodreads.{Book, Goodreads, Series}

import scala.collection.immutable.Map

object Main extends App() {

  val goodreads: Goodreads = new Goodreads()

  val books: List[Book] = goodreads.encodeBooksFromCsv()

  val uncompletedSeries = goodreads.getUncompletedSeries(books, true)

  uncompletedSeries.foreach(Series.print)
  // uncompletedSeries.foreach(println)
}
