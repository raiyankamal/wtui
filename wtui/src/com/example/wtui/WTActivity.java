package com.example.wtui;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WTActivity extends Activity {

	TextView txt_status ;
	int q = FSM.NO_STATE ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wt);
		txt_status = (TextView)findViewById(R.id.txt_status);
		FSM.initialize();
		fsm(FSM.LAUNCH);
		writeDebug("onCreate");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wt, menu);

		//invalidateOptionsMenu();

		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean r = super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_stop).setVisible(false);
		menu.findItem(R.id.action_talk).setVisible(false);

		switch(q) {
		case(FSM.IDLE) :
			menu.findItem(R.id.action_talk).setVisible(true);
			break;
		case(FSM.TX) :
			menu.findItem(R.id.action_stop).setVisible(true);
			break;
		case(FSM.RX) :
			break;
		case(FSM.EXIT_STATE) :
			break;
		}
		
		return r ;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case(R.id.action_talk) :
    		fsm(FSM.TX);
    		break;
    	case(R.id.action_stop) :
    		fsm(FSM.TX_END);
    		break;
    	default :
    		return super.onOptionsItemSelected(item);
    	}

    	return true;
    }
	
	void fsm(int a) {
		int pre_q = q ;
		int q_ = FSM.transition(q, a);
		writeDebug("state transition : "+pre_q+"/"+a+"->"+q_);
		if(q_==FSM.ERROR_STATE)//something went wrong, no state change should occur
			return ;
		else
			q = q_ ;
		
		switch(q) {
		case(FSM.IDLE) :
			//TODO stop running threads
			//TODO cleanup garbage
			//TODO update UI
	    	invalidateOptionsMenu();
			txt_status.setText("Idle");
			break;
		case(FSM.RX) :
			//TODO start rx worker threads
			//TODO update UI
	    	invalidateOptionsMenu();
			txt_status.setText("Receiving");
			break;
		case(FSM.TX) :
			//TODO start tx worker threads
			//TODO update UI
	    	invalidateOptionsMenu();
			txt_status.setText("Talking");
			break;
		case(FSM.EXIT_STATE) :
			//TODO stop running threads if any
			//TODO update UI
			txt_status.setText("Exiting");
			//TODO exit app
			break;
		}
	}
	
	public static void writeDebug(String s) {
		Log.d("wtui", s);
	}
}
