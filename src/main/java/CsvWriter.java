import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CsvWriter implements AutoCloseable {

     protected BufferedWriter writer;

    public CsvWriter(Path  file) throws IOException {
        writer = Files.newBufferedWriter(file);
    }



    protected String toCSV(Vector v) {
        return Arrays.stream(v.components).mapToObj(Double::toString).collect(Collectors.joining(","));
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
