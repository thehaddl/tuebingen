class Particle implements Cloneable {

    Vector position;
    Vector velocity;
    static final double GRAVITY= 6.67;

    public Particle(Vector pos, Vector vel){
        this.position = pos;
        this.velocity = vel;
    }
    public Particle(Particle p){
        this.position = p.position;
        this.velocity = p.velocity;
    }

    Vector getGavitationalForce(Particle other) {
        var vec = other.position.subtract(this.position);
        var distance = vec.getMagnitude();
        double forceScalar = GRAVITY /((distance* distance)+1);
        return vec.getUnitVec().scale(forceScalar);
    }

}