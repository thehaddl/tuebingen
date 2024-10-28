import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleSystemTest {
    @Test
    void centerOfGravity_should_return_center_of_gravity () {
        // setup
        Particle[] p1 = new Particle[]{
                new Particle(new Vector(1,1,0), new Vector(0,0,0)),
                new Particle(new Vector(-1,-1,0), new Vector(0,0,0))
        };
        ParticleSystem p = ParticleSystem.createFrom(p1);
        //execute
        var actual = p.centerOfGravity();
        // verify
        assertEquals(new Vector(0,0,0), actual);
    }
    @Test
    void getParticles_should_return_particles () {
        // setup
        Particle[] p1 = new Particle[]{
                new Particle(new Vector(1,1,0), new Vector(0,0,0)),
                new Particle(new Vector(-1,-1,0), new Vector(0,0,0))
        };
        ParticleSystem p = ParticleSystem.createFrom(p1);
        //execute
        var actual = p.getParticles();
        // verify
        assertArrayEquals(p1, actual);
    }

    @Test
    void createRandomPosition_should_return_System_with_random_positions(){
        ParticleSystem p = ParticleSystem.createRandomPositions(17,100);
        
        assertEquals(17,p.getParticles().length);
        for(Particle a : p.getParticles()){
            for(var b : a.position.components){
                assertTrue(b<100 && b >-100);
            }

        }
    }

}
