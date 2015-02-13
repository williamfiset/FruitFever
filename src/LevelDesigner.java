import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.nio.file.*;

public class LevelDesigner extends GraphicsProgram implements MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/** TO DO:

		Necessary:
		-Change menu when it's not in add mode
		-fixLayering() should place selected blocks on top of other blocks
		-Moving selected blocks onto menu deletes them/makes them move twice as fast? This is a weird bug. Maybe they are overlapped
		Extras:
		-Add icon to program (William made a ghetto icon for you. You best respect him :P)
		-Don't save during export if there are no unsaved changes (currently saves regardless)
		-Add Help to the menu
		-Unselect all button in Mode menu
		-Add automatic orientation detection for spikes and torches

		Unneeded Features that could take a fair bit of work to add:
		-Write script replace one image for another throughout each levelDesigner.ser file.
		-Undo/Redo
		-Add different cursors (research this)
		-Add mini-map
		-Add animations to drawing board and even to the menu?
		-Add Box-Select Mode (Clicking and dragging will select a box of objects)
		-Copy/Paste

		Refactoring:
		-Add a method which gets called by both the shortcut keys and the button presses, so that we can always be certain that they execute the same code
		-Add trimExtension method to get rid of .ser from the end of a String
		-Re-organize methods so they are in a logical order

		Possible Issues:
		-If the user clicks while on one mode, changes the mode, and then releases the mouse, weird things could possible happen, maybe? Just a thought...
		
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
	private static ArrayList<GImage> selectedObjects = new ArrayList<>();
	private static ArrayList<GRect> selectedObjectsHighlighting = new ArrayList<>();

	private static ArrayList<GImage> fallingBlocks = new ArrayList<>();
	private static ArrayList<GRect> fallingBlocksHighlighting = new ArrayList<>();

	private static HashMap<GImage, String> hints = new HashMap<>();

	private double startX, startY;

	private static GRect selectedMenuBox = new GRect(0, 0, 0, 0);
	private double selectedMenuX, selectedMenuY;
	private static GObject selected = null;
	

/** MOUSE/KEYBOARD **/

	private static enum MouseButtonPressed { LEFT, RIGHT, NONE }
	private static MouseButtonPressed mouseButtonPressed = MouseButtonPressed.NONE;

	private static double mouseX, mouseY, extraX, extraY;

	private boolean controlPressed = false, shiftPressed = false, altPressed = false;


/** LEVEL VARIABLES **/
	
	public static int level;
	private static String name = null;

	GImage vortex = null, player = null;

