import java.util.Arrays;
import java.util.Objects;

class Vector {
    final double[] components;

    Vector(double x, double y, double z) {
        components = new double[]{x, y, z};
    }

    public Vector add(Vector v) {
        return new Vector(
                this.components[0] + v.components[0],
                this.components[1] + v.components[1],
                this.components[2] + v.components[2]
        );
    }

    public Vector subtract(Vector v) {
        return new Vector(
                this.components[0] - v.components[0],
                this.components[1] - v.components[1],
                this.components[2] - v.components[2]
        );
    }

    Vector scale(double scalar) {
        return new Vector(
                this.components[0] * scalar,
                this.components[1] * scalar,
                this.components[2] * scalar
        );
    }

    double getMagnitude() {
        return Math.sqrt(this.components[0] * this.components[0] + this.components[1] * this.components[1] + this.components[2] * this.components[2]);
    }

    double calcDistance(Vector vec2) {
        return this.subtract(vec2).getMagnitude();
    }

    Vector getUnitVec(){
        return this.scale(1/this.getMagnitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector vector)) return false;
        return Objects.deepEquals(components, vector.components);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(components);
    }

    @Override
    public String toString() {
        return "Vector" + Arrays.toString(components);
    }
}
