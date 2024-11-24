class Particle implements Cloneable {

    double mass;
    double charge;
    Vector position;
    Vector velocity;
    static final double GRAVITY = 6.67;
    static final double ELECTRICFIELDCONST = 8.8541878188 * Math.pow(10, -12);


    public Particle(Vector pos, Vector vel) {
        mass = 1;
        charge = 0;
        this.position = pos;
        this.velocity = vel;
    }

    public Particle(Vector pos, Vector vel, double m, double c) {
        mass = m;
        charge = c;
        this.position = pos;
        this.velocity = vel;
    }

    public Particle(Particle p) {
        mass = p.mass;
        charge = p.charge;
        this.position = p.position;
        this.velocity = p.velocity;
    }

    public void putCharge(double c) {
        this.charge = c;
    }

    Vector getGravitationalForce(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (GRAVITY * this.mass * other.mass) / ((distance * distance) + 1);
        return vec.getUnitVec().scale(forceScalar);
    }

    Vector getElectricalForce(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (this.charge * other.charge) / ((distance * distance*4*Math.PI*ELECTRICFIELDCONST) + 1);
        return vec.getUnitVec().scale(forceScalar);
    }


}