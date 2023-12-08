# Huffman Compression and Decompression Project

This project is an implementation of the Huffman coding algorithm in Java. Huffman coding is a widely used algorithm for lossless data compression.

## Features

- Generate Huffman codes for a given file.
- Compress a file using Huffman coding.
- Decompress a Huffman-compressed file back into the original file.
- The program considers `n` bytes together for compression and decompression, where `n` is an integer.

## Files

- `State.java`: Defines the `State` class, which represents a node in the Huffman tree.
- `Huffman.java`: Contains the `Huffman` class, which implements the Huffman coding algorithm.
- `App.java`: Contains the `App` class, which is the entry point of the program.

## How to Run

### To compress a file:

```bash
java -jar huffman_17010210.jar c absolute_path_to_input_file n
```
Replace `n` with the number of bytes per group.

### To decompress a file:

```bash
java -jar huffman_17010210.jar d absolute_path_to_input_file
```


## Output

- If a file is compressed, the compressed file will have the name `17010210.<n>.<original_file_name>.hc` and will appear in the same directory as the input file. The program will print the compression ratio and the compression time.
- If a file is decompressed, the output file will be named `extracted.<original_file_name>` and will appear in the same directory as the input file. The program will print the decompression time.

## Analysis

The program can be used to analyze the compression ratio for different values of `n`. The compression ratio can be compared with other compression tools like 7-zip.
