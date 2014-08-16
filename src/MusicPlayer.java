

/* 

This class allows you to play compressed audio files.

Example:

MusicPlayer audio = new MusicPlayer("sound/LionKingSoundTrack.mp3");
audio.play();


Dependancies:
- jl1.0.1.jar
- mp3spi1.9.5.jar
- tritonus_share.jar


*/

import javax.sound.sampled.*;

public class MusicPlayer {

	private Clip clip;

	public MusicPlayer(String pathToMp3) {
			
		try {
			
			AudioInputStream ais = AudioSystem.getAudioInputStream( getClass().getResourceAsStream(pathToMp3));
			AudioFormat baseFormat = ais.getFormat();

			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);

			AudioInputStream dais = AudioSystem.getAudioInputStream( decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}

		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void play() {

		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();

	}
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
}











