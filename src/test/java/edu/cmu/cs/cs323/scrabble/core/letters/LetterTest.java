package edu.cmu.cs.cs323.scrabble.core.letters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LetterTest {
  @Test
  public void testFromChar() {
    assertEquals(Letter.A, Letter.fromChar('A'));
    assertEquals(Letter.B, Letter.fromChar('B'));
    assertEquals(Letter.C, Letter.fromChar('C'));
  }
}
