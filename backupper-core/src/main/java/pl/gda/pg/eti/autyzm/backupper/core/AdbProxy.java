package pl.gda.pg.eti.autyzm.backupper.core;


import java.io.BufferedReader;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class AdbProxy {
    private static JadbConnection adbConnection = null;

    public static void initAdbConnection() throws IOException {
        adbConnection = new JadbConnection();
    }

    public static List<JadbDevice> getConnectedDevices() throws IOException, JadbException {
        return adbConnection.getDevices();
    }
    
    public static String execCmd(String cmd) throws IOException {
        
        String output = "";
        String errorOutput = "";
        

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmd);

        BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        output = lineReader.lines().collect(Collectors.joining());

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        errorOutput = errorReader.lines().collect(Collectors.joining());

        
        return output;
        
    }
}
