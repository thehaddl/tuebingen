
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    private final List<Particle> particles;

    private ParticleSystem(List<Particle>  particles) {
        this.particles = particles;
    }

    public List<Particle>  getParticles() {
        List<Particle> copy = new ArrayList<>();
        for (Particle particle : particles) {
            copy.add(new Particle(particle));
        }
        return copy;
    }

    public Vector centerOfGravity() {
        var sum = new Vector(0, 0, 0);
        for (var p : this.particles) {
            sum = sum.add(p.position);
        }
        return sum.scale(1.0 / particles.size());
    }

    public static ParticleSystem createRandomPositions(int particleCount, double dimension) {
        Random r = new Random();
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < particleCount; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = Vector.fromPolar(r.nextDouble() * dimension, r.nextDouble() * Math.PI *2, r.nextDouble() * Math.PI *2);
            //Vector pos = new Vector(r.nextDouble() * dimension,r.nextDouble() * dimension,r.nextDouble() * dimension);
            particles.add(new Particle(pos, vel));
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
    //future purpose

    public void putIDs() {
        Random r = new Random();
        int i = 1;
        for (Particle p : this.particles) {
            p.putID(i);
            i+=1;
        }
    }


    public static ParticleSystem createFrom(List<Particle> particles) {
        List<Particle> copy = new ArrayList<>();
        for(Particle p: particles){
            copy.add(new Particle(p));
        }
        return new ParticleSystem(copy);

    }

    public double calcAverageDeviation(ParticleSystem reference) {
        double deviation = 0.0;

        for (int i = 0; i < this.particles.size(); i++) {
            Particle a = this.particles.get(i);
            Particle b = reference.particles.get(i);
            deviation += a.position.subtract(b.position).getMagnitude();
        }
        return deviation / this.particles.size();
    }

}
