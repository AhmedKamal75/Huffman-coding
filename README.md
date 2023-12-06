# Huffman Compression Project

This project is a simple implementation of the Huffman coding algorithm in Java. Huffman coding is a popular algorithm used for lossless data compression.

## Features

- Generate Huffman codes for a given text file.
- Compress a text file using Huffman coding.
- Decompress a Huffman-compressed file back into the original text.

## Files

- `State.java`: Defines the `State` class, which represents a node in the Huffman tree.
- `Huffman.java`: Contains the `Huffman` class, which implements the Huffman coding algorithm.
- `App.java`: Contains the `App` class, which is the entry point of the program.

## How to Run

1. Compile all the `.java` files:

```bash
javac *.java
```
2. Run the App class, providing the operation and file paths as arguments. For example, to compress a file:

```bash
java App compress data/temp_input.txt data/compressed.huff
```
Or to decompress a file:
```bash
java App decompress data/compressed.huff data/decompressed.txt
```

Replace `data/temp_input.txt`, `data/compressed.huff`, and `data/decompressed.txt` with your actual file paths.

Note
This project is for educational purposes and may not be suitable for real-world, production use cases.