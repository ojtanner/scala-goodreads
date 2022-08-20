import goodreads.{Book, Goodreads, Series, CSVReader}

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
  uncompletedSeries.foreach(Series.print)
  // completedSeries.foreach(Series.print)
  // unreadSeries.foreach(Series.print)
}
