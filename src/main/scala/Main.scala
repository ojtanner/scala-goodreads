import goodreads.{Book, Goodreads}

object Main extends App() {

  val goodreads: Goodreads = new Goodreads()

  val books: List[Book] = goodreads.encodeBooksFromCsv()
}
