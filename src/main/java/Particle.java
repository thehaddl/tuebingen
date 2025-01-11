class Particle implements Cloneable {

    double mass;
    double charge;
    Vector position;
    Vector velocity;
    static final double GRAVITY = 6.67;
    static final double ELECTRICFIELDCONST = 8.8541878188 * Math.pow(10, -12);

    //standard particle
    public Particle(Vector pos, Vector vel) {
        mass = 1;
        charge = 0;
        this.position = pos;
        this.velocity = vel;
    }
    //particle with specified mass and charge
    public Particle(Vector pos, Vector vel, double m, double c) {
        mass = m;
        charge = c;
        this.position = pos;
        this.velocity = vel;
    }
    //copy constructor
    public Particle(Particle p) {
        mass = p.mass;
        charge = p.charge;
        this.position = p.position;
        this.velocity = p.velocity;
    }
    //puts new charge on existing particle
    public void putCharge(double c) {
        this.charge = c;
    }
    public void setVelocity(Vector vel){
        this.velocity = vel;
    }
    //term for the gravitational force
    Vector getGravitationalForce(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (GRAVITY * this.mass * other.mass) / ((distance * distance));
        return vec.getUnitVec().scale(forceScalar);
    }
    //term for the electrical force
    Vector getElectricalForce(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = -(this.charge * other.charge) / ((distance * distance*4*Math.PI*ELECTRICFIELDCONST));
        return vec.getUnitVec().scale(forceScalar);
    }
    //adjust gravitational for to prevent singularities
    Vector getGravitationalForceWithoutSingularities(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (GRAVITY * this.mass * other.mass) / ((distance * distance+1));
        return vec.getUnitVec().scale(forceScalar);
    }
    //adjust electrical for to prevent singularities
    Vector getElectricalForceWithoutSingularities(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = -(this.charge * other.charge) / ((distance * distance*4*Math.PI*ELECTRICFIELDCONST+1));
        return vec.getUnitVec().scale(forceScalar);
    }



}