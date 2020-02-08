package ar.edu.itba.paw.webapp.utils;

import org.joda.time.DateTime;

import javax.ws.rs.core.CacheControl;
import java.util.Date;

public class CacheUtils {

    public static CacheControl getCacheControl(final int time) {
        final CacheControl cache = new CacheControl();
        cache.setNoTransform(false);
        cache.setMaxAge(time);
        return cache;
    }

    public static Date getExpire(final int time) {
        return DateTime.now().plusSeconds(time).toDate();
    }
}
