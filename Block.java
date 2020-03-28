import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;


public class Block implements Serializable{

	public int[] timeStamp=new int[7];
	private byte[] hashPrevius;
	private int nonce;
	private boolean valid;
	private byte[] hash;
	private byte[] merkleRoot;

	
	public Block(byte[] merkleRoot,byte[] hashPrevius,int nonce) throws IOException,
	NoSuchAlgorithmException {//builder
		this.setTimeStamp();
		this.hashPrevius=hashPrevius;
		this.nonce=nonce;
		this.merkleRoot=merkleRoot;
		this.setHash(this.findHash());
		this.isValid();
	}

	public Block(Block b){//copy builder
		timeStamp=b.timeStamp;
		hashPrevius=b.hashPrevius;
		nonce=b.nonce;
		merkleRoot=b.merkleRoot;
		valid=b.valid;
		hash=b.hash;
	}

	@Override
	public boolean equals(Object o){//overriden equals method, makes sure equals() checks for equality in the attributes rather than checking if the objects are the same
		if (this == o) {//if the objects are the same, returns true
                return true;
            }
            if (o == null || getClass() != o.getClass()) {//if the object is empty or the object do not belong to the same class, returns false
                return false;
            }
            Block b = (Block) o;//if all the attributes are the same, returns true, if even one is different returns false
            return timeStamp == b.timeStamp &&
					hashPrevius == b.hashPrevius &&
					nonce == b.nonce &&
					merkleRoot==b.merkleRoot&&
					valid == b.valid &&
					hash == b.hash;
	}

	public byte[] getPrevHash(){
		return this.hashPrevius;
	}

	public void setPrevHash(byte[] hash){
		this.hashPrevius=hash;
	}

	public byte[] findHash() throws IOException, NoSuchAlgorithmException {//returns the hash of the block based on all the attributes
		byte[] b=this.getBytes();

		return hash(b);
	}

	public void setHash(byte[] hash){//sets hash
		this.hash=hash;
	}

	public byte[] getHash(){//returns hash
		return this.hash;
	}

	public static byte[] byteGen() {// GENERATES random bytes
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[2];
		random.nextBytes(salt);
		return salt;
	}

	public void changeNonce() throws IOException, NoSuchAlgorithmException {//adds 1 to the nonce, sets the new hash and the new valid, doesn't check if the block is valid
		this.nonce++;//adds 1 to the nonce
		this.setHash(this.findHash());//finds hash with the new nonce
		this.isValid();//checks if the new hash is valid
	}

	public int getNonce(){//returns nonce
		return this.nonce;
	}

	public void isValid() throws IOException {//check if the first two bytes are = to 0, if so the difficult is matched
		byte[] h=this.getHash();
		
		byte b=(byte)0;
		if(Byte.compare(h[0], b)==0&&Byte.compare(h[1], b)==0/*&&Byte.compare(h[2], b)==0*/){//if the two bytes are = to 0, than the difficulty is met, and the block is considered valid, this could be increased of the decreased at will
			this.setValid(true);
		}else{
			this.setValid(false);
		}
	}

	public static String toHex(byte[] input) {
        StringBuilder sb = new StringBuilder();
        
        for (byte b : input) {
            sb.append(Integer.toHexString(0xFF & b));
        }
        
        return sb.toString();
    }

	public boolean getValid(){//returns whether the block is valid or not
		return this.valid;
	}

	private void setValid(boolean valid){//sets block's valid value
		this.valid=valid;
	}

	public void setTimeStamp() {// sets time stamp to the nanoseconds
		this.timeStamp[0] = LocalDateTime.now().getYear();
		this.timeStamp[1] = LocalDateTime.now().getMonthValue();
		this.timeStamp[2] = LocalDateTime.now().getDayOfMonth();
		this.timeStamp[3] = LocalDateTime.now().getHour();
		this.timeStamp[4] = LocalDateTime.now().getMinute();
		this.timeStamp[5]= LocalDateTime.now().getSecond();
		this.timeStamp[6]= LocalDateTime.now().getNano();
	}


	public static byte[] hash(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] output = md.digest(input);

        return output;
	}

	public byte[] getBytes() throws IOException {//returns byte[] of the block considering all the attributes (caused a lot of trouble)
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		ObjectOutputStream out= new ObjectOutputStream(bos);
		out.writeObject(this);//writes in the object output stream this
		out.flush();//transfers the date from the obj out stream to byte[] out stream, transforming its type in the process
		return bos.toByteArray();
	}

	public void printTime(){//prints time the block was created (debug use)
		for(int i=0;i<this.timeStamp.length;i++){
			System.out.println(this.timeStamp[i]);
		}
	}

/*	public static void main(String args[]) throws NoSuchAlgorithmException, IOException {
		byte[] prev=byteGen();
		byte[] mr=byteGen();

		Block block1=new Block(mr,prev,0);
		
		while(!block1.getValid()){

			block1.changeNonce();
			System.out.println(block1.nonce);
		}
		Block block2=new Block(block1);
		System.out.println(block1.equals(block2));
		System.out.println(block1==block2);
	}*/
}