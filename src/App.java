import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    private static final String COMPRESSION_EXTENSION = "hc";
    private static final String ID = "17010210";

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java -jar huffman_<id>.jar <c|d> <inputfile> [<n>]");
            return;
        }

        String mode = args[0];
        String inputFile = args[1];
        String fileExtension = getFileExtension(inputFile);
        String directory = inputFile.substring(0, inputFile.lastIndexOf(File.separator));
        String fileName = inputFile.substring(inputFile.lastIndexOf(File.separator) + 1, inputFile.lastIndexOf("."));

        if (mode.equals("c")) {
            if (args.length < 3) {
                System.out.println("Usage: java -jar huffman_<id>.jar c <inputfile> <n>");
                return;
            }

            int n = Integer.parseInt(args[2]);
            String outputFilePath = directory + File.separator + ID + "." + n + "." + fileName + "." + COMPRESSION_EXTENSION;

            long startTime = System.currentTimeMillis();
            Huffman.compressFile(inputFile, outputFilePath, fileExtension, n);
            long endTime = System.currentTimeMillis();
            System.out.println("Compression time: " + (endTime - startTime) + " ms");
            long sizeBefore = Files.size(Paths.get(inputFile));
            long sizeAfter = Files.size(Paths.get(outputFilePath));
            System.out.println("before: " + sizeBefore + ",\tafter: " + sizeAfter + ",\tratio: " + ((double) sizeAfter) / ((double) sizeBefore));
        } else if (mode.equals("d")) {
            String outputFilePath = directory + File.separator + "extracted." + fileName;
            long startTime = System.currentTimeMillis();
            Huffman.decompressFile(inputFile, outputFilePath);
            long endTime = System.currentTimeMillis();
            System.out.println("Decompression time: " + (endTime - startTime) + " ms");
        }
    }

    private static String getFileExtension(String filePath) {
        int lastIndexOfDot = filePath.lastIndexOf(".");
        if (lastIndexOfDot == -1) { // No extension.
            return "";
        }
        return filePath.substring(lastIndexOfDot);
    }
}
