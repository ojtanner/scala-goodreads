import goodreads.{Book, Goodreads, Series}

import scala.collection.immutable.Map

object Main extends App() {

  val goodreads: Goodreads = new Goodreads()

  val books: List[Book] = goodreads.encodeBooksFromCsv()

  val uncompletedSeries = goodreads.getUncompletedSeries(books)
  val completedSeries = goodreads.getCompletedSeries(books)
  val unreadSeries = goodreads.getUnreadSeries(books)

  val line = "49950044	\"Winds of Wrath (Destroyermen, #15)\"	Taylor Anderson	\"Anderson, Taylor\"				0	4.61	Ace	Kindle Edition		2020			04/09/2021	to-read	to-read (#1056)	to-read				0			0"

  // println(goodreads.lineToBook(line))

  // books.foreach(println)

  // uncompletedSeries.foreach(Series.print)
  completedSeries.foreach(Series.print)
  // unreadSeries.foreach(Series.print)
}
