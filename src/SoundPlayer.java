
/**
*
* This class can be used to play soundtracks and sound effects throughout the lifetime of a program
* 
*
* @Author William Fiset
* March 13, 2014
*
* Micah said to try:
* http://java-demos.blogspot.ca/2012/11/java-code-to-play-mp3-file.html 
*
* SoundEffect FileFormat preferably: aac 
* SoundTrack FileFormat preferably: mp3
*
*
**/

// import javax.media.*;
// import javafx.media.*;
// import com.sun.media.*;

// import musicJar.javax.media.*;
// import musicJar.javafx.media.*;
// import musicJar.com.sun.media.*;

// import java.net.*;
import java.util.*;


import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.sound.midi.*;
import javax.swing.JOptionPane;
import java.net.URL;

// import javax.sound.sampled.*;

// Requires java 7 & doesn't seem to work 
// Refer to http://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;


public class SoundPlayer {
	
	// Change to get class path! 
	private static String makePath(String fileName) { return System.getProperty("user.dir") + "/" + fileName; }

	/* Usage:

	Clip soundClip = loadSound("marioSound.wav");
	
	soundClip.start();

	soundClip.loop(intNumber);
	soundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
	
	soundClip.stop();
	soundClip.flush();

	http://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/Clip.html

	*/

	public static Clip loadSound(String fileName){

		String pathDir = System.getProperty("user.dir") + "/"; // current directory
		try {

			File musicFile = new File(pathDir + fileName);

			AudioInputStream audio = AudioSystem.getAudioInputStream(musicFile);
			Clip musicClip = AudioSystem.getClip();
			musicClip.open(audio);
			
			return musicClip;

		} catch(Exception e) {

			System.out.println( "Might wanna check path:\n" + pathDir + fileName + "\n" );
			e.printStackTrace();
			return null;

		}

	}

	public static void main(String[] args) {
		
		// Plays clip once (needs like 1/2 to start)
		Clip marioSoundClip = loadSound("sound/jump.wav");
		marioSoundClip.start();
		
		// Let the program live for ≈ 3 sec		
		Timer_ timer = new Timer_();
		while (timer.getTime() < 3.0){}

	}

/*
	 
	//  SoundTracks can be and should be a .mp3 file 
	//  @param soundTrack: the name of the soundTrack you wish to play
	 
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

*/

/*

	// Loads an uncompressed sound file format such as .wav or aif and plays it
	//	NOTE: Playing sound with this is alright for short clips but definitely not for a soundtrack 
	//	as they take up lots of space (60+ mb for ≈6min soundstack) 

	public static void playSound(String fileName) {

		String pathDir = makePath("sound/" + fileName);

		try {

			File musicFile = new File(pathDir);

			// AudioInputStream audio = AudioSystem.getAudioInputStream(musicFile);
			// Clip musicClip = AudioSystem.getClip();
			// musicClip.open(audio);
			// musicClip.start();

			System.out.println(musicFile.toURI().toURL());

			final plr p = Manager.createRealizedPlayer(musicFile.toURI().toURL());

			// Start the music
			plr.start();

		} catch(Exception e){
			System.out.println( "\nMusic Folder does not exist or does not contain:\n" + pathDir + fileName + "\n" );
			e.printStackTrace();
		}
		
	}

*/

}




