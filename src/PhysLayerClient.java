import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class PhysLayerClient {
	public static void main(String[] args) throws IOException {
		
		try(Socket socket = new Socket("18.221.102.182", 38002)){

			System.out.println("Connected to server.");
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			HashMap<String, String> Hmap = new HashMap<>();	//Create a map for conversion of 5 bits to 4	
			ConvFivetoFour(Hmap);			//Function call to generate map
	
			double baseline = 0;
			for(int i = 0; i <64; i++){ //loop to grab server message/bits
				int signal = is.read();
				baseline += signal;
			}
			baseline /=64;	//calculate baseline
			System.out.printf("Baseline established from preamble: %.2f\n",baseline);
			String[] halfByte = new String[64];
			boolean cur;
			boolean prev = false;
			//NRZI decoder 
			for(int i = 0; i < 64; i++){
				String checkByte = "";
				for(int j = 0; j < 5; j++){
					if(is.read()> baseline){
						cur = true;}
					else
						cur = false;
					if(cur == prev){
						checkByte+="0";}
					else
						checkByte+="1";
					prev = cur;
				}
				halfByte[i] = Hmap.get(checkByte); 	//conversion from 5 to 4 bits
			}
			
			System.out.print("Received 32 bytes: ");
			byte[] wholeNewBytes = new byte[32];
			//Display 32bytes and generates return 32 bit message
			for(int i = 0; i < 32; i++){
	
				String firstBytes = halfByte[i*2];
				String secondBytes = halfByte[i*2+1];
				System.out.printf("%X", Integer.parseInt(firstBytes, 2));
				System.out.printf("%X", Integer.parseInt(secondBytes, 2));
				wholeNewBytes[i]=(byte)Integer.parseInt((firstBytes + secondBytes),2);
			
			}	
			System.out.println();
			os.write(wholeNewBytes);	
			if(is.read()==1){
				System.out.println("Response is good.");
			}
			else
				System.out.println("Response is bad.");
		}
		System.out.println("Disconnected from server. ");
	}
	public static void ConvFivetoFour(HashMap<String,String> chart){
		//Creation of chart for conversion
		chart.put("11110","0000");
		chart.put("01001","0001");
		chart.put("10100","0010");
		chart.put("10101","0011");
		chart.put("01010","0100");
		chart.put("01011","0101");
		chart.put("01110","0110");
		chart.put("01111","0111");
		chart.put("10010","1000");
		chart.put("10011","1001");
		chart.put("10110","1010");
		chart.put("10111","1011");
		chart.put("11010","1100");
		chart.put("11011","1101");
		chart.put("11100","1110");
		chart.put("11101","1111");
	}


}


