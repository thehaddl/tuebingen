
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
        // Position errors
        double[] posErrors = calculatePositionError(reference);

        // Velocity errors
        double[] velErrors = calculateVelocityError(reference);

        // Energy errors
        double[] energyErrors = calculateEnergyErrors(reference);

        // Structural errors
        double comDrift = calculateCenterOfMassDrift(reference);
        double pairDistError = calculateMeanPairDistanceError(reference);
        double spreadError = calculateSystemSpreadError(reference);

        return new SimulationComparer(
                posErrors[0], posErrors[1], posErrors[2],  // mean, rmse, max position
                velErrors[0], velErrors[1], velErrors[2],  // mean, rmse, max velocity
                energyErrors[0], energyErrors[1], energyErrors[2], // kinetic, potential, total
                comDrift, pairDistError, spreadError
        );
    }
    private double[] calculatePositionError(ParticleSystem reference){
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
    private double[] calculateVelocityError(ParticleSystem reference){
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
    private double[] calculateEnergyErrors(ParticleSystem reference) {
        double thisKinetic = calculateKineticEnergy();
        double refKinetic = reference.calculateKineticEnergy();
        double kineticError = Math.abs(thisKinetic - refKinetic) / Math.abs(refKinetic);

        double thisPotential = calculatePotentialEnergy();
        double refPotential = reference.calculatePotentialEnergy();
        double potentialError = Math.abs(thisPotential - refPotential) / Math.abs(refPotential);

        double thisTotal = thisKinetic + thisPotential;
        double refTotal = refKinetic + refPotential;
        double totalError = Math.abs(thisTotal - refTotal) / Math.abs(refTotal);

        return new double[]{kineticError, potentialError, totalError};
    }
    private double calculateSystemSpreadError(ParticleSystem reference) {
        double thisSpread = calculateSystemSpread();
        double refSpread = reference.calculateSystemSpread();
        return Math.abs(thisSpread - refSpread) / refSpread;
    }
    private double calculateCenterOfMassDrift(ParticleSystem reference) {
        Vector thisCOM = this.centerOfGravity();
        Vector refCOM = reference.centerOfGravity();
        return thisCOM.subtract(refCOM).getMagnitude();
    }
    private double calculateMeanPairDistanceError(ParticleSystem reference) {
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
    private double calculateSystemSpread() {
        Vector com = centerOfMass();
        double sumSquaredDistance = 0.0;
        for (Particle p : particles) {
            double dist = p.position.subtract(com).getMagnitude();
            sumSquaredDistance += dist * dist;
        }
        return Math.sqrt(sumSquaredDistance / particles.size());
    }
    private double calculatePotentialEnergy() {
        double totalPE = 0.0;
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                Particle pi = particles.get(i);
                Particle pj = particles.get(j);
                double r = pi.position.subtract(pj.position).getMagnitude();
                if (r > 0) { // Avoid division by zero
                    totalPE += pi.charge * pj.charge / r; // Coulomb potential
                }
            }
        }
        return totalPE;
    }
    private double calculateKineticEnergy() {
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
