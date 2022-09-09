package goodreads

import goodreads.writer.SeriesWriter
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File
import java.io.IOException
import scala.util.Try

object MarkdownWriter extends SeriesWriter {

  def convert(series: Series): String = {
    seriesToMarkdown(series)
  }

  def convert(seriesList: List[Series]): String = {
    seriesList
      .map(seriesToMarkdown(_))
      .fold("")(_ ++ "\n" ++ _)
  }
  
  private def bookToMarkdown(book: Book, withTable: Boolean = false): String = {
    val seriesInstalment: Option[Float] = book.series match {
      case None => None
      case Some(instalment) => instalment.installmentNumber
    }

    val instalmentNumberAsString: String = seriesInstalment match {
      case None => ""
      case Some(float) => float.toInt.toString
    }

    s"| ${book.exclusiveShelf} | Book ${instalmentNumberAsString}: ${book.title} |\n"
  }

  private def seriesToMarkdown(series: Series): String = {

    val withSeriesHeader = s"# Series: ${series.title}\n"

    val withBookTableHeader = withSeriesHeader ++ "| Shelf | Book |\n" ++ "| --- | --- | \n"

    val books = series.books.sorted.map(bookToMarkdown(_)).fold("")(_ ++ _)

    val withBooks = withBookTableHeader ++ books

    withBooks
  }
}
