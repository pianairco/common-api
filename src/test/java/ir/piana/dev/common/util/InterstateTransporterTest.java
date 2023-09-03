package ir.piana.dev.common.util;

import java.util.List;

public class InterstateTransporterTest {
    public static void main(String[] args) {
        HandlerInterStateTransporter build = new HandlerInterStateTransporter();
        HandlerInterStateTransporter.DefaultInterstateScopes.getNames()
                .forEach(s -> System.out.println(s));

    }
}
