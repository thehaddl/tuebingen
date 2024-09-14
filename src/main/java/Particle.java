class Particle{
    Vector position;
    Vector velocity;
    Vector force;
    public Particle(Vector pos, Vector vel, Vector f){
        this.position = pos;
        this.velocity = vel;
        this.force = f;
    }

    public double[][] particleToArray() {
        double[][] p = new double[3][];
        p[0] = position.getComponents();
        p[1] = velocity.getComponents();
        p[2] = force.getComponents();

        return p;
    }

}