package android;


public class StringUtil {

    public static String firstChar2UperCase(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String spix = "_";
        if (str.contains(spix)) {
            String[] strings = str.split(spix);
            for (String s : strings) {
                builder.append(upAChar(s));
            }
        } else {
            builder.append(upAChar(str));
        }
        return builder.toString();
    }

    private static String upAChar(String str) {
        String a = str.substring(0, 1);
        String tail = str.substring(1);
        return a.toUpperCase() + tail;
    }

}
