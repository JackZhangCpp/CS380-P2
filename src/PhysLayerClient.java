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
			
			HashMap<String, String> Hmap = new HashMap<>();
			ConvFivetoFour(Hmap);
			
			double baseline = 0;
			for(int i = 0; i <64; i++){ //loop to grab server message/bits
				int signal = is.read();
				baseline += signal;
			}
			baseline /=64;	//calculate baseline
			System.out.printf("Baseline established from preamble: %.2f\n",baseline);
			
		}

	}

	public static void ConvFivetoFour(HashMap<String,String> chart){
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


