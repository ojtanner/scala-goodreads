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
}

object Book {
  def print(book: Book): Unit = {

    val seriesInstalment: Option[Float] = book.series match {
      case None => None
      case Some(instalment) => instalment.installmentNumber
    }

    val instalmentNumberAsString: String = seriesInstalment match {
      case None => ""
      case Some(float) => float.toInt.toString
    }

    println(s"${book.exclusiveShelf}\t\tBook ${instalmentNumberAsString}: ${book.title}")
  }

  def lineToBook(line: List[String]): Book = {
    val data = line

    val fullTitle = normalizeTitle(data(1))
    val seriesAndNumber = extractSeries(fullTitle)

    val id = data(0)
    val title = dropSeriesFromTitle(fullTitle)
    val series = seriesAndNumber
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

def extractSeries(bookTitle: String): Option[SeriesInstalment] = {
    if (!generalSeriesPattern.matches(bookTitle)) {
      return None
    }

    val generalSeriesPattern(rawSeries) = bookTitle

    val series: List[String] = rawSeries.drop(1).dropRight(1).split("; (?=[^#])").toList

    if (series.length <= 0) return None

    Some(extractSeriesTitleAndNumber(series.head.trim))
  }

  private val generalSeriesPattern = ".*(\\(.*\\))".r

private def extractSeriesTitleAndNumber(seriesTitle: String): SeriesInstalment = {
    val seriesNumber = extractNumberFromSeries(seriesTitle)

    seriesNumber match {
      case Some(number) =>
        val seriesTitleWithoutNumber(title) = seriesTitle
        SeriesInstalment(title, Some(number))

      case None =>
        SeriesInstalment(seriesTitle, None)
    }
  }

  private def extractNumberFromSeries(seriesTitle: String): Option[Float] = {
    if (seriesNumber.matches(seriesTitle)) {
      val seriesNumber(number) = seriesTitle

      return Some(number.toFloat)
    }

    None
  }

  // These regex are a mess. There must be a better way without repetition
  private val seriesNumber = "[\\w'\\-’&íè:. ]+,? #?(\\d+(?:\\.\\d+)?)".r
  private val seriesTitleWithoutNumber = "([\\w'\\-’&íè:. ]+),? #?\\d+(?:\\.\\d+)?".r
  private val titleWithoutSeriesPattern = "(.*)\\(.*\\)".r

  private def dropSeriesFromTitle(bookTitle: String): String = {
    try {
      val titleWithoutSeriesPattern(title) = bookTitle

      title
    } catch {
      case _: Throwable => bookTitle
    }
  }
}
