import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Huffman {
	String message;
	String encMessage;
	Node huffmanTree;
	Map<Character, String> codeList = new HashMap<>();
	Map<Character, Integer> charFrequency;
	int orgSize = 0;
	int compSize = 0;
	double packageSize = 0;
	
	public Huffman(String message, double packageSize, String encodedMessage) {
		
		//-------DEÐERLERÝ BAÞLATMA--------
		this.message = message;
		this.encMessage = encodedMessage;
		this.packageSize = packageSize;
		this.charFrequency = calculateCharacterFrequency(message);
		this.huffmanTree = buildHuffmanTree(message);
		addCharCodeInMap(huffmanTree, "");
		this.orgSize = originalSize(charFrequency);
		this.compSize = compressSize(charFrequency);
		
		//-------------A þýkký---------------
		System.out.println("\na.1)Karakter Frekanslarý:"); 
		printMap(charFrequency);
		System.out.println("\na.2) Ouþturulan Huffman Aðacýnýn Ýçeriði:");
		printTree(huffmanTree);
		System.out.println("\na.3)Kodlanmýþ Mesaj: " + encodedMessage(message));
		
		//-------------B þýkký---------------
		System.out.println("\nb) Karakterlerin Kod Karþýlýklarý:");
		printCharCode(huffmanTree, "");
		
		//-------------C þýkký---------------
		System.out.println("\nOrjinal boyut: " + orgSize +" bit" );
		System.out.println("Sýkýþtýrýlmýþ boyut: " + compSize + " bit");
		System.out.println("\nc) Sýkýþtýrma Yüzdesi: %" + compressRatio());
		
		//-------------D þýkký---------------
		System.out.println("\nd) Mesajýnýz "+packageSize+" bit Paket Boyutu için " + calculatePackNumber(packageSize) + " Paket Halinde Gönderilecek");
		System.out.println();
		
		//-------------E þýkký---------------
		System.out.println("e) Gönderilen Kodlanmýþ Mesajýn Geçerliliði: ");
		isValid(isComeEncodedMessageValid(encMessage, huffmanTree));
		
		
		System.out.println("\nKarakter Kod Dizisi (Sýralý): ");
		printCodeMap(codeList);
		
		System.out.println();
	}
	
	public Node buildHuffmanTree(String message){ //Huffman aðacýný oluþturma
		int size = findDistinctCharacterCount(message);
		PriorityQueue<Node> priorityQueue = buildQueue(calculateCharacterFrequency(message));
			
		for(int i = 1 ; i < size ; i++) {
			
			Node node = new Node();
			Node nodeLeft = priorityQueue.poll();
			Node nodeRight = priorityQueue.poll();		
			node.left = nodeLeft ;
			node.right = nodeRight;
			node.frequency = nodeLeft.frequency + nodeRight.frequency;
			priorityQueue.add(node);
		
		}
		
		return priorityQueue.poll();
		//return null;
	}

	public PriorityQueue<Node> buildQueue(Map<Character, Integer> charFrequency){ //Kuyruðu öncelikli kuyruk yapýsý þeklinde oluþturma
		PriorityQueue<Node> queue = new PriorityQueue<>((n1,n2) -> n1.frequency - n2.frequency);
		
		
		for (Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
            char character = entry.getKey();
            int frequency = entry.getValue();
            Node node = new  Node(character, frequency, null, null);
            queue.add(node);
        }
		
		return queue;
	}
	
	public int findDistinctCharacterCount(String str) { //Farklý karakter sayýlarýný hesaplayan fonksiyon
	    Set<Character> distinctCharacters = new HashSet<>();

	    for (int i = 0; i < str.length(); i++) {
	        distinctCharacters.add(str.charAt(i));
	    }

	    return distinctCharacters.size();
	}
	
	public Map<Character, Integer> calculateCharacterFrequency(String message) { //Mesajdaki Karakter ve Frekans deðerlerini map olarak döner
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : message.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }

        return frequencyMap;
    }
	
	public int originalSize(Map<Character, Integer> charFrequency) { //Sýkýþtýrmadan önceki boyutu hesaplama 
		int sum = 0;
		int fr = 0;
		for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			fr = entry.getValue();
			sum += fr * 8;
		}
		
		return sum;
	}
	
	public int compressSize(Map<Character, Integer> charFrequency) { //Sýkýþtýrmadan sonraki boyut
		int sum = 0;
		char key;
		for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			key = entry.getKey();
			sum += this.codeList.get(key).length() * charFrequency.get(key);
		}
		
		return sum;
	}
	
	public double compressRatio(){ //Sýkýþma oranýný yüzde olarak hesaplar
		double ratio = (double)(this.compSize * 100) / this.orgSize;
		//System.out.println("%" + ratio);
		return ratio;
	}

	public int calculatePackNumber(double packageSize) {  //Gönderilecek paket sayýsýný paket boyutuna göre hesaplar
		int packNum = (int) Math.ceil(compSize / packageSize);
		
		return packNum;
	}
	
	
	 public void printTree(Node root) { //Huffman aðacýný yazdýrma

		if (root == null) {
	        return;
	    }

	    System.out.println("Karakter: " + root.character + ", Frekans: " + root.frequency);
	    printTree(root.left);
	    printTree(root.right);
	}
	 

	 public void printCharCode(Node root,String code) { //karakterlerin kod karþýlýðýný aðaç üzerinden yazdýrma
	    if (root == null) {
	        return;
	    }
	    if(root.left == null && root.right == null) {
	    	  System.out.println("Karakter: " + root.character + ", Kod: " + code);
	    }

	  
	    printCharCode(root.left,code + '0');
	    printCharCode(root.right,code + '1');
	}
	 
	 public void addCharCodeInMap(Node root,String code) { //karakterlerin kod karþýlýðýný codeList Map ine hash olarak ekler
		    if (root == null) {
		        return;
		    }
		    if(root.left == null && root.right == null) {
		    	  this.codeList.put(root.character, code);
		    }

		    addCharCodeInMap(root.left,code + '0');
		    addCharCodeInMap(root.right,code + '1');
		}
	 
	 public String encodedMessage(String message) {  //Sýkýþmýþ mesajý yazar 
		 StringBuilder encodedMessage = new StringBuilder();
		 
		 for(char c : message.toCharArray()) {
			 encodedMessage.append(this.codeList.get(c));
		 }
		 
		 return encodedMessage.toString();
	 }
	 
	 public boolean isComeEncodedMessageValid(String encodedMessage,Node huffmanTree) { //Gelen mesaj bizim aðacýmýza uygun mu kontrol eder
		 int count = 0;
		 Node current = huffmanTree;
		    for (char c : encodedMessage.toCharArray()) {
		    	count++;
		    	if (c == '0') {
		            current = current.left;
		            //System.out.println("L");
		        } else if (c == '1') {
		            current = current.right;
		            //System.out.println("R");
		        }

		        if (current.isLeaf()) {
		            current = huffmanTree; // Yeni bir karakterin baþýna dön
		           //System.out.println("H");
		        } else if (current == null || (!current.isLeaf() && encodedMessage.length() == count)) {
		            return false; // Kodlama hatasý var
		        }
		    }

		    return true;
	 }
	 
	 public String decodeMessage(String encodedMessage, Node huffmanTree) { //Gelen mesajý çözen fonksiyon
		 	
		 Node current = huffmanTree;
		 String decodeMess = "";
		 
		 	for (char c : encodedMessage.toCharArray()) {
		    	
		    	if (c == '0') {
		            current = current.left;
		        
		            
		        } else if (c == '1') {
		            current = current.right;
		           
		        }

		        if (current.isLeaf()) {
		        	decodeMess += current.character;
		        	current = huffmanTree;
		        }

		 	}
		 	
		 return decodeMess;
	 }
	 
	 public void isValid(boolean control) { //Geçerli bir kodlamamý kontrol eder doðruysa mesajý çözer
		if (control) {
			 System.out.println("Geçerli bir Kodlama");
			 System.out.println("Gelen Mesaj: " + decodeMessage(this.encMessage, this.huffmanTree));
		}
		else
			System.out.println("Geçersiz Bir Kodlama");
	 }
	 
	 public void printMap(Map<Character, Integer> charFrequency) { //Karakter ve Frekanslarýný tutan Map'i yazdýran metot
		 for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			 System.out.println("Karakter: " + entry.getKey() + " Frekans: " + entry.getValue());
		 }
		
	 }
	 
	 public void printCodeMap(Map<Character, String> codeList) { //Karakterlerin Kod Karþýlýklarýný Alfabetik Yazdýran Metot
		 for(Map.Entry<Character, String> entry : codeList.entrySet()) {
			 System.out.println("Karakter: " + entry.getKey() + " Deðer: " + entry.getValue());
		 }
		 System.out.println(); 
	 }
	 
    
}
