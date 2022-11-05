package goodreads

import scala.math.Ordered.orderingToOrdered
import goodreads.reader.RawBook

case class Book(
                 id: String,
                 title: String,
                 series: Option[SeriesInstalment],
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
               ) extends Ordered[Book] {

  def compare(that: Book): Int =
    this.series.getOrElse(SeriesInstalment("", Some(0.0f))).installmentNumber compare
      that.series.getOrElse(SeriesInstalment("", Some(0.0f))).installmentNumber


  override def toString(): String = {

    val seriesInstalment: Option[Float] = this.series match {
      case None => None
      case Some(instalment) => instalment.installmentNumber
    }

    val instalmentNumberAsString: String = seriesInstalment match {
      case None => ""
      case Some(float) => float.toInt.toString
    }

    s"| ${this.exclusiveShelf} | Book ${instalmentNumberAsString}: ${this.title} |\n"
  }
}

object Book {
  def fromRawBook(rawBook: RawBook): Book = {
    val fullTitle = normalizeTitle(rawBook.title)
    val (bookTitle, seriesInstalment) = SeriesInstalment.extractSeriesInstalment(fullTitle)

    val id = rawBook.bookId
    val title = bookTitle
    val series = seriesInstalment
    val author = rawBook.author
    val additionalAuthors = rawBook.additionalAuthors
    val isbn = rawBook.isbn
    val isbn13 = rawBook.isbn13
    val myRating = rawBook.myRating
    val averageRating = rawBook.averageRating
    val publisher = rawBook.publisher
    val pageNumber = rawBook.pageNumber.toIntOption
    val yearPublished = rawBook.publicationYear
    val originalYearPublished = rawBook.originalPublicationYear
    val dateRead = rawBook.dateRead
    val dateAdded = rawBook.dateAdded
    val exclusiveShelf = rawBook.exclusiveShelf

    Book(
      id,
      title,
      series,
      author,
      additionalAuthors,
      isbn,
      isbn13,
      myRating,
      averageRating,
      publisher,
      pageNumber,
      yearPublished,
      originalYearPublished,
      dateRead,
      dateAdded,
      exclusiveShelf,
    )
  }

  private def normalizeTitle(title: String): String = {
    val firstChar = title(0)
    val lastChar = title(title.length - 1)

    if (firstChar == '\"' && lastChar == '\"') {
      title.drop(1).dropRight(1)
    } else {
      title
    }
  }
}
