package ch.h2m.connect.four.model;

public enum Disc {
    EMPTY,
    RED,
    YELLOW;

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }

    public Disc opposite() {

        switch (this) {
            case RED:
                return YELLOW;
            case YELLOW:
                return RED;
        }
        return EMPTY;
    }
}
