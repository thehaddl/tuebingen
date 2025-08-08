class Particle implements Cloneable {

    boolean inuse;
    double mass;
    int id;
    double charge;
    Vector position;
    Vector velocity;

    //standard particle
    public Particle(Vector pos, Vector vel) {
        mass = 1;
        charge = 0;
        inuse = false ;
        this.position = pos;
        this.velocity = vel;
    }
    //particle with specified mass and charge

    public Particle(Vector pos, Vector vel, double m, int id,int charge) {
        mass = m;
        this.id = id;
        this.position = pos;
        this.velocity = vel;
        this.charge = charge;
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
    public void putCharge(double charge){
        this.charge = charge;
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
    //adjust gravitational force to prevent singularities
    Vector getGravitationalForceWithoutSingularities(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = (this.mass * other.mass) / ((distance * distance)+10);
        return vec.getUnitVec().scale(forceScalar);
    }
    Vector getCouloumbForce(Particle other){
        var vec = this.position.subtract(other.position);
        var distance = vec.getMagnitude();
        double forceScalar=(this.charge * other.charge) / ((distance * distance));
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