package goodreads

import scala.io.Source
import scala.collection.JavaConverters._
import java.io.{File, FileReader, Reader}
import org.apache.commons.csv.{CSVFormat, CSVRecord}

object CSVReader {

  def readCSV(): List[List[String]] = { 

    val fileReader = new FileReader("src/main/scala/resources/goodreads_library_export.csv")
    val rows = CSVFormat.EXCEL
      .parse(fileReader)
      .getRecords().asScala.toList
      .map((record: CSVRecord) => record.toList.asScala.toList)

    rows
  } 
}
