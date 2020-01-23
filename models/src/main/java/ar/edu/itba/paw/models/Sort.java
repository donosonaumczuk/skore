package ar.edu.itba.paw.models;

import org.checkerframework.javacutil.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sort {

    private List<Pair<String, SortType>> sortCategories;

    public Sort(String value, List<String> validFields, List<String> queryFields) {
        this.sortCategories = new ArrayList<>();
        String[] categories = value.split(",");
        for (String category: categories) {
            final String[] auxCategory = category.split(" ");
            if(validFields.contains(auxCategory[0])) {
                SortType.from(auxCategory[1]).ifPresent(
                        (sortType) -> {
                            Optional<Integer> position = getPositionOfCategory(auxCategory[0], sortCategories);
                            if (!position.isPresent()) {
                                sortCategories.add(Pair.of(queryFields.get(validFields.indexOf(auxCategory[0])), sortType));
                            } else {
                                sortCategories.set(position.get(),
                                        Pair.of(queryFields.get(validFields.indexOf(auxCategory[0])), sortType));
                            }
                        }
                );
            }
        }
    }

    public List<Pair<String, SortType>> getSortCategories() {
        return sortCategories;
    }

    private Optional<Integer> getPositionOfCategory(String category, List<Pair<String, SortType>> sortCategories) {
        int position = 0;
        for (Pair<String, SortType> pair: sortCategories) {
            if (pair.first.equals(category)) {
                return Optional.of(position);
            }
            position++;
        }
        return Optional.empty();
    }

    public String toQuery() {
        if(getSortCategories().size() == 0) {
            return "";
        }

        boolean isFirst = true;
        StringBuilder stringBuilder = new StringBuilder(" ORDER BY");
        for (Pair<String, SortType> sortValue: getSortCategories()) {
            if(isFirst) {
                isFirst = false;
            }
            else {
                stringBuilder.append(',');
            }
            stringBuilder.append(' ').append(sortValue.first).append(" ").append(sortValue.second.toString());
        }
        return stringBuilder.toString();
    }
}