/** ENUMS **/	

	private static enum Layer {

		BLOCK(blockLayer),
		SCENERY(sceneryLayer);

		public ArrayList<GImage> array;

		Layer(ArrayList<GImage> array) {
			this.array = array;
		}

	}
	
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

	public static enum Mode { ADD, SELECT, MOVE, FALLING_BLOCKS }
	public static Mode currentMode = Mode.ADD;
	

	/** Contains the main game loop **/
	@Override public void run() {

		/** Set file chooser to proper directory **/
		fileChooser.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toString() + "/levels/designedLevels"));
		
	
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

		/** Set up keyboard and mouse **/
		addMouseListeners();
		addKeyListeners();

		while (true) {
			if (!DesignerStarter.appletFrame.isFocused()) {
				controlPressed = false;
				shiftPressed = false;
				altPressed = false;
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

		for (GImage obj : blockLayer) {

			if (obj.equals(Data.playerStill[0]))
				player = null;

			if (obj.equals(Data.vortexAnimation[0]))
				vortex = null;

			remove(obj);
		}

		blockLayer.clear();

		for (GImage obj : sceneryLayer)
			remove(obj);

		sceneryLayer.clear();

		unselectAll();

		fallingBlocks.clear();

		for (GRect obj : fallingBlocksHighlighting)
			remove(obj);

		hints.clear();

		name = null;

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
			} else {
				if (currentMode == Mode.ADD)
					addObject();
				else if (currentMode == Mode.SELECT)
					selectObject();
				else if (currentMode == Mode.MOVE) {
					startX = mouseX;
					startY = mouseY;
				}
				else if (currentMode == Mode.FALLING_BLOCKS)
					setFalling();

			}
		/** Erase Object **/
		} else if (mouse.getButton() == 3) {
			
			mouseButtonPressed = MouseButtonPressed.RIGHT;
			
			if (currentMode == Mode.ADD)
				eraseObject();
			else if (currentMode == Mode.SELECT)
				unselectObject();
			else if (currentMode == Mode.MOVE)
				unselectAll();
			else if (currentMode == Mode.FALLING_BLOCKS)
				setNotFalling();

		}

	}

	public void unselectAll() {

		selectedObjects.clear();

		for (GRect highlight : selectedObjectsHighlighting)
			remove(highlight);

		selectedObjectsHighlighting.clear();

	}

	private void unselectObject() {

		for (GImage obj : currentLayer.array)
			if (obj.contains(mouseX, mouseY) && selectedObjects.contains(obj)) {

				for (GRect highlight : selectedObjectsHighlighting)
					if (highlight.contains(mouseX, mouseY)) {
						remove(highlight);
						selectedObjectsHighlighting.remove(highlight);
						break;
					}

				selectedObjects.remove(obj);

				break;
			}

	}

	private void selectObject() {

		for (GImage obj : currentLayer.array)
			if (obj.contains(mouseX, mouseY) && !selectedObjects.contains(obj)) {

				selectedObjects.add(obj);
				GRect highlight = new GRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
				highlight.setFilled(true);
				highlight.setColor(new Color(1f, 1f, 0f, 0.5f));
				selectedObjectsHighlighting.add(highlight);
				add(highlight);

				break;
			}

	}

	public void setFallingHighlightingVisibility(boolean visible) {

		for (GRect obj : fallingBlocksHighlighting)
			obj.setVisible(visible);

	}

	private void setNotFalling() {

		for (GImage obj : blockLayer)
			if (obj.contains(mouseX, mouseY) && fallingBlocks.contains(obj)) {

				for (GRect highlight : fallingBlocksHighlighting)
					if (highlight.contains(mouseX, mouseY)) {
						remove(highlight);
						fallingBlocksHighlighting.remove(highlight);
						break;
					}

				fallingBlocks.remove(obj);

				break;
			}

	}


	private void setFalling() {

		for (GImage obj : blockLayer)
			if (obj.contains(mouseX, mouseY) && !fallingBlocks.contains(obj)) {

				GRect highlight = new GRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
				highlight.setFilled(true);
				highlight.setColor(new Color(1f, 0f, 0f, 0.5f));
				fallingBlocksHighlighting.add(highlight);
				add(highlight);

				fallingBlocks.add(obj);

				break;
			}

	}

	private void addObject() {

		if (selected != null) {
			if (!controlPressed) {
				selected = copyImage(selected);
				updatePosition();
				add(selected);
				finalizeObject();

				/** Hint functionality **/
				if (ImageTransformer.isIdentical((GImage) selected, Data.hintSign[1]))
					hints.put((GImage) selected, JOptionPane.showInputDialog(DesignerStarter.appletFrame, "Enter the hint's text", "Hint", JOptionPane.PLAIN_MESSAGE));

			}
		}
	}

	private void moveObjects() {
		extraX += mouseX - startX;
		extraY += mouseY - startY;

		translateImages(selectedObjects, roundPos(extraX), roundPos(extraY));
		translateMutualRects(selectedObjectsHighlighting, fallingBlocksHighlighting, roundPos(extraX), roundPos(extraY));
		translateRects(selectedObjectsHighlighting, roundPos(extraX), roundPos(extraY));
		
		startX = mouseX;
		startY = mouseY;
		extraX %= Data.TILE_SIZE;
		extraY %= Data.TILE_SIZE;
	}

	@Override public void mouseDragged(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

		if (mouse.getY() >= MENU_HEIGHT) {
			if (mouseButtonPressed == MouseButtonPressed.LEFT) {
				if (currentMode == Mode.ADD)
					addObject();
				else if (currentMode == Mode.SELECT)
					selectObject();
				else if (currentMode == Mode.MOVE)
					moveObjects();
				else if (currentMode == Mode.FALLING_BLOCKS)
					setFalling();
			} else if (mouseButtonPressed == MouseButtonPressed.RIGHT) {
				if (currentMode == Mode.ADD)
					eraseObject();
				else if (currentMode == Mode.SELECT)
					unselectObject();
				else if (currentMode == Mode.FALLING_BLOCKS)
					setNotFalling();
			}
		}

	}

	@Override public void mouseReleased(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

		if (currentMode == Mode.MOVE) {
			finalizeMovedObjects();

			if (mouseButtonPressed == MouseButtonPressed.RIGHT) {
				extraX = 0;
				extraY = 0;
			}

		}

		mouseButtonPressed = MouseButtonPressed.NONE;

	}

	@Override public void mouseMoved(MouseEvent mouse) {

		mouseX = mouse.getX();
		mouseY = mouse.getY();

	}
	
	/** Adds an object to the drawing board, removing any underlying objects **/
	private void finalizeObject() {
		
		removeObjectsFromLayer(currentLayer.array);
		currentLayer.array.add((GImage) selected);
		
		fixLayering();
	}

	private void finalizeMovedObjects() {

		for (GImage obj : selectedObjects) {
			removeObjectsFromLayer(currentLayer.array, obj.getX() - findXOffset(obj), obj.getY() - findYOffset(obj));
			currentLayer.array.add(obj);
			add(obj);
		}

		fixLayering();

	}

	private void removeObjectsFromLayer(ArrayList<GImage> arr, double x, double y) {

		for (GImage obj : arr)
			if (getTileBoundary(obj).contains(x, y)) {

				if (obj.equals(Data.playerStill[0]))
					player = null;

				if(obj.equals(Data.vortexAnimation[0]))
					vortex = null;

				remove(obj);
				arr.remove(obj);
				break;
			}

	}

	private void removeObjectsFromLayer(ArrayList<GImage> arr) {
		removeObjectsFromLayer(arr, mouseX, mouseY);
	}

	private void switchObjects(GObject oldImg, GObject newImg) {

		if (newImg == null)
				return;

		/** Replace all old images with the new ones **/
		for (int i = 0 ; i < currentLayer.array.size(); i++)
			if (ImageTransformer.isIdentical(currentLayer.array.get(i), (GImage) oldImg))
				currentLayer.array.get(i).setImage(copyImage(newImg).getImage());

	}
	
	/** Sets the position of the selected object, snapping it to the grid **/
	private void updatePosition() {

		double x = roundPos(mouseX) + findXOffset((GImage) selected);
		double y = Math.max(roundPos(mouseY + findYOffset((GImage) selected) - MENU_HEIGHT) + MENU_HEIGHT, MENU_HEIGHT);

		selected.setLocation(x, y);

	}

	private GRect getTileBoundary(GImage img) {

		double x = img.getX() - findXOffset(img);
		double y = Math.max(img.getY() - findYOffset(img), MENU_HEIGHT);

		return new GRect(x, y, Data.TILE_SIZE, Data.TILE_SIZE);

	}

	private GRect getTileBoundary(GRect obj) {

		double x = obj.getX();
		double y = Math.max(obj.getY(), MENU_HEIGHT);

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
		if (obj != null) 
			return new GImage(((GImage) obj).getImage());
		else return null;
	}
	
	/** Take user input **/
	@Override public void keyPressed(KeyEvent key) {

		switch (key.getKeyCode()) {

			case KeyEvent.VK_0:
				if (altPressed)
					changeSelectedSet(Set.BLOCKS);
				break;

			case KeyEvent.VK_1:
				if (altPressed)
					changeSelectedSet(Set.FRUIT);
				break;
				
			case KeyEvent.VK_2:
				if (altPressed)
					changeSelectedSet(Set.SPECIAL);
				break;

			case KeyEvent.VK_3:
				if (altPressed)
					changeSelectedSet(Set.POWERUPS);
				break;

			case KeyEvent.VK_4:
				if (altPressed)
					changeSelectedSet(Set.SCENERY_1);
				break;

			case KeyEvent.VK_5:
				if (altPressed)
					changeSelectedSet(Set.SCENERY_2);
				break;
				
			case KeyEvent.VK_LEFT:
				moveAllArraysOfObjects(Data.TILE_SIZE, 0);
				break;
				
			case KeyEvent.VK_RIGHT:
				moveAllArraysOfObjects(-Data.TILE_SIZE, 0);
				break;
				
			case KeyEvent.VK_UP:
				moveAllArraysOfObjects(0, Data.TILE_SIZE);
				break;
				
			case KeyEvent.VK_DOWN:
				moveAllArraysOfObjects(0, -Data.TILE_SIZE);
				break;

			case KeyEvent.VK_E:
				if (controlPressed) {
					if (shiftPressed)
						exportAll();
					else
						export();
				}
				break;

			case KeyEvent.VK_F:
				if (altPressed) {
					currentMode = Mode.FALLING_BLOCKS;
					unselectAll();
					setFallingHighlightingVisibility(true);
				}
				break;

			case KeyEvent.VK_M:
				if (altPressed) {
					currentMode = Mode.MOVE;
					setFallingHighlightingVisibility(false);
				}
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

			case KeyEvent.VK_Q:
				if (controlPressed) {
					if (shiftPressed)
						save(false);
					System.exit(0);
				}
				break;

			case KeyEvent.VK_S:
				if (controlPressed)
					save(shiftPressed);
				else if (altPressed) {
					currentMode = Mode.SELECT;
					setFallingHighlightingVisibility(false);
				}
				break;

			case KeyEvent.VK_T:
				if (controlPressed)
					DesignerStarter.testLevel();
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

			case KeyEvent.VK_ALT:
				altPressed = true;
				break;
				
		}
		
	}

	private void translateImages(ArrayList<GImage> arr, double horizontalMovement, double verticalMovement) {
		for (GImage obj : arr)
			obj.move(horizontalMovement, verticalMovement);
	}
	private void translateRects(ArrayList<GRect> arr, double horizontalMovement, double verticalMovement) {
		for (GRect obj : arr)
			obj.move(horizontalMovement, verticalMovement);
	}

	/** Translates all overlapping GRects in arr2 that are in arr1 **/
	private void translateMutualRects(ArrayList<GRect> arr1, ArrayList<GRect> arr2, double horizontalMovement, double verticalMovement) {

		for (GRect obj1 : arr1)
			for (GRect obj2 : arr2)
				if (obj1.getX() == obj2.getX() && obj1.getY() == obj2.getY())
					obj2.move(horizontalMovement, verticalMovement);

	}

	private void moveAllArraysOfObjects(double horizontalMovement, double verticalMovement) {
		translateImages(blockLayer, horizontalMovement, verticalMovement);
		translateImages(sceneryLayer, horizontalMovement, verticalMovement);
		translateRects(selectedObjectsHighlighting, horizontalMovement, verticalMovement);
		translateRects(fallingBlocksHighlighting, horizontalMovement, verticalMovement);
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

			case KeyEvent.VK_ALT:
				altPressed = false;
				break;
				
		}
		
	}
	
	/** Changes the menu set on the screen **/
	public void changeSelectedSet(Set newSet) {
		currentMode = Mode.ADD;
		unselectAll();

		setFallingHighlightingVisibility(false);

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
				break;
				
			case SCENERY_1:
				for (int i = 0; i < 25; i++)
					remove(Data.sceneryImages[i]);
				break;
					
			case SCENERY_2:
				for (int i = 25; i < Data.sceneryImages.length; i++)
					remove(Data.sceneryImages[i]);
				break;
		}
	
	}

	/** Re-exports all levels in the designedLevels folder **/
	public void exportAll() {

		File folder = new File(Paths.get("").toAbsolutePath().toString() + "/levels/designedLevels");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile()) {
				newDrawingBoard();
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

		save(false);

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

		ArrayList<String> hintsStrings  = new ArrayList<>();
		
		/** Block Layer **/
		
		for (int y = 0; y < tilesHigh; y++) {
			nextChar:
			for (int x = 0; x < tilesWide; x++) {
				
				for (GImage obj : blockLayer)
					if (obj.getX() - findXOffset(obj) == x*Data.TILE_SIZE && obj.getY() - findYOffset(obj) == y*Data.TILE_SIZE + MENU_HEIGHT) {
						
						/** Fruit **/
						for (int i = 0; i < Data.fruits.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.fruits[i][0])) {
								writer.print(i);
								continue nextChar;
							}
						
						/** Blocks **/
						for (int i = 0; i < Data.blockImages.length; i++)
							if (ImageTransformer.isIdentical(obj, Data.blockImages[i][0])) {
								
								boolean fallingBlock = false;

								if (fallingBlocks.contains(obj))
									writer.print((char) (i + 65)); // ASCII Chart (Upper-case letters start at 65)
								else
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

							hintsStrings.add(hints.get(obj));

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
						
						System.out.println("Unrecognizable object. Could not assign a character to export it as. The image, however, is still in the designed level file.");
					
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
					if (obj.getX() - findXOffset(obj) == x*Data.TILE_SIZE && obj.getY() - findYOffset(obj) == y*Data.TILE_SIZE + MENU_HEIGHT) {
						
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

		writer.println("+");

		/** Hint Layer **/
		for (String str : hintsStrings)
			writer.println(str);

		System.out.println("Hint Layer Generated.");

		writer.close();
		
		System.out.println("Finished Exporting.");
	
	}

	public void save(boolean saveAs) {

		/** Create file and then use it **/

		if (infoFile == null || saveAs) {

			fileChooser.setDialogTitle("Enter this level's number");

			int result = fileChooser.showSaveDialog(this);
         	if (result != JFileChooser.APPROVE_OPTION) {
         		if (infoFile == null)
         			System.out.println("No file was chosen. Save aborted.");
         		else
         			System.out.println("No new save file was chosen.");
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
			
		infoFile.addItem("blockLayer", makeSerializableImageArray(blockLayer));
		infoFile.addItem("sceneryLayer", makeSerializableImageArray(sceneryLayer));

		infoFile.addItem("name", name);

		infoFile.addItem("fallingBlocks", makeSerializableImageArray(fallingBlocks));

		ArrayList<SerializableHint> serializableHints = new ArrayList<>();
		
		for (Map.Entry<GImage, String> entry: hints.entrySet())
			if (blockLayer.contains(entry.getKey()))
				serializableHints.add(new SerializableHint(entry.getKey().getX(), entry.getKey().getY(), entry.getValue()));
			else
				hints.remove(entry.getKey());

		infoFile.addItem("hints", serializableHints);

		System.out.println("Finished Saving.");

	}

	private ArrayList<SerializableImage> makeSerializableImageArray(ArrayList<GImage> arr1) {

		ArrayList<SerializableImage> arr2 = new ArrayList<>();

		for (GImage img : arr1)
			arr2.add(new SerializableImage((int) img.getX(), (int) img.getY(), img.getPixelArray()));

		return arr2;
	}

	// private ArrayList<SerializableRect> makeSerializableRectArray(ArrayList<GRect> arr1) {
		
	// 	ArrayList<SerializableRect> arr2 = new ArrayList<>();

	// 	for (GRect rect : arr1)
	// 		arr2.add(new SerializableRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight(), rect.getFillColor()));

	// 	return arr2;
	// }

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

		unselectAll();

		if (levelNumber.lastIndexOf(".ser") != -1)
     		level = Integer.valueOf(levelNumber.substring(0, levelNumber.lastIndexOf('.')));
     	else
     		level = Integer.valueOf(levelNumber);

 		infoFile = new InformationStorer("levels/designedLevels/" + level);
 		DesignerStarter.appletFrame.setTitle("Level Designer (Level " + level + ")");	

		blockLayer = loadFromFile(blockLayer, "blockLayer", true);
		sceneryLayer = loadFromFile(sceneryLayer, "sceneryLayer", true);
		name = (String) infoFile.getItem("name");
		
		fallingBlocks = loadFromFile(fallingBlocks, "fallingBlocks", false);

		/** This sets the objects in the fallingBlocks array as the same instance as the ones in the blockLayer **/
		for (int i = 0; i < fallingBlocks.size(); i++) {

			GImage obj1 = fallingBlocks.get(i);

			for (GImage obj2 : blockLayer)
				if (obj1.getX() == obj2.getX() && obj1.getY() == obj2.getY()) {
					fallingBlocks.set(i, obj2);
					break;
				}

		}

		// Generate list of falling block highlights and add them to the screen
		for (GImage obj : fallingBlocks) {
			System.out.println(obj.getX() + obj.getY());
			GRect highlight = new GRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
			highlight.setFilled(true);
			highlight.setColor(new Color(1f, 0f, 0f, 0.5f));
			fallingBlocksHighlighting.add(highlight);
			add(highlight);
		}
		setFallingHighlightingVisibility(false);


		hints.clear();
		@SuppressWarnings("unchecked") ArrayList<SerializableHint> serializedHints = (ArrayList<SerializableHint>) infoFile.getItem("hints");

		outerLoop:
		for (SerializableHint hint : serializedHints)
			for (GImage obj : blockLayer)
				if (obj.getX() == hint.getX() && obj.getY() == hint.getY()) {
					hints.put(obj, hint.getHint());
					continue outerLoop;
				}

		fixLayering();
	}

	private ArrayList<GImage> loadFromFile(ArrayList<GImage> arr, String str, boolean addToScreen) {

		/** Add new objects **/

		if (infoFile.checkItemExists(str)) {

			@SuppressWarnings("unchecked") ArrayList<SerializableImage> tempArr = (ArrayList<SerializableImage>) infoFile.getItem(str);
			try {
				for (SerializableImage obj : tempArr) {
					GImage img = new GImage(obj.pixelArray);

					arr.add(img);

					img.setLocation(obj.x, obj.y);

					if (addToScreen) {
						if (img.equals(Data.playerStill[0]))
							player = img;

						if (img.equals(Data.vortexAnimation[0]))
							vortex = img;

						add(img);
					}
				}
			} catch (ClassCastException e) {}
			
		}
		
		return arr;
	
	}
	
	/** Properly layers the images on the sceen **/
	private void fixLayering() {
	
		menuBackground.sendToBack();
	
		for (GImage obj : sceneryLayer)
			obj.sendToBack();
			
		for (GImage obj : blockLayer)
			obj.sendToBack();

		for (GRect obj : selectedObjectsHighlighting)
			obj.sendToFront();
			
		for (GRect obj : fallingBlocksHighlighting)
			obj.sendToFront();

		selectedMenuBox.sendToFront();
		
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
			minY = Math.min(minY, obj.getY() - MENU_HEIGHT - findYOffset(obj));
		}
		
		/** Move Objects **/
		moveAllArraysOfObjects(-minX, -minY);
		
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

					for (GRect rect : fallingBlocksHighlighting)
						if (getTileBoundary(rect).contains(mouseX, mouseY)) {
							fallingBlocksHighlighting.remove(rect);
							remove(rect);
							break;
						}

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