package es.cesar.quitesleep.components.listeners;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;

import es.cesar.quitesleep.ui.dialogs.fragments.LogsFragmentDialog;


/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *
 * This interface is used as CallBack to communicate the {@link SherlockDialogFragment}
 * with the {@link SherlockFragment} when the user press different Yes button
 * from the {@link LogsFragmentDialog}
 */
public interface LogsDialogListener {

	/**
	 * When the user presses the yes button from the clear dialog
	 */
	public void clickYesClearLogs ();
	
	/**
	 * When the user presses the no button from the refresh dialog
	 */
	public void clickYesRefreshLogs ();
	
}
