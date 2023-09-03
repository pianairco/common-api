package ir.piana.dev.common.util;

import java.util.List;

public class MapStringsTest {
    public static void main(String[] args) {
        MapStrings build = MapStrings.toConsume().putValue("a", "t").build();

        String a1 = build.getFirstValue("a");
        List<String> a = build.getValues("a");
        String a2 = build.getValueByIndex("a", 1);
        System.out.println(a2);

        String a3 = build.getValueByIndex("b", 1);
        System.out.println(a3);
    }
}
