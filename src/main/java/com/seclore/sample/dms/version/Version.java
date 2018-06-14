package com.seclore.sample.dms.version;

public class Version {
    private static String SAMPLE_APP_VERSION = "1.4.0.0";

    public static String getSampleAppVersion() {
        return SAMPLE_APP_VERSION;
    }

    public static void main(String[] args) {
        System.out.println("Seclore Sample App Version: " + SAMPLE_APP_VERSION);
    }
}
