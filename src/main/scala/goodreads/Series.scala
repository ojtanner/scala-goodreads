package goodreads

case class Series(
                 val title: String,
                 val books: Map[Float, Book] = Map.empty
                 )
