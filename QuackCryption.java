/*
	QuackCryption v1.1.0
	June 4, 2016
	http://www.kevindong.net/QuackCryption
	http://www.github.com/kevindong/QuackCryption
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class QuackCryption {
	// Begin main methods
	public String throughQuack(String key, String initVector, String normalText) {
		return toQuack(toAES(key, initVector, normalText));
	}

	public String throughNormal(String key, String initVector, String quackedText) {
		return fromAES(key, initVector, fromQuack(quackedText));
	}
	// End main methods

	// Begin file methods
	public int fileThroughQuack(String key, String initVector, String fileName) {
		File inputFile = new File(fileName);
		FileInputStream fis;
		try {
			fis = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Input file not valid.");
			return 0;
		}

		File outputFile = new File("encrypted.quack");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Output file not valid. This shouldn't ever occur.");
			return 0;
		}

		fileName = throughQuack(key, initVector, fileName);
		try {
			fos.write(("quackquAck " + fileName + " QuackquAck ").getBytes());
		} catch (IOException e) {
			System.out.println("Writing header failed.");
			return 0;
		}

		int piece;
		String body = "";
		try {
			int length = fis.available();
			for (int i = 0; i < length; i++) {
				piece = fis.read();
				if (piece == -1) {
					throw new Exception("Invalid byte read in body.");
				}
				body += piece + " ";
			}
			fos.write(throughQuack(key, initVector, body).getBytes());
		} catch (IOException e) {
			System.out.println("Writing body failed.");
			return 0;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return 0;
		}

		try {
			fis.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Closing open files failed.");
			return 0;
		}

		return 1;
	}

	public int fileThroughNormal(String key, String initVector, String fileName) {
		File inputFile = new File(fileName);
		FileInputStream fis;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Input file not valid.");
			return 0;
		}

		String header = "";
		boolean headerHasEnded = false;
		while (!headerHasEnded) {
			try {
				header += scanner.next() + " ";
				if (header.contains("QuackquAck")) {
					headerHasEnded = true;
				}
			} catch (NoSuchElementException e) {
				System.out.println("Header is invalid.");
				return 0;
			}
		}
		String outputFileName = throughNormal(key, initVector, header.substring(11, header.length() - 11));

		String body = "";
		boolean bodyHasEnded = false;
		while (!bodyHasEnded) {
			try {
				body += scanner.next() + " ";
			} catch (NoSuchElementException e) {
				bodyHasEnded = true;
			}
		}
		body = throughNormal(key, initVector, body);
		String[] byteArray = body.split(" ");

		File output = new File(outputFileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(output);
			for (int i = 0; i < byteArray.length; i++) {
				fos.write(Integer.parseInt(byteArray[i]));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Output file not found. This shouldn't ever execute.");
			return 0;
		} catch (IOException e) {
			System.out.println("Error occurred while writing output file.");
			return 0;
		}

		try {
			fos.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("Error occurred while closing files.");
			return 0;
		}
		return 1;
	}
	// End file methods

	// Begin AES
	public String toAES(String key, String initVector, String normalText) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("ASCII"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(normalText.getBytes());

			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String fromAES(String key, String initVector, String encryptedText) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("ASCII"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	// End AES

	// Begin Quack
	public String toQuack(String normalText) {
		String quacked = "";
		char[] inputArray = normalText.toCharArray();
		for (int i = 0; i < inputArray.length; i++) {
			switch (inputArray[i]) {
				case 0:
					quacked += "quackquack";
					break;
				case 1:
					quacked += "Quackquack";
					break;
				case 2:
					quacked += "qUackquack";
					break;
				case 3:
					quacked += "QUackquack";
					break;
				case 4:
					quacked += "quAckquack";
					break;
				case 5:
					quacked += "QuAckquack";
					break;
				case 6:
					quacked += "qUAckquack";
					break;
				case 7:
					quacked += "QUAckquack";
					break;
				case 8:
					quacked += "quaCkquack";
					break;
				case 9:
					quacked += "QuaCkquack";
					break;
				case 10:
					quacked += "qUaCkquack";
					break;
				case 11:
					quacked += "QUaCkquack";
					break;
				case 12:
					quacked += "quACkquack";
					break;
				case 13:
					quacked += "QuACkquack";
					break;
				case 14:
					quacked += "qUACkquack";
					break;
				case 15:
					quacked += "QUACkquack";
					break;
				case 16:
					quacked += "quacKquack";
					break;
				case 17:
					quacked += "QuacKquack";
					break;
				case 18:
					quacked += "qUacKquack";
					break;
				case 19:
					quacked += "QUacKquack";
					break;
				case 20:
					quacked += "quAcKquack";
					break;
				case 21:
					quacked += "QuAcKquack";
					break;
				case 22:
					quacked += "qUAcKquack";
					break;
				case 23:
					quacked += "QUAcKquack";
					break;
				case 24:
					quacked += "quaCKquack";
					break;
				case 25:
					quacked += "QuaCKquack";
					break;
				case 26:
					quacked += "qUaCKquack";
					break;
				case 27:
					quacked += "QUaCKquack";
					break;
				case 28:
					quacked += "quACKquack";
					break;
				case 29:
					quacked += "QuACKquack";
					break;
				case 30:
					quacked += "qUACKquack";
					break;
				case 31:
					quacked += "QUACKquack";
					break;
				case 32:
					quacked += "quackQuack";
					break;
				case 33:
					quacked += "QuackQuack";
					break;
				case 34:
					quacked += "qUackQuack";
					break;
				case 35:
					quacked += "QUackQuack";
					break;
				case 36:
					quacked += "quAckQuack";
					break;
				case 37:
					quacked += "QuAckQuack";
					break;
				case 38:
					quacked += "qUAckQuack";
					break;
				case 39:
					quacked += "QUAckQuack";
					break;
				case 40:
					quacked += "quaCkQuack";
					break;
				case 41:
					quacked += "QuaCkQuack";
					break;
				case 42:
					quacked += "qUaCkQuack";
					break;
				case 43:
					quacked += "QUaCkQuack";
					break;
				case 44:
					quacked += "quACkQuack";
					break;
				case 45:
					quacked += "QuACkQuack";
					break;
				case 46:
					quacked += "qUACkQuack";
					break;
				case 47:
					quacked += "QUACkQuack";
					break;
				case 48:
					quacked += "quacKQuack";
					break;
				case 49:
					quacked += "QuacKQuack";
					break;
				case 50:
					quacked += "qUacKQuack";
					break;
				case 51:
					quacked += "QUacKQuack";
					break;
				case 52:
					quacked += "quAcKQuack";
					break;
				case 53:
					quacked += "QuAcKQuack";
					break;
				case 54:
					quacked += "qUAcKQuack";
					break;
				case 55:
					quacked += "QUAcKQuack";
					break;
				case 56:
					quacked += "quaCKQuack";
					break;
				case 57:
					quacked += "QuaCKQuack";
					break;
				case 58:
					quacked += "qUaCKQuack";
					break;
				case 59:
					quacked += "QUaCKQuack";
					break;
				case 60:
					quacked += "quACKQuack";
					break;
				case 61:
					quacked += "QuACKQuack";
					break;
				case 62:
					quacked += "qUACKQuack";
					break;
				case 63:
					quacked += "QUACKQuack";
					break;
				case 64:
					quacked += "quackqUack";
					break;
				case 65:
					quacked += "QuackqUack";
					break;
				case 66:
					quacked += "qUackqUack";
					break;
				case 67:
					quacked += "QUackqUack";
					break;
				case 68:
					quacked += "quAckqUack";
					break;
				case 69:
					quacked += "QuAckqUack";
					break;
				case 70:
					quacked += "qUAckqUack";
					break;
				case 71:
					quacked += "QUAckqUack";
					break;
				case 72:
					quacked += "quaCkqUack";
					break;
				case 73:
					quacked += "QuaCkqUack";
					break;
				case 74:
					quacked += "qUaCkqUack";
					break;
				case 75:
					quacked += "QUaCkqUack";
					break;
				case 76:
					quacked += "quACkqUack";
					break;
				case 77:
					quacked += "QuACkqUack";
					break;
				case 78:
					quacked += "qUACkqUack";
					break;
				case 79:
					quacked += "QUACkqUack";
					break;
				case 80:
					quacked += "quacKqUack";
					break;
				case 81:
					quacked += "QuacKqUack";
					break;
				case 82:
					quacked += "qUacKqUack";
					break;
				case 83:
					quacked += "QUacKqUack";
					break;
				case 84:
					quacked += "quAcKqUack";
					break;
				case 85:
					quacked += "QuAcKqUack";
					break;
				case 86:
					quacked += "qUAcKqUack";
					break;
				case 87:
					quacked += "QUAcKqUack";
					break;
				case 88:
					quacked += "quaCKqUack";
					break;
				case 89:
					quacked += "QuaCKqUack";
					break;
				case 90:
					quacked += "qUaCKqUack";
					break;
				case 91:
					quacked += "QUaCKqUack";
					break;
				case 92:
					quacked += "quACKqUack";
					break;
				case 93:
					quacked += "QuACKqUack";
					break;
				case 94:
					quacked += "qUACKqUack";
					break;
				case 95:
					quacked += "QUACKqUack";
					break;
				case 96:
					quacked += "quackQUack";
					break;
				case 97:
					quacked += "QuackQUack";
					break;
				case 98:
					quacked += "qUackQUack";
					break;
				case 99:
					quacked += "QUackQUack";
					break;
				case 100:
					quacked += "quAckQUack";
					break;
				case 101:
					quacked += "QuAckQUack";
					break;
				case 102:
					quacked += "qUAckQUack";
					break;
				case 103:
					quacked += "QUAckQUack";
					break;
				case 104:
					quacked += "quaCkQUack";
					break;
				case 105:
					quacked += "QuaCkQUack";
					break;
				case 106:
					quacked += "qUaCkQUack";
					break;
				case 107:
					quacked += "QUaCkQUack";
					break;
				case 108:
					quacked += "quACkQUack";
					break;
				case 109:
					quacked += "QuACkQUack";
					break;
				case 110:
					quacked += "qUACkQUack";
					break;
				case 111:
					quacked += "QUACkQUack";
					break;
				case 112:
					quacked += "quacKQUack";
					break;
				case 113:
					quacked += "QuacKQUack";
					break;
				case 114:
					quacked += "qUacKQUack";
					break;
				case 115:
					quacked += "QUacKQUack";
					break;
				case 116:
					quacked += "quAcKQUack";
					break;
				case 117:
					quacked += "QuAcKQUack";
					break;
				case 118:
					quacked += "qUAcKQUack";
					break;
				case 119:
					quacked += "QUAcKQUack";
					break;
				case 120:
					quacked += "quaCKQUack";
					break;
				case 121:
					quacked += "QuaCKQUack";
					break;
				case 122:
					quacked += "qUaCKQUack";
					break;
				case 123:
					quacked += "QUaCKQUack";
					break;
				case 124:
					quacked += "quACKQUack";
					break;
				case 125:
					quacked += "QuACKQUack";
					break;
				case 126:
					quacked += "qUACKQUack";
					break;
				case 127:
					quacked += "QUACKQUack";
					break;
			}
			quacked += " ";
		}
		return quacked;
	}

	public String fromQuack(String quackedText) {
		String deQuacked = "";
		String[] translateArray = quackedText.split(" ");
		for (int i = 0; i < translateArray.length; i++) {
			switch (translateArray[i]) {
				case "quackquack":
					deQuacked += (char)0;
					break;
				case "Quackquack":
					deQuacked += (char)1;
					break;
				case "qUackquack":
					deQuacked += (char)2;
					break;
				case "QUackquack":
					deQuacked += (char)3;
					break;
				case "quAckquack":
					deQuacked += (char)4;
					break;
				case "QuAckquack":
					deQuacked += (char)5;
					break;
				case "qUAckquack":
					deQuacked += (char)6;
					break;
				case "QUAckquack":
					deQuacked += (char)7;
					break;
				case "quaCkquack":
					deQuacked += (char)8;
					break;
				case "QuaCkquack":
					deQuacked += (char)9;
					break;
				case "qUaCkquack":
					deQuacked += (char)10;
					break;
				case "QUaCkquack":
					deQuacked += (char)11;
					break;
				case "quACkquack":
					deQuacked += (char)12;
					break;
				case "QuACkquack":
					deQuacked += (char)13;
					break;
				case "qUACkquack":
					deQuacked += (char)14;
					break;
				case "QUACkquack":
					deQuacked += (char)15;
					break;
				case "quacKquack":
					deQuacked += (char)16;
					break;
				case "QuacKquack":
					deQuacked += (char)17;
					break;
				case "qUacKquack":
					deQuacked += (char)18;
					break;
				case "QUacKquack":
					deQuacked += (char)19;
					break;
				case "quAcKquack":
					deQuacked += (char)20;
					break;
				case "QuAcKquack":
					deQuacked += (char)21;
					break;
				case "qUAcKquack":
					deQuacked += (char)22;
					break;
				case "QUAcKquack":
					deQuacked += (char)23;
					break;
				case "quaCKquack":
					deQuacked += (char)24;
					break;
				case "QuaCKquack":
					deQuacked += (char)25;
					break;
				case "qUaCKquack":
					deQuacked += (char)26;
					break;
				case "QUaCKquack":
					deQuacked += (char)27;
					break;
				case "quACKquack":
					deQuacked += (char)28;
					break;
				case "QuACKquack":
					deQuacked += (char)29;
					break;
				case "qUACKquack":
					deQuacked += (char)30;
					break;
				case "QUACKquack":
					deQuacked += (char)31;
					break;
				case "quackQuack":
					deQuacked += (char)32;
					break;
				case "QuackQuack":
					deQuacked += (char)33;
					break;
				case "qUackQuack":
					deQuacked += (char)34;
					break;
				case "QUackQuack":
					deQuacked += (char)35;
					break;
				case "quAckQuack":
					deQuacked += (char)36;
					break;
				case "QuAckQuack":
					deQuacked += (char)37;
					break;
				case "qUAckQuack":
					deQuacked += (char)38;
					break;
				case "QUAckQuack":
					deQuacked += (char)39;
					break;
				case "quaCkQuack":
					deQuacked += (char)40;
					break;
				case "QuaCkQuack":
					deQuacked += (char)41;
					break;
				case "qUaCkQuack":
					deQuacked += (char)42;
					break;
				case "QUaCkQuack":
					deQuacked += (char)43;
					break;
				case "quACkQuack":
					deQuacked += (char)44;
					break;
				case "QuACkQuack":
					deQuacked += (char)45;
					break;
				case "qUACkQuack":
					deQuacked += (char)46;
					break;
				case "QUACkQuack":
					deQuacked += (char)47;
					break;
				case "quacKQuack":
					deQuacked += (char)48;
					break;
				case "QuacKQuack":
					deQuacked += (char)49;
					break;
				case "qUacKQuack":
					deQuacked += (char)50;
					break;
				case "QUacKQuack":
					deQuacked += (char)51;
					break;
				case "quAcKQuack":
					deQuacked += (char)52;
					break;
				case "QuAcKQuack":
					deQuacked += (char)53;
					break;
				case "qUAcKQuack":
					deQuacked += (char)54;
					break;
				case "QUAcKQuack":
					deQuacked += (char)55;
					break;
				case "quaCKQuack":
					deQuacked += (char)56;
					break;
				case "QuaCKQuack":
					deQuacked += (char)57;
					break;
				case "qUaCKQuack":
					deQuacked += (char)58;
					break;
				case "QUaCKQuack":
					deQuacked += (char)59;
					break;
				case "quACKQuack":
					deQuacked += (char)60;
					break;
				case "QuACKQuack":
					deQuacked += (char)61;
					break;
				case "qUACKQuack":
					deQuacked += (char)62;
					break;
				case "QUACKQuack":
					deQuacked += (char)63;
					break;
				case "quackqUack":
					deQuacked += (char)64;
					break;
				case "QuackqUack":
					deQuacked += (char)65;
					break;
				case "qUackqUack":
					deQuacked += (char)66;
					break;
				case "QUackqUack":
					deQuacked += (char)67;
					break;
				case "quAckqUack":
					deQuacked += (char)68;
					break;
				case "QuAckqUack":
					deQuacked += (char)69;
					break;
				case "qUAckqUack":
					deQuacked += (char)70;
					break;
				case "QUAckqUack":
					deQuacked += (char)71;
					break;
				case "quaCkqUack":
					deQuacked += (char)72;
					break;
				case "QuaCkqUack":
					deQuacked += (char)73;
					break;
				case "qUaCkqUack":
					deQuacked += (char)74;
					break;
				case "QUaCkqUack":
					deQuacked += (char)75;
					break;
				case "quACkqUack":
					deQuacked += (char)76;
					break;
				case "QuACkqUack":
					deQuacked += (char)77;
					break;
				case "qUACkqUack":
					deQuacked += (char)78;
					break;
				case "QUACkqUack":
					deQuacked += (char)79;
					break;
				case "quacKqUack":
					deQuacked += (char)80;
					break;
				case "QuacKqUack":
					deQuacked += (char)81;
					break;
				case "qUacKqUack":
					deQuacked += (char)82;
					break;
				case "QUacKqUack":
					deQuacked += (char)83;
					break;
				case "quAcKqUack":
					deQuacked += (char)84;
					break;
				case "QuAcKqUack":
					deQuacked += (char)85;
					break;
				case "qUAcKqUack":
					deQuacked += (char)86;
					break;
				case "QUAcKqUack":
					deQuacked += (char)87;
					break;
				case "quaCKqUack":
					deQuacked += (char)88;
					break;
				case "QuaCKqUack":
					deQuacked += (char)89;
					break;
				case "qUaCKqUack":
					deQuacked += (char)90;
					break;
				case "QUaCKqUack":
					deQuacked += (char)91;
					break;
				case "quACKqUack":
					deQuacked += (char)92;
					break;
				case "QuACKqUack":
					deQuacked += (char)93;
					break;
				case "qUACKqUack":
					deQuacked += (char)94;
					break;
				case "QUACKqUack":
					deQuacked += (char)95;
					break;
				case "quackQUack":
					deQuacked += (char)96;
					break;
				case "QuackQUack":
					deQuacked += (char)97;
					break;
				case "qUackQUack":
					deQuacked += (char)98;
					break;
				case "QUackQUack":
					deQuacked += (char)99;
					break;
				case "quAckQUack":
					deQuacked += (char)100;
					break;
				case "QuAckQUack":
					deQuacked += (char)101;
					break;
				case "qUAckQUack":
					deQuacked += (char)102;
					break;
				case "QUAckQUack":
					deQuacked += (char)103;
					break;
				case "quaCkQUack":
					deQuacked += (char)104;
					break;
				case "QuaCkQUack":
					deQuacked += (char)105;
					break;
				case "qUaCkQUack":
					deQuacked += (char)106;
					break;
				case "QUaCkQUack":
					deQuacked += (char)107;
					break;
				case "quACkQUack":
					deQuacked += (char)108;
					break;
				case "QuACkQUack":
					deQuacked += (char)109;
					break;
				case "qUACkQUack":
					deQuacked += (char)110;
					break;
				case "QUACkQUack":
					deQuacked += (char)111;
					break;
				case "quacKQUack":
					deQuacked += (char)112;
					break;
				case "QuacKQUack":
					deQuacked += (char)113;
					break;
				case "qUacKQUack":
					deQuacked += (char)114;
					break;
				case "QUacKQUack":
					deQuacked += (char)115;
					break;
				case "quAcKQUack":
					deQuacked += (char)116;
					break;
				case "QuAcKQUack":
					deQuacked += (char)117;
					break;
				case "qUAcKQUack":
					deQuacked += (char)118;
					break;
				case "QUAcKQUack":
					deQuacked += (char)119;
					break;
				case "quaCKQUack":
					deQuacked += (char)120;
					break;
				case "QuaCKQUack":
					deQuacked += (char)121;
					break;
				case "qUaCKQUack":
					deQuacked += (char)122;
					break;
				case "QUaCKQUack":
					deQuacked += (char)123;
					break;
				case "quACKQUack":
					deQuacked += (char)124;
					break;
				case "QuACKQUack":
					deQuacked += (char)125;
					break;
				case "qUACKQUack":
					deQuacked += (char)126;
					break;
				case "QUACKQUack":
					deQuacked += (char)127;
					break;
			}
		}
		return deQuacked;
	}
	// End Quack
	
	public static void main(String[] args) {
		QuackCryption quack = new QuackCryption();
		quack.fileThroughQuack("QuackQuackQuack!", "RandomInitVector", "test.png");
		quack.fileThroughNormal("QuackQuackQuack!", "RandomInitVector", "encrypted.quack")
	}
}