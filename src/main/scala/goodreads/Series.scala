package goodreads

case class Series(
                 title: String,
                 books: List[Book]
                 )

object Series {
  def print(series: Series): Unit = {
    println(s"Series: ${series.title}")
    println(s"*-*-*-*-*-*")

    val sortedPrimaryWorks: List[Book] = series.books.sorted

    sortedPrimaryWorks.foreach(Book.print)
    println()
  }
}
