package goodreads

case class SeriesInstalment(
                 title: String,
                 installmentNumber: Option[Float]
                 )

object SeriesInstalment {
  // These regex are a mess. There must be a better way without repetition
  private val generalSeriesPattern = ".*(\\(.*\\))".r
  private val seriesNumber = "[\\w'\\-’&íè:. ]+,? #?(\\d+(?:\\.\\d+)?)".r
  private val seriesTitleWithoutNumber = "([\\w'\\-’&íè:. ]+),? #?\\d+(?:\\.\\d+)?".r
  private val titleWithoutSeriesPattern = "(.*)\\(.*\\)".r

  def extractSeriesInstalment(bookTitle: String): (String, Option[SeriesInstalment]) = {
    if (!generalSeriesPattern.matches(bookTitle)) {
      return (bookTitle, None)
    }

    val generalSeriesPattern(rawSeries) = bookTitle

    val series: List[String] = rawSeries.drop(1).dropRight(1).split("; (?=[^#])").toList
    val bookTitleWithoutSeries = dropSeriesFromTitle(bookTitle)

    if (series.length <= 0) return (bookTitle, None)

    (bookTitleWithoutSeries, Some(extractSeriesTitleAndNumber(series.head.trim)))
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

  private def dropSeriesFromTitle(bookTitle: String): String = {
    try {
      val titleWithoutSeriesPattern(title) = bookTitle

      title
    } catch {
      case _: Throwable => bookTitle
    }
  }
}