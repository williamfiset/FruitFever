import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.nio.file.*;

public class LevelDesigner extends GraphicsProgram implements MouseMotionListener {

	/** TO DO 

		Crucial:
		-Add hint functionality

		Necessary:
		-If you saveAs and then cancel it, next time you go to save you need to specify which file you want to load, since infoFile is now null
		-Images seem to be overlapping each other multiple times? Aren't the ones underneath supposed to be erased?
		-Add ability to set whether a block is able to fall or not (capital letters for now). Best approach, add a layer of GRects
		to the screen that are slightly transparent, to represent the ones that can fall

		Extras:
		-Write script replace one image for another throughout each levelDesigner.ser file.
		-Add icon to program (William made a ghetto icon for you. You best respect him :P)
		-Move blocks (multi-select?)
		-Add modes: Add, Move, etc.
		-Add animations to drawing board and even to the menu?
		-Add Exit and Help to the menu
		-Copy/Paste

		Unneeded Extras:
		-Add different cursors (research this)
		-Add mini-map
		-Undo/Redo

		Refactoring:
		-Add trimExtension method to get rid of .ser from the end of a String
		-Add method to move all images in both layers by a given amount
		-Re-organize methods so they are in a logical order
		
	**/

/** MAIN OBJECTS AND ARRAYLISTS **/
	
	private static InformationStorer infoFile = null;

	private final JFileChooser fileChooser = new JFileChooser();

	private static ArrayList<GImage> blockLayer = new ArrayList<>(), sceneryLayer = new ArrayList<>();
	
/** CONSTANTS **/
	
	public static final int MENU_HEIGHT = 70;
	private static final int MENU_ORIGIN_X = 10;
	
/** GRAPHICS **/

	private static GRect
		background = new GRect(0, 0, FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT + MENU_HEIGHT),
		menuBackground = new GRect(0, 0, FruitFever.SCREEN_WIDTH, MENU_HEIGHT);

	private static GRect selectedObjectsBox = new GRect(0, 0, 0, 0);
	private double selectedObjectsX, selectedObjectsY;
	private ArrayList<GImage> selectedObjects = new ArrayList<>();

	private static GRect selectedMenuBox = new GRect(0, 0, 0, 0);
	private double selectedMenuX, selectedMenuY;
	private static GObject selected = null;
	

/** MOUSE/KEYBOARD **/

	private static enum MouseButtonPressed { LEFT, RIGHT, NONE }
	private static MouseButtonPressed mouseButtonPressed = MouseButtonPressed.NONE;

	private static double mouseX, mouseY;

	private boolean controlPressed = false, shiftPressed = false;


/** LEVEL VARIABLES **/

	private static int level;
	private static String name = null;

	GImage vortex = null, player = null;

/** ENUMS **/	

	private static enum Layer { BLOCK, SCENERY }
	
	public static enum Set {
	
		BLOCKS(Layer.BLOCK),
		FRUIT(Layer.BLOCK),
		SPECIAL(Layer.BLOCK),
		SCENERY_1(Layer.SCENERY),
		SCENERY_2(Layer.SCENERY),
		POWERUPS(Layer.SCENERY);
		
		public Layer layer;
		
		Set(Layer layer) {
			this.layer = layer;
		}
		
	}

	private static Layer currentLayer = Layer.BLOCK;
	private static Set currentSet = Set.BLOCKS;

	public static enum Mode { ADD, SELECT, MOVE }
	public static Mode currentMode = Mode.MOVE;
	

