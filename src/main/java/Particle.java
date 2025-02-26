class Particle implements Cloneable {
    boolean inuse;
    double mass;
    int id;
    Vector position;
    Vector velocity;

    //standard particle
    public Particle(Vector pos, Vector vel) {
        mass = 1;
        inuse = false ;
        this.position = pos;
        this.velocity = vel;
    }
    //particle with specified mass and charge
    public Particle(Vector pos, Vector vel, double m, int id) {
        mass = m;
        this.id = id;
        this.position = pos;
        this.velocity = vel;
    }
    //copy constructor
    public Particle(Particle p) {
        mass = p.mass;
        id = p.id;
        this.position = p.position;
        this.velocity = p.velocity;
    }
    //puts new charge on existing particle
    public void putID(int id) {
        this.id = id;
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
    //not yet validated
    Vector getSwarmForce(Particle other){
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        var dv = other.velocity.subtract(this.velocity).getMagnitude();
        double forceScalar = dv/Math.pow((1+distance),0.5);
        return vec.getUnitVec().scale(forceScalar);
    }



}