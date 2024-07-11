import java.util.Arrays
class Vector {
    double[] components;

    Vector(double x, double y, double z) {
        components = new double[]{x, y, z};
    }

    Vector(double[] components) {
        this.components = Arrays.copyOf(components, components.length);
    }

    Vector add(Vector other) {
        return new Vector(
                this.components[0] + other.components[0],
                this.components[1] + other.components[1],
                this.components[2] + other.components[2]
        );
    }

    Vector subtract(Vector other) {
        return new Vector(
                this.components[0] - other.components[0],
                this.components[1] - other.components[1],
                this.components[2] - other.components[2]
        );
    }

    Vector scale(double scalar) {
        return new Vector(
                this.components[0] * scalar,
                this.components[1] * scalar,
                this.components[2] * scalar
        );
    }

    double lengthSquared() {
        return this.components[0] * this.components[0] +
               this.components[1] * this.components[1] +
               this.components[2] * this.components[2];
    }

    double[] toArray() {
        return Arrays.copyOf(this.components, this.components.length);
    }
}
