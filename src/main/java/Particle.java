class Particle{
    Vector position;
    Vector velocity;
    public Particle(Vector pos, Vector vel){
        this.position = pos;
        this.velocity = vel;
    }
    String toCsvString(){
        String out = this.position.toCsvString() + this.velocity.toCsvString();
        return out;
    }
}