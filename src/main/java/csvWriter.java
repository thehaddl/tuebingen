import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {
    final String filePath;
    File file;
    csvWriter(String pathToFile){
        filePath = pathToFile;
    }
    void writeParticlesToCsv(Particle[] particles) throws IOException {
        file = new File(filePath);
        try (FileWriter writer = new FileWriter(file,true)) {
            for (Particle p : particles) {
                writer.write(p.toCsvString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
