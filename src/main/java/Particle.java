class Particle{
    Vector position;
    Vector velocity;
    Vector force;
    public Particle(Vector pos, Vector vel, Vector f){
        this.position = new Vector(pos.getComponents);
        this.velocity = new Vector(vel.getComponents);
        this.force = new Vector(f.getComponents);
    }

    public double[][] particleToArray() {
        double[][] p = new double[3][];
        p[0] = position.getComponents();
        p[1] = velocity.getComponents();
        p[2] = force.getComponents();

        return p;
    }

}