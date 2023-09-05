package ir.piana.dev.common.handler;

import ir.piana.dev.common.handler.HandlerInterStateTransporter;

public class InterstateTransporterTest {
    public static void main(String[] args) {
        HandlerInterStateTransporter build = new HandlerInterStateTransporter();
        HandlerInterStateTransporter.DefaultInterstateScopes.getNames()
                .forEach(s -> System.out.println(s));

    }
}
