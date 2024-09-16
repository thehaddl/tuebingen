import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {
    private final String filePath;
    private static boolean isHeaderWritten = false;
    csvWriter(String pathToFile){
        this.filePath = pathToFile;
    }
    void writeParticlesToCsv(Particle[] particles) throws IOException {
        File file = new File(filePath);
        boolean fileExists = new File(filePath).exists();
        try (FileWriter writer = new FileWriter(filePath,true)) {
            if(!fileExists || !isHeaderWritten) {
                writer.write("Position_X,Position_Y,Position_Z,Velocity_X,Velocity_Y,Velocity_Z,\n");
                isHeaderWritten = true;
            }
            for (Particle p : particles) {
                writer.write(p.toCsvString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
