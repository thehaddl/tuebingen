class Particle implements Cloneable {

    double mass;
    double charge;
    Vector position;
    Vector velocity;

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
        double forceScalar = (this.mass * other.mass) / ((distance * distance));
        return vec.getUnitVec().scale(forceScalar);
    }
    //adjust gravitational for to prevent singularities
    Vector getGravitationalForceWithoutSingularities(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (this.mass * other.mass) / ((distance * distance));
        return vec.getUnitVec().scale(forceScalar);
    }

    Vector getSwarmForce(Particle other){
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        var dv = other.velocity.subtract(this.velocity).getMagnitude();
        double forceScalar = dv/Math.pow((1+distance),0.5);
        return vec.getUnitVec().scale(forceScalar);
    }



}