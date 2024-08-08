class Vector {
    double[] components;

    Vector(double x, double y, double z) {
        components = new double[]{x, y, z};
    }

    public Vector add(Vector v) {
        Vector k = new Vector(
                this.components[0] + v.components[0],
                this.components[1] + v.components[1],
                this.components[2] + v.components[2]
        );
        return k;
    }

    public Vector subtract(Vector v) {
        Vector k = new Vector(
                this.components[0] - v.components[0],
                this.components[1] - v.components[1],
                this.components[2] - v.components[2]
        );
        return k;
    }

    Vector scale(double scalar) {
        Vector k = new Vector(
                this.components[0] * scalar,
                this.components[1] * scalar,
                this.components[2] * scalar
        );
        return k;
    }
    
}
