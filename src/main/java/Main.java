class Main{
    public static void main(String[] args) {
        Simulation s = new Simulation(100, 50, 100,1);
        s.runSim();
        for(Particle p : s.particles)
            System.out.println(Array.deepToString(p));
    }
}