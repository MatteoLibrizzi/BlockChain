import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//NEED TO FIX #47
public class Chain{
	public static final String CHARSET="UTF-8";
	private Block first;
	private LinkedList<Block> list;
	
	public Chain() throws NoSuchAlgorithmException, IOException {
		this.list=new LinkedList<Block>();
		makeFirstBlock();
	}

	public Block getLast(){//returns last block of the chain
		return this.list.getLast();
	}

	private void makeFirstBlock()throws IOException,
	NoSuchAlgorithmException{//creates the first block of the chain (making sure its valid)
		byte[] nullb=byteGen();
		byte[] mr=byteGen();//the bytes generated are random, so the chain starts different everytime
		int nonce=0;
		this.first=new Block(mr,nullb, nonce);
		while(!this.first.getValid()){//while the block isn't valid the nonce gets changed
			this.first.changeNonce();
		}
		this.startList(this.first);//once nonce is found the block is put at the start of the list
	}

	public byte[] getPrevBlockHash(Block block) throws NullPointerException{//returns the previus block's hash
		Block prev=this.first;
		for(int i=1;i<this.list.size();i++){
			if(this.list.get(i).equals(block)){//equals being overriden checks for equality in the attributes
				return prev.getHash();//once block attributes are found the hash of the previus one is returned
			}
			prev=this.list.get(i);//if it is not found the current block becomes the previus, as for moves forward
			
		}
		return prev.getHash();//if the chain only has one block (which is must have) its hash is returned
	}

	public static byte[] byteGen() {// GENERATES SALT
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[2];
		random.nextBytes(salt);
		return salt;
	}

	public boolean addBlock(Block block) throws IOException, NoSuchAlgorithmException,NullPointerException{//adds block to the chain (only if valid)
		boolean ok=false;
		if(Arrays.equals((this.getPrevBlockHash(block)),(block.getPrevHash()))){//checks if the previus block's hash and the given block's prev hash are the same
			ok=true;
		}
		if(block.getValid()&&ok){//if they are the block is considered valid, and can be added
			this.list.add(block);
			return true;
		}else{
			return false;
		}
	}

	private void startList(Block firstBlock){//starts the list adding the first block
		this.list.add(firstBlock);
	}

	public Block getFirst(){//returns the first block
		return this.first;
	}

	public static String toHex(byte[] input) {
        StringBuilder sb = new StringBuilder();
        
        for (byte b : input) {
            sb.append(Integer.toHexString(0xFF & b));
        }
        
        return sb.toString();
	}
	
	public static byte[] hash(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] output = md.digest(input);

        return output;
	}

	public String toStringTime(){
		int chainSize=this.list.size();
		String data="";
		Block b=null;
		for(int i=1;i<chainSize;i++){//loops throught the whole chain
			b=this.list.get(i);
			for(int j=0;j<7;j++){//timestamp's fixed length is 5
				int t=b.timeStamp[j];
				data=data+t;//appends time
				if(j<6){
					data=data+"-";//unless it's the end of the row it adds separation
				}
			}
			data=data+"\n";
		}
		return data;
	}

	public String toStringHash(){
		int chainSize=this.list.size();
		String data="";
		Block b=null;
		for(int i=1;i<chainSize;i++){//loops throught the whole chain
			b=this.list.get(i);
			String s=toHex(b.getHash());
			data=data+s+"\n";//appends hash and starts a new line
		}
		return data;
	}

	public boolean[] isValid(){
		int ll=this.list.size();
		boolean[] areValid=new boolean[ll];
		for(int i=0;i<ll;i++){
			Block b=this.list.get(i);
			if(b.getValid()){
				areValid[i]=true;
			}else{
				areValid[i]=false;
			}
		}
		return areValid;
	}

	public static void main(String args[]) throws NoSuchAlgorithmException, IOException, InterruptedException {
		Scanner scan=new Scanner(System.in);
		Chain c1=new Chain();

		System.out.println("Inserisci 3 Stringhe\n***ATTENZIONE***\nL'inserimento dei dati nella BlockChain potrebbe non essere sequenziale.");

		System.out.print("\nStringa 1: ");
		String data1s=scan.nextLine();

		System.out.print("\nStringa 2: ");
		String data2s=scan.nextLine();

		System.out.print("\nStringa 3: ");
		String data3s=scan.nextLine();

		Miner m1=new Miner(c1,data1s.getBytes(CHARSET));//miners require the chain they have to work on, and the data they have to insert
		m1.start();

		Miner m2=new Miner(c1,data2s.getBytes(CHARSET));
		m2.start();

		Miner m3=new Miner(c1,data3s.getBytes(CHARSET));
		m3.start();

		TimeUnit.MILLISECONDS.sleep(10000);//wait time is required to make sure the miners find the nonces before this prints the chain

		System.out.println(c1.toStringTime()+"toStringTime\n");

		System.out.println(c1.toStringHash()+"toStringHash");

	}

	/*public static void main(String args[]){
		Chain c1=new Chain();
		Chain c2=new Chain();

		String data1s="bla bla bla";
		String data2s="blo blo blo";
		String data3s="bli bli bli";

		Miner m1=new Miner(c1,data1s.getBytes(CHARSET));//miners require the chain they have to work on, and the data they have to insert
		m1.start();

		Miner m11=new Miner(c2,data2s.getBytes(CHARSET));
		m11.start();

		Miner m2=new Miner(c1,data2s.getBytes(CHARSET));
		m2.start();

		Miner m22=new Miner(c2,data2s.getBytes(CHARSET));
		m22.start();

		Miner m3=new Miner(c1,data3s.getBytes(CHARSET));
		m3.start();

		Miner m33=new Miner(c2,data1s.getBytes(CHARSET));
		m33.start();

		TimeUnit.MILLISECONDS.sleep(10000);

		System.out.println(c1.toStringTime()+"toStringTime 1");

		System.out.println(c1.toStringHash()+"toStringHash 1");

		System.out.println(c2.toStringTime()+"toStringTime 2");

		System.out.println(c2.toStringHash()+"toStringHash 2");
	}*/
}