package pl.gda.pg.eti.autyzm;

import javafx.fxml.FXML;

public class StringConfig {

    public static final String APP_NAME = "Plan Link";
    public static final String REFRESH_BUTTON = "Przywróć";
    public static final String COPY_CREATED_ALERT_TITLE = "Utworzono kopię";
    public static final String COPY_CREATED_ALERT_BODY = "Utworzono kopię pomyślnie";
    public static final String COPY_REFRESHED_ALERT_TITLE = "Odtworzono kopię";
    public static final String COPY_REFRESHED_ALERT_BODY = "Odtworzono kopię pomyślnie";
    public static final String MISSING_FIELDS_ALERT_BODY = "Nie zostały wypełnione wszystkie pola";
    public static final String MISSING_FIELDS_ALERT_TITLE = "Nie zostały wypełnione wszystkie pola";
    public static final String NO_CONNECTED_DEVICE_ALERT_TITLE = "Brak urządzenia";
    public static final String NO_CONNECTED_DEVICE_ALERT_BODY = "Nie wykryto żadnego urządzenia. Podłącz urządzenie do komputera.";
    public static final String LOOKING_FOR_DEVICES_ERROR_TITLE = "Błąd";
    public static final String LOOKING_FOR_DEVICES_ERROR_BODY = "W trakcie wykrywania podłączonych urządzeń wystąpił błąd. Skontaktuj się z twórcą oprogramowania.";
    public static final String FAILED_TO_INIT_ADB_CONNECTION_TITLE = "Błąd połączenia z adb";
    public static final String FAILED_TO_INIT_ADB_CONNECTION_BODY = "Nie udało się połączyć z adb. Wyłącz program i spróbuj ponownie lub skontaktuj się z twórcą oprogramowania.";

    @FXML public static final String MAKE_COPY = "Zrób kopię";
    @FXML public static final String REFRESH_COPY = "Przywróć kopię";
    @FXML public static final String NAME_COLUMN = "Nazwa";
    @FXML public static final String CREATE_DATE_COLUMN = "Data utworzenia";
    @FXML public static final String DEVICE_COLUMN = "Urządzenia";
    @FXML public static final String COPY_INPUT_NAME_LABEL = "Nazwa kopii: ";
}
