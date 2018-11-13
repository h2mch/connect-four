package ch.h2m.connect.four.model;

public enum Disc {
    EMPTY,
    RED,
    YELLOW;

    public static Disc converse(Disc dics) {
        switch (dics) {
            case EMPTY:
                throw new IllegalArgumentException("Disc can not be Empty");
            case YELLOW:
                return RED;
            case RED:
                return YELLOW;
        }
        throw new IllegalArgumentException("Unknown Disc type");
    }


    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isNonEmpty() {
        return this != EMPTY;
    }

}
