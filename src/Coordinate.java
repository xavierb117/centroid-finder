import java.util.Objects;

public record Coordinate(double x, double y) {
        private static final double EPSILON = 1e-9;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate other)) return false;
        return Math.abs(x - other.x) < EPSILON &&
               Math.abs(y - other.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            Math.round(x / EPSILON),
            Math.round(y / EPSILON)
        );
    }
}
