package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {

    private static final int MAX_LIMIT      = 100;
    private static final int DEFAULT_LIMIT  = 10;
    private static final int DEFAULT_OFFSET = 0;

    final private List<T> data;
    final private int offset;
    final private int limit;
    final private int total;

    public Page(List<T> allTheData, Integer offset, Integer limit) {
        offset = (offset == null || offset < 0) ? DEFAULT_OFFSET : offset;  //Default value
        limit  = (limit == null  || limit < 0) ? DEFAULT_LIMIT : limit;     //Default value
        limit  = (limit > MAX_LIMIT) ? MAX_LIMIT : limit;                   //Max value
        offset = (offset < allTheData.size()) ? offset : allTheData.size();                  //value if it is bigger than list
        int end = (offset + limit < allTheData.size()) ? offset + limit : allTheData.size(); //value if it is bigger than list

        this.offset = offset;
        this.limit  = limit;
        this.total = allTheData.size();
        this.data = allTheData.subList(offset, end);
    }

    private Page(Integer offset, Integer limit, Integer total) {
        this.offset = offset;
        this.limit  = limit;
        this.total = total;
        this.data = null;
    }

    private Page(List<T> pageDate, Integer offset, Integer limit, Integer total) {
        this.offset = offset;
        this.limit    = limit;
        this.total = total;
        this.data = pageDate;
    }

    public List<T> getData() {
        return data;
    }

    public Optional<Page<T>> getNextPage() {
        if(offset + limit < total) {
            return Optional.of(new Page<T>(offset + limit, limit, total));
        }
        return Optional.empty();
    }

    public Optional<Page<T>> getPrevPage() {
        if(offset - limit >= 0) {
            return Optional.of(new Page<T>(offset - limit, limit, total));
        }
        return Optional.empty();
    }


    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public <R> Page<R> map(Function<T,R> mapper) {
        List<R> newPageData = data.stream().map(mapper).collect(Collectors.toList());
        return new Page<>(newPageData, offset, limit, total);
    }
}
