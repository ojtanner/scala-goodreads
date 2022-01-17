package goodreads

import scala.io.Source

class Goodreads {

  def encodeBooksFromCsv(path: String = ""): List[Book] = {
    val file = readCSV()

    encode(file)
  }

  private def encode(goodreadsCsv: List[String]): List[Book] = {

    val linesToEncode = removeHeader(goodreadsCsv)

    linesToEncode.map(lineToBook)
  }

  def getStandaloneBooks(books: List[Book]): List[Book] = books.filter(book => book.series.isEmpty)

  private def readCSV(): List[String] = {
    val source = Source.fromFile("src/main/scala/ressources/goodreads_library_export.txt")
    val file = source.getLines().toList
    source.close()

    file
  }

  private val generalSeriesPattern = ".*(\\(.*\\))".r
  private val seriesNumber = "[\\w ]+,? #(\\d(?:\\.\\d+)?)".r
  private val seriesTitleWithoutNumber = "([\\w ]+),? #\\d(?:\\.\\d+)?".r

  private def removeHeader(goodreadsCsv: List[String]): List[String] = goodreadsCsv.tail

  private val titleWithoutSeriesPattern = "(.*)\\(.*\\)".r

  private def dropSeriesFromTitle(bookTitle: String): String = {
    try {
      val titleWithoutSeriesPattern(title) = bookTitle

      title
    } catch {
      case _ => bookTitle
    }
  }

  private def extractAllSeries(bookTitle: String): Option[List[Series]] = {
    if (!generalSeriesPattern.matches(bookTitle)) {
      return None
    }

    val generalSeriesPattern(rawSeries) = bookTitle

    val series = rawSeries.drop(1).dropRight(1).split("; (?=[^#])").toList

    if (series.length <= 0) return None

    Some(series.map(extractSeriesTitleAndNumber))
  }

  private def extractSeriesTitleAndNumber(seriesTitle: String): Series = {
    val seriesNumber = extractNumberFromSeries(seriesTitle)

    seriesNumber match {
      case Some(number) =>
        val seriesTitleWithoutNumber(title) = seriesTitle
        Series(title, Some(number))

      case None =>
        Series(seriesTitle, None)
    }
  }

  private def extractNumberFromSeries(seriesTitle: String): Option[Float] = {
    if (seriesNumber.matches(seriesTitle)) {
      val seriesNumber(number) = seriesTitle

      return Some(number.toFloat)
    }

    None
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

  private def lineToBook(line: String): Book = {
    val data: Array[String] = line.split("\t")

    val fullTitle = normalizeTitle(data(1))
    val seriesAndNumber = extractAllSeries(fullTitle)

    val id = data(0)
    val title = dropSeriesFromTitle(fullTitle)
    val series = seriesAndNumber
    val author = data(2)
    val authorLF = data(3)
    val additionalAuthors = data(4)
    val isbn = data(5)
    val isbn13 = data(6)
    val myRating = data(7)
    val averageRating = data(8)
    val publisher = data(9)
    val binding = data(10)
    val pageNumber = data(11)
    val yearPublished = data(12)
    val originalYearPubhlished = data(13)
    val dateRead = data(14)
    val dateAdded = data(15)
    val bookshelves = data(16)
    val bookshelvesWithPosition = data(17)
    val exclusiveShelf = data(18)

    Book(
      id,
      title,
      series,
      author,
      authorLF,
      additionalAuthors,
      isbn,
      isbn13,
      myRating,
      averageRating,
      publisher,
      binding,
      pageNumber,
      yearPublished,
      originalYearPubhlished,
      dateRead,
      dateAdded,
      bookshelves,
      bookshelvesWithPosition,
      exclusiveShelf,
    )
  }
}
