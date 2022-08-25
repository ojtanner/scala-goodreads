package goodreads

import scala.math.Ordered.orderingToOrdered

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
  def lineToBook(line: List[String]): Book = {
    val data = line
    val fullTitle = normalizeTitle(data(1))
    val (bookTitle, seriesInstalment) = SeriesInstalment.extractSeriesInstalment(fullTitle)

    val id = data(0)
    val title = bookTitle
    val series = seriesInstalment
    val author = data(2)
    val additionalAuthors = data(4)
    val isbn = data(5)
    val isbn13 = data(6)
    val myRating = data(7)
    val averageRating = data(8)
    val publisher = data(9)
    val pageNumber = data(11).toIntOption
    val yearPublished = data(12)
    val originalYearPublished = data(13)
    val dateRead = data(14)
    val dateAdded = data(15)
    val exclusiveShelf = data(18)

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
