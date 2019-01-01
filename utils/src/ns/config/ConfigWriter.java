/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.config;

import ns.utils.GU;
import resources.Out;

import java.io.IOException;
import java.io.OutputStream;

class ConfigWriter {

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