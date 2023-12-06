public class App {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java App <compress|decompress> <inputFilePath> <outputFilePath>");
            return;
        }

        String operation = args[0];
        String inputFilePath = args[1];
        String outputFilePath = args[2];

        if ("compress".equals(operation)) {
            Huffman.compressFile(inputFilePath, outputFilePath);
        } else if ("decompress".equals(operation)) {
            Huffman.decompressFile(inputFilePath, outputFilePath);
        } else {
            System.out.println("Invalid operation. Use 'compress' or 'decompress'.");
        }
    }
}
