package goodreads

case class Book(
                 id: String,
                 title: String,
                 series: Option[List[Series]],
                 author: String,
                 authorLF: String,
                 additionalAuthors: String,
                 isbn: String,
                 isbn13: String,
                 myRating: String,
                 averageRating: String,
                 publisher: String,
                 binding: String,
                 pageNumber: String,
                 yearPublished: String,
                 originalYearPubhlished: String,
                 dateRead: String,
                 dateAdded: String,
                 bookshelves: String,
                 bookshelvesWithPosition: String,
                 exclusiveShelf: String,
               )
