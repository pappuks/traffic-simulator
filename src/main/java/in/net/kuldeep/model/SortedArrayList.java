package in.net.kuldeep.model;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T extends Car> extends ArrayList<T> {

    @SuppressWarnings("unchecked")
    public void insertSorted(T value) {
        add(value);
        for (int i = size()-1; i > 0 && value.compareLocationTo(get(i-1)) < 0; i--)
            Collections.swap(this, i, i-1);
    }
}
