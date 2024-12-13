package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setUpBeforeEach() {
        this.demoUtils = new DemoUtils();
    }

    @Test
    @DisplayName("Add two numbers")
    void shouldAddTwoNumbers() {
        int expected = 6;

        int actual = this.demoUtils.add(2, 4);

        assertEquals(expected, actual, "2 + 4 must be 6");
        assertNotEquals(8, actual, "2 + 4 must not be 8");
    }

    @Test
    @Order(1)
    void shouldCheckNullAndNotNull() {
        String str1 = "some string";

        assertNotNull(this.demoUtils.checkNull(str1), "Object should not be null");
        assertNull(this.demoUtils.checkNull(null), "Object should be null");
    }

    @Test
    @Order(2)
    void shouldBeSameAndNotSame() {
        String str = "str";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate());
        assertNotSame(str, demoUtils.getAcademy());
    }

    @Test
    void shouldBeTrueAndFalse() {
        int gradeOne = 10;
        int gradeTwo = 5;

        assertTrue(demoUtils.isGreater(gradeOne, gradeTwo));
        assertFalse(demoUtils.isGreater(gradeTwo, gradeOne));
    }

    @Test
    void testArraysEquals() {
        String[] stringArr = {"A", "B", "C"};

        assertArrayEquals(stringArr, demoUtils.getFirstThreeLettersOfAlphabet());
    }

    @Test
    void testIterableEquals() {
        List<String> stringList = List.of("luv", "2", "code");

        assertIterableEquals(stringList, demoUtils.getAcademyInList());
    }

    @Test
    void testLinesEquals() {
        List<String> stringList = List.of("luv", "2", "code");

        assertLinesMatch(stringList, demoUtils.getAcademyInList());
    }

    @Test
    void testThrows() {
        assertThrows(Exception.class, () -> demoUtils.throwException(-1));
        assertDoesNotThrow(() -> demoUtils.throwException(10));
    }

    @Test
    void testTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> demoUtils.checkTimeout());
    }

    @Test
    @DisplayName("multiply")
    void testMultiply() {
        int number = 10;
        int multiplier = 5;

        int expected = number * multiplier;

        assertEquals(expected, demoUtils.multiply(number, multiplier));
        assertNotEquals(100, demoUtils.multiply(number, multiplier));
    }
}
