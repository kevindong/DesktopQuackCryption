/*
	QuackCryption v1.2.0
	August 8, 2016
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
		StringBuilder body = new StringBuilder();
		try {
			int length = fis.available();
			for (int i = 0; i < length; i++) {
				piece = fis.read();
				if (piece == -1) {
					throw new Exception("Invalid byte read in body.");
				}
				body.append(piece + " ");
			}
			fos.write(throughQuack(key, initVector, body.toString()).getBytes());
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

		StringBuilder body = new StringBuilder();
		boolean bodyHasEnded = false;
		while (!bodyHasEnded) {
			try {
				body.append(scanner.next() + " ");
			} catch (NoSuchElementException e) {
				bodyHasEnded = true;
			}
		}
		String[] byteArray = throughNormal(key, initVector, body.toString()).split(" ");

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
		StringBuilder quacked = new StringBuilder();
		char[] inputArray = normalText.toCharArray();
		for (int i = 0; i < inputArray.length; i++) {
			switch (inputArray[i]) {
				case 0:
					quacked.append("quackquack");
					break;
				case 1:
					quacked.append("Quackquack");
					break;
				case 2:
					quacked.append("qUackquack");
					break;
				case 3:
					quacked.append("QUackquack");
					break;
				case 4:
					quacked.append("quAckquack");
					break;
				case 5:
					quacked.append("QuAckquack");
					break;
				case 6:
					quacked.append("qUAckquack");
					break;
				case 7:
					quacked.append("QUAckquack");
					break;
				case 8:
					quacked.append("quaCkquack");
					break;
				case 9:
					quacked.append("QuaCkquack");
					break;
				case 10:
					quacked.append("qUaCkquack");
					break;
				case 11:
					quacked.append("QUaCkquack");
					break;
				case 12:
					quacked.append("quACkquack");
					break;
				case 13:
					quacked.append("QuACkquack");
					break;
				case 14:
					quacked.append("qUACkquack");
					break;
				case 15:
					quacked.append("QUACkquack");
					break;
				case 16:
					quacked.append("quacKquack");
					break;
				case 17:
					quacked.append("QuacKquack");
					break;
				case 18:
					quacked.append("qUacKquack");
					break;
				case 19:
					quacked.append("QUacKquack");
					break;
				case 20:
					quacked.append("quAcKquack");
					break;
				case 21:
					quacked.append("QuAcKquack");
					break;
				case 22:
					quacked.append("qUAcKquack");
					break;
				case 23:
					quacked.append("QUAcKquack");
					break;
				case 24:
					quacked.append("quaCKquack");
					break;
				case 25:
					quacked.append("QuaCKquack");
					break;
				case 26:
					quacked.append("qUaCKquack");
					break;
				case 27:
					quacked.append("QUaCKquack");
					break;
				case 28:
					quacked.append("quACKquack");
					break;
				case 29:
					quacked.append("QuACKquack");
					break;
				case 30:
					quacked.append("qUACKquack");
					break;
				case 31:
					quacked.append("QUACKquack");
					break;
				case 32:
					quacked.append("quackQuack");
					break;
				case 33:
					quacked.append("QuackQuack");
					break;
				case 34:
					quacked.append("qUackQuack");
					break;
				case 35:
					quacked.append("QUackQuack");
					break;
				case 36:
					quacked.append("quAckQuack");
					break;
				case 37:
					quacked.append("QuAckQuack");
					break;
				case 38:
					quacked.append("qUAckQuack");
					break;
				case 39:
					quacked.append("QUAckQuack");
					break;
				case 40:
					quacked.append("quaCkQuack");
					break;
				case 41:
					quacked.append("QuaCkQuack");
					break;
				case 42:
					quacked.append("qUaCkQuack");
					break;
				case 43:
					quacked.append("QUaCkQuack");
					break;
				case 44:
					quacked.append("quACkQuack");
					break;
				case 45:
					quacked.append("QuACkQuack");
					break;
				case 46:
					quacked.append("qUACkQuack");
					break;
				case 47:
					quacked.append("QUACkQuack");
					break;
				case 48:
					quacked.append("quacKQuack");
					break;
				case 49:
					quacked.append("QuacKQuack");
					break;
				case 50:
					quacked.append("qUacKQuack");
					break;
				case 51:
					quacked.append("QUacKQuack");
					break;
				case 52:
					quacked.append("quAcKQuack");
					break;
				case 53:
					quacked.append("QuAcKQuack");
					break;
				case 54:
					quacked.append("qUAcKQuack");
					break;
				case 55:
					quacked.append("QUAcKQuack");
					break;
				case 56:
					quacked.append("quaCKQuack");
					break;
				case 57:
					quacked.append("QuaCKQuack");
					break;
				case 58:
					quacked.append("qUaCKQuack");
					break;
				case 59:
					quacked.append("QUaCKQuack");
					break;
				case 60:
					quacked.append("quACKQuack");
					break;
				case 61:
					quacked.append("QuACKQuack");
					break;
				case 62:
					quacked.append("qUACKQuack");
					break;
				case 63:
					quacked.append("QUACKQuack");
					break;
				case 64:
					quacked.append("quackqUack");
					break;
				case 65:
					quacked.append("QuackqUack");
					break;
				case 66:
					quacked.append("qUackqUack");
					break;
				case 67:
					quacked.append("QUackqUack");
					break;
				case 68:
					quacked.append("quAckqUack");
					break;
				case 69:
					quacked.append("QuAckqUack");
					break;
				case 70:
					quacked.append("qUAckqUack");
					break;
				case 71:
					quacked.append("QUAckqUack");
					break;
				case 72:
					quacked.append("quaCkqUack");
					break;
				case 73:
					quacked.append("QuaCkqUack");
					break;
				case 74:
					quacked.append("qUaCkqUack");
					break;
				case 75:
					quacked.append("QUaCkqUack");
					break;
				case 76:
					quacked.append("quACkqUack");
					break;
				case 77:
					quacked.append("QuACkqUack");
					break;
				case 78:
					quacked.append("qUACkqUack");
					break;
				case 79:
					quacked.append("QUACkqUack");
					break;
				case 80:
					quacked.append("quacKqUack");
					break;
				case 81:
					quacked.append("QuacKqUack");
					break;
				case 82:
					quacked.append("qUacKqUack");
					break;
				case 83:
					quacked.append("QUacKqUack");
					break;
				case 84:
					quacked.append("quAcKqUack");
					break;
				case 85:
					quacked.append("QuAcKqUack");
					break;
				case 86:
					quacked.append("qUAcKqUack");
					break;
				case 87:
					quacked.append("QUAcKqUack");
					break;
				case 88:
					quacked.append("quaCKqUack");
					break;
				case 89:
					quacked.append("QuaCKqUack");
					break;
				case 90:
					quacked.append("qUaCKqUack");
					break;
				case 91:
					quacked.append("QUaCKqUack");
					break;
				case 92:
					quacked.append("quACKqUack");
					break;
				case 93:
					quacked.append("QuACKqUack");
					break;
				case 94:
					quacked.append("qUACKqUack");
					break;
				case 95:
					quacked.append("QUACKqUack");
					break;
				case 96:
					quacked.append("quackQUack");
					break;
				case 97:
					quacked.append("QuackQUack");
					break;
				case 98:
					quacked.append("qUackQUack");
					break;
				case 99:
					quacked.append("QUackQUack");
					break;
				case 100:
					quacked.append("quAckQUack");
					break;
				case 101:
					quacked.append("QuAckQUack");
					break;
				case 102:
					quacked.append("qUAckQUack");
					break;
				case 103:
					quacked.append("QUAckQUack");
					break;
				case 104:
					quacked.append("quaCkQUack");
					break;
				case 105:
					quacked.append("QuaCkQUack");
					break;
				case 106:
					quacked.append("qUaCkQUack");
					break;
				case 107:
					quacked.append("QUaCkQUack");
					break;
				case 108:
					quacked.append("quACkQUack");
					break;
				case 109:
					quacked.append("QuACkQUack");
					break;
				case 110:
					quacked.append("qUACkQUack");
					break;
				case 111:
					quacked.append("QUACkQUack");
					break;
				case 112:
					quacked.append("quacKQUack");
					break;
				case 113:
					quacked.append("QuacKQUack");
					break;
				case 114:
					quacked.append("qUacKQUack");
					break;
				case 115:
					quacked.append("QUacKQUack");
					break;
				case 116:
					quacked.append("quAcKQUack");
					break;
				case 117:
					quacked.append("QuAcKQUack");
					break;
				case 118:
					quacked.append("qUAcKQUack");
					break;
				case 119:
					quacked.append("QUAcKQUack");
					break;
				case 120:
					quacked.append("quaCKQUack");
					break;
				case 121:
					quacked.append("QuaCKQUack");
					break;
				case 122:
					quacked.append("qUaCKQUack");
					break;
				case 123:
					quacked.append("QUaCKQUack");
					break;
				case 124:
					quacked.append("quACKQUack");
					break;
				case 125:
					quacked.append("QuACKQUack");
					break;
				case 126:
					quacked.append("qUACKQUack");
					break;
				case 127:
					quacked.append("QUACKQUack");
					break;
			}
			quacked.append(" ");
		}
		return quacked.toString();
	}

	public String fromQuack(String quackedText) {
		StringBuilder deQuacked = new StringBuilder();
		String[] translateArray = quackedText.split(" ");
		for (int i = 0; i < translateArray.length; i++) {
			switch (translateArray[i]) {
				case "quackquack":
					deQuacked.append((char)0);
					break;
				case "Quackquack":
					deQuacked.append((char)1);
					break;
				case "qUackquack":
					deQuacked.append((char)2);
					break;
				case "QUackquack":
					deQuacked.append((char)3);
					break;
				case "quAckquack":
					deQuacked.append((char)4);
					break;
				case "QuAckquack":
					deQuacked.append((char)5);
					break;
				case "qUAckquack":
					deQuacked.append((char)6);
					break;
				case "QUAckquack":
					deQuacked.append((char)7);
					break;
				case "quaCkquack":
					deQuacked.append((char)8);
					break;
				case "QuaCkquack":
					deQuacked.append((char)9);
					break;
				case "qUaCkquack":
					deQuacked.append((char)10);
					break;
				case "QUaCkquack":
					deQuacked.append((char)11);
					break;
				case "quACkquack":
					deQuacked.append((char)12);
					break;
				case "QuACkquack":
					deQuacked.append((char)13);
					break;
				case "qUACkquack":
					deQuacked.append((char)14);
					break;
				case "QUACkquack":
					deQuacked.append((char)15);
					break;
				case "quacKquack":
					deQuacked.append((char)16);
					break;
				case "QuacKquack":
					deQuacked.append((char)17);
					break;
				case "qUacKquack":
					deQuacked.append((char)18);
					break;
				case "QUacKquack":
					deQuacked.append((char)19);
					break;
				case "quAcKquack":
					deQuacked.append((char)20);
					break;
				case "QuAcKquack":
					deQuacked.append((char)21);
					break;
				case "qUAcKquack":
					deQuacked.append((char)22);
					break;
				case "QUAcKquack":
					deQuacked.append((char)23);
					break;
				case "quaCKquack":
					deQuacked.append((char)24);
					break;
				case "QuaCKquack":
					deQuacked.append((char)25);
					break;
				case "qUaCKquack":
					deQuacked.append((char)26);
					break;
				case "QUaCKquack":
					deQuacked.append((char)27);
					break;
				case "quACKquack":
					deQuacked.append((char)28);
					break;
				case "QuACKquack":
					deQuacked.append((char)29);
					break;
				case "qUACKquack":
					deQuacked.append((char)30);
					break;
				case "QUACKquack":
					deQuacked.append((char)31);
					break;
				case "quackQuack":
					deQuacked.append((char)32);
					break;
				case "QuackQuack":
					deQuacked.append((char)33);
					break;
				case "qUackQuack":
					deQuacked.append((char)34);
					break;
				case "QUackQuack":
					deQuacked.append((char)35);
					break;
				case "quAckQuack":
					deQuacked.append((char)36);
					break;
				case "QuAckQuack":
					deQuacked.append((char)37);
					break;
				case "qUAckQuack":
					deQuacked.append((char)38);
					break;
				case "QUAckQuack":
					deQuacked.append((char)39);
					break;
				case "quaCkQuack":
					deQuacked.append((char)40);
					break;
				case "QuaCkQuack":
					deQuacked.append((char)41);
					break;
				case "qUaCkQuack":
					deQuacked.append((char)42);
					break;
				case "QUaCkQuack":
					deQuacked.append((char)43);
					break;
				case "quACkQuack":
					deQuacked.append((char)44);
					break;
				case "QuACkQuack":
					deQuacked.append((char)45);
					break;
				case "qUACkQuack":
					deQuacked.append((char)46);
					break;
				case "QUACkQuack":
					deQuacked.append((char)47);
					break;
				case "quacKQuack":
					deQuacked.append((char)48);
					break;
				case "QuacKQuack":
					deQuacked.append((char)49);
					break;
				case "qUacKQuack":
					deQuacked.append((char)50);
					break;
				case "QUacKQuack":
					deQuacked.append((char)51);
					break;
				case "quAcKQuack":
					deQuacked.append((char)52);
					break;
				case "QuAcKQuack":
					deQuacked.append((char)53);
					break;
				case "qUAcKQuack":
					deQuacked.append((char)54);
					break;
				case "QUAcKQuack":
					deQuacked.append((char)55);
					break;
				case "quaCKQuack":
					deQuacked.append((char)56);
					break;
				case "QuaCKQuack":
					deQuacked.append((char)57);
					break;
				case "qUaCKQuack":
					deQuacked.append((char)58);
					break;
				case "QUaCKQuack":
					deQuacked.append((char)59);
					break;
				case "quACKQuack":
					deQuacked.append((char)60);
					break;
				case "QuACKQuack":
					deQuacked.append((char)61);
					break;
				case "qUACKQuack":
					deQuacked.append((char)62);
					break;
				case "QUACKQuack":
					deQuacked.append((char)63);
					break;
				case "quackqUack":
					deQuacked.append((char)64);
					break;
				case "QuackqUack":
					deQuacked.append((char)65);
					break;
				case "qUackqUack":
					deQuacked.append((char)66);
					break;
				case "QUackqUack":
					deQuacked.append((char)67);
					break;
				case "quAckqUack":
					deQuacked.append((char)68);
					break;
				case "QuAckqUack":
					deQuacked.append((char)69);
					break;
				case "qUAckqUack":
					deQuacked.append((char)70);
					break;
				case "QUAckqUack":
					deQuacked.append((char)71);
					break;
				case "quaCkqUack":
					deQuacked.append((char)72);
					break;
				case "QuaCkqUack":
					deQuacked.append((char)73);
					break;
				case "qUaCkqUack":
					deQuacked.append((char)74);
					break;
				case "QUaCkqUack":
					deQuacked.append((char)75);
					break;
				case "quACkqUack":
					deQuacked.append((char)76);
					break;
				case "QuACkqUack":
					deQuacked.append((char)77);
					break;
				case "qUACkqUack":
					deQuacked.append((char)78);
					break;
				case "QUACkqUack":
					deQuacked.append((char)79);
					break;
				case "quacKqUack":
					deQuacked.append((char)80);
					break;
				case "QuacKqUack":
					deQuacked.append((char)81);
					break;
				case "qUacKqUack":
					deQuacked.append((char)82);
					break;
				case "QUacKqUack":
					deQuacked.append((char)83);
					break;
				case "quAcKqUack":
					deQuacked.append((char)84);
					break;
				case "QuAcKqUack":
					deQuacked.append((char)85);
					break;
				case "qUAcKqUack":
					deQuacked.append((char)86);
					break;
				case "QUAcKqUack":
					deQuacked.append((char)87);
					break;
				case "quaCKqUack":
					deQuacked.append((char)88);
					break;
				case "QuaCKqUack":
					deQuacked.append((char)89);
					break;
				case "qUaCKqUack":
					deQuacked.append((char)90);
					break;
				case "QUaCKqUack":
					deQuacked.append((char)91);
					break;
				case "quACKqUack":
					deQuacked.append((char)92);
					break;
				case "QuACKqUack":
					deQuacked.append((char)93);
					break;
				case "qUACKqUack":
					deQuacked.append((char)94);
					break;
				case "QUACKqUack":
					deQuacked.append((char)95);
					break;
				case "quackQUack":
					deQuacked.append((char)96);
					break;
				case "QuackQUack":
					deQuacked.append((char)97);
					break;
				case "qUackQUack":
					deQuacked.append((char)98);
					break;
				case "QUackQUack":
					deQuacked.append((char)99);
					break;
				case "quAckQUack":
					deQuacked.append((char)100);
					break;
				case "QuAckQUack":
					deQuacked.append((char)101);
					break;
				case "qUAckQUack":
					deQuacked.append((char)102);
					break;
				case "QUAckQUack":
					deQuacked.append((char)103);
					break;
				case "quaCkQUack":
					deQuacked.append((char)104);
					break;
				case "QuaCkQUack":
					deQuacked.append((char)105);
					break;
				case "qUaCkQUack":
					deQuacked.append((char)106);
					break;
				case "QUaCkQUack":
					deQuacked.append((char)107);
					break;
				case "quACkQUack":
					deQuacked.append((char)108);
					break;
				case "QuACkQUack":
					deQuacked.append((char)109);
					break;
				case "qUACkQUack":
					deQuacked.append((char)110);
					break;
				case "QUACkQUack":
					deQuacked.append((char)111);
					break;
				case "quacKQUack":
					deQuacked.append((char)112);
					break;
				case "QuacKQUack":
					deQuacked.append((char)113);
					break;
				case "qUacKQUack":
					deQuacked.append((char)114);
					break;
				case "QUacKQUack":
					deQuacked.append((char)115);
					break;
				case "quAcKQUack":
					deQuacked.append((char)116);
					break;
				case "QuAcKQUack":
					deQuacked.append((char)117);
					break;
				case "qUAcKQUack":
					deQuacked.append((char)118);
					break;
				case "QUAcKQUack":
					deQuacked.append((char)119);
					break;
				case "quaCKQUack":
					deQuacked.append((char)120);
					break;
				case "QuaCKQUack":
					deQuacked.append((char)121);
					break;
				case "qUaCKQUack":
					deQuacked.append((char)122);
					break;
				case "QUaCKQUack":
					deQuacked.append((char)123);
					break;
				case "quACKQUack":
					deQuacked.append((char)124);
					break;
				case "QuACKQUack":
					deQuacked.append((char)125);
					break;
				case "qUACKQUack":
					deQuacked.append((char)126);
					break;
				case "QUACKQUack":
					deQuacked.append((char)127);
					break;
			}
		}
		return deQuacked.toString();
	}
	// End Quack
	
	public static void main(String[] args) {
		QuackCryption quack = new QuackCryption();
		quack.fileThroughQuack("QuackQuackQuack!", "RandomInitVector", "test.png");
		quack.fileThroughNormal("QuackQuackQuack!", "RandomInitVector", "encrypted.quack");
	}
}