	/** Contains the main game loop **/
	@Override public void run() {

		/** Set file chooser to proper directory **/
		fileChooser.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toString() + "/levels/designedLevels"));
		
		/** Set up keyboard and mouse **/
		addMouseListeners();
		addKeyListeners();
	
		/** Imports images into the Data class **/
		Data.loadImages();
		
		/** Draw background **/
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);
		
		/** Draw menu **/
		menuBackground.setFillColor(Color.GRAY);
		menuBackground.setFilled(true);
		add(menuBackground);
		drawSet(currentSet);
		
		/** Add selected boxes to screen **/

		updateSelectedMenuBox();
		selectedMenuBox.setColor(Color.YELLOW);
		add(selectedMenuBox);

		selectedObjectsBox.setColor(Color.GREEN);
		selectedObjectsBox.setVisible(false);
		add(selectedObjectsBox);

		while (true) {
			if (!DesignerStarter.appletFrame.isFocused()) {
				controlPressed = false;
				shiftPressed = false;
			}
		}

	}

	/** Updates the location and size of the selected box **/
	private void updateSelectedMenuBox() {
		if (selected == null)
			selectedMenuBox.setLocation(-100, 100);
		else {
			selectedMenuBox.setLocation(selectedMenuX, selectedMenuY);
			selectedMenuBox.setSize(selected.getWidth(), selected.getHeight());
		}
	}

	/** Removes objects from screen and clears the lists **/
	public void newDrawingBoard() {

		infoFile = null;

		DesignerStarter.appletFrame.setTitle("Level Designer");	

		for (GImage img : blockLayer) {

			if (img.equals(Data.playerStill[0]))
				player = null;

			if (img.equals(Data.vortexAnimation[0]))
				vortex = null;

			remove(img);
		}

		blockLayer.clear();

		for (GImage img : sceneryLayer)
			remove(img);

		sceneryLayer.clear();

	}
	
	@Override public void mousePressed(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();
		
		/** Change Objects/Selection or Add Object **/
		if (mouse.getButton() == 1) {

			mouseButtonPressed = MouseButtonPressed.LEFT;

			if (mouse.getY() < MENU_HEIGHT) {
				try {

					if (controlPressed)
						switchObjects(selected, getElementAt(mouse.getX(), mouse.getY()));

					GObject obj = getElementAt(mouse.getX(), mouse.getY());

					selected = copyImage(obj);
					selectedMenuX = obj.getX();
					selectedMenuY = obj.getY();
					updateSelectedMenuBox();

				} catch (ClassCastException e) { }
			} else
				addObject();
		
		/** Erase Object **/
		} else if (mouse.getButton() == 3) {
			mouseButtonPressed = MouseButtonPressed.RIGHT;
			eraseObject();
		}

	}

	private void addObject() {

		if (selected != null) {
			if (!controlPressed) {
				selected = copyImage(selected);
				updatePosition();
				add(selected);
				finalizeObject();
			}
		}
	}

	@Override public void mouseDragged(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

		if (mouse.getY() >= MENU_HEIGHT) {
			if (mouseButtonPressed == MouseButtonPressed.LEFT)
				addObject();
			else if (mouseButtonPressed == MouseButtonPressed.RIGHT)
				eraseObject();
		}

	}

	@Override public void mouseReleased(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

		mouseButtonPressed = MouseButtonPressed.NONE;

	}

	@Override public void mouseMoved(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

	}
	
	/** Adds an object to the drawing board, removing any underlying objects **/
	private void finalizeObject() {
			
		if (currentLayer == Layer.BLOCK) {
		
			/** Remove previous object in Block Layer if it exists **/
			removeObjectsFromLayer(blockLayer);
				
			/** Add new object to the Block Layer **/
			blockLayer.add((GImage) selected);
	
		} else if (currentLayer == Layer.SCENERY){
			
			/** Remove previous object in Scenery Layer if it exists **/
			removeObjectsFromLayer(sceneryLayer);
				
			/** Add new object to the Scenery Layer **/
			sceneryLayer.add((GImage) selected);
		
		}
		
		fixLayering();
	}

	private void removeObjectsFromLayer(ArrayList<GImage> arr) {

		for (GImage obj : arr)
			if (getTileBoundary(obj).contains(mouseX, mouseY)) {

				if (obj.equals(Data.playerStill[0]))
					player = null;

				if(obj.equals(Data.vortexAnimation[0]))
					vortex = null;

				remove(obj);
				blockLayer.remove(obj);
				break;
			}

	}

	private void switchObjects(GObject oldImg, GObject newImg) {

		if (newImg == null)
				return;

		if (currentLayer == Layer.BLOCK) {

			/** Replace all old images with the new ones **/
			for (int i = 0 ; i < blockLayer.size(); i++)
				if (ImageTransformer.isIdentical(blockLayer.get(i), (GImage) oldImg))
					blockLayer.get(i).setImage(copyImage(newImg).getImage());

		} else if (currentLayer == Layer.SCENERY) {
			
			/** Replace all old images with the new ones **/
			for (int i = 0 ; i < sceneryLayer.size(); i++)
				if (ImageTransformer.isIdentical(sceneryLayer.get(i), (GImage) oldImg))
					sceneryLayer.get(i).setImage(copyImage(newImg).getImage());

		}

	}
	
	/** Sets the position of the selected object, snapping it to the grid **/
	private void updatePosition() {

		double x = roundPos(mouseX) + findXOffset((GImage) selected);
		double y = Math.max(roundPos(mouseY + findYOffset((GImage) selected) - MENU_HEIGHT) + MENU_HEIGHT, MENU_HEIGHT);

		selected.setLocation(x, y);

	}

	private GRect getTileBoundary(GImage img) {

		/**
		This should do.. it rounds from the center of the image.
		If we have massive or oddly off-centered objects we could run into issues
		Note: We could just use xOffset and yOffset, test to make sure it works..!
		**/

		double x = roundPos(img.getX() + img.getWidth()/2);
		double y = Math.max(roundPos(img.getY() + img.getHeight()/2 - MENU_HEIGHT) + MENU_HEIGHT, MENU_HEIGHT);

		return new GRect(x, y, Data.TILE_SIZE, Data.TILE_SIZE);

	}

	private double snapToGridY(double y) { 
		return Math.max(roundPos(y - MENU_HEIGHT) + MENU_HEIGHT, MENU_HEIGHT);
	}

	private int findXOffset(GImage img) {

		if (ImageTransformer.isIdentical(img, Data.sceneryImages[0]))
			return -Data.TILE_SIZE/2;
		else if (ImageTransformer.isIdentical(img, Data.sceneryImages[6]))
			return -3;
		else if (ImageTransformer.isIdentical(img, Data.playerStill[0]))
			return -Data.TILE_SIZE;
		else
			return 0;

	}

	private int findYOffset(GImage img) {

		if (ImageTransformer.isIdentical(img, Data.checkpointFlagRed))
			return -Data.TILE_SIZE;
		else
			return 0;

	}
	
	/** Snap to grid **/
	private double roundPos(double pos) { return (double) ((((int)pos)/Data.TILE_SIZE)*Data.TILE_SIZE); }
	
	/** Makes a copy of an image **/
	private GImage copyImage(GObject obj) {
		return new GImage(((GImage) obj).getImage());
	}
	
	/** Take user input **/
	@Override public void keyPressed(KeyEvent key) {

		switch (key.getKeyCode()) {
				
			case KeyEvent.VK_LEFT:
				for (GImage obj : sceneryLayer)
					obj.move(Data.TILE_SIZE, 0);
				for (GImage obj : blockLayer)
					obj.move(Data.TILE_SIZE, 0);
				break;
				
			case KeyEvent.VK_RIGHT:
				for (GImage obj : sceneryLayer)
					obj.move(-Data.TILE_SIZE, 0);
				for (GImage obj : blockLayer)
					obj.move(-Data.TILE_SIZE, 0);
				break;
				
			case KeyEvent.VK_UP:
				for (GImage obj : sceneryLayer)
					obj.move(0, Data.TILE_SIZE);
				for (GImage obj : blockLayer)
					obj.move(0, Data.TILE_SIZE);
				break;
				
			case KeyEvent.VK_DOWN:
				for (GImage obj : sceneryLayer)
					obj.move(0, -Data.TILE_SIZE);
				for (GImage obj : blockLayer)
					obj.move(0, -Data.TILE_SIZE);
				break;

			case KeyEvent.VK_E:
				if (controlPressed)
					export();
				break;

			case KeyEvent.VK_N:
				if (controlPressed)
					newDrawingBoard();
				break;

			case KeyEvent.VK_O:
				if (controlPressed)
					open();
				break;

			case KeyEvent.VK_P:
				if (player == null) {
					player = Data.playerStill[0];
					player.setLocation(roundPos(mouseX) + findXOffset(Data.playerStill[0]), snapToGridY(mouseY) + findYOffset(Data.playerStill[0]));
					add(player);
					removeObjectsFromLayer(blockLayer);
					blockLayer.add(player);
				} else
					player.setLocation(roundPos(mouseX) + findXOffset(Data.playerStill[0]), snapToGridY(mouseY) + findYOffset(Data.playerStill[0]));
				break;

			case KeyEvent.VK_S:
				if (controlPressed) {
					if (shiftPressed) saveAs();
					else save();
				}
				break;

			case KeyEvent.VK_V:
				if (vortex == null) {
					vortex = Data.vortexAnimation[0];
					vortex.setLocation(roundPos(mouseX) + findXOffset(Data.vortexAnimation[0]), snapToGridY(mouseY) + findYOffset(Data.vortexAnimation[0]));
					add(vortex);
					removeObjectsFromLayer(blockLayer);
					blockLayer.add(vortex);
				} else
					vortex.setLocation(roundPos(mouseX) + findXOffset(Data.vortexAnimation[0]), snapToGridY(mouseY) + findYOffset(Data.vortexAnimation[0]));
				break;

			case KeyEvent.VK_CONTROL:
				controlPressed = true;
				break;

			case KeyEvent.VK_SHIFT:
				shiftPressed = true;
				break;
				
		}
		
	}

	/** Take user input **/
	@Override public void keyReleased(KeyEvent key){
				
		int keyCode = key.getKeyCode();

		switch (keyCode) {

			case KeyEvent.VK_CONTROL:
				controlPressed = false;
				break;

			case KeyEvent.VK_SHIFT:
				shiftPressed = false;
				break;
				
		}
		
	}
	
	/** Changes the menu set on the screen **/
	public void changeSelectedSet(Set newSet) {
		
		currentMode = Mode.ADD;

		selected = null;
		updateSelectedMenuBox();
		
		removeSet(currentSet);
		drawSet(newSet);
		
		currentSet = newSet;
		currentLayer = currentSet.layer;
		
		fixLayering();
		
	}
	
	/** Adds a menu set to the screen **/
	private void drawSet(Set set) {
	
		switch (set) {
		
			case BLOCKS:
				for (int i = 0; i < Data.blockImages.length; i++)
					addToMenu(Data.blockImages[i][0], i*Data.TILE_SIZE);
				break;
				
			case FRUIT:
				for (int i = 0; i < Data.fruits.length; i++)
					addToMenu(Data.fruits[i][0], i*Data.TILE_SIZE);
				break;
				
			case SPECIAL:
				addToMenu(Data.fruitRingAnimation[5], 0*Data.TILE_SIZE);
				addToMenu(Data.lava[0], 1*Data.TILE_SIZE);
				addToMenu(Data.spikes[0], 2*Data.TILE_SIZE);
				addToMenu(Data.torches[0][0], 3*Data.TILE_SIZE);
				addToMenu(Data.gasBubbles[0], 4*Data.TILE_SIZE);
				addToMenu(Data.hintSign[1], 5*Data.TILE_SIZE);
				addToMenu(Data.checkpointFlagRed, 6*Data.TILE_SIZE);
				break;
				
			case POWERUPS:
				for (int i = 0; i < Data.powerups.length; i++)
					addToMenu(Data.powerups[i], i*Data.TILE_SIZE);
				break;
				
			case SCENERY_1:
				int xPos = 0;
				for (int i = 0; i < 25; i++) {
					addToMenu(Data.sceneryImages[i], xPos);
					xPos += Data.sceneryImages[i].getWidth();
				}
				break;
				
			case SCENERY_2:
				xPos = 0;
				for (int i = 25; i < Data.sceneryImages.length; i++) {
					addToMenu(Data.sceneryImages[i], xPos);
					xPos += Data.sceneryImages[i].getWidth();
				}
				break;
		}	
	
	}
	
	private void addToMenu(GImage img, int x) {
		img.setLocation(x + MENU_ORIGIN_X, (MENU_HEIGHT - img.getHeight())/2);
		add(img);
	}

	/** Removes a menu set from the screen **/
	private void removeSet(Set set) {
	
		switch (set) {
		
			case BLOCKS:
				for (int i = 0; i < Data.blockImages.length; i++)
					remove(Data.blockImages[i][0]);
				break;
				
			case FRUIT:
				for (int i = 0; i < Data.fruits.length; i++)
					remove(Data.fruits[i][0]);
				break;
				
			case SPECIAL:
				remove(Data.fruitRingAnimation[5]);
				remove(Data.lava[0]);
				remove(Data.spikes[0]);
				remove(Data.torches[0][0]);
				remove(Data.gasBubbles[0]);
				remove(Data.hintSign[1]);
				remove(Data.checkpointFlagRed);
				break;
			
			case POWERUPS:
				for (int i = 0; i < Data.powerups.length; i++)
					remove(Data.powerups[i]);
				
			case SCENERY_1:
				for (int i = 0; i < 25; i++)
					remove(Data.sceneryImages[i]);
					
			case SCENERY_2:
				for (int i = 25; i < Data.sceneryImages.length; i++)
					remove(Data.sceneryImages[i]);
		}
	
	}

	/** Re-exports all levels in the designedLevels folder **/
	public void exportAll() {

		File folder = new File(Paths.get("").toAbsolutePath().toString() + "/levels/designedLevels");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile()) {
				loadDesignedLevel(listOfFiles[i].getName());
				export();
			}
	}

	public void promptLevelTitle() {
		name = JOptionPane.showInputDialog(DesignerStarter.appletFrame, "Enter the level's title", "Title", JOptionPane.PLAIN_MESSAGE);
		
	}
	
	/** Exports the drawing board to file **/
	public void export() {

		if (name == null)
			promptLevelTitle();

		save();

		System.out.println("Export Has Begun.");
	
		resetView();
	
		PrintWriter writer = null;

		try {
			writer = new PrintWriter("levels/exportedLevels/" + level + ".txt", "UTF-8");
		} catch (IOException e) { e.printStackTrace(); }

		writer.println(name);

		int width = findWidth(), height = findHeight();
		int tilesWide = width/Data.TILE_SIZE, tilesHigh = height/Data.TILE_SIZE;

		writer.println(width);
		writer.println(height);
		
		/** Block Layer **/
		
		for (int y = 0; y < tilesHigh; y++) {
			nextChar:
			for (int x = 0; x < tilesWide; x++) {
				
				for (GImage obj : blockLayer)
					if (obj.getX() - findXOffset((GImage) obj) == x*Data.TILE_SIZE && obj.getY() - findYOffset((GImage) obj) == y*Data.TILE_SIZE + MENU_HEIGHT) {
						
						/** Fruit **/
						for (int i = 0; i < Data.fruits.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.fruits[i][0])) {
								writer.print(i);
								continue nextChar;
							}
						
						/** Blocks **/
						for (int i = 0; i < Data.blockImages.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.blockImages[i][0])) {
								writer.print((char) (i + 97)); // ASCII Chart (lower-case letters start at 97)
								continue nextChar;
							}

						if (ImageTransformer.isIdentical(obj, Data.fruitRingAnimation[5])) {
							writer.print('*');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.lava[0])) {
							writer.print('~');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.spikes[0])) {
							writer.print('^');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.torches[0][0])) {
							writer.print('&');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.gasBubbles[0])) {
							writer.print(':');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.hintSign[1])) {
							writer.print('?');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.checkpointFlagRed)) {
							writer.print('|');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.playerStill[0])) {
							writer.print('@');
							continue nextChar;
						}

						if (ImageTransformer.isIdentical(obj, Data.vortexAnimation[0])) {
							writer.print('%');
							continue nextChar;
						}
						
						System.out.println("Unrecognizable object. Could not assign a character to export it as. The image is still in the designed level file.");
					
					}
				
				// No block was located, so it's a blank
				writer.print("-");
				
			}
			
			writer.println();
		}

		System.out.println("Block Layer Generated.");
		
		writer.println("+");
		
		/** Scenery Layer **/
		
		for (int y = 0; y < tilesHigh; y++) {
			nextChar:
			for (int x = 0; x < tilesWide; x++) {
				
				for (GImage obj : sceneryLayer)
					if (obj.getX() - findXOffset(obj) == x*Data.TILE_SIZE && obj.getY() - findYOffset((GImage) obj) == y*Data.TILE_SIZE + MENU_HEIGHT) {
						
						/** Powerups **/
						for (int i = 0; i < Data.powerups.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.powerups[i])) {
								writer.print(String.valueOf(i));
								continue nextChar;
							}
	
						/** Scenery **/
						for (int i = 0; i < Data.sceneryImages.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.sceneryImages[i])) {
								if (i < 26)
									writer.print((char) (i + 97)); // ASCII Chart (lower-case letters start at 97)
								else
									writer.print((char) (i - 26 + 65)); // ASCII Chart (upper-case letters start at 65)
								continue nextChar;
							}
							
						}
					
				
				// No block was located, so it's a blank
				writer.print("-");
				
			}
			
			writer.println();
		}

		System.out.println("Scenery Layer Generated.");

		writer.close();
		
		System.out.println("Finished Exporting.");
	
	}

	public void saveAs() {
		infoFile = null;
		save();
	}

	public void save() {

		/** Create file and then use it **/

		if (infoFile == null) {

			fileChooser.setDialogTitle("Enter this level's number");

			int result = fileChooser.showSaveDialog(this);
         	if (result != JFileChooser.APPROVE_OPTION) {
         		System.out.println("No file was chosen. Save aborted.");
         		return;
         	}


         	File selectedFile = fileChooser.getSelectedFile();
         	String name = selectedFile.getName();

         	if (name.lastIndexOf(".ser") != -1)
         		level = Integer.valueOf(name.substring(0, name.lastIndexOf('.')));
         	else
         		level = Integer.valueOf(name);

			infoFile = new InformationStorer("levels/designedLevels/" + level);
			DesignerStarter.appletFrame.setTitle("Level Designer (Level " + level + ")");	

		}

		System.out.println("Beginning to Save.");

		/** Saves objects to file **/
		
		ArrayList<SerializableThing> blocks = new ArrayList<>();
		
		for (GImage img : blockLayer)
			blocks.add(new SerializableThing((int) img.getX(), (int) img.getY(), img.getPixelArray()));
			
		infoFile.addItem("blockLayer", blocks);
			
		ArrayList<SerializableThing> scenery = new ArrayList<>();
		
		for (GImage img : sceneryLayer)
			scenery.add(new SerializableThing((int) img.getX(), (int) img.getY(), img.getPixelArray()));	

		infoFile.addItem("sceneryLayer", scenery);

		infoFile.addItem("name", name);

		System.out.println("Finished Saving.");

	}

	/** Initiates loading procedure by selecting a file and clearing the drawing board **/
	public void open() {

		fileChooser.setDialogTitle("Select a level to load");
		fileChooser.showOpenDialog(this);

		int result = fileChooser.showOpenDialog(this);
     	if (result != JFileChooser.APPROVE_OPTION) {
     		System.out.println("No file was chosen. Load aborted.");
     		return;
     	}

     	File selectedFile = fileChooser.getSelectedFile();
       	
        /** Attempt to load saved level **/

        newDrawingBoard();

        loadDesignedLevel(selectedFile.getName());

	}

	private void loadDesignedLevel(String levelNumber) {
		if (levelNumber.lastIndexOf(".ser") != -1)
     		level = Integer.valueOf(levelNumber.substring(0, levelNumber.lastIndexOf('.')));
     	else
     		level = Integer.valueOf(levelNumber);

 		infoFile = new InformationStorer("levels/designedLevels/" + level);
 		DesignerStarter.appletFrame.setTitle("Level Designer (Level " + level + ")");	

		blockLayer = loadFromFile(blockLayer, "blockLayer");
		sceneryLayer = loadFromFile(sceneryLayer, "sceneryLayer");
		name = (String) infoFile.getItem("name");

		fixLayering();
	}

	private ArrayList<GImage> loadFromFile(ArrayList<GImage> arr, String str) {

		/** Add new objects **/

		if (infoFile.checkItemExists(str)) {

			@SuppressWarnings("unchecked") ArrayList<SerializableThing> tempArr = (ArrayList<SerializableThing>) infoFile.getItem(str);
			
			for (SerializableThing obj : tempArr) {
				GImage img = new GImage(obj.pixelArray);

				if (img.equals(Data.playerStill[0]))
					player = img;

				if (img.equals(Data.vortexAnimation[0]))
					vortex = img;

				arr.add(img);
				img.setLocation(obj.x, obj.y);
				add(img);
			}
			
		}
		
		return arr;
	
	}
	
	/** Properly layers the images on the sceen **/
	private void fixLayering() {
	
		menuBackground.sendToBack();
		
		selectedMenuBox.sendToFront();
	
		for (GImage obj : sceneryLayer)
			obj.sendToBack();
			
		for (GImage obj : blockLayer)
			obj.sendToBack();
		
		background.sendToBack();

	}
	
	/** Resets the view in preparation for export **/
	private void resetView() {
		
		/** Calculate Distance to Move **/
		
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		
		for (GImage obj : sceneryLayer) {
			minX = Math.min(minX, obj.getX() - findXOffset(obj));
			minY = Math.min(minY, obj.getY() - MENU_HEIGHT - findYOffset(obj));
		}
		
		for (GImage obj : blockLayer) {
			minX = Math.min(minX, obj.getX() - findXOffset(obj));
			minY = Math.min(minY, obj.getY() - MENU_HEIGHT- findYOffset(obj));
		}
		
		/** Move Objects **/
		
		for (GImage obj : sceneryLayer)
			obj.move(-minX, -minY);
			
		for (GImage obj : blockLayer)
			obj.move(-minX, -minY);
		
	}
	
	/** Finds the number of pixels wide the drawing board **/
	private int findWidth() {
	
		double maxX = Double.MIN_VALUE;
		
		for (GImage obj : sceneryLayer)
			maxX = Math.max(maxX, obj.getX());
		
		for (GImage obj : blockLayer)
			maxX = Math.max(maxX, obj.getX());
			
		return (int) (maxX + Data.TILE_SIZE);
	
	}
	
	/** Finds the number of pixels high the drawing board **/
	private int findHeight() {
	
		double maxY = Double.MIN_VALUE;
		
		for (GImage obj : sceneryLayer)
			maxY = Math.max(maxY, obj.getY() - MENU_HEIGHT);
		
		for (GImage obj : blockLayer) 
			maxY = Math.max(maxY, obj.getY() - MENU_HEIGHT);
		
		return (int) (maxY + Data.TILE_SIZE);
	
	}
	
	/** Removes an object from the drawing board **/
	private void eraseObject() {
		
		if (currentLayer == Layer.BLOCK) {

			for (int i = 0; i < blockLayer.size(); i++)
				if (getTileBoundary(blockLayer.get(i)).contains(mouseX, mouseY)) {
					remove(blockLayer.get(i));
					blockLayer.remove(blockLayer.get(i));
					i--;
				}

			if (vortex != null && getTileBoundary(vortex).contains(mouseX, mouseY)) {
				remove(vortex);
				vortex = null;
			}

			if (player != null && getTileBoundary(player).contains(mouseX, mouseY)){
				remove(player);
				player = null;
			}

		} else if (currentLayer == Layer.SCENERY) {
			for (int i = 0; i < sceneryLayer.size(); i++)
				if (getTileBoundary(sceneryLayer.get(i)).contains(mouseX, mouseY)) {
					remove(sceneryLayer.get(i));
					sceneryLayer.remove(sceneryLayer.get(i));
					i--;
				}
		}
	
	}

}