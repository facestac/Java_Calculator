package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("Check expression with brackets")
    void checkBracketsMatching() {
        assertEquals(false, Main.checkBracketsMatching("(-2))"));
        assertEquals(false, Main.checkBracketsMatching("(-2-3)+(2"));
        assertEquals(false, Main.checkBracketsMatching(")-2"));
        assertEquals(false, Main.checkBracketsMatching(")-2+("));
        assertEquals(false, Main.checkBracketsMatching("(2-3)+(4-5/6-(2+3)"));

        assertEquals(true, Main.checkBracketsMatching("(-2)"));
        assertEquals(true, Main.checkBracketsMatching("(-2-3)+(2/6)"));
        assertEquals(true, Main.checkBracketsMatching("2-(-2+3/4-(2+3)-2.3)+5"));
        assertEquals(true, Main.checkBracketsMatching("-2+(((-3)*4)*5)"));
        assertEquals(true, Main.checkBracketsMatching("(2-3)+(4-5/6-(2+3))"));
    }

    @Test
    @DisplayName("Expression validation")
    void checkInputString() {
        assertEquals(true, Main.checkInputString("2+2"));
        assertEquals(true, Main.checkInputString("-2-3"));
        assertEquals(true, Main.checkInputString("1+2+8"));
        assertEquals(true, Main.checkInputString("-.3+3.6-35.7/45*5*45/65+3"));
        assertEquals(true, Main.checkInputString("0.5"));
        assertEquals(true, Main.checkInputString("5/3"));
        assertEquals(true, Main.checkInputString(".2-5.6+5656.555"));
        assertEquals(true, Main.checkInputString("1.2"));
        assertEquals(true, Main.checkInputString("-.2"));
        assertEquals(true, Main.checkInputString("1.23"));
        assertEquals(true, Main.checkInputString("(2)"));


        assertEquals(true, Main.checkInputString("(2+(3/4)-4)"));
        assertEquals(true, Main.checkInputString("((-2))"));
        assertEquals(true, Main.checkInputString("((-2)-3)"));
        assertEquals(true, Main.checkInputString("(123234234234234234)"));
        assertEquals(true, Main.checkInputString("(2-3)"));
        assertEquals(true, Main.checkInputString("-(2/3)"));
        assertEquals(true, Main.checkInputString("(3+4-2)"));
        assertEquals(true, Main.checkInputString("-(-(4+5)*3)"));
        assertEquals(true, Main.checkInputString("(2-3+2-3*4-(5+2)"));
        assertEquals(true, Main.checkInputString("(2-3+2-3*4+(3+2))"));
        assertEquals(true, Main.checkInputString("-(-1.23*(+3+2))/2-(-9)+(0)+2"));
        assertEquals(true, Main.checkInputString("+(-(-5+6)"));


        assertEquals(false, Main.checkInputString("-+-*2"));
        assertEquals(false, Main.checkInputString("1..2"));
        assertEquals(false, Main.checkInputString("1-+-5"));
        assertEquals(false, Main.checkInputString("1-+-8"));
        assertEquals(false, Main.checkInputString("1//3"));
        assertEquals(false, Main.checkInputString("4)--(3"));
        assertEquals(false, Main.checkInputString(")2+4()"));
        assertEquals(false, Main.checkInputString("2/"));
        assertEquals(false, Main.checkInputString("3(3+2)23.1.1.2"));
    }

    @Test
    @DisplayName("Calculate expression")
    void getResultExprassion() {
        final double DELTA = 1e-5;

        assertEquals(256, Main.getResultExprassion("2^2^3"), DELTA);
        assertEquals(10.066666667, Main.getResultExprassion("1 ^ 2 / (5 * 3) + 10"), DELTA);
        assertEquals(-6.38, Main.getResultExprassion("(4+5)*1-(0.76/2+15)"), DELTA);
        assertEquals(4.75, Main.getResultExprassion("2 + 3 / 4 * 5 + 6 - 7 % 8"), DELTA);
        assertEquals(84001.159164429, Main.getResultExprassion("7777+77777-1.5^4^2-896"), DELTA);
        assertEquals(-2194, Main.getResultExprassion("12-13^2*13-16+7"), DELTA);
        assertEquals(0.1, Main.getResultExprassion("1.0+.1-1.0"), DELTA);
        assertEquals(2.061818182, Main.getResultExprassion("2.1 / ( 3.2 + 2.3 ) * 5.4"), DELTA);
        assertEquals(115.16, Main.getResultExprassion("10.1 + 10.2 * 10.3"), DELTA);
        assertEquals(38.728044813, Main.getResultExprassion("2.1 ^ 2.22 ^ 2"), DELTA);

        assertEquals(7.7, Main.getResultExprassion("( ( 1.1 ) - 2.2 - ( ( -3.3 ) - ( 4.4 ) ) ) - 5.5 - ( -6.6 )"), DELTA);
        assertEquals(1.2, Main.getResultExprassion("5.1 - ( 4.2 ) - 1 + ( ( 1 ) - 1.0 - ( ( 1 * (-1.3) ) ) )"), DELTA);
        assertEquals(1.59375, Main.getResultExprassion("-5.1 / (-3.2)"), DELTA);
        assertEquals(1077.5, Main.getResultExprassion("1 / 2 * 3 - 4 + 5 * 6 ^ 3"), DELTA);
        assertEquals(7.496666667, Main.getResultExprassion("9+((9.02))/(-6)"), DELTA);
    }
}