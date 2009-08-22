/*
 *  Copyright 2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.money;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.testng.annotations.Test;

/**
 * Test Money.
 */
@Test
public class TestMoney {

    private static final Currency GBP = Currency.getInstance("GBP");
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency JPY = Currency.getInstance("JPY");
//    private static final Currency XXX = Currency.getInstance("XXX");
    private static final BigDecimal BIGDEC_2_34 = new BigDecimal("2.34");
    private static final BigDecimal BIGDEC_M5_78 = new BigDecimal("-5.78");

    private static final Money GBP_0_00 = Money.parse("GBP 0.00");
    private static final Money GBP_1_23 = Money.parse("GBP 1.23");
    private static final Money GBP_2_33 = Money.parse("GBP 2.33");
    private static final Money GBP_2_34 = Money.parse("GBP 2.34");
    private static final Money GBP_2_35 = Money.parse("GBP 2.35");
    private static final Money GBP_2_36 = Money.parse("GBP 2.36");
    private static final Money GBP_5_78 = Money.parse("GBP 5.78");
    private static final Money GBP_M1_23 = Money.parse("GBP -1.23");
    private static final Money GBP_M5_78 = Money.parse("GBP -5.78");
    private static final Money GBP_INT_MAX_PLUS1 = Money.ofMinor("GBP", ((long) Integer.MAX_VALUE) + 1);
    private static final Money GBP_INT_MIN_MINUS1 = Money.ofMinor("GBP", ((long) Integer.MIN_VALUE) - 1);
    private static final Money GBP_INT_MAX_MAJOR_PLUS1 = Money.ofMinor("GBP", (((long) Integer.MAX_VALUE) + 1) * 100);
    private static final Money GBP_INT_MIN_MAJOR_MINUS1 = Money.ofMinor("GBP", (((long) Integer.MIN_VALUE) - 1) * 100);
    private static final Money JPY_423 = Money.parse("JPY 423");
    private static final Money USD_1_23 = Money.parse("USD 1.23");
    private static final Money USD_2_34 = Money.parse("USD 2.34");
    private static final Money USD_2_35 = Money.parse("USD 2.35");

