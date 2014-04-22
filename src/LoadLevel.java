import java.util.*;
import java.io.*;
import acm.graphics.*;

public abstract class LoadLevel {
	
	public static Block[] loadBlocks(String fileName){
		

		/** Read in and store Blocks **/

		ArrayList<Block> blocksArray = new ArrayList<Block>();

		try{

			Scanner sc = new Scanner(new File(fileName));

			int lineNumber = 0;

			// Read in the lines one at a time and store them
			while(sc.hasNextLine()){
				
				String line = sc.nextLine();

				// Iterate through each character in the line, instantiating the specified block (if it exists)
				for(int i = 0; i < line.length(); i++){

					// Skip if it's a blank
					if(line.charAt(i) == '-')
						continue;

					GImage image;
					int type;

					// Normal Blocks
					if(line.charAt(i) - 'a' >= 0){
						type = line.charAt(i) - 'a';
						image = Data.blockImages[type];
						System.out.println(image);
					}
					// Grass Blocks
					else{
						type = line.charAt(i) - 'A';
						image = Data.blockGrassImages[type];
						System.out.println(image);
					}

					// Add Block to the ArrayList
					blocksArray.add(new Block(i*Block.SIZE, lineNumber*Block.SIZE, type, image));

				}

				lineNumber++;

			}

			sc.close();
		
		}
		catch(IOException e){
			return null;
		}

		return blocksArray.toArray(new Block[0]);

	}

}