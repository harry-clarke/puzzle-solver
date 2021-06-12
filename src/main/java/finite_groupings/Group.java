package finite_groupings;

import java.util.Set;

/**
 */
public interface Group<E> {
    Set<Cell<E>> getAllCells();

    Set<E> getValues();
}
