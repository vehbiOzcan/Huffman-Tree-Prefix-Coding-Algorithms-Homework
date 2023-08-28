import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		Scanner scann = new Scanner(System.in);
		
		System.out.print("Mesajýnýzý Giriniz: ");
		String message = scan.nextLine();
		
		System.out.println();
		
		System.out.print("Paket Boyutunu Giriniz (bit):");
		double packageSize = scan.nextDouble(); 
		
		System.out.println();
		
		System.out.print("Kodlanmýþ Bir Mesaj Girin:");
		String encodedMessage = scann.nextLine();
		
		Huffman hf = new Huffman(message,packageSize,encodedMessage);
		
	}

	//abbaaababffeeedaaddeea
	//bbddaacccdbb
}
