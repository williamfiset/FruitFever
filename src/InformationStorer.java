
/**
*
* @Author William Fiset
* March 15, 2014
*
* The InformationStorer class creates a .ser (Serialable) file that stores a HashMap 
* object that can contain key-value information. An InformationStorer object will let you easily 
* access the information stored in its file using addItem() and getItem(). The objects you 
* store in the HashMap MUST implement 'Serializable' to be saved and reterived successfully. Also
* note that getItem() returns a generic object meaning that you need to cast which type of object
* you're getting when you're using that method.
*
* ex:
*
*	Store Fruit object in file:
*
*		InformationStorer infoFile = new InformationStorer("fruitDataFile");
*		Fruit apple = new Fruit(); // Fruit class implements Serializable
*		infoFile.addItem("myFavFruit", apple);
*
*
*	Get Fruit object in file:
*
*		Fruit aFruit = (Fruit) infoFile.getItem("myFavFruit");
*
*
* NOTE: As deserialze has @SuppressWarnings("unchecked") attached to it, the class/method
* containing the InformationStorer will need to also have @SuppressWarnings("unchecked")
*
**/

import java.io.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class InformationStorer {

    private String fileName;
    private Object defaultValue;

    /** This Hashmap gets serialized within the file and contains all the object you wish to save **/
    private HashMap<String, Object> instanceDictionary = new HashMap<String, Object>();
    
    // Needed for internal usage
    private InformationStorer() {}

    public InformationStorer(String fileName) {
        this.fileName = fileName;
    }
    
    public InformationStorer(String fileName, String key, Object defaultValue) {
        this.fileName = fileName;
        this.defaultValue = defaultValue;
        addItem(key, defaultValue);
    }

    /** Adds a new element to instanceDictionary & to fileName.ser **/
    public void addItem(String key, Object value) {
        instanceDictionary.put(key, value);
        serialize();
    }

    /** Gets an element from the instanceDictionary **/
    public Object getItem(String key) {
        
        deserialize();

        if (instanceDictionary.containsKey(key)) 
             return instanceDictionary.get(key);

        System.out.println("Couldn't find Key in HashMap: " + key);
		
        return defaultValue;
    }
	
	 /** Checks to see if an item exists **/
    public boolean checkItemExists(String key) {
        
         HashMap<String, Object> fileHashMap = new HashMap<String, Object>();

        try
        {
            FileInputStream fileIn = new FileInputStream(fileName + ".ser");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);
            
            fileHashMap = (HashMap<String, Object>) inStream.readObject();
            inStream.close();
            fileIn.close();

            for (String str : fileHashMap.keySet())
                instanceDictionary.put(str, fileHashMap.get(str));

        }
		catch (IOException i) { }
		catch (ClassNotFoundException c) { }

        if (instanceDictionary.containsKey(key)) 
             return true;
		
        return false;
    }

    /** Checks if file exists in Directory **/
    public static boolean fileExists(String file) {
        
        // Generic Object used to get class 
        InformationStorer obj = new InformationStorer();

        return new File( obj.getClass().getResource(file).getFile() ).exists();
    }

    /** Stores a byte HashMap Object into a file called: fileName.ser */
    protected void serialize() {

        try {

        FileOutputStream fileOut = new FileOutputStream(fileName + ".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
         
         // Store HashMap object in file
        out.writeObject(instanceDictionary);
        out.close();
        fileOut.close();

        } catch(IOException i) {
            i.printStackTrace();
        }

    }

    /** Loads the information found in the file into instanceDictionary HashMap **/
    protected void deserialize() {

        HashMap<String, Object> fileHashMap = new HashMap<String, Object>();

        try
        {
            FileInputStream fileIn = new FileInputStream(fileName + ".ser");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);
            
            fileHashMap = (HashMap) inStream.readObject();
            inStream.close();
            fileIn.close();

            for (String key : fileHashMap.keySet())
                instanceDictionary.put(key, fileHashMap.get(key));

        }
		catch(IOException i) {
            System.out.println("\nError reading File. Either content was changed or wrong file name. \n");
            i.printStackTrace();
		}
		catch(ClassNotFoundException c) {
            System.out.println("\nClass not found\n");
            c.printStackTrace();
        }


    }
}