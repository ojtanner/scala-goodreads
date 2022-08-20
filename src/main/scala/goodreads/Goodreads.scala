package goodreads

import scala.io.Source
import scala.collection.immutable.Map
import scala.util.chaining.scalaUtilChainingOps

class Goodreads {

  def encodeBooksFromCsv(path: String = ""): List[Book] = {
    val csvRows = CSVReader.readCSV()

    encode(csvRows)
  }

  def getStandaloneBooks(books: List[Book]): List[Book] = 
    books.filter(book => book.series.isEmpty)

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

  private def bookIsPrimaryWorkOfSeries(book: Book, series: Series): Boolean = {
    book.series match {
      case None =>
        false

      /* Either the whole series has no numbers and therefore we have no way of distinguishing primary works from secondary works
         Or the number is a round number and therefore a primary work
       */
      case Some(seriesInstalment: SeriesInstalment) =>
          seriesInstalment.installmentNumber.isEmpty ||
            (seriesInstalment.installmentNumber.isDefined && seriesInstalment.installmentNumber.get == Math.floor(seriesInstalment.installmentNumber.get))
    }
  }

  private def addBookSeriesToMap(book: Book, seriesMap: Map[String, Series]): Map[String, Series] = {
    book.series match {
      case None =>
        seriesMap

      case Some(series) =>
        val seriesInMap = seriesMap.get(series.title)

        seriesInMap match {
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

  private def encode(goodreadsCsv: List[List[String]]): List[Book] = {

    val linesToEncode = removeHeader(goodreadsCsv)

    linesToEncode.map(Book.lineToBook)
  }

  private def removeHeader(goodreadsCsv: List[List[String]]): List[List[String]] = goodreadsCsv.tail

}
