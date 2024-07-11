class Particle {
    Vector position;
    Vector velocity;

    Particle(Vector position, Vector velocity) {
        this.position = new Vector(position.toArray());
        this.velocity = new Vector(velocity.toArray());
    }

    Vector calculateInteractionForce(Vector otherPosition) {
       Vector vec = otherPosition.subtract(this.position);
        return vec.scale(vec.lengthSquared());
    }
}
