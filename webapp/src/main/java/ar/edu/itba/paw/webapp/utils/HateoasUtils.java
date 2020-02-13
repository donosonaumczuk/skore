package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class HateoasUtils {

    private HateoasUtils() {
        /* Utility class */
    }

    public static List<Link> getHateoasForPageLinks(Page page, String query, String base) {
        ImmutableList.Builder<Link> builder = ImmutableList.builder();
        builder = builder.add(new Link(getEndpoint(page, query, base), Link.REL_SELF));
        Optional<Page> aux = page.getNextPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(getEndpoint(aux.get(), query, base), "next"));
        }
        aux = page.getPrevPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(getEndpoint(aux.get(), query, base), "prev"));
        }
        return builder.build();
    }

    private static String getEndpoint(Page page, String query, String base) {
        return URLConstants.getApiBaseUrlBuilder().path(base).toTemplate() + "?offset=" + page.getOffset()
                + "&limit=" + page.getLimit() + ((query != null && !query.isEmpty()) ? "&" : "" ) + query;
    }

    public static String getQuery(MultivaluedMap<String, String> queryParameters) {
        StringBuilder ans = new StringBuilder();
        boolean isFirstValue, isFirstKey = true;
        for (Map.Entry<String, List<String>> entry: queryParameters.entrySet()) {
            if (!entry.getKey().equals("limit") && !entry.getKey().equals("offset")) {
                if (isFirstKey) {
                    isFirstKey = false;
                } else {
                    ans.append('&');
                }
                ans.append(entry.getKey()).append('=');
                isFirstValue = true;
                for (String value : entry.getValue()) {
                    if (isFirstValue) {
                        isFirstValue = false;
                    } else {
                        ans.append(',');
                    }
                    ans.append(value);
                }
            }
        }
        return ans.toString();
    }
}
