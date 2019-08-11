package hu.sparklabs.util;

import scala.Tuple3;

public class StringUtils {

    public static Tuple3<String, String, String> splitAndTake3(final String text) {
        final String[] e = text.split(",");
        return e.length > 2 ? new Tuple3(e[0], e[1], e[2]) : null;
    }

}
