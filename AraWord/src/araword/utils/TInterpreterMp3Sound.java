package araword.utils;


import javazoom.jl.player.*;
import java.io.*;

/**
 * The audio thread (MP3 format)
 * 
 * @author shaines (used by Antonio Rodríguez)
 * @version 1.0 Aug 22, 2007
 */

public class TInterpreterMp3Sound {

    private Player player = null;
	private InputStream is;
	private TPlayerThread pt;

	/** Creates a new instance of MP3Player */
	public TInterpreterMp3Sound(String filename) {
		try {
			// Create an InputStream to the file
			is = new FileInputStream(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void TPlay() {

		try {
			player = new Player(is);

			pt = new TPlayerThread();
			pt.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public boolean TIsFinished(){
        if(player!=null){
            return player.isComplete();
        }else{
            return true;
        }
    }

	public void TJoin() {
		try {
			pt.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void TStop() {
		try {
            player.close();
            //pt.stop();
		} catch (Exception e) {

		}

	}

	class TPlayerThread extends Thread {

        @Override
		public void run() {
			try {
				player.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}