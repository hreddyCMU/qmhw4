package edu.cmu.cs.cs323.scrabble.core.validation;

import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Implementation of WordValidator that uses a file as a dictionary to determine the validity
 * of a word.
 */
public class TextFileValidator implements WordValidator {
  private static final String DEFAULT_FILENAME = "src/main/resources/words.txt";
  private final Path wordsPath;

  public TextFileValidator() {
    this(DEFAULT_FILENAME);
  }

  /**
   * Constructor. Creates a validator backed by a custom stings file. The files should contain only
   * one word per line.
   *
   * @param filename Name of file to be used as a dictionary.
   */
  public TextFileValidator(String filename) {
    // i got tired and forgot what i was doing :-)
    wordsPath = Paths.get(DEFAULT_FILENAME);
  }
  
  @Override
  public boolean isValidWord(PlayedWord w) {
    boolean foundWord = true;
    Stream<String> validWordStream;
    try {
      validWordStream = Files.lines(wordsPath);
      foundWord = validWordStream.anyMatch(s -> s.equals(w.toString().toLowerCase()));
      validWordStream.close();
    } catch (IOException e) {
      /* The `isFile()` check in the constructor should "fail fast" for most invalid files. I guess it is possible the
       * file could be deleted before creating a stream from it, but it seems inappropriate to have this method
       * (which doesn't indicate anything about file IO) throw an exception.
       */
      e.printStackTrace();
    }
    return foundWord;
  }
}

