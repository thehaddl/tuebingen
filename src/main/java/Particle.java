class Particle{
    Vector position;
    Vector velocity;
    public Particle(Vector pos, Vector vel){
        this.position = pos;
        this.velocity = vel;
    }
    public Particle(Particle p){
        this.position = p.position;
        this.velocity = p.velocity;
    }
    String toCsvString(){
        String out = this.position.toCsvString() + this.velocity.toCsvString();
        return out;
    }
}