
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

    Vector interactionForce(Vector pos) {
        double forceScalar = (1 / this.calcDistance(pos) * this.calcDistance(pos)) * 6.67;
        return pos.scale(forceScalar);
    }
    String toCsvString(){
        String out = "";
        for(int i = 0; i < 3; i++){
            out += this.components[i] + ",";
        }
        return out;
    }
}
