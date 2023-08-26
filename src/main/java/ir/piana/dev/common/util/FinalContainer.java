package ir.piana.dev.common.util;

public class FinalContainer<T> {
    private T field;

    public FinalContainer(T field) {
        this.field = field;
    }

    public FinalContainer() {
    }

    public void set(T field) {
        this.field = field;
    }

    public T get() {
        return field;
    }

    public boolean nonNull() {
        return field != null;
    }

    public boolean isNull() {
        return field == null;
    }
}
