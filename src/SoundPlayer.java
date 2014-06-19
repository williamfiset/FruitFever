
/**
*
* This class can be used to play soundtracks and sound effects throughout the lifetime of a program
*
*
* @Author William Fiset
* March 13, 2014
*
* 
*
* SoundEffect FileFormat: aac 
* SoundTrack FileFormat: mp3
*
*
**/

import java.io.File;
import javax.sound.sampled.*;

// Requires java 7
// Refer to http://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {
	
	// Change to get class path! 
	private static String makePath(String fileName) { return System.getProperty("user.dir") + "/" + fileName; }

	/** Loads an uncompressed sound file format such as .wav or aif and plays it
		NOTE: Playing sound with this is alright for short clips but definitely not for a soundtrack 
		as they take up lots of space (60+ mb for ≈6min soundstack) **/

	public static void playSound(String fileName) {

		String pathDir = makePath("sound/" + fileName);

		try {

			File musicFile = new File(pathDir);

			AudioInputStream audio = AudioSystem.getAudioInputStream(musicFile);
			Clip musicClip = AudioSystem.getClip();
			musicClip.open(audio);
			musicClip.start();

		} catch(Exception e){
			System.out.println( "\nMusic Folder does not exist or does not contain:\n" + pathDir + fileName + "\n" );
			e.printStackTrace();
		}
		
	}

	/* 
	 * SoundTracks can be and should be a .mp3 file 
	 * @param soundTrack: the name of the soundTrack you wish to play
	 */
	public static void playSoundTrack (String soundTrack){

		String pathDir = makePath(soundTrack);
		System.out.println(pathDir);

		Media media = new Media(pathDir);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();



		// String bip = "bip.mp3";
		// Media hit = new Media(bip);
		// MediaPlayer mediaPlayer = new MediaPlayer(hit);
		// mediaPlayer.play();



	}
}
















