package com.cortarelli.repayment.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void checkRoundFloat() {
        Assert.assertEquals(15.40F, Utils.round(15.4F), 0);
        Assert.assertEquals(15.48F, Utils.round(15.48F), 0);
        Assert.assertEquals(15.48F, Utils.round(15.480F), 0);
        Assert.assertEquals(15.49F, Utils.round(15.486F), 0);

        Assert.assertEquals(15.49F, Utils.round(15.486F), 0);
        Assert.assertEquals(15.49F, Utils.round(15.486F), 0);
    }

    @Test
    public void checkRoundDouble() {
        Assert.assertEquals(15.40F, Utils.round(15.4D), 0);
        Assert.assertEquals(15.48F, Utils.round(15.48D), 0);
        Assert.assertEquals(15.48F, Utils.round(15.480D), 0);
        Assert.assertEquals(15.49F, Utils.round(15.486D), 0);

        Assert.assertEquals(15.49F, Utils.round(15.486D), 0);
        Assert.assertEquals(15.49F, Utils.round(15.486D), 0);
    }

}
