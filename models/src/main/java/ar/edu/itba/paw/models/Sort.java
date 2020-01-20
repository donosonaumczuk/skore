package ar.edu.itba.paw.models;

import javafx.util.Pair;

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
                                sortCategories.add(new Pair<>(queryFields.get(validFields.indexOf(auxCategory[0])), sortType));
                            } else {
                                sortCategories.set(position.get(),
                                        new Pair<>(queryFields.get(validFields.indexOf(auxCategory[0])), sortType));
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
            if (pair.getKey().equals(category)) {
                return Optional.of(position);
            }
            position++;
        }
        return Optional.empty();
    }
}