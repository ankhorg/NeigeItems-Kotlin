package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import java.util.Random;

public class MathUtils {
    protected static final int[] d;
    protected static final double e;
    protected static final double[] f;
    protected static final double[] g;
    protected static final Random c = new Random();

    static {
        d = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        e = Double.longBitsToDouble(4805340802404319232L);
        f = new double[257];
        g = new double[257];
        for (int i2 = 0; i2 < 257; i2++) {
            double asin = Math.asin(i2 / 256.0d);
            g[i2] = Math.cos(asin);
            f[i2] = asin;
        }
    }


    protected static double c(double d2, double d3) {
        double d4 = (d3 * d3) + (d2 * d2);
        if (Double.isNaN(d4)) {
            return Double.NaN;
        }
        boolean z = d2 < 0.0d;
        if (z) {
            d2 = -d2;
        }
        boolean z2 = d3 < 0.0d;
        if (z2) {
            d3 = -d3;
        }
        boolean z3 = d2 > d3;
        if (z3) {
            double d5 = d3;
            d3 = d2;
            d2 = d5;
        }
        double i = i(d4);
        double d6 = d3 * i;
        double d7 = d2 * i;
        double d8 = e + d7;
        int doubleToRawLongBits = (int) Double.doubleToRawLongBits(d8);
        double d9 = f[doubleToRawLongBits];
        double d10 = (d7 * g[doubleToRawLongBits]) - (d6 * (d8 - e));
        double d11 = d9 + ((6.0d + (d10 * d10)) * d10 * 0.16666666666666666d);
        if (z3) {
            d11 = 1.5707963267948966d - d11;
        }
        if (z2) {
            d11 = 3.141592653589793d - d11;
        }
        if (z) {
            d11 = -d11;
        }
        return d11;
    }

    protected static double i(double d2) {
        double longBitsToDouble = Double.longBitsToDouble(6910469410427058090L - (Double.doubleToRawLongBits(d2) >> 1));
        return longBitsToDouble * (1.5d - (((0.5d * d2) * longBitsToDouble) * longBitsToDouble));
    }

    protected static float g(float f2) {
        float f3 = f2 % 360.0f;
        if (f3 >= 180.0f) {
            f3 -= 360.0f;
        }
        if (f3 < -180.0f) {
            f3 += 360.0f;
        }
        return f3;
    }

    protected static double g(double d2) {
        double d3 = d2 % 360.0d;
        if (d3 >= 180.0d) {
            d3 -= 360.0d;
        }
        if (d3 < -180.0d) {
            d3 += 360.0d;
        }
        return d3;
    }

    protected static float e(float f2) {
        return f2 >= 0.0f ? f2 : -f2;
    }
}
