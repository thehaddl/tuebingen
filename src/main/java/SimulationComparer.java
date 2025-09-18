public class SimulationComparer {

    public final double meanPositionError;
    public final double rmsePositionError;
    public final double maxPositionError;

    public final double meanVelocityError;
    public final double rmseVelocityError;
    public final double maxVelocityError;



    public final double centerOfMassDrift;
    public final double meanPairDistanceError;
    public final double systemSpreadError;

    public SimulationComparer(double meanPosErr, double rmsePosErr, double maxPosErr,
                                double meanVelErr, double rmseVelErr, double maxVelErr,
                                double comDrift, double pairDistErr, double spreadErr) {
        this.meanPositionError = meanPosErr;
        this.rmsePositionError = rmsePosErr;
        this.maxPositionError = maxPosErr;
        this.meanVelocityError = meanVelErr;
        this.rmseVelocityError = rmseVelErr;
        this.maxVelocityError = maxVelErr;

        this.centerOfMassDrift = comDrift;
        this.meanPairDistanceError = pairDistErr;
        this.systemSpreadError = spreadErr;
    }
    public double getOverallError() {

        return Math.max(rmsePositionError, rmseVelocityError);}


    @Override
    public String toString() {
        return String.format("""
            Simulation Comparison Results:
            Position Errors:
              Mean: %.6f, RMSE: %.6f, Max: %.6f
            Velocity Errors:
              Mean: %.6f, RMSE: %.6f, Max: %.6f

            Structural Errors:
              Center of Mass Drift: %.6f
              Mean Pair Distance Error: %.6f
              System Spread Error: %.6f
            Overall Error Score: %.6f
            """,
                meanPositionError, rmsePositionError, maxPositionError,
                meanVelocityError, rmseVelocityError, maxVelocityError,

                centerOfMassDrift, meanPairDistanceError, systemSpreadError,
                getOverallError());
    }
    public double getMeanPositionError() {
        return meanPositionError;
    }
    public double getMeanVelocityError() {
        return meanVelocityError;
    }
    public double getMaxPositionError() {
        return maxPositionError;
    }
    public double getRmsePositionError() {
        return rmsePositionError;
    }
    public double getRmseVelocityError() {
        return rmseVelocityError;
    }
    public double getMaxVelocityError() {
        return  maxVelocityError;
    }


    public double getMeanPairDistanceError() {
        return meanPairDistanceError;
    }
    public double getSystemSpreadError(){
        return systemSpreadError;
    }

    public double getCenterOfMassDrift(){
        return centerOfMassDrift;
    }

}

