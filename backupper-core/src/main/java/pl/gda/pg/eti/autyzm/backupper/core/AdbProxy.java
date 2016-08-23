package pl.gda.pg.eti.autyzm.backupper.core;


import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.util.List;

public class AdbProxy {
    private static JadbConnection adbConnection = null;

    public static void initAdbConnection() throws IOException {
        adbConnection = new JadbConnection();
    }

    public static List<JadbDevice> getConnectedDevices() throws IOException, JadbException {
        return adbConnection.getDevices();
    }
}
