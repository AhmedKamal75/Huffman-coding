import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    private static HashMap<ArrayList<Byte>, Integer> getFrequencies(String inputFilePath, int n) {
        HashMap<ArrayList<Byte>, Integer> frequencies = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(inputFilePath)) {
            ArrayList<Byte> buffer = new ArrayList<>();
            int byteRead;
            boolean running = true;
            while (running) {
                running = (byteRead = fis.read()) != -1;
                if (running) {
                    buffer.add((byte) byteRead);
                }
                if (!buffer.isEmpty() && (buffer.size() == n || !running)) {
                    ArrayList<Byte> key = new ArrayList<>(buffer);
                    frequencies.put(key, frequencies.getOrDefault(key, 0) + 1);
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            System.out.println("Error in the getFrequencies method");
//            e.printStackTrace();
        }
        return frequencies;
    }

    public static Map<ArrayList<Byte>, String> getCodes(String inputFilePath, int n) {
        // Step 1: Read the file and calculate frequencies.
        HashMap<ArrayList<Byte>, Integer> frequencies = getFrequencies(inputFilePath, n);

        // Step 2: Build the Huffman tree.
        PriorityQueue<State> queue = new PriorityQueue<>();
        for (Map.Entry<ArrayList<Byte>, Integer> entry : frequencies.entrySet()) {
            queue.add(new State(entry.getKey(), entry.getValue()));
        }
        while (queue.size() > 1) {
            State left = queue.poll();
            State right = queue.poll();
            assert right != null;
            queue.add(new State(left, right, new ArrayList<>(), left.getCount() + right.getCount()));
        }

        // Step 3: Generate Huffman codes.
        Map<ArrayList<Byte>, String> codes = new HashMap<>();
        if (!queue.isEmpty()) {
            queue.peek().buildCode("", codes);
        }

        return codes;
    }


    public static void compressFile(String inputFilePath, String outputFilePath, String fileExtension, int n) throws IOException {
        // Step 0: get the Huffman code
        Map<ArrayList<Byte>, String> huffmanCodes = getCodes(inputFilePath, n);

        // Step 1: Read the file and encode it.
        StringBuilder encodedData = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(inputFilePath)) {
            ArrayList<Byte> buffer = new ArrayList<>();
            int byteRead;
            boolean running = true;
            while (running) {
                running = (byteRead = fis.read()) != -1;
                if (running) {
                    buffer.add((byte) byteRead);
                }
                if (!buffer.isEmpty() && (buffer.size() == n || !running)) {
                    ArrayList<Byte> key = new ArrayList<>(buffer);
                    String code = huffmanCodes.get(key);
                    if (code != null) {
                        encodedData.append(code);
                    }
                    buffer.clear();
                }
            }
        }

        // Step 2: Write the Huffman codes and the encoded data to a file.
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            // Write the file extension.
            out.writeUTF(fileExtension);

            // Write the number of Huffman codes.
            out.writeInt(huffmanCodes.size());

            // Write each Huffman code.
            for (Map.Entry<ArrayList<Byte>, String> entry : huffmanCodes.entrySet()) {
                out.writeInt(entry.getKey().size());
                for (Byte b : entry.getKey()) {
                    out.writeByte(b);
                }
                out.writeUTF(entry.getValue());
            }

            // Calculate padding.
            int padding = 8 - (encodedData.length() % 8);
            padding = padding == 8 ? 0 : padding;  // No padding if length is already a multiple of 8.

            // Write the number of padding bits.
            out.writeByte(padding);

            // Add padding to the encoded data.
            for (int i = 0; i < padding; i++) {
                encodedData.append('0');
            }

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
            // Read the file extension.
            String fileExtension = in.readUTF();

            // Read the number of Huffman codes.
            int numCodes = in.readInt();

            // Read each Huffman code.
            Map<String, ArrayList<Byte>> huffmanCodes = new HashMap<>();
            for (int i = 0; i < numCodes; i++) {
                int size = in.readInt();
                ArrayList<Byte> sequence = new ArrayList<>(size);
                for (int j = 0; j < size; j++) {
                    sequence.add(in.readByte());
                }
                String code = in.readUTF();
                huffmanCodes.put(code, sequence);
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
            ArrayList<Byte> decodedData = new ArrayList<>();
            StringBuilder code = new StringBuilder();
            for (char c : encodedData.toString().toCharArray()) {
                code.append(c);
                ArrayList<Byte> sequence = huffmanCodes.get(code.toString());
                if (sequence != null) {
                    decodedData.addAll(sequence);
                    code.setLength(0);  // Clear the code.
                }
            }

            // Write the decoded data to a file.
            try (FileOutputStream out = new FileOutputStream(outputFilePath + fileExtension)) {
                for (Byte b : decodedData) {
                    out.write(b);
                }
            }
        }
    }

