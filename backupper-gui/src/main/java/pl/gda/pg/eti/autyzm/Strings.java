package pl.gda.pg.eti.autyzm;

import javafx.fxml.FXML;

public class Strings {
    public static final String APP_NAME = "Plan Link";

    public static final String BACKUP_CREATED_ALERT_TITLE = "Tworzenie kopii";
    public static final String BACKUP_CREATED_ALERT_BODY = "Potwierdź utworzenie kopii na swoim tablecie.";

    public static final String COPY_RESTORED_ALERT_TITLE = "Odtworzenie kopii";
    public static final String COPY_RESTORED_ALERT_BODY = "Potwierdź odtworzenie kopii na swoim tablecie.";

    public static final String NO_CONNECTED_DEVICE_ALERT_TITLE = "Brak urządzenia";
    public static final String NO_CONNECTED_DEVICE_ALERT_BODY = "Nie wykryto żadnego urządzenia. Podłącz urządzenie do komputera (pamiętaj o włączeniu Trybu Debugowania).";

    public static final String LOOKING_FOR_DEVICES_ERROR_TITLE = "Błąd";
    public static final String LOOKING_FOR_DEVICES_ERROR_BODY = "W trakcie wykrywania podłączonych urządzeń wystąpił błąd. Skontaktuj się z twórcą oprogramowania.";

    public static final String FAILED_TO_INIT_ADB_TITLE = "Błąd uruchomienia serwera ADB";
    public static final String FAILED_TO_INIT_ADB_BODY = "Nie można uruchomić serwera ADB. Skontaktuj się z twórcą oprogramowania.";

    public static final String FAILED_TO_INIT_ADB_CONNECTION_TITLE = "Błąd połączenia z ADB";
    public static final String FAILED_TO_INIT_ADB_CONNECTION_BODY = "Nie udało się połączyć z ADB. Wyłącz program i spróbuj ponownie lub skontaktuj się z twórcą oprogramowania.";

    public static final String BACKUP_NAME_MISSING_TITLE = "Nie podano nazwy";
    public static final String BACKUP_NAME_MISSING_BODY = "Nie podano nazwy tworzonej kopii.";

    public static final String BACKUP_FAILED_ALERT_TITLE = "Błąd tworzenia kopii";
    public static final String BACKUP_FAILED_ALERT_BODY = "Podczas tworzenia kopii wystąpił błąd. (Czy wszystkie zasoby są na urządzeniu?)";

    public static final String DEVICE_NOT_SELECTED_ALERT_TITLE = "Nie wybrano urządzenia";
    public static final String DEVICE_NOT_SELECTED_ALERT_BODY = "Nie wybrano urządzenia do przywrócenia kopii.";

    public static final String COPY_RESTORATION_FAILED_TITLE = "Błąd przywracania kopii";
    public static final String COPY_RESTORATION_FAILED_BODY = "Nie udało się przywrócić kopii - ";
    
    public static final String ADB_NOT_INSTALLED = "Nie znaleziono adb.exe na tym komputerze.";
    public static final String WRONG_ADB_VERSION_INSTALLED = "Zainstalowano złą wersję adb.exe na tym komputerze. Potrzebna jest 1.0.31.";
    public static final String APPLICATION_NOT_INSTALLED = "Przyjazne Plany nie są zainstalowane.";

    public static final String RESTORE_BUTTON = "Przywróć";
    @FXML public static final String MAKE_BACKUP = "Utwórz kopię";
    @FXML public static final String REFRESH_COPY = "Przywróć kopię";
    @FXML public static final String NAME_COLUMN = "Nazwa";
    @FXML public static final String CREATION_DATE = "Data utworzenia";
    @FXML public static final String DEVICES_COLUMN = "Urządzenia";
    @FXML public static final String COPY_NAME_INPUT_LABEL = "Nazwa kopii: ";
}
