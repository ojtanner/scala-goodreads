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
    println(s"${book.exclusiveShelf}\t\tBook ${book.series.getOrElse(SeriesInstalment("", Some(0))).installmentNumber.getOrElse(0.0f).toInt}: ${book.title}")

  }
}
