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
		JMenu menu, submenu;
		JMenuItem menuItem;

		// Build the first menu.
		menu = new JMenu("File");
		menu.getAccessibleContext().setAccessibleDescription("File");
		menuBar.add(menu);

		// A group of JMenuItems

		menuItem = new JMenuItem("New", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("New");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Save As", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save As");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Open", KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Load");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Export", KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Export");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// Build second menu in the menu bar.
		menu = new JMenu("Add");
		menuBar.add(menu);

		/** Block Layer **/

		submenu = new JMenu("Block Layer");

		menuItem = new JMenuItem("Blocks");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Fruit");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Special");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menu.add(submenu);

		/** Scenery Layer **/

		submenu = new JMenu("Scenery Layer");

		menuItem = new JMenuItem("Powerups");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Scenery 1");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Scenery 2");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menu.add(submenu);

		/** **/

		appletFrame.setJMenuBar(menuBar);

	}

	 public void actionPerformed(ActionEvent e) {

		System.err.println(e.getActionCommand());

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