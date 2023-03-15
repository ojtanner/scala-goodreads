package goodreads

import cats.Show

object Implicits {

  private val seriesString = (series: SeriesInstalment) => "Series: " + series.title + series.installmentNumber.map((installmentNumber: Float) => ", #" + installmentNumber.toString).getOrElse("") + ", "

  implicit val showBook: Show[Book] = Show.show((book: Book) => s"Book | Title: ${book.title}${book.series.map(seriesString).getOrElse("")}")

}
