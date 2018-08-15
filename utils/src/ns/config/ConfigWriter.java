package ns.config;

import ns.utils.GU;
import resources.Out;

import java.io.IOException;
import java.io.OutputStream;

public class ConfigWriter {

	public static void main(String[] args) throws IOException {
		OutputStream stream = Out.create("gameData/config/gameConfiguration.config").asOutputStream();
		while (true) {
			System.out.println("Full screen?Y/N");
			int in = System.in.read();
			if (in == 89 || in == 121) {
				stream.write(GU.binaryInt("0001 0001"));
				stream.write(GU.binaryInt("0000 0000"));
				break;
			}
			if (in == 78 || in == 110) {
				stream.write(GU.binaryInt("0001 0000"));
				stream.write(GU.binaryInt("0000 0000"));
				break;
			}
		}
		stream.close();
	}
}