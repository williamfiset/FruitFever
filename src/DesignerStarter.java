/** 
  * DesignerStarter - Starts the application by putting the Applet into a JFrame.
  * 
  * @Author Micah Stairs
  *
  */

// http://docs.oracle.com/javase/tutorial/uiswing/components/menu.html

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class DesignerStarter implements ActionListener {

	final static int FRAME_BORDER_HEIGHT = 22;
	static JFrame appletFrame; 
	LevelDesigner gameApplet;

	public static void main(String[] args) {
		DesignerStarter designerStarter = new DesignerStarter();
	}

	public DesignerStarter() {
		
		gameApplet = new LevelDesigner();
		appletFrame = new JFrame();
		appletFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("../icons/levelDesignerIcon.ico"));
		//appletFrame.setIconImage(new ImageIcon("../icons/levelDesignerIcon..ico").getImage());

		addMenu();
		
		// Center Frame to the middle of screen on start
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int screenStartX = screenDimension.width/2 - appletFrame.getSize().width/2;
		int screenStartY = screenDimension.height/2 - appletFrame.getSize().height/2;
   		appletFrame.setLocation(screenStartX - FruitFever.SCREEN_WIDTH/2, screenStartY - FruitFever.SCREEN_HEIGHT/2 - FRAME_BORDER_HEIGHT/2);

   		// Set Applet Default Settings
		gameApplet.setSize(FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT + LevelDesigner.MENU_HEIGHT);
		gameApplet.init();

		// Set Frame default settings
		appletFrame.setResizable(false);
		appletFrame.setSize(FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT  + LevelDesigner.MENU_HEIGHT + FRAME_BORDER_HEIGHT);
		appletFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		appletFrame.setTitle("Level Designer");	
		appletFrame.setVisible(true);

		// Add Applet to Frame and start
    	appletFrame.add(gameApplet);
		gameApplet.start();

	}

	private void addMenu() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu, submenu, submenu2;
		JMenuItem menuItem;

		// Build the first menu.
		menu = new JMenu("File");
		menu.getAccessibleContext().setAccessibleDescription("File");
		menuBar.add(menu);

		// A group of JMenuItems

		menuItem = new JMenuItem("New");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("New");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Save");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Save As");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save As");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Open");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Load");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Export");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Export");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Export All");
		menuItem.getAccessibleContext().setAccessibleDescription("Export All");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		
		/** Edit Menu **/

		menu = new JMenu("Edit");
		menu.getAccessibleContext().setAccessibleDescription("Edit");
		menuBar.add(menu);

		menuItem = new JMenuItem("Change Level's Title");
		menuItem.getAccessibleContext().setAccessibleDescription("Change Level's Title");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		/** Mode Menu **/

		menu = new JMenu("Mode");
		menu.getAccessibleContext().setAccessibleDescription("Mode");
		menuBar.add(menu);

		/** ADD  **/

		submenu = new JMenu("Add");
		menu.add(submenu);

		/** Block Layer **/

		submenu2 = new JMenu("Block Layer");

		menuItem = new JMenuItem("Blocks");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		menuItem = new JMenuItem("Fruit");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		menuItem = new JMenuItem("Special");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		submenu.add(submenu2);

		/** Scenery Layer **/

		submenu2 = new JMenu("Scenery Layer");

		menuItem = new JMenuItem("Powerups");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		menuItem = new JMenuItem("Scenery 1");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		menuItem = new JMenuItem("Scenery 2");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu2.add(menuItem);

		submenu.add(submenu2);

		/** SELECT **/

		menuItem = new JMenuItem("Select");
		menuItem.getAccessibleContext().setAccessibleDescription("Select");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		/** MOVE **/

		menuItem = new JMenuItem("Move");
		menuItem.getAccessibleContext().setAccessibleDescription("Move");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		/** **/

		appletFrame.setJMenuBar(menuBar);

	}

	 public void actionPerformed(ActionEvent e) {

	 	switch (e.getActionCommand()) {

	 		case "New":
	 			gameApplet.newDrawingBoard();
	 			break;

	 		case "Save":
	 			gameApplet.save();
	 			break;

	 		case "Save As":
	 			gameApplet.saveAs();
	 			break;

	 		case "Open":
	 			gameApplet.open();
	 			break;

	 		case "Export":
	 			gameApplet.export();
	 			break;

	 		case "Export All":
	 			gameApplet.exportAll();
	 			break;


	 		case "Change Level's Title":
	 			gameApplet.promptLevelTitle();
	 			break;


	 		case "Blocks":
				gameApplet.changeSelectedSet(LevelDesigner.Set.BLOCKS);
				break;
				
			case "Fruit":
				gameApplet.changeSelectedSet(LevelDesigner.Set.FRUIT);
				break;
			
			case "Special":
				gameApplet.changeSelectedSet(LevelDesigner.Set.SPECIAL);
				break;
				
			case "Powerups":
				gameApplet.changeSelectedSet(LevelDesigner.Set.POWERUPS);
				break;
			
			case "Scenery 1":
				gameApplet.changeSelectedSet(LevelDesigner.Set.SCENERY_1);
				break;
				
			case "Scenery 2":
				gameApplet.changeSelectedSet(LevelDesigner.Set.SCENERY_2);
				break;


			case "Select":
				LevelDesigner.currentMode = LevelDesigner.Mode.SELECT;
				break;

			case "Move":
				LevelDesigner.currentMode = LevelDesigner.Mode.MOVE;
				break;

	 	}
	 }

	// private static void addMenu() {

	// 	//Where the GUI is created:
	// 	JMenuBar menuBar;
	// 	JMenu menu, submenu;
	// 	JMenuItem menuItem;
	// 	JRadioButtonMenuItem rbMenuItem;
	// 	JCheckBoxMenuItem cbMenuItem;

	// 	//Create the menu bar.
	// 	menuBar = new JMenuBar();

	// 	//Build the first menu.
	// 	menu = new JMenu("A Menu");
	// 	menu.setMnemonic(KeyEvent.VK_A);
	// 	menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
	// 	menuBar.add(menu);

	// 	//a group of JMenuItems
	// 	menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
	// 	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
	// 	menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
	// 	menu.add(menuItem);

	// 	menuItem = new JMenuItem("Both text and icon",
	// 	                         new ImageIcon("images/middle.gif"));
	// 	menuItem.setMnemonic(KeyEvent.VK_B);
	// 	menu.add(menuItem);

	// 	menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
	// 	menuItem.setMnemonic(KeyEvent.VK_D);
	// 	menu.add(menuItem);

	// 	//a group of radio button menu items
	// 	menu.addSeparator();
	// 	ButtonGroup group = new ButtonGroup();
	// 	rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
	// 	rbMenuItem.setSelected(true);
	// 	rbMenuItem.setMnemonic(KeyEvent.VK_R);
	// 	group.add(rbMenuItem);
	// 	menu.add(rbMenuItem);

	// 	rbMenuItem = new JRadioButtonMenuItem("Another one");
	// 	rbMenuItem.setMnemonic(KeyEvent.VK_O);
	// 	group.add(rbMenuItem);
	// 	menu.add(rbMenuItem);

	// 	//a group of check box menu items
	// 	menu.addSeparator();
	// 	cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
	// 	cbMenuItem.setMnemonic(KeyEvent.VK_C);
	// 	menu.add(cbMenuItem);

	// 	cbMenuItem = new JCheckBoxMenuItem("Another one");
	// 	cbMenuItem.setMnemonic(KeyEvent.VK_H);
	// 	menu.add(cbMenuItem);

	// 	//a submenu
	// 	menu.addSeparator();
	// 	submenu = new JMenu("A submenu");
	// 	submenu.setMnemonic(KeyEvent.VK_S);

	// 	menuItem = new JMenuItem("An item in the submenu");
	// 	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
	// 	submenu.add(menuItem);

	// 	menuItem = new JMenuItem("Another item");
	// 	submenu.add(menuItem);
	// 	menu.add(submenu);

	// 	//Build second menu in the menu bar.
	// 	menu = new JMenu("Another Menu");
	// 	menu.setMnemonic(KeyEvent.VK_N);
	// 	menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
	// 	menuBar.add(menu);

	// 	appletFrame.setJMenuBar(menuBar);

	// }

}