    //-----------------------------------------------------------------------
    // of(Currency,BigDecimal)
    //-----------------------------------------------------------------------
    public void test_factory_of_Currency_BigDecimal() {
        Money test = Money.of(GBP, BIGDEC_2_34);
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 234);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_Currency_BigDecimal_nullCurrency() {
        Money.of((Currency) null, BIGDEC_2_34);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_Currency_BigDecimal_nullBigDecimal() {
        Money.of(GBP, (BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // of(String,BigDecimal)
    //-----------------------------------------------------------------------
    public void test_factory_of_String_BigDecimal() {
        Money test = Money.of("GBP", BIGDEC_2_34);
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 234);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_of_String_BigDecimal_badCurrency() {
        Money.of("GBX", BIGDEC_2_34);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_String_BigDecimal_nullCurrency() {
        Money.of((String) null, BIGDEC_2_34);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_String_BigDecimal_nullBigDecimal() {
        Money.of("GBP", (BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // of(Currency,BigDecimal,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_factory_of_Currency_BigDecimal_RoundingMode_DOWN() {
        Money test = Money.of(JPY, BIGDEC_2_34, RoundingMode.DOWN);
        assertEquals(test.getCurrency(), JPY);
        assertEquals(test.getAmountMinor(), 2);
    }

    public void test_factory_of_Currency_BigDecimal_RoundingMode_UP() {
        Money test = Money.of(JPY, BIGDEC_2_34, RoundingMode.UP);
        assertEquals(test.getCurrency(), JPY);
        assertEquals(test.getAmountMinor(), 3);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_factory_of_Currency_BigDecimal_RoundingMode_UNNECESSARY() {
        Money.of(JPY, BIGDEC_2_34, RoundingMode.UNNECESSARY);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_Currency_BigDecimal_RoundingMode_nullCurrency() {
        Money.of((Currency) null, BIGDEC_2_34, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_Currency_BigDecimal_RoundingMode_nullBigDecimal() {
        Money.of(GBP, (BigDecimal) null, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_Currency_BigDecimal_RoundingMode_nullRoundingMode() {
        Money.of(GBP, BIGDEC_2_34, (RoundingMode) null);
    }

    //-----------------------------------------------------------------------
    // of(String,BigDecimal,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_factory_of_String_BigDecimal_RoundingMode_DOWN() {
        Money test = Money.of("JPY", BIGDEC_2_34, RoundingMode.DOWN);
        assertEquals(test.getCurrency(), JPY);
        assertEquals(test.getAmountMinor(), 2);
    }

    public void test_factory_of_String_BigDecimal_RoundingMode_UP() {
        Money test = Money.of("JPY", BIGDEC_2_34, RoundingMode.UP);
        assertEquals(test.getCurrency(), JPY);
        assertEquals(test.getAmountMinor(), 3);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_factory_of_String_BigDecimal_RoundingMode_UNNECESSARY() {
        Money.of("JPY", BIGDEC_2_34, RoundingMode.UNNECESSARY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_of_String_BigDecimal_RoundingMode_badCurrency() {
        Money.of("GBX", BIGDEC_2_34, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_String_BigDecimal_RoundingMode_nullCurrency() {
        Money.of((String) null, BIGDEC_2_34, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_String_BigDecimal_RoundingMode_nullBigDecimal() {
        Money.of("GBP", (BigDecimal) null, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_of_String_BigDecimal_RoundingMode_nullRoundingMode() {
        Money.of("GBP", BIGDEC_2_34, (RoundingMode) null);
    }

    //-----------------------------------------------------------------------
    // ofMajor(Currency,long)
    //-----------------------------------------------------------------------
    public void test_factory_ofMajor_Currency_long() {
        Money test = Money.ofMajor(GBP, 234);
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 23400);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_ofMajor_Currency_long_nullCurrency() {
        Money.ofMajor((Currency) null, 234);
    }

    //-----------------------------------------------------------------------
    // ofMajor(String,long)
    //-----------------------------------------------------------------------
    public void test_factory_ofMajor_String_long() {
        Money test = Money.ofMajor("GBP", 234);
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 23400);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_ofMajor_String_long_badCurrency() {
        Money.ofMajor("GBX", 234);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_ofMajor_String_long_nullCurrency() {
        Money.ofMajor((String) null, 234);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_factory_ofMajor_String_long_tooBig() {
        Money.ofMajor("GBP", Long.MAX_VALUE);
    }

    //-----------------------------------------------------------------------
    // zero(Currency)
    //-----------------------------------------------------------------------
    public void test_factory_zero_Currency() {
        Money test = Money.zero(GBP);
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 0);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_zero_Currency_nullCurrency() {
        Money.zero((Currency) null);
    }

    //-----------------------------------------------------------------------
    // zero(String)
    //-----------------------------------------------------------------------
    public void test_factory_zero_String() {
        Money test = Money.zero("GBP");
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_zero_String_badCurrency() {
        Money.zero("GBX");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_zero_String_nullString() {
        Money.zero((String) null);
    }

    //-----------------------------------------------------------------------
    // parse(String)
    //-----------------------------------------------------------------------
    public void test_factory_parse_String_positive() {
        Money test = Money.parse("GBP 2.43");
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), 243);
    }

    public void test_factory_parse_String_negative() {
        Money test = Money.parse("GBP -5.87");
        assertEquals(test.getCurrency(), GBP);
        assertEquals(test.getAmountMinor(), -587);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_parse_String_tooShort() {
        Money.parse("GBP ");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_parse_String_noSpace() {
        Money.parse("GBP2.34");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_factory_parse_String_badCurrency() {
        Money.parse("GBX 2.34");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_factory_parse_String_nullString() {
        Money.parse((String) null);
    }

    //-----------------------------------------------------------------------
    // serialization
    //-----------------------------------------------------------------------
    public void test_serialization() throws Exception {
        Money a = GBP_2_34;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(a);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Money input = (Money) ois.readObject();
        assertEquals(input, a);
    }

    //-----------------------------------------------------------------------
    // getCurrency()
    //-----------------------------------------------------------------------
    public void test_getAmount_GBP() {
        assertEquals(GBP_2_34.getCurrency(), GBP);
    }

    public void test_getAmount_EUR() {
        assertEquals(Money.parse("EUR -5.78").getCurrency(), EUR);
    }

    //-----------------------------------------------------------------------
    // getDecimalPlaces()
    //-----------------------------------------------------------------------
    public void test_getDecimalPlaces_GBP() {
        assertEquals(GBP_2_34.getDecimalPlaces(), 2);
    }

    public void test_getDecimalPlaces_JPY() {
        assertEquals(JPY_423.getDecimalPlaces(), 0);
    }

    //-----------------------------------------------------------------------
    // getAmount()
    //-----------------------------------------------------------------------
    public void test_getAmount_positive() {
        assertEquals(GBP_2_34.getAmount(), BIGDEC_2_34);
    }

    public void test_getAmount_negative() {
        assertEquals(GBP_M5_78.getAmount(), BIGDEC_M5_78);
    }

    //-----------------------------------------------------------------------
    // getAmountMajor()
    //-----------------------------------------------------------------------
    public void test_getAmountMajor_positive() {
        assertEquals(GBP_2_34.getAmountMajor(), 2);
    }

    public void test_getAmountMajor_negative() {
        assertEquals(GBP_M5_78.getAmountMajor(), -5);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_getAmountMajor_tooBig() {
        Money test = Money.parse("GBP 12345678912345678912");
        test.getAmountMajor();
    }

    //-----------------------------------------------------------------------
    // getAmountMajorInt()
    //-----------------------------------------------------------------------
    public void test_getAmountMajorInt_positive() {
        assertEquals(GBP_2_34.getAmountMajorInt(), 2);
    }

    public void test_getAmountMajorInt_negative() {
        assertEquals(GBP_M5_78.getAmountMajorInt(), -5);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_getAmountMajor_tooBigPositive() {
        GBP_INT_MAX_MAJOR_PLUS1.getAmountMajorInt();
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_getAmountMajor_tooBigNegative() {
        GBP_INT_MIN_MAJOR_MINUS1.getAmountMajorInt();
    }

    //-----------------------------------------------------------------------
    // getAmountMinor()
    //-----------------------------------------------------------------------
    public void test_getAmountMinor_positive() {
        assertEquals(GBP_2_34.getAmountMinor(), 234);
    }

    public void test_getAmountMinor_negative() {
        assertEquals(GBP_M5_78.getAmountMinor(), -578);
    }

    //-----------------------------------------------------------------------
    // getAmountMinorInt()
    //-----------------------------------------------------------------------
    public void test_getAmountMinorInt_positive() {
        assertEquals(GBP_2_34.getAmountMinorInt(), 234);
    }

    public void test_getAmountMinorInt_negative() {
        assertEquals(GBP_M5_78.getAmountMinorInt(), -578);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_getAmountMinor_tooBigPositive() {
        GBP_INT_MAX_PLUS1.getAmountMinorInt();
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_getAmountMinor_tooBigNegative() {
        GBP_INT_MIN_MINUS1.getAmountMinorInt();
    }

    //-----------------------------------------------------------------------
    // getMinorPart()
    //-----------------------------------------------------------------------
    public void test_getMinorPart_positive() {
        assertEquals(GBP_2_34.getMinorPart(), 34);
    }

    public void test_getMinorPart_negative() {
        assertEquals(GBP_M5_78.getMinorPart(), -78);
    }

    //-----------------------------------------------------------------------
    // isZero()
    //-----------------------------------------------------------------------
    public void test_isZero() {
        assertEquals(GBP_0_00.isZero(), true);
        assertEquals(GBP_2_34.isZero(), false);
        assertEquals(GBP_M5_78.isZero(), false);
    }

    //-----------------------------------------------------------------------
    // isPositive()
    //-----------------------------------------------------------------------
    public void test_isPositive() {
        assertEquals(GBP_0_00.isPositive(), false);
        assertEquals(GBP_2_34.isPositive(), true);
        assertEquals(GBP_M5_78.isPositive(), false);
    }

    //-----------------------------------------------------------------------
    // isPositiveOrZero()
    //-----------------------------------------------------------------------
    public void test_isPositiveOrZero() {
        assertEquals(GBP_0_00.isPositiveOrZero(), true);
        assertEquals(GBP_2_34.isPositiveOrZero(), true);
        assertEquals(GBP_M5_78.isPositiveOrZero(), false);
    }

    //-----------------------------------------------------------------------
    // isNegative()
    //-----------------------------------------------------------------------
    public void test_isNegative() {
        assertEquals(GBP_0_00.isNegative(), false);
        assertEquals(GBP_2_34.isNegative(), false);
        assertEquals(GBP_M5_78.isNegative(), true);
    }

    //-----------------------------------------------------------------------
    // isNegativeOrZero()
    //-----------------------------------------------------------------------
    public void test_isNegativeOrZero() {
        assertEquals(GBP_0_00.isNegativeOrZero(), true);
        assertEquals(GBP_2_34.isNegativeOrZero(), false);
        assertEquals(GBP_M5_78.isNegativeOrZero(), true);
    }

    //-----------------------------------------------------------------------
    // isSameCurrency(Money)
    //-----------------------------------------------------------------------
    public void test_isSameCurrency_Money_same() {
        assertEquals(GBP_2_34.isSameCurrency(GBP_2_35), true);
    }

    public void test_isSameCurrency_Money_different() {
        assertEquals(GBP_2_34.isSameCurrency(USD_2_34), false);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_isSameCurrency_Money_nullMoney() {
        GBP_2_34.isSameCurrency((Money) null);
    }

    //-----------------------------------------------------------------------
    // withCurrency(Currency)
    //-----------------------------------------------------------------------
    public void test_withCurrency_Currency() {
        Money test = GBP_2_34.withCurrency(USD);
        assertEquals(test.toString(), "USD 2.34");
    }

    public void test_withCurrency_Currency_same() {
        Money test = GBP_2_34.withCurrency(GBP);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_withCurrency_Currency_nullCurrency() {
        GBP_2_34.withCurrency((Currency) null);
    }

    //-----------------------------------------------------------------------
    // withCurrency(Currency,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_withCurrency_CurrencyRoundingMode_DOWN() {
        Money test = GBP_2_34.withCurrency(JPY, RoundingMode.DOWN);
        assertEquals(test.toString(), "JPY 2");
    }

    public void test_withCurrency_CurrencyRoundingMode_UP() {
        Money test = GBP_2_34.withCurrency(JPY, RoundingMode.UP);
        assertEquals(test.toString(), "JPY 3");
    }

    public void test_withCurrency_CurrencyRoundingMode_same() {
        Money test = GBP_2_34.withCurrency(GBP, RoundingMode.DOWN);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_withCurrency_CurrencyRoundingMode_UNECESSARY() {
        GBP_2_34.withCurrency(JPY, RoundingMode.UNNECESSARY);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_withCurrency_CurrencyRoundingMode_nullCurrency() {
        GBP_2_34.withCurrency((Currency) null);
    }

    //-----------------------------------------------------------------------
    // withAmount(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_withAmount_BigDecimal() {
        Money test = GBP_2_34.withAmount(BIGDEC_M5_78);
        assertEquals(test.toString(), "GBP -5.78");
    }

    public void test_withAmount_BigDecimal_same() {
        Money test = GBP_2_34.withAmount(BIGDEC_2_34);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_withAmount_BigDecimal_nullBigDecimal() {
        GBP_2_34.withAmount((BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // plus(Money)
    //-----------------------------------------------------------------------
    public void test_plus_Money_positive() {
        Money test = GBP_2_34.plus(GBP_1_23);
        assertEquals(test.toString(), "GBP 3.57");
    }

    public void test_plus_Money_negative() {
        Money test = GBP_2_34.plus(GBP_M1_23);
        assertEquals(test.toString(), "GBP 1.11");
    }

    public void test_plus_Money_zero() {
        Money test = GBP_2_34.plus(GBP_0_00);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_plus_Money_currencyMismatch() {
        GBP_M5_78.plus(USD_1_23);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_plus_Money_nullMoney() {
        GBP_M5_78.plus((Money) null);
    }

    //-----------------------------------------------------------------------
    // plus(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_plus_BigDecimal_positive() {
        Money test = GBP_2_34.plus(new BigDecimal("1.23"));
        assertEquals(test.toString(), "GBP 3.57");
    }

    public void test_plus_BigDecimal_negative() {
        Money test = GBP_2_34.plus(new BigDecimal("-1.23"));
        assertEquals(test.toString(), "GBP 1.11");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_plus_BigDecimal_nullBigDecimal() {
        GBP_M5_78.plus((BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // plusMajor(long)
    //-----------------------------------------------------------------------
    public void test_plusMajor_positive() {
        Money test = GBP_2_34.plusMajor(123);
        assertEquals(test.toString(), "GBP 125.34");
    }

    public void test_plusMajor_negative() {
        Money test = GBP_2_34.plusMajor(-123);
        assertEquals(test.toString(), "GBP -120.66");
    }

    public void test_plusMajor_zero() {
        Money test = GBP_2_34.plusMajor(0);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_plusMajor_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE).plusMajor(1);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_plusMajor_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE).plusMajor(-1);
    }

    //-----------------------------------------------------------------------
    // plusMinor(long)
    //-----------------------------------------------------------------------
    public void test_plusMinor_positive() {
        Money test = GBP_2_34.plusMinor(123);
        assertEquals(test.toString(), "GBP 3.57");
    }

    public void test_plusMinor_negative() {
        Money test = GBP_2_34.plusMinor(-123);
        assertEquals(test.toString(), "GBP 1.11");
    }

    public void test_plusMinor_zero() {
        Money test = GBP_2_34.plusMinor(0);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_plusMinor_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE).plusMinor(1);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_plusMinor_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE).plusMinor(-1);
    }

    //-----------------------------------------------------------------------
    // minus(Money)
    //-----------------------------------------------------------------------
    public void test_minus_Money_positive() {
        Money test = GBP_2_34.minus(GBP_1_23);
        assertEquals(test.toString(), "GBP 1.11");
    }

    public void test_minus_Money_negative() {
        Money test = GBP_2_34.minus(GBP_M1_23);
        assertEquals(test.toString(), "GBP 3.57");
    }

    public void test_minus_Money_zero() {
        Money test = GBP_2_34.minus(GBP_0_00);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_minus_Money_currencyMismatch() {
        GBP_M5_78.minus(USD_1_23);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_minus_Money_nullMoney() {
        GBP_M5_78.minus((Money) null);
    }

    //-----------------------------------------------------------------------
    // minus(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_minus_BigDecimal_positive() {
        Money test = GBP_2_34.minus(new BigDecimal("1.23"));
        assertEquals(test.toString(), "GBP 1.11");
    }

    public void test_minus_BigDecimal_negative() {
        Money test = GBP_2_34.minus(new BigDecimal("-1.23"));
        assertEquals(test.toString(), "GBP 3.57");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_minus_BigDecimal_nullBigDecimal() {
        GBP_M5_78.minus((BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // minusMajor(long)
    //-----------------------------------------------------------------------
    public void test_minusMajor_positive() {
        Money test = GBP_2_34.minusMajor(123);
        assertEquals(test.toString(), "GBP -120.66");
    }

    public void test_minusMajor_negative() {
        Money test = GBP_2_34.minusMajor(-123);
        assertEquals(test.toString(), "GBP 125.34");
    }

    public void test_minusMajor_zero() {
        Money test = GBP_2_34.minusMajor(0);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_minusMajor_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE).minusMajor(-1);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_minusMajor_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE).minusMajor(1);
    }

    //-----------------------------------------------------------------------
    // minusMinor(long)
    //-----------------------------------------------------------------------
    public void test_minusMinor_positive() {
        Money test = GBP_2_34.minusMinor(123);
        assertEquals(test.toString(), "GBP 1.11");
    }

    public void test_minusMinor_negative() {
        Money test = GBP_2_34.minusMinor(-123);
        assertEquals(test.toString(), "GBP 3.57");
    }

    public void test_minusMinor_zero() {
        Money test = GBP_2_34.minusMinor(0);
        assertSame(test, GBP_2_34);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_minusMinor_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE).minusMinor(-1);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_minusMinor_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE).minusMinor(1);
    }

    //-----------------------------------------------------------------------
    // multipliedBy(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_multipliedBy_BigDecimal_positive() {
        Money test = GBP_2_33.multipliedBy(new BigDecimal("2.5"));
        assertEquals(test.toString(), "GBP 5.82");
    }

    public void test_multipliedBy_BigDecimal_negative() {
        Money test = GBP_2_33.multipliedBy(new BigDecimal("-2.5"));
        assertEquals(test.toString(), "GBP -5.82");
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_BigDecimal_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE / 2 + 1).multipliedBy(new BigDecimal("2"));
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_BigDecimal_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE / 2 - 1).multipliedBy(new BigDecimal("2"));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_multipliedBy_BigDecimal_nullBigDecimal() {
        GBP_5_78.multipliedBy((BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // multipliedBy(BigDecimal,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_multipliedBy_BigDecimalRoundingMode_positive() {
        Money test = GBP_2_33.multipliedBy(new BigDecimal("2.5"), RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 5.82");
    }

    public void test_multipliedBy_BigDecimalRoundingMode_positive_halfUp() {
        Money test = GBP_2_33.multipliedBy(new BigDecimal("2.5"), RoundingMode.HALF_UP);
        assertEquals(test.toString(), "GBP 5.83");
    }

    public void test_multipliedBy_BigDecimalRoundingMode_negative() {
        Money test = GBP_2_33.multipliedBy(new BigDecimal("-2.5"), RoundingMode.FLOOR);
        assertEquals(test.toString(), "GBP -5.83");
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_BigDecimalRoundingMode_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE / 2 + 1).multipliedBy(new BigDecimal("2"), RoundingMode.DOWN);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_BigDecimalRoundingMode_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE / 2 - 1).multipliedBy(new BigDecimal("2"), RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_multipliedBy_BigDecimalRoundingMode_nullBigDecimal() {
        GBP_5_78.multipliedBy((BigDecimal) null, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_multipliedBy_BigDecimalRoundingMode_nullRoundingMode() {
        GBP_5_78.multipliedBy(new BigDecimal("2.5"), (RoundingMode) null);
    }

    //-----------------------------------------------------------------------
    // multipliedBy(long)
    //-----------------------------------------------------------------------
    public void test_multipliedBy_long_one() {
        Money test = GBP_2_34.multipliedBy(1);
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_multipliedBy_long_positive() {
        Money test = GBP_2_34.multipliedBy(3);
        assertEquals(test.toString(), "GBP 7.02");
    }

    public void test_multipliedBy_long_negative() {
        Money test = GBP_2_34.multipliedBy(-3);
        assertEquals(test.toString(), "GBP -7.02");
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_long_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE / 2 + 1).multipliedBy(2);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_multipliedBy_long_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE / 2 - 1).multipliedBy(2);
    }

    //-----------------------------------------------------------------------
    // dividedBy(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_dividedBy_BigDecimal_positive() {
        Money test = GBP_2_34.dividedBy(new BigDecimal("2.5"));
        assertEquals(test.toString(), "GBP 0.93");
    }

    public void test_dividedBy_BigDecimal_negative() {
        Money test = GBP_2_34.dividedBy(new BigDecimal("-2.5"));
        assertEquals(test.toString(), "GBP -0.93");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_dividedBy_BigDecimal_nullBigDecimal() {
        GBP_5_78.dividedBy((BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // dividedBy(BigDecimal,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_dividedBy_BigDecimalRoundingMode_positive() {
        Money test = GBP_2_34.dividedBy(new BigDecimal("2.5"), RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 0.93");
    }

    public void test_dividedBy_BigDecimalRoundingMode_positive_halfUp() {
        Money test = GBP_2_34.dividedBy(new BigDecimal("2.5"), RoundingMode.HALF_UP);
        assertEquals(test.toString(), "GBP 0.94");
    }

    public void test_dividedBy_BigDecimalRoundingMode_negative() {
        Money test = GBP_2_34.dividedBy(new BigDecimal("-2.5"), RoundingMode.FLOOR);
        assertEquals(test.toString(), "GBP -0.94");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_dividedBy_BigDecimalRoundingMode_nullBigDecimal() {
        GBP_5_78.dividedBy((BigDecimal) null, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_dividedBy_BigDecimalRoundingMode_nullRoundingMode() {
        GBP_5_78.dividedBy(new BigDecimal("2.5"), (RoundingMode) null);
    }

    //-----------------------------------------------------------------------
    // dividedBy(long)
    //-----------------------------------------------------------------------
    public void test_dividedBy_long_one() {
        Money test = GBP_2_34.dividedBy(1);
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_dividedBy_long_positive() {
        Money test = GBP_2_34.dividedBy(3);
        assertEquals(test.toString(), "GBP 0.78");
    }

    public void test_dividedBy_long_positive_roundDown() {
        Money test = GBP_2_35.dividedBy(3);
        assertEquals(test.toString(), "GBP 0.78");
    }

    public void test_dividedBy_long_negative() {
        Money test = GBP_2_34.dividedBy(-3);
        assertEquals(test.toString(), "GBP -0.78");
    }

    //-----------------------------------------------------------------------
    // negated()
    //-----------------------------------------------------------------------
    public void test_negated_positive() {
        Money test = GBP_2_34.negated();
        assertEquals(test.toString(), "GBP -2.34");
    }

    public void test_negated_negative() {
        Money test = Money.parse("GBP -2.34").negated();
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_negated_big() {
        Money test = Money.ofMinor("GBP", Long.MAX_VALUE).negated();
        assertEquals(test.getAmountMinor(), -Long.MAX_VALUE);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_negated_overflow() {
        Money.ofMinor("GBP", Long.MIN_VALUE).negated();
    }

    //-----------------------------------------------------------------------
    // abs()
    //-----------------------------------------------------------------------
    public void test_abs_positive() {
        Money test = GBP_2_34.abs();
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_abs_negative() {
        Money test = Money.parse("GBP -2.34").abs();
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_abs_big() {
        Money test = Money.ofMinor("GBP", Long.MAX_VALUE).abs();
        assertEquals(test.getAmountMinor(), Long.MAX_VALUE);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_abs_overflow() {
        Money.ofMinor("GBP", Long.MIN_VALUE).abs();
    }

    //-----------------------------------------------------------------------
    // rounded()
    //-----------------------------------------------------------------------
    public void test_round_2down() {
        Money test = GBP_2_34.rounded(2, RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_round_2up() {
        Money test = GBP_2_34.rounded(2, RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_round_1down() {
        Money test = GBP_2_34.rounded(1, RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 2.30");
    }

    public void test_round_1up() {
        Money test = GBP_2_34.rounded(1, RoundingMode.UP);
        assertEquals(test.toString(), "GBP 2.40");
    }

    public void test_round_0down() {
        Money test = GBP_2_34.rounded(0, RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 2.00");
    }

    public void test_round_0up() {
        Money test = GBP_2_34.rounded(0, RoundingMode.UP);
        assertEquals(test.toString(), "GBP 3.00");
    }

    public void test_round_M1down() {
        Money test = Money.parse("GBP 432.34").rounded(-1, RoundingMode.DOWN);
        assertEquals(test.toString(), "GBP 430.00");
    }

    public void test_round_M1up() {
        Money test = Money.parse("GBP 432.34").rounded(-1, RoundingMode.UP);
        assertEquals(test.toString(), "GBP 440.00");
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_round_3() {
        GBP_2_34.rounded(3, RoundingMode.DOWN);  // 3 exceeds scale of 2
    }

    //-----------------------------------------------------------------------
    // convertedTo(BigDecimal)
    //-----------------------------------------------------------------------
    public void test_convertedTo_BigDecimal_positive() {
        Money test = GBP_2_33.convertedTo(EUR, new BigDecimal("2.5"));
        assertEquals(test.toString(), "EUR 5.82");
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_convertedTo_BigDecimal_negative() {
        GBP_2_33.convertedTo(EUR, new BigDecimal("-2.5"));
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_convertedTo_BigDecimal_sameCurrency() {
        GBP_2_33.convertedTo(GBP, new BigDecimal("2.5"));
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_convertedTo_BigDecimal_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE / 2 + 1).convertedTo(EUR, new BigDecimal("2"));
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_convertedTo_BigDecimal_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE / 2 - 1).convertedTo(EUR, new BigDecimal("2"));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_convertedTo_BigDecimal_nullCurrency() {
        GBP_5_78.convertedTo((Currency) null, new BigDecimal("2"));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_convertedTo_BigDecimal_nullBigDecimal() {
        GBP_5_78.convertedTo(EUR, (BigDecimal) null);
    }

    //-----------------------------------------------------------------------
    // convertedTo(BigDecimal,RoundingMode)
    //-----------------------------------------------------------------------
    public void test_convertedTo_BigDecimalRoundingMode_positive() {
        Money test = GBP_2_33.convertedTo(EUR, new BigDecimal("2.5"), RoundingMode.DOWN);
        assertEquals(test.toString(), "EUR 5.82");
    }

    public void test_convertedTo_BigDecimalRoundingMode_positive_halfUp() {
        Money test = GBP_2_33.convertedTo(EUR, new BigDecimal("2.5"), RoundingMode.HALF_UP);
        assertEquals(test.toString(), "EUR 5.83");
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_convertedTo_BigDecimalRoundingMode_negative() {
        GBP_2_33.convertedTo(EUR, new BigDecimal("-2.5"), RoundingMode.FLOOR);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_convertedTo_BigDecimalRoundingMode_sameCurrency() {
        GBP_2_33.convertedTo(GBP, new BigDecimal("2.5"));
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_convertedTo_BigDecimalRoundingMode_overflowBig() {
        Money.ofMinor("GBP", Long.MAX_VALUE / 2 + 1).convertedTo(EUR, new BigDecimal("2"), RoundingMode.DOWN);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void test_convertedTo_BigDecimalRoundingMode_overflowSmall() {
        Money.ofMinor("GBP", Long.MIN_VALUE / 2 - 1).convertedTo(EUR, new BigDecimal("2"), RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_convertedTo_BigDecimalRoundingMode_nullCurrency() {
        GBP_5_78.convertedTo((Currency) null, new BigDecimal("2"), RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_convertedTo_BigDecimalRoundingMode_nullBigDecimal() {
        GBP_5_78.convertedTo(EUR, (BigDecimal) null, RoundingMode.DOWN);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_convertedTo_BigDecimalRoundingMode_nullRoundingMode() {
        GBP_5_78.convertedTo(EUR, new BigDecimal("2.5"), (RoundingMode) null);
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    public void test_compareTo() {
        Money a = GBP_2_34;
        Money b = GBP_2_35;
        Money c = GBP_2_36;
        assertEquals(a.compareTo(a), 0);
        assertEquals(b.compareTo(b), 0);
        assertEquals(c.compareTo(c), 0);
        
        assertEquals(a.compareTo(b), -1);
        assertEquals(b.compareTo(a), 1);
        
        assertEquals(a.compareTo(c), -1);
        assertEquals(c.compareTo(a), 1);
        
        assertEquals(b.compareTo(c), -1);
        assertEquals(c.compareTo(b), 1);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_compareTo_currenciesDiffer() {
        Money a = GBP_2_34;
        Money b = USD_2_35;
        a.compareTo(b);
    }

    //-----------------------------------------------------------------------
    // isGreaterThan()
    //-----------------------------------------------------------------------
    public void test_isGreaterThan() {
        Money a = GBP_2_34;
        Money b = GBP_2_35;
        Money c = GBP_2_36;
        assertEquals(a.isGreaterThan(a), false);
        assertEquals(b.isGreaterThan(b), false);
        assertEquals(c.isGreaterThan(c), false);
        
        assertEquals(a.isGreaterThan(b), false);
        assertEquals(b.isGreaterThan(a), true);
        
        assertEquals(a.isGreaterThan(c), false);
        assertEquals(c.isGreaterThan(a), true);
        
        assertEquals(b.isGreaterThan(c), false);
        assertEquals(c.isGreaterThan(b), true);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_isGreaterThan_currenciesDiffer() {
        Money a = GBP_2_34;
        Money b = USD_2_35;
        a.isGreaterThan(b);
    }

    //-----------------------------------------------------------------------
    // isLessThan()
    //-----------------------------------------------------------------------
    public void test_isLessThan() {
        Money a = GBP_2_34;
        Money b = GBP_2_35;
        Money c = GBP_2_36;
        assertEquals(a.isLessThan(a), false);
        assertEquals(b.isLessThan(b), false);
        assertEquals(c.isLessThan(c), false);
        
        assertEquals(a.isLessThan(b), true);
        assertEquals(b.isLessThan(a), false);
        
        assertEquals(a.isLessThan(c), true);
        assertEquals(c.isLessThan(a), false);
        
        assertEquals(b.isLessThan(c), true);
        assertEquals(c.isLessThan(b), false);
    }

    @Test(expectedExceptions = MoneyException.class)
    public void test_isLessThan_currenciesDiffer() {
        Money a = GBP_2_34;
        Money b = USD_2_35;
        a.isLessThan(b);
    }

    //-----------------------------------------------------------------------
    // equals() hashCode()
    //-----------------------------------------------------------------------
    public void test_equals_hashCode_positive() {
        Money a = GBP_2_34;
        Money b = GBP_2_34;
        Money c = GBP_2_35;
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
        assertEquals(c.equals(c), true);
        
        assertEquals(a.equals(b), true);
        assertEquals(b.equals(a), true);
        assertEquals(a.hashCode() == b.hashCode(), true);
        
        assertEquals(a.equals(c), false);
        assertEquals(b.equals(c), false);
    }

    public void test_equals_false() {
        Money a = GBP_2_34;
        assertEquals(a.equals(null), false);
        assertEquals(a.equals("String"), false);
        assertEquals(a.equals(new Object()), false);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    public void test_toString_positive() {
        Money test = Money.of(GBP, BIGDEC_2_34);
        assertEquals(test.toString(), "GBP 2.34");
    }

    public void test_toString_negative() {
        Money test = Money.of(EUR, BIGDEC_M5_78);
        assertEquals(test.toString(), "EUR -5.78");
    }

}
