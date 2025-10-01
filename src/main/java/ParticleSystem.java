
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

    public static ParticleSystem createRandomPositionsByDensity(int particleCount, double density) {

        Random r = new Random();

        // Calculate radius from density
        double volume = particleCount / density;
        double radius = Math.cbrt(3.0 * volume / (4.0 * Math.PI));

        System.out.printf("Creating uniform system: %d particles, density %.3f → radius %.3f\n",
                particleCount, density, radius);

        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < particleCount; i++) {
            Vector vel = new Vector(0, 0, 0);
            double r_uniform = radius * Math.cbrt(r.nextDouble());
            double theta = r.nextDouble() * 2 * Math.PI;
            double phi = Math.acos(2 * r.nextDouble() - 1);
            Vector pos = Vector.fromPolar(r_uniform, theta, phi);
            particles.add(new Particle(pos, vel));
        }
        return new ParticleSystem(particles);
    }
    public static ParticleSystem createRandomPositionsByRadius(int particleCount, double radius) {

        Random r = new Random();


        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < particleCount; i++) {
            Vector vel = new Vector(0, 0, 0);
            double r_uniform = radius * Math.cbrt(r.nextDouble());
            double theta = r.nextDouble() * 2 * Math.PI;
            double phi = Math.acos(2 * r.nextDouble() - 1);

            Vector pos = Vector.fromPolar(r_uniform, theta, phi);
            particles.add(new Particle(pos, vel));
        }
        return new ParticleSystem(particles);
    }



    //future purpose
    public void putRandomCharges(){
        Random r = new Random();
        for(Particle p: this.particles){
            if(r.nextBoolean()){
                p.putCharge(Math.round(r.nextDouble() * 100.0) / 10.0);
            }
            else{
                p.putCharge(-Math.round(r.nextDouble() * 100.0) / 10.0);
            }
        }
    }

    public void putIDs() {
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
    public SimulationComparer compare(ParticleSystem reference){
        if (this.particles.size() != reference.particles.size()) {
            throw new IllegalArgumentException("Cannot compare systems with different particle counts");
        }

        double[] posErrors = calculatePositionDiff(reference);

        double[] velErrors = calculateVelocityDiff(reference);



        // Structural errors
        double comDrift = calculateCenterOfMassDrift(reference);
        double pairDistError = calculateMeanPairDistanceError(reference);

        double spreadError = calculateSystemSpreadDiff(reference);

        return new SimulationComparer(
                posErrors[0], posErrors[1], posErrors[2],
                velErrors[0], velErrors[1], velErrors[2],
                comDrift, pairDistError, spreadError
        );
    }
    double[] calculatePositionDiff(ParticleSystem reference){

        double sumError= 0.0;
        double sumSquaredError = 0.0;
        double maxError = 0.0;
        for(int i = 0; i< particles.size();i++){
            double error = particles.get(i).position.subtract(reference.particles.get(i).position).getMagnitude();
            sumError += error;
            sumSquaredError += error*error;
            maxError = Math.max(maxError,error);
        }
        double mean = sumError / particles.size();
        double rmse = Math.sqrt(sumSquaredError / particles.size());
        return new double[]{mean, rmse, maxError};
    }

     double[] calculateVelocityDiff(ParticleSystem reference){

        double sumError= 0.0;
        double sumSquaredError = 0.0;
        double maxError = 0.0;
        for(int i = 0; i< particles.size();i++){
            double error = particles.get(i).velocity.subtract(reference.particles.get(i).velocity).getMagnitude();
            sumError += error;
            sumSquaredError += error*error;
            maxError = Math.max(maxError,error);
        }
        double mean = sumError / particles.size();
        double rmse = Math.sqrt(sumSquaredError / particles.size());
        return new double[]{mean, rmse, maxError};
    }


     double calculateSystemSpreadDiff(ParticleSystem reference) {
        double thisSpread = calculateSystemSpread();
        double refSpread = reference.calculateSystemSpread();
        return Math.abs(thisSpread - refSpread) / refSpread;
    }
     double calculateCenterOfMassDrift(ParticleSystem reference) {
        Vector thisCOM = this.centerOfGravity();
        Vector refCOM = reference.centerOfGravity();
        return thisCOM.subtract(refCOM).getMagnitude();
    }
     double calculateMeanPairDistanceError(ParticleSystem reference) {
        double sumError = 0.0;
        int pairCount = 0;

        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                double thisDist = particles.get(i).position
                        .subtract(particles.get(j).position).getMagnitude();
                double refDist = reference.particles.get(i).position
                        .subtract(reference.particles.get(j).position).getMagnitude();
                sumError += Math.abs(thisDist - refDist);
                pairCount++;
            }
        }

        return sumError / pairCount;
    }
     double calculateSystemSpread() {
        Vector com = centerOfMass();
        double sumSquaredDistance = 0.0;
        for (Particle p : particles) {
            double dist = p.position.subtract(com).getMagnitude();
            sumSquaredDistance += dist * dist;
        }
        return Math.sqrt(sumSquaredDistance / particles.size());
    }

     double calculateKineticEnergy() {
        double totalKE = 0.0;
        for (Particle p : particles) {
            double v2 = p.velocity.getMagnitude();
            v2 = v2 * v2;
            totalKE += 0.5 * p.mass * v2;
        }
        return totalKE;
    }

    public Vector centerOfMass() {
        var massWeightedSum = new Vector(0, 0, 0);
        double totalMass = 0.0;

        for (Particle p : this.particles) {
            massWeightedSum = massWeightedSum.add(p.position.scale(p.mass));
            totalMass += p.mass;
        }

        return massWeightedSum.scale(1.0 / totalMass);
    }
}
