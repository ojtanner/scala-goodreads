package goodreads.reader

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.std.Console

object CLIReader {

  def readUserInput(prompt: String = initialPrompt): IO[String] = {
    val userInput: IO[String] = readFromStdIn(prompt)
    val verifiedInput: IO[Either[String, String]] = userInput.flatMap(input => IO(verifyUserInput(input)))

    for {
      either <- verifiedInput
      input <- either match {
        case Left(error) => readUserInput(s"$error. Try again: ")
        case Right(input) => IO(input)
      }
    } yield input
  }

  private def readFromStdIn(prompt: String): IO[String] = {
    for {
      _ <- Console[IO].print(prompt)
      input <- Console[IO].readLine
    } yield input
  }

  private val initialPrompt: String =
    """Please select the desired output by providing the corresponding number:
      |1. All books in your Goodreads export
      |2. All series in your Goodreads export
      |3. All finished series
      |4. All unfinished series
      |
      |Your choice: """.stripMargin

  private val permittedInputs: NonEmptyList[String] = NonEmptyList.of("1", "2", "3", "4")

  private def verifyUserInput(userInput: String): Either[String, String] = {
    if (permittedInputs.exists(_.equals(userInput))) {
      Right(userInput)
    } else {
      Left(s"Input $userInput is not permitted!")
    }
  }
}
