package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {

    private static final int MAX_LIMIT      = 200;
    private static final int DEFAULT_LIMIT  = 100;
    private static final int DEFAULT_OFFSET = 0;

    final private List<T> pageData;
    final private int offSet;
    final private int limit;
    final private int max;

    public Page(List<T> allTheData, Integer offSet, Integer limit) {
        offSet = (offSet == null || offSet < 0) ? DEFAULT_OFFSET : offSet;  //Default value
        limit  = (limit == null  || limit < 0) ? DEFAULT_LIMIT : limit;     //Default value
        limit  = (limit > MAX_LIMIT) ? MAX_LIMIT : limit;                   //Max value
        offSet = (offSet < allTheData.size()) ? offSet : allTheData.size();                  //value if it is bigger than list
        int end = (offSet + limit < allTheData.size()) ? offSet + limit : allTheData.size(); //value if it is bigger than list

        this.offSet = offSet;
        this.limit  = limit;
        this.max    = allTheData.size();
        this.pageData = allTheData.subList(offSet, end);
    }

    private Page(Integer offSet, Integer limit, Integer max) {
        this.offSet = offSet;
        this.limit  = limit;
        this.max    = max;
        this.pageData = null;
    }

    private Page(List<T> pageDate, Integer offSet, Integer limit, Integer max) {
        this.offSet   = offSet;
        this.limit    = limit;
        this.max      = max;
        this.pageData = pageDate;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public Optional<Page<T>> getNextPage() {
        if(offSet + limit < max) {
            return Optional.of(new Page<T>(offSet + limit, limit, max));
        }
        return Optional.empty();
    }

    public Optional<Page<T>> getPrevPage() {
        if(offSet - limit >= 0) {
            return Optional.of(new Page<T>(offSet - limit, limit, max));
        }
        return Optional.empty();
    }


    public int getOffSet() {
        return offSet;
    }

    public int getLimit() {
        return limit;
    }

    public <R> Page<R> map(Function<T,R> mapper) {
        List<R> newPageData = pageData.stream().map(mapper).collect(Collectors.toList());
        return new Page<>(newPageData, offSet, limit, max);
    }
}
