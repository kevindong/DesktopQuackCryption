# QuackCryption

## Changelog (v1.0.0 to v1.1.0)
An update of [QuackCryption v1.0.0](https://github.com/Meegul304/DesktopQuackCryption/releases/tag/v1.0.0). The changes are as follows:

* Consolidated the original 6 source files into one file
* Removed dependency on the Apache Commons Codec
* Header (which contains the filename) for files is now dynamically sized
* (Slightly) optimized it; file encryption/decryption is still horribly slow and is still O(n^2)

Strings are compatible with v1.0.0. File encryption/decryption is not compatible. 

## Description
QuackCryption is a novel approach to data structures, app development, and encryption by four, 2nd-semester Purdue University computer science majors. In essence, either text or a file is inputted (along with maybe a passphrase of the user's choosing), the text is encrypted using 128-bit AES (in CBC mode), and then that text is then converted into 'Quack' code. The data encrypted can be decrypted back to its original form, bit-for-bit. This software is open source.

## Website
See [here](http://www.kevindong.net/QuackCryption/) for the website. 

## Repo Contents
* `QuackCryption.java` — The source code file for QuackCryption.
* `Quacks.csv` — The table for how the byte-level data is represented. 
* `test.png` and `encrypted.quack` — An example of QuackCryption's file encryption.

## Documentation
* All keys and initial vectors (called `initVector` in the code) must be 16 characters long.
* `String throughQuack(String key, String initVector, String normalText)` — Takes in the key, inital vector, and the text you want to encrypt. It then outputs the final quacked text.
* `String throughNormal(String key, String initVector, String quackedText)` — Same as above, but in reverse.
* `int fileThroughQuack(String key, String initVector, String fileName)` — Takes in a file (via the `fileName` string) and outputs a file named `encrypted.quack`. That represents the original file using quacks. Returns 0 if an error occurs. Returns 1 if it suceeds.
* `int fileThroughNormal(String key, String initVector, String fileName)` — Same as above, but in reverse.
* `String toAES(String key, String initVector, String normalText)` — Takes in the key, initial vector, and the text to be encrypted (using AES). It then outputs a string encrypted using AES. 
* `String fromAES(String key, String initVector, String encryptedText)` — Same as above, but in reverse.
* `String toQuack(String normalText)` — Takes in a string and outputs its quacked equivalent. Essentially just the `Quacks.csv` table in code.
* `String fromQuack(String quackedText)` — Same as above, but in reverse.

### Inner Workings
#### Strings
Inputted strings are first encrypted using 128-bit AES (CBC with PKCS5 padding). Then, each character in the outputted string (that's already been encrypted with AES) is replaced with a 'quackquack' (where each character can be either upper case or lower case). As a result of AES encryption, the encrypted string only contains ASCII table characters. Therefore, only 128 varying capitalizations of 'quackquack' are needed. The reason 'quackquack' is used rather than 'quack' is because a single 'quack' only has 2^5 (32) possibliites, which is insufficient to cover the ASCII table.

#### Files
Each quacked file consists of two parts: the header and the actual file's contents. The header starts with `quackquAck` and ends with `QuackquAck`. Everything in between represents the original file's name. The file name is encrypted separately from the body. Everything after the `quackquack` signaling the end of the header represents the body. 

Files are just binary data (which can be represented using integers) in a container. Each byte is read in one at a time, converted into an integer, and stored into a string with a space separating each integer. That data is then encrypted using QuackCryption in the same manner as strings. Each `quackquack` in the body represents a character in the AES-encrypted string. 

## License
MIT License. 