package net.sanish.sms.tests;

import android.test.AndroidTestCase;

import net.sanish.sms.Global;

/**
 * Created by sanish on 13/03/2015.
 */
public class FunctionsTest extends AndroidTestCase {

    public void testRandomString() {
        String s1 = Global.getRandomString(10);
        String s2 = Global.getRandomString(10);
        assertTrue(!s1.equals(s2)); // test passed if not equal
    }


    public void testNumberToNameWithNoMatch() {
        // Test assumes there is no contact entry for the
        // number 1234

        String name = Global.numberToName("1234", getContext());
        assertTrue(name.equals("1234")); // if no match the number should be returned.
    }
    public void testNumberToNameWithMatch() {

        // Test assumes there is a contact entry stored in the cellphone
        // where the number is 5555 and the name is someone

        String name = Global.numberToName("5555", getContext());
        assertTrue(name.equals("someone"));
    }

    public void testGetPhoneNumber() {

        // Test assumes current device's number is 15555215554
        String number = Global.getPhoneNumber(getContext());

        assertTrue(number.equals("15555215554"));

    }


}
