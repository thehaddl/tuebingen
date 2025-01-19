import java.util.Random;

public class ParticleSystem {

    private final Particle[] particles;

    private ParticleSystem(Particle[] particles) {
        this.particles = particles;
    }

    public Particle[] getParticles() {
        Particle[] copy = new Particle[particles.length];
        for (int i = 0; i < particles.length; i++) {
            copy[i] = new Particle(particles[i]);
        }
        return copy;
    }

    public Vector centerOfGravity() {
        var sum = new Vector(0, 0, 0);
        for (var p : this.particles) {
            sum = sum.add(p.position);
        }
        return sum.scale(1.0 / particles.length);
    }

    public static ParticleSystem createRandomPositions(int particleCount, double dimension) {
        Random r = new Random();
        var particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = Vector.fromPolar(r.nextDouble() * dimension, r.nextDouble() * Math.PI *2, r.nextDouble() * Math.PI *2);
            //Vector pos = new Vector(r.nextDouble() * dimension,r.nextDouble() * dimension,r.nextDouble() * dimension);
            particles[i] = new Particle(pos, vel);
        }
        return new ParticleSystem(particles);
    }

    public void putRandomVelocities(double dimension){
        Random r = new Random();
        for(Particle p : this.particles){
            Vector vel = Vector.fromPolar(r.nextDouble() * dimension, r.nextDouble()*2 * Math.PI, r.nextDouble()*2 * Math.PI);
            p.setVelocity(vel);
        }
    }

    public void putRandomCharges() {
        Random r = new Random();
        for (Particle p : this.particles) {
            if (r.nextBoolean()) {
                p.putCharge(r.nextDouble() * (-1));
            } else {
                p.putCharge(r.nextDouble());
            }
        }
    }
    public void putRandomUniformCharges() {
        Random r = new Random();
        for (Particle p : this.particles) {
            if (r.nextBoolean()) {
                p.putCharge(-1);
            } else {
                p.putCharge(1);
            }
        }
    }


    public static ParticleSystem createFrom(Particle[] particles) {
        return new ParticleSystem(particles.clone());
    }

    public double calcAverageDeviation(ParticleSystem reference) {
        double deviation = 0.0;
        for (int i = 0; i < this.particles.length; i++) {
            Particle a = this.particles[i];
            Particle b = reference.particles[i];
            deviation += a.position.subtract(b.position).getMagnitude();
        }
        return deviation / this.particles.length;
    }
}
