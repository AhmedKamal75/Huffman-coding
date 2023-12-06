import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Huffman {
    public static Map<Character, String> getCodes(String inputFilePath) throws IOException {
        // Step 1: Read the file and calculate frequencies.
        FileReader fr = new FileReader(inputFilePath);
        HashMap<Character, Integer> map = new HashMap<>();
        int c;
        while ((c = fr.read()) != -1) {
            char character = (char) c;
            map.put(character, map.getOrDefault(character, 0) + 1);
        }
        fr.close();

        // Step 2: Build the Huffman tree.
        PriorityQueue<State> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            queue.add(new State(entry.getKey(), entry.getValue()));
        }
        while (queue.size() > 1) {
            State left = queue.poll();
            State right = queue.poll();
            assert right != null;
            queue.add(new State(left, right, '\\', left.getCount() + right.getCount()));
        }

        // Step 3: Generate Huffman codes.
        Map<Character, String> codes = new HashMap<>();
        if (!queue.isEmpty()) {
            queue.peek().buildCode("", codes);
        }

        // visualize the tree.
//         assert queue.peek() != null;
//         queue.peek().printTree("",true);

        return codes;
    }

    public static void compressFile(String inputFilePath, String outputFilePath) throws IOException {
        // Step 0: get the Huffman code
        Map<Character, String> huffmanCodes = getCodes(inputFilePath);

        // Step 1: Read the file.
        String text = new String(Files.readAllBytes(Paths.get(inputFilePath)));

        // Step 2: Encode the file.
        StringBuilder encodedData = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedData.append(huffmanCodes.get(c));
        }

        // Step 3: Write the Huffman codes and the encoded data to a file.
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            // Write the number of Huffman codes.
            out.writeInt(huffmanCodes.size());

            // Write each Huffman code.
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                out.writeChar(entry.getKey());
                out.writeUTF(entry.getValue());
            }

            // Calculate padding.
            int padding = 8 - (encodedData.length() % 8);
            padding = padding == 8 ? 0 : padding;  // No padding if length is already a multiple of 8.

            // Write the number of padding bits.
            out.writeByte(padding);

            // Add padding to the encoded data.
            encodedData.append("0".repeat(padding));

            // Write the encoded data.
            for (int i = 0; i < encodedData.length(); i += 8) {
                String byteStr = encodedData.substring(i, i + 8);
                int byteVal = Integer.parseInt(byteStr, 2);
                out.writeByte(byteVal);
            }
        }
    }

    public static void decompressFile(String inputFilePath, String outputFilePath) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFilePath))) {
            // Read the number of Huffman codes.
            int numCodes = in.readInt();

            // Read each Huffman code.
            Map<String, Character> huffmanCodes = new HashMap<>();
            for (int i = 0; i < numCodes; i++) {
                char character = in.readChar();
                String code = in.readUTF();
                huffmanCodes.put(code, character);
            }

            // Read the encoded data.
            // Read the number of padding bits.
            int padding = in.readByte();

            // Read the encoded data.
            StringBuilder encodedData = new StringBuilder();
            try {
                while (true) {
                    byte byteVal = in.readByte();
                    String byteStr = String.format("%8s", Integer.toBinaryString(byteVal & 0xFF)).replace(' ', '0');
                    encodedData.append(byteStr);
                }
            } catch (EOFException e) {
                // End of file reached.
            }

            // Remove padding from the encoded data.
            encodedData.setLength(encodedData.length() - padding);

            // Decode the encoded data.
            StringBuilder decodedData = new StringBuilder();
            StringBuilder code = new StringBuilder();
            for (char c : encodedData.toString().toCharArray()) {
                code.append(c);
                Character character = huffmanCodes.get(code.toString());
                if (character != null) {
                    decodedData.append(character);
                    code.setLength(0);  // Clear the code.
                }
            }

            // Write the decoded data to a file.
            try (FileWriter out = new FileWriter(outputFilePath)) {
                out.write(decodedData.toString());
            }

        }

    }
}
