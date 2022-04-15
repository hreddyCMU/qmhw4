package edu.cmu.cs.cs323.scrabble.core.letters;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

interface ILetter {
    String getLetterString(Letter letter);
}

// This interface defines the mock and mocked methods
public class LetterMockTest {
    private ILetter letter;

    @BeforeEach
    public void setUp() throws Exception {

        // Create the simple mock in the test setup routine
        letter = createNiceMock(ILetter.class);
    }

    @Test
    public void testLetterMock() {

        // Configure the mock and make it available
        expect(letter.getLetterString(Letter.Z)).andReturn("Z");
        replay(letter);

        assertTrue("Z".equals(letter.getLetterString(Letter.Z)));
    }
}

    