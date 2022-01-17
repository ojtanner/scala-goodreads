package goodreads

case class Book(
                 id: String,
                 title: String,
                 series: Option[List[SeriesInstalment]],
                 author: String,
                 additionalAuthors: String,
                 isbn: String,
                 isbn13: String,
                 myRating: String,
                 averageRating: String,
                 publisher: String,
                 pageNumber: Option[Int],
                 yearPublished: String,
                 originalYearPubhlished: String,
                 dateRead: String,
                 dateAdded: String,
                 exclusiveShelf: String,
               )
