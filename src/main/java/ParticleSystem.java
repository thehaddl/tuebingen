import java.nio.file.Path;
import java.util.Random;

public class ParticleSystem {

    private final Particle[] particles;

    private ParticleSystem(Particle[] particles) {
        this.particles = particles;
    }

    public Particle[] getParticles() {
        return particles.clone();
    }

    int systemCount;
    double dimension;

    public ParticleSystem(int systemCount, double dimension) {
        this.systemCount = systemCount;
        particles = new Particle[systemCount];
        this.dimension = dimension;
    }

    public Vector centerOfGravity() {
        var sum = new Vector(0, 0, 0);
        for (var p : this.particles) {
            sum = sum.add(p.position);
        }
        return sum.scale(1.0 / particles.length);
    }

    public static ParticleSystem createFrom(Particle[] particles) {
        return new ParticleSystem(particles.clone());
    }

    public static ParticleSystem createRandomPositions(int particleCount, double dimension) {
        Random r = new Random();
        var particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            Vector vel = new Vector(0, 0, 0);
            Vector pos = new Vector(r.nextDouble() * dimension, r.nextDouble() * dimension, r.nextDouble() * dimension);
            particles[i] = new Particle(pos, vel);
        }
        return new ParticleSystem(particles);
    }

}
