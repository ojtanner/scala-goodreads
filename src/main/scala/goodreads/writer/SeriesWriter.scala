package goodreads.writer

import goodreads.Series
import scala.util.Try
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter

trait SeriesWriter {

  def write(seriesList: List[Series], fileName: String): Unit = {
    Try {
      val file = new File(fileName)
      val bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName)))
      bufferedWriter.write(convert(seriesList))
      bufferedWriter.close()
    }.toEither match {
      case Left(ex) => 
        println(ex)

      case Right(_) =>
        println("Print job successful")
    }
  }

  def convert(series: Series): String

  def convert(seriesList: List[Series]): String
}
