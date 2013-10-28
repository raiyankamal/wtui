/**
 * 
 */
package com.example.wtui;

import java.util.HashMap;
import android.util.Pair;

/**
 * @author Raiyan Kamal ( raiyan.kamal@gmail.com )
 * @since 2013 10 28
 *
 * Holds the FSM for generic walkie talkie app.
 * 
 * The state transformation of the application is implemented with a finite state machine.
 * The FSM is implemented in this class. The transition table is hardcoded by storing
 * <state, signal> pairs hashed against state values in a hashtable.
 * <p>
 * A singleton instance of the class should be instantiated at the beginning of an activity 
 * to ensure access to the table.
 *
 */
public class FSM {

	/**
	 * Indicates error. This state is reached when irrecoverable error occurs.
	 */
	public static final int ERROR_STATE 	=-1 ;
	public static final int EXIT_STATE 		=-2 ;
	
	public static final int NO_STATE 		= 0 ;
	public static final int IDLE 			= 1 ;
	public static final int RECEIVING 		= 2 ;
	public static final int TRANSMITTING 	= 3 ;
	
	//symbols :
	public static final int LAUNCH 	= 1 ;
	public static final int RX 		= 2 ;
	public static final int TX 		= 3 ;
	public static final int TX_END 	= 4 ;
	public static final int RX_END 	= 5 ;
	public static final int EXIT 	= 6 ;	

	/**
	 * The encoded transition table
	 */
	private static HashMap<Pair<Integer, Integer>, Integer> Q ;

	/**
	 * The constructor of the FSM
	 */
	private FSM() {

		Q = new HashMap<Pair<Integer, Integer>, Integer>() ;
		
		Q.put(new Pair<Integer, Integer>(NO_STATE,LAUNCH), IDLE);
		
		Q.put(new Pair<Integer, Integer>(IDLE,RX), RECEIVING);
		Q.put(new Pair<Integer, Integer>(IDLE,TX), TRANSMITTING);
		Q.put(new Pair<Integer, Integer>(IDLE,EXIT), EXIT_STATE);
		
		Q.put(new Pair<Integer, Integer>(RECEIVING,RX_END), IDLE);

		Q.put(new Pair<Integer, Integer>(TRANSMITTING,TX_END), IDLE);
	}

	/**
	 * The state transition is implemented in this function
	 * @param q Previous state
	 * @param a Signal
	 * @return Next state or ERROR_STATE if the transition cannot be carried out
	 */
	static public int transition(int q, int a) {
		int state = ERROR_STATE ;
		try {
			state = Q.get(new Pair<Integer, Integer>(q,a));
		} catch(Exception e) {
			WTActivity.writeDebug("Please initialize FSM before using");
			System.out.println(e);
		}
		return state ;
	}

	private static FSM fsm;
	/**
	 * Initializes a singleton instance of the class
	 */
	static public void initialize() {
		fsm = new FSM();
	}
}
