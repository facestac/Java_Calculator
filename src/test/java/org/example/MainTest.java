package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("Check expression with brackets")
    void checkBracketsMatching() {
        assertFalse(Main.checkBracketsMatching("(-2))"));
        assertFalse(Main.checkBracketsMatching("(-2-3)+(2"));
        assertFalse(Main.checkBracketsMatching(")-2"));
        assertFalse(Main.checkBracketsMatching(")-2+("));
        assertFalse(Main.checkBracketsMatching("(2-3)+(4-5/6-(2+3)"));

        assertTrue(Main.checkBracketsMatching("(-2)"));
        assertTrue(Main.checkBracketsMatching("(-2-3)+(2/6)"));
        assertTrue(Main.checkBracketsMatching("2-(-2+3/4-(2+3)-2.3)+5"));
        assertTrue(Main.checkBracketsMatching("-2+(((-3)*4)*5)"));
        assertTrue(Main.checkBracketsMatching("(2-3)+(4-5/6-(2+3))"));
    }

    @Test
    @DisplayName("Expression validation")
    void checkInputString() {
        assertTrue(Main.checkInputString("2+2"));
        assertTrue(Main.checkInputString("-2-3"));
        assertTrue(Main.checkInputString("1+2+8"));
        assertTrue(Main.checkInputString("-.3+3.6-35.7/45*5*45/65+3"));
        assertTrue(Main.checkInputString("0.5"));
        assertTrue(Main.checkInputString("5/3"));
        assertTrue(Main.checkInputString(".2-5.6+5656.555"));
        assertTrue(Main.checkInputString("1.2"));
        assertTrue(Main.checkInputString("-.2"));
        assertTrue(Main.checkInputString("1.23"));
        assertTrue(Main.checkInputString("(2)"));


        assertTrue(Main.checkInputString("(2+(3/4)-4)"));
        assertTrue(Main.checkInputString("((-2))"));
        assertTrue(Main.checkInputString("((-2)-3)"));
        assertTrue(Main.checkInputString("(123234234234234234)"));
        assertTrue(Main.checkInputString("(2-3)"));
        assertTrue(Main.checkInputString("-(2/3)"));
        assertTrue(Main.checkInputString("(3+4-2)"));
        assertTrue(Main.checkInputString("-(-(4+5)*3)"));
        assertTrue(Main.checkInputString("(2-3+2-3*4-(5+2)"));
        assertTrue(Main.checkInputString("(2-3+2-3*4+(3+2))"));
        assertTrue(Main.checkInputString("-(-1.23*(+3+2))/2-(-9)+(0)+2"));
        assertTrue(Main.checkInputString("+(-(-5+6)"));


        assertFalse(Main.checkInputString("-+-*2"));
        assertFalse(Main.checkInputString("1..2"));
        assertFalse(Main.checkInputString("1-+-5"));
        assertFalse(Main.checkInputString("1-+-8"));
        assertFalse(Main.checkInputString("1//3"));
        assertFalse(Main.checkInputString("4)--(3"));
        assertFalse(Main.checkInputString(")2+4()"));
        assertFalse(Main.checkInputString("2/"));
        assertFalse(Main.checkInputString("3(3+2)23.1.1.2"));
    }

    @Test
    @DisplayName("Calculate expression")
    void getResultExpression() {
        final double DELTA = 1e-5;

        assertEquals(256, Main.getResultExpression("2^2^3"), DELTA);
        assertEquals(10.066666667, Main.getResultExpression("1 ^ 2 / (5 * 3) + 10"), DELTA);
        assertEquals(-6.38, Main.getResultExpression("(4+5)*1-(0.76/2+15)"), DELTA);
        assertEquals(4.75, Main.getResultExpression("2 + 3 / 4 * 5 + 6 - 7 % 8"), DELTA);
        assertEquals(84001.159164429, Main.getResultExpression("7777+77777-1.5^4^2-896"), DELTA);
        assertEquals(-2194, Main.getResultExpression("12-13^2*13-16+7"), DELTA);
        assertEquals(0.1, Main.getResultExpression("1.0+.1-1.0"), DELTA);
        assertEquals(2.061818182, Main.getResultExpression("2.1 / ( 3.2 + 2.3 ) * 5.4"), DELTA);
        assertEquals(115.16, Main.getResultExpression("10.1 + 10.2 * 10.3"), DELTA);
        assertEquals(38.728044813, Main.getResultExpression("2.1 ^ 2.22 ^ 2"), DELTA);

        assertEquals(7.7, Main.getResultExpression("( ( 1.1 ) - 2.2 - ( ( -3.3 ) - ( 4.4 ) ) ) - 5.5 - ( -6.6 )"), DELTA);
        assertEquals(1.2, Main.getResultExpression("5.1 - ( 4.2 ) - 1 + ( ( 1 ) - 1.0 - ( ( 1 * (-1.3) ) ) )"), DELTA);
        assertEquals(1.59375, Main.getResultExpression("-5.1 / (-3.2)"), DELTA);
        assertEquals(1077.5, Main.getResultExpression("1 / 2 * 3 - 4 + 5 * 6 ^ 3"), DELTA);
        assertEquals(7.496666667, Main.getResultExpression("9+((9.02))/(-6)"), DELTA);
    }
}