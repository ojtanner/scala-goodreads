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
}