//    public static void compressFile(String inputFilePath, String outputFilePath, String fileExtension, int n, int CHUNK_SIZE) throws IOException {
//        // Step 0: get the Huffman code
//        Map<ArrayList<Byte>, String> huffmanCodes = getCodes(inputFilePath, n);
//
//        // Step 1: Write the Huffman codes to a file.
//        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFilePath))) {
//
//            // Write the file extension.
//            out.writeUTF(fileExtension);
//
//            // Write the number of Huffman codes.
//            out.writeInt(huffmanCodes.size());
//
//            // Write each Huffman code.
//            for (Map.Entry<ArrayList<Byte>, String> entry : huffmanCodes.entrySet()) {
//                out.writeInt(entry.getKey().size());
//                for (Byte b : entry.getKey()) {
//                    out.writeByte(b);
//                }
//                out.writeUTF(entry.getValue());
//            }
//
//            // Step 2: Read the file, encode it, and write the encoded data to the file in chunks.
//            StringBuilder encodedData = new StringBuilder();
//            try (FileInputStream fis = new FileInputStream(inputFilePath)) {
//                ArrayList<Byte> buffer = new ArrayList<>();
//                int byteRead;
//                boolean running = true;
//                while (running) {
//                    running = (byteRead = fis.read()) != -1;
//                    if (running) {
//                        buffer.add((byte) byteRead);
//                    }
//                    if (!buffer.isEmpty() && (buffer.size() == n || !running)) {
//                        ArrayList<Byte> key = new ArrayList<>(buffer);
//                        String code = huffmanCodes.get(key);
//                        if (code != null) {
//                            encodedData.append(code);
//                        }
//                        buffer.clear();
//
//                        // If the encoded data reaches the chunk size, write it to the file and clear the buffer.
//                        if (encodedData.length() >= CHUNK_SIZE) {
//                            String remainingCode = writeChunk(encodedData, out);
//                            encodedData.setLength(0);  // Clear the buffer.
//                            encodedData.append(remainingCode);
//                        }
//                    }
//                }
//            }
//
//            // Write any remaining encoded data to the file.
//            if (!encodedData.isEmpty()) {
//                String incompleteCode = writeChunk(encodedData, out);
//                encodedData.append(incompleteCode);
//                if (!incompleteCode.isEmpty()) {
//                    writeChunk(new StringBuilder(incompleteCode), out);
//                }
//            }
//        }
//    }
//
//    private static String writeChunk(StringBuilder encodedData, DataOutputStream out) throws IOException {
//        int i;
//        for (i = 0; i + 8 <= encodedData.length(); i += 8) {
//            String byteStr = encodedData.substring(i, i + 8);
//            int byteVal = Integer.parseInt(byteStr, 2);
//            out.writeByte(byteVal);
//        }
//        // Return the remaining bits.
//        return encodedData.substring(i);
//    }
//
//
//    public static void decompressFile(String inputFilePath, String outputFilePath, int CHUNK_SIZE) throws IOException {
//        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFilePath))) {
//            // Read the file extension.
//            String fileExtension = in.readUTF();
//
//            // Read the number of Huffman codes.
//            int numCodes = in.readInt();
//
//            // Read each Huffman code.
//            Map<String, ArrayList<Byte>> huffmanCodes = new HashMap<>();
//            for (int i = 0; i < numCodes; i++) {
//                int size = in.readInt();
//                ArrayList<Byte> sequence = new ArrayList<>(size);
//                for (int j = 0; j < size; j++) {
//                    sequence.add(in.readByte());
//                }
//                String code = in.readUTF();
//                huffmanCodes.put(code, sequence);
//            }
//
//            // Read the encoded data in chunks, decode it, and write the decoded data to the output file.
//            try (FileOutputStream out = new FileOutputStream(outputFilePath + fileExtension)) {
//                StringBuilder encodedData = new StringBuilder();
//                try {
//                    while (true) {
//                        byte byteVal = in.readByte();
//                        String byteStr = String.format("%8s", Integer.toBinaryString(byteVal & 0xFF)).replace(' ', '0');
//                        encodedData.append(byteStr);
//
//                        // If the encoded data reaches the chunk size, decode it and write it to the file.
//                        if (encodedData.length() >= CHUNK_SIZE) {
//                            String incompleteCode = decodeChunk(encodedData, huffmanCodes, out);
//                            encodedData.setLength(0);  // Clear the buffer.
//                            encodedData.append(incompleteCode);
//                        }
//                    }
//                } catch (EOFException e) {
//                    // End of file reached.
//                }
//
//                // Decode any remaining encoded data.
//                String trashCode = decodeChunk(encodedData, huffmanCodes, out);
//                if (!trashCode.isEmpty()) {
//                    System.out.println(trashCode);
//                    System.out.println(huffmanCodes.get(trashCode));
//                    decodeChunk(new StringBuilder(trashCode), huffmanCodes, out);
//                }
//            }
//        }
//    }
//
//    private static String decodeChunk(StringBuilder encodedData, Map<String, ArrayList<Byte>> huffmanCodes, FileOutputStream out) throws IOException {
//        StringBuilder code = new StringBuilder();
//        for (char c : encodedData.toString().toCharArray()) {
//            code.append(c);
//            ArrayList<Byte> sequence = huffmanCodes.get(code.toString());
//            if (sequence != null) {
//                for (Byte b : sequence) {
//                    out.write(b);
//                }
//                code.setLength(0);  // Clear the code.
//            }
//        }
//        // Return any incomplete code.
//        return code.toString();
//    }

}