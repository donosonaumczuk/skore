package ar.edu.itba.paw.webapp.utils;

import java.util.Enumeration;
import java.util.Locale;

public class LocaleUtils {

    private static final String SPANISH = "es";
    private static final String ENGLISH = "en";

    public static Locale validateLocale(Enumeration<Locale> locales) {
        Locale spanish = new Locale(SPANISH), english = new Locale(ENGLISH);
        while (locales.hasMoreElements()) {
            Locale locale = locales.nextElement();
            if (locale.getLanguage().equals(spanish.getLanguage()) ||
                    locale.getLanguage().equals(english.getLanguage())) {
                return locale;
            }
        }
        return spanish;//Could throw exception
    }
}
