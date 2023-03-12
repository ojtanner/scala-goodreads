package goodreads.reader

import scala.io.Source
import cats.effect.{IO, Resource}
import cats.data.NonEmptyList
import com.github.gekomad.ittocsv.parser.IttoCSVFormat.default
import com.github.gekomad.ittocsv.core.FromCsv._
import com.github.gekomad.ittocsv.core.ParseFailure

case class RawBook(
  bookId: String,
  title: String,
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
  publicationYear: String,
  originalPublicationYear: String,
  dateRead: String,
  dateAdded: String,
  bookshelves: String,
  bookshelvesWithPosition: String,
  exclusiveShelf: String,
  myReview: String,
  spoiler: String,
  privateNotes: String,
  readCount: String,
  ownedCopies: String
)

object CSVReader {

  private def readFile(file: String): IO[List[String]] = {
    def sourceIO: IO[Source] = IO(Source.fromResource(file))
    def readLines(source: Source): IO[List[String]] = IO(source.getLines().toList)
    def closeFile(source: Source): IO[Unit] = IO(source.close())

    val makeResource: Resource[IO, Source] = Resource.make(sourceIO)(src => closeFile(src))
    val readResource: IO[List[String]] = makeResource.use(src => readLines(src))

    readResource
  } 

  private implicit val newFormatter = default
    .withTrim(true)
    .withIgnoreEmptyLines(true)

  private def csvRowStringToList(csv: String): List[Either[NonEmptyList[ParseFailure], RawBook]] = {
    fromCsv[RawBook](csv)
  }

  def parseCsv(file: String): IO[List[RawBook]] = for {
    rowsAsStringList <- readFile(file)
    parsingResultsAsList <- IO.pure(rowsAsStringList.flatMap(csvRowStringToList))
    onlySuccessfulyParsedBooks <- IO.pure(parsingResultsAsList.collect { case Right(book) => book })
  } yield onlySuccessfulyParsedBooks

}
