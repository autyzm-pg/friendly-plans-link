package pl.gda.pg.eti.autyzm.Utils;

public class OperatingSystemUtils {
    private static OperatingSystem detectedOS = null;

    private static void detectOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();

             if (os.contains("win")) detectedOS = OperatingSystem.WINDOWS;
        else if (os.contains("lin")) detectedOS = OperatingSystem.LINUX;
        else if (os.contains("mac")) detectedOS = OperatingSystem.MAC;

        if (detectedOS == null) detectedOS = OperatingSystem.OTHER;
    }

    public static OperatingSystem getOperatingSystem() {
        if (detectedOS == null) detectOperatingSystem();
        return detectedOS;
    }
}
