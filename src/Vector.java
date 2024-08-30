import javv.util.Math
class Vector {
    double[] components;

    Vector(double x, double y, double z) {
        components = new double[]{x, y, z};
    }
    Vector(double x, double y, double z) {
        components = new double[];
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

    public double[] getComponents() {
        return this.components;
    }

    Vector scale(double scalar) {
        Vector k = new Vector(
                this.components[0] * scalar,
                this.components[1] * scalar,
                this.components[2] * scalar
        );
        return k;
    }
    double getMagnitude(){
    return Math.sqrt(this.components[0]*this.components[0]+this.components[1]*this.components[1]+this.components[2]*this.components[2]);
    }
}
    double calcDistance(Vector vec2){
        return this.subtract(vec2).getMagnitude();

    
}
