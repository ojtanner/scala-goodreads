import goodreads.{Book, Goodreads};

@main def main(): Unit = {

  val goodreads: Goodreads = new Goodreads();

  val file = goodreads.readCSV();

  val books: List[Book] = goodreads.encode(file);

  val unnumberedSeries = "([\\w ]+, [^#])".r

  val test = "The Vampire Genevieve, #1, The Vampire Genevieve #1, The Vampire Genevieve"
  val split = test.split(", (?=[^#])")
split.foreach(println)

  /*val test2 = "Drachenfels (The Vampire Genevieve)"

  val goodreads.generalSeriesPattern(onlySeries) = test;
  val goodreads.generalSeriesPattern(onlySeries2) = test2;

  val onlySeriesSanitzed = onlySeries.drop(1).dropRight(1);

  val iterator = goodreads.numberedSeries.findAllIn(onlySeriesSanitzed)
  iterator.toList.foreach(println)
  val goodreads.unnumberedSeries(unnumberedSeries) = onlySeries2;

  // goodreads.getStandaloneBooks(books).foreach(println)*/
}