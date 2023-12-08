import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
//        Map<ArrayList<Byte>, String> codes = Huffman.getCodes("data/temp_input.txt", 1);
        Huffman.compressFile("data/input.txt","data/temp_compressed.huff", 7);
        Huffman.decompressFile("data/temp_compressed.huff", "data/temp_decompressed.txt");
    }
}
