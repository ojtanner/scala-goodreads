import goodreads.{Book, Goodreads}

object Main extends App() {

  val goodreads: Goodreads = new Goodreads()

  val file = goodreads.readCSV()

  val books: List[Book] = goodreads.encode(file)

  // books.foreach(println);

  books.filter(book => book.series.getOrElse(List()).length > 1).foreach(println)
}
