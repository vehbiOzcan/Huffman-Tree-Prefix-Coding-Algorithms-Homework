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
		
		//-------DE�ERLER� BA�LATMA--------
		this.message = message;
		this.encMessage = encodedMessage;
		this.packageSize = packageSize;
		this.charFrequency = calculateCharacterFrequency(message);
		this.huffmanTree = buildHuffmanTree(message);
		addCharCodeInMap(huffmanTree, "");
		this.orgSize = originalSize(charFrequency);
		this.compSize = compressSize(charFrequency);
		
		//-------------A ��kk�---------------
		System.out.println("\na.1)Karakter Frekanslar�:"); 
		printMap(charFrequency);
		System.out.println("\na.2) Ou�turulan Huffman A�ac�n�n ��eri�i:");
		printTree(huffmanTree);
		System.out.println("\na.3)Kodlanm�� Mesaj: " + encodedMessage(message));
		
		//-------------B ��kk�---------------
		System.out.println("\nb) Karakterlerin Kod Kar��l�klar�:");
		printCharCode(huffmanTree, "");
		
		//-------------C ��kk�---------------
		System.out.println("\nOrjinal boyut: " + orgSize +" bit" );
		System.out.println("S�k��t�r�lm�� boyut: " + compSize + " bit");
		System.out.println("\nc) S�k��t�rma Y�zdesi: %" + compressRatio());
		
		//-------------D ��kk�---------------
		System.out.println("\nd) Mesaj�n�z "+packageSize+" bit Paket Boyutu i�in " + calculatePackNumber(packageSize) + " Paket Halinde G�nderilecek");
		System.out.println();
		
		//-------------E ��kk�---------------
		System.out.println("e) G�nderilen Kodlanm�� Mesaj�n Ge�erlili�i: ");
		isValid(isComeEncodedMessageValid(encMessage, huffmanTree));
		
		
		System.out.println("\nKarakter Kod Dizisi (S�ral�): ");
		printCodeMap(codeList);
		
		System.out.println();
	}
	
	public Node buildHuffmanTree(String message){ //Huffman a�ac�n� olu�turma
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

	public PriorityQueue<Node> buildQueue(Map<Character, Integer> charFrequency){ //Kuyru�u �ncelikli kuyruk yap�s� �eklinde olu�turma
		PriorityQueue<Node> queue = new PriorityQueue<>((n1,n2) -> n1.frequency - n2.frequency);
		
		
		for (Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
            char character = entry.getKey();
            int frequency = entry.getValue();
            Node node = new  Node(character, frequency, null, null);
            queue.add(node);
        }
		
		return queue;
	}
	
	public int findDistinctCharacterCount(String str) { //Farkl� karakter say�lar�n� hesaplayan fonksiyon
	    Set<Character> distinctCharacters = new HashSet<>();

	    for (int i = 0; i < str.length(); i++) {
	        distinctCharacters.add(str.charAt(i));
	    }

	    return distinctCharacters.size();
	}
	
	public Map<Character, Integer> calculateCharacterFrequency(String message) { //Mesajdaki Karakter ve Frekans de�erlerini map olarak d�ner
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : message.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }

        return frequencyMap;
    }
	
	public int originalSize(Map<Character, Integer> charFrequency) { //S�k��t�rmadan �nceki boyutu hesaplama 
		int sum = 0;
		int fr = 0;
		for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			fr = entry.getValue();
			sum += fr * 8;
		}
		
		return sum;
	}
	
	public int compressSize(Map<Character, Integer> charFrequency) { //S�k��t�rmadan sonraki boyut
		int sum = 0;
		char key;
		for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			key = entry.getKey();
			sum += this.codeList.get(key).length() * charFrequency.get(key);
		}
		
		return sum;
	}
	
	public double compressRatio(){ //S�k��ma oran�n� y�zde olarak hesaplar
		double ratio = (double)(this.compSize * 100) / this.orgSize;
		//System.out.println("%" + ratio);
		return ratio;
	}

	public int calculatePackNumber(double packageSize) {  //G�nderilecek paket say�s�n� paket boyutuna g�re hesaplar
		int packNum = (int) Math.ceil(compSize / packageSize);
		
		return packNum;
	}
	
	
	 public void printTree(Node root) { //Huffman a�ac�n� yazd�rma

		if (root == null) {
	        return;
	    }

	    System.out.println("Karakter: " + root.character + ", Frekans: " + root.frequency);
	    printTree(root.left);
	    printTree(root.right);
	}
	 

	 public void printCharCode(Node root,String code) { //karakterlerin kod kar��l���n� a�a� �zerinden yazd�rma
	    if (root == null) {
	        return;
	    }
	    if(root.left == null && root.right == null) {
	    	  System.out.println("Karakter: " + root.character + ", Kod: " + code);
	    }

	  
	    printCharCode(root.left,code + '0');
	    printCharCode(root.right,code + '1');
	}
	 
	 public void addCharCodeInMap(Node root,String code) { //karakterlerin kod kar��l���n� codeList Map ine hash olarak ekler
		    if (root == null) {
		        return;
		    }
		    if(root.left == null && root.right == null) {
		    	  this.codeList.put(root.character, code);
		    }

		    addCharCodeInMap(root.left,code + '0');
		    addCharCodeInMap(root.right,code + '1');
		}
	 
	 public String encodedMessage(String message) {  //S�k��m�� mesaj� yazar 
		 StringBuilder encodedMessage = new StringBuilder();
		 
		 for(char c : message.toCharArray()) {
			 encodedMessage.append(this.codeList.get(c));
		 }
		 
		 return encodedMessage.toString();
	 }
	 
	 public boolean isComeEncodedMessageValid(String encodedMessage,Node huffmanTree) { //Gelen mesaj bizim a�ac�m�za uygun mu kontrol eder
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
		            current = huffmanTree; // Yeni bir karakterin ba��na d�n
		           //System.out.println("H");
		        } else if (current == null || (!current.isLeaf() && encodedMessage.length() == count)) {
		            return false; // Kodlama hatas� var
		        }
		    }

		    return true;
	 }
	 
	 public String decodeMessage(String encodedMessage, Node huffmanTree) { //Gelen mesaj� ��zen fonksiyon
		 	
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
	 
	 public void isValid(boolean control) { //Ge�erli bir kodlamam� kontrol eder do�ruysa mesaj� ��zer
		if (control) {
			 System.out.println("Ge�erli bir Kodlama");
			 System.out.println("Gelen Mesaj: " + decodeMessage(this.encMessage, this.huffmanTree));
		}
		else
			System.out.println("Ge�ersiz Bir Kodlama");
	 }
	 
	 public void printMap(Map<Character, Integer> charFrequency) { //Karakter ve Frekanslar�n� tutan Map'i yazd�ran metot
		 for(Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
			 System.out.println("Karakter: " + entry.getKey() + " Frekans: " + entry.getValue());
		 }
		
	 }
	 
	 public void printCodeMap(Map<Character, String> codeList) { //Karakterlerin Kod Kar��l�klar�n� Alfabetik Yazd�ran Metot
		 for(Map.Entry<Character, String> entry : codeList.entrySet()) {
			 System.out.println("Karakter: " + entry.getKey() + " De�er: " + entry.getValue());
		 }
		 System.out.println(); 
	 }
	 
    
}
