package goodreads

case class Series(
                 title: String,
                 books: List[Book]
                 ) {
                  
  override def toString(): String = {

    val withSeriesHeader = s"# Series: ${this.title}\n"

    val withBookTableHeader = withSeriesHeader ++ "| Shelf | Book |\n" ++ "| --- | --- | \n"

    val books = this.books.sorted.map(_.toString()).fold("")(_ ++ _)

    val withBooks = withBookTableHeader ++ books

    withBooks
  }
                 }

object Series {
}
