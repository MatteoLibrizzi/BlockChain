import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Miner extends Thread {
	Chain chain;
	byte[] data;

	public Miner(Chain chain, byte[] data) {
		this.chain = chain;
		this.data = data;
	}

	public static byte[] hash(byte[] input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		byte[] output = md.digest(input);

		return output;
	}

	public static String toHex(byte[] input) {
        StringBuilder sb = new StringBuilder();
        
        for (byte b : input) {
            sb.append(Integer.toHexString(0xFF & b));
        }
        
        return sb.toString();
	}

	@Override
	public void run() {
		try {
			byte[] hdata = hash(this.data);//transforms the data to an hash so no matter its length, the stored length is fixed
			Block block=null;
			do{
				Block last = this.chain.getLast();
				byte[] lasth = last.getHash();//stores the hash of the last block of the chain

				int i = 0;

			
				block = new Block(hdata, lasth, i);//creates a new block with the hashed data and the hash of the last block to make it legitimate

				while(!block.getValid()){//changes the nonce until it's valid
					block.changeNonce();
					//System.out.println(toHex(block.getHash()));
				}
			}while(!this.chain.addBlock(block));
			System.out.println("Successful inserting - "+new String(this.data));

		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
}