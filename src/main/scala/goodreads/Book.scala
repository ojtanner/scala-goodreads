package goodreads

case class Book(
                 val id: String,
                 val title: String,
                 val series: Option[List[Series]],
                 val author: String,
                 val authorLF: String,
                 val additionalAuthors: String,
                 val isbn: String,
                 val isbn13: String,
                 val myRating: String,
                 val averageRating: String,
                 val publisher: String,
                 val binding: String,
                 val pageNumber: String,
                 val yearPublished: String,
                 val originalYearPubhlished: String,
                 val dateRead: String,
                 val dateAdded: String,
                 val bookshelves: String,
                 val bookshelvesWithPosition: String,
                 val exclusiveShelf: String,
               )
