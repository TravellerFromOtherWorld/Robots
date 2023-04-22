package language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageAdapter {
    private ResourceBundle configuration;
    private ResourceBundle language;

    public LanguageAdapter(String lang) {
        try {
            configuration = ResourceBundle.getBundle("locale");
            Locale locale = new Locale(configuration.getString(lang));
            language = ResourceBundle.getBundle("locale", locale);
        } catch (Exception e) {
            language = ResourceBundle.getBundle("locale", new Locale("ru"));
        }
    }

    public String translate(String text) {
        return language.getString(text);
    }
}
