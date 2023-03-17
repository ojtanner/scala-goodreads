package goodreads.reader

/**
 * What does the user need ?
 *
 * Command
 * -------
 *
 * Options:
 *
 * (optional) --file / -f : specify the file location. Default will search in cwd.
 *
 * Arguments:
 *
 * [books | series] : choose subcommand
 *
 * Subcommand books
 * ----------------
 *
 * Options:
 *
 * (optional) --output-format / -o : specify the output format. Default will be unformatted string.
 *
 * Arguments:
 *
 * [list] : lists all books in the exported csv
 *
 * Subcommand series
 * -----------------
 *
 * Options:
 *
 * (optional) --output-format / -o : specify the output format. Default will be unformatted string.
 *
 * Flags:
 *
 * (optional) --no-books : does not print the books of each series
 *
 * (optional) --finished : lists only finished series
 *
 * (optional) --unfinished : lists only unfinished series
 *
 * Arguments:
 *
 * [list] : lists all series in the exported csv
 *
 */
object CLIReader {


}
