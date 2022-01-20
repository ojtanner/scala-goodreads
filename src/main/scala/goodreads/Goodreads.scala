package goodreads

import scala.io.Source
import scala.collection.immutable.Map
import scala.util.chaining.scalaUtilChainingOps

class Goodreads {

  def encodeBooksFromCsv(path: String = ""): List[Book] = {
    val file = readCSV()

    encode(file)
  }

  def getReadBooks(books: List[Book]): List[Book] =
    books.filter(book => book.exclusiveShelf == "read")

  def getUnreadBooks(books: List[Book]): List[Book] =
    books.filter(book => book.exclusiveShelf == "to-read")

  def booksToSeriesOfBooks(books: List[Book]): Map[String, Series] =
    books.foldRight(Map.empty[String, Series])(addBookSeriesToMap)

  def getCompletedSeries(books: List[Book], includeSecondaryWorks: Boolean = false): List[Series] = {
    val seriesMap: Map[String, Series] = booksToSeriesOfBooks(books)

    seriesMap
      .toList
      .map(_._2)
      .pipe((allSeries: List[Series]) => allSeries.map((series: Series) => filterOutSecondaryWorks(series, includeSecondaryWorks)))
      .filter(series => series.books.forall(book => book.exclusiveShelf == "read"))
  }

  def getUncompletedSeries(books: List[Book], includeSecondaryWorks: Boolean = false): List[Series] = {
    val seriesMap: Map[String, Series] = booksToSeriesOfBooks(books)

    seriesMap
      .toList
      .map(_._2)
      .pipe((allSeries: List[Series]) => allSeries.map((series: Series) => filterOutSecondaryWorks(series, includeSecondaryWorks)))
      .filter(series => series.books.exists(book => book.exclusiveShelf == "read"))
      .filter(series => series.books.exists(book => book.exclusiveShelf == "to-read"))
  }

  def getUnreadSeries(books: List[Book], includeSecondaryWorks: Boolean = false): List[Series] = {
    val seriesMap: Map[String, Series] = booksToSeriesOfBooks(books)

    seriesMap
      .toList
      .map(_._2)
      .pipe((allSeries: List[Series]) => allSeries.map((series: Series) => filterOutSecondaryWorks(series, includeSecondaryWorks)))
      .filter(series => series.books.forall(book => book.exclusiveShelf == "to-read"))
  }

  private def filterOutSecondaryWorks(series: Series, includeSecondaryWorks: Boolean = false): Series = {
    if (!includeSecondaryWorks) {
      val primaryWorks = series.books.filter(book => bookIsPrimaryWorkOfSeries(book, series))
      Series(series.title, primaryWorks)
    } else {
      series
    }
  }

  def bookIsPrimaryWorkOfSeries(book: Book, series: Series): Boolean = {
    book.series match {
      case None =>
        false

      case Some(seriesInstalment: SeriesInstalment) =>
        seriesInstalment.title == series.title &&
          (seriesInstalment.installmentNumber.isEmpty ||
            (seriesInstalment.installmentNumber.isDefined && seriesInstalment.installmentNumber.get == Math.floor(seriesInstalment.installmentNumber.get)))

    }
  }

  private def addBookSeriesToMap(book: Book, seriesMap: Map[String, Series]): Map[String, Series] = {
    book.series match {
      case None =>
        seriesMap

      case Some(series) =>
        val maybeSeries = seriesMap.get(series.title)

        maybeSeries match {
          case None =>
            val newSeries: Series = Series(series.title, List(book))
            val updatedSeriesMap: Map[String, Series] = seriesMap + (series.title -> newSeries)
            updatedSeriesMap

          case Some(series) =>
            val currentBooksOfSeries: List[Book] = series.books
            val updatedBooksOfSeries: List[Book] = currentBooksOfSeries.appended(book)
            val updatedSeries: Series = Series(series.title, updatedBooksOfSeries)
            val updatedSeriesMap: Map[String, Series] = seriesMap + (series.title -> updatedSeries)
            updatedSeriesMap
        }
    }
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
  private val seriesNumber = "[\\w'\\-’&íè:. ]+,? #?(\\d+(?:\\.\\d+)?)".r
  private val seriesTitleWithoutNumber = "([\\w'\\-’&íè:. ]+),? #?\\d+(?:\\.\\d+)?".r

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

  def extractAllSeries(bookTitle: String): Option[SeriesInstalment] = {
    if (!generalSeriesPattern.matches(bookTitle)) {
      return None
    }

    val generalSeriesPattern(rawSeries) = bookTitle

    val series: List[String] = rawSeries.drop(1).dropRight(1).split("; (?=[^#])").toList

    if (series.length <= 0) return None

    Some(extractSeriesTitleAndNumber(series.head.trim))
  }

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

  private def normalizeTitle(title: String): String = {
    val firstChar = title(0)
    val lastChar = title(title.length - 1)

    if (firstChar == '\"' && lastChar == '\"') {
      title.drop(1).dropRight(1)
    } else {
      title
    }
  }

  def lineToBook(line: String): Book = {
    val data: Array[String] = line.split("\t")

    val fullTitle = normalizeTitle(data(1))
    val seriesAndNumber = extractAllSeries(fullTitle)

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
    val originalYearPubhlished = data(13)
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
      originalYearPubhlished,
      dateRead,
      dateAdded,
      exclusiveShelf,
    )
  }
}
