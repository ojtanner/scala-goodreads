package goodreads

import scala.io.Source;

class Goodreads {

  def readCSV(): List[String] =
    Source.fromFile("src/main/scala/ressources/goodreads_library_export.txt").getLines().toList;

  def encode(goodreadsCsv: List[String]): List[Book] = {
    val linesToEncode = removeHeader(goodreadsCsv);

    linesToEncode.map(lineToBook);
  }

  def getStandaloneBooks(books: List[Book]): List[Book] = books.filter(book => book.series == None)

  val generalSeriesPattern = ".*(\\(.*\\))".r
  val numberedSeries = "([\\w ]+,? #\\d(?:\\.\\d+)?)".r
  val unnumberedSeries = "\\(([\\w ]+)\\)".r

  private def removeHeader(goodreadsCsv: List[String]): List[String] = goodreadsCsv.tail;

  private val titleWithoutSeriesPattern = "(.*)\\(.*\\)".r

  private def belongsToSeries(bookTitle: String): Boolean = {
    generalSeriesPattern.matches(bookTitle);
  }

  def dropSeriesFromTitle(bookTitle: String): String = {
    try {
      val titleWithoutSeriesPattern(title) = bookTitle;

      return title;
    } catch {
      case _ => bookTitle;
    }
  }

  def extractSeriesTitleAndNumber(bookTitle: String): Option[(String, Float)] = {
    /*try {
      val generalSeriesPattern(series) = bookTitle;
      val titleAndNumber: Array[String] = series.drop(1).dropRight(1).split(", #");
      val title = titleAndNumber(0);
      val goodreadsTitleFormatsAreAbsoluteAndUtterGarbagePattern(possiblyNumber) = titleAndNumber(1);
      val number = possiblyNumber.toFloat;

      Some(title, number);
    } catch {
      case e: MatchError => None;
      case _ => None;
    }*/

    Some("a", 1.0F)
  }

  private def normalizeTitle(title: String): String = {
    val firstChar = title(0);
    val lastChar = title(title.length - 1);

    if (firstChar == '\"' && lastChar == '\"') {
      return title.drop(1).dropRight(1);
    } else {
      return title;
    }
  }

  private def lineToBook(line: String): Book = {
    val data: Array[String] = line.split("\t");

    val fullTitle = normalizeTitle(data(1));
    val seriesAndNumber = extractSeriesTitleAndNumber(fullTitle);

    val id = data(0);
    val title = dropSeriesFromTitle(fullTitle);
    val series = seriesAndNumber.map((seriesTitle, seriesNumber) => seriesTitle);
    val installment = seriesAndNumber.map((seriesTitle, seriesNumber) => seriesNumber);
    val author = data(2);
    val authorLF = data(3);
    val additionalAuthors = data(4);
    val isbn = data(5);
    val isbn13 = data(6);
    val myRating = data(7);
    val averageRating = data(8);
    val publisher = data(9);
    val binding = data(10);
    val pageNumber = data(11);
    val yearPublished = data(12);
    val originalYearPubhlished = data(13);
    val dateRead = data(14);
    val dateAdded = data(15);
    val bookshelves = data(16);
    val bookshelvesWithPosition = data(17);
    val exclusiveShelf = data(18);

    return Book(
      id,
      title,
      series,
      installment,
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
