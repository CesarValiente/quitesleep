/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.cesar.quitesleep.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.interfaces.ICallbacks;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Settings;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.operations.MailOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.base.BaseFragmentActivity;
import es.cesar.quitesleep.ui.dialogs.fragments.SmsEmailFragmentDialog;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 */
public class MailSettings extends BaseFragmentActivity implements
        OnClickListener, ICallbacks.SaveData {

    private final String CLASS_NAME = getClass().getName();

    // Widgets
    private EditText userEditText;
    private EditText passwdEditText;
    private EditText subjectEditText;
    private EditText bodyEditText;
    private ToggleButton mailServiceToggleButton;

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.homeToUp(true);

        setContentView(R.layout.mailsettings);
        getSupportActionBar().setTitle(
                getString(R.string.mailsettings_actionbar_title));

        // Set the widgets elements
        userEditText = (EditText) findViewById(R.id.mailsettings_edittext_user);
        passwdEditText = (EditText) findViewById(R.id.mailsettings_edittext_passwd);
        subjectEditText = (EditText) findViewById(R.id.mailsettings_edittext_subject);
        bodyEditText = (EditText) findViewById(R.id.mailsettings_edittext_body);
        mailServiceToggleButton = (ToggleButton) findViewById(R.id.mailsettings_togglebutton_mailservice);

        // Set OnClickListener events
        mailServiceToggleButton.setOnClickListener(this);

        // Put in the widgets the prevoious data saved into ddbb.
        getDefaultValues();
    }

    @Override
    public void onClick(final View view) {

        int viewId = view.getId();

        switch (viewId) {
            case R.id.mailsettings_togglebutton_mailservice:
                if (mailServiceToggleButton.isChecked()) {
                    FragmentTransaction ft = getSupportFragmentManager()
                            .beginTransaction();
                    SherlockDialogFragment dialog = SmsEmailFragmentDialog
                            .newInstance(
                                    ConfigAppValues.DialogType.MAIL_DIALOG,
                                    this);
                    dialog.show(ft, "dialog");
                } else {
                    DialogOperations.checkMailService(this,
                            mailServiceToggleButton.isChecked());
                }
                break;

            default:
                break;
        }
    }

    /**
     * Function that put in the widgets the data saved into ddbb.
     */
    private void getDefaultValues() {

        try {

            ClientDDBB clientDDBB = new ClientDDBB();
            Settings settings = clientDDBB.getSelects().selectSettings();

            if (settings != null) {

                if (settings.getUser() != null
                        && !settings.getUser().equals("")) {
                    userEditText.setText(settings.getUser());
                }
                if (settings.getPasswd() != null
                        && !settings.getPasswd().equals("")) {
                    passwdEditText.setText(settings.getPasswd());
                }
                if (settings.getSubject() != null
                        && !settings.getSubject().equals("")) {
                    subjectEditText.setText(settings.getSubject());
                }
                if (settings.getBody() != null
                        && !settings.getBody().equals("")) {
                    bodyEditText.setText(settings.getBody());
                }

                mailServiceToggleButton.setChecked(settings.isMailService());
            }
            // If Settings object haven't been created previously, here we
            // create.
            else {
                settings = new Settings(false);

                /*
                 * Save the mail settings, only the subject and the body if the
                 * Settings object haven't been created for have something for
                 * default attributes, user and passwd not because is important
                 * for the mail send, and the user soon or later will have set
                 * this.
                 */
                settings.setSubject(subjectEditText.getText().toString());
                settings.setBody(bodyEditText.getText().toString());

                clientDDBB.getInserts().insertSettings(settings);
                clientDDBB.commit();
            }

            clientDDBB.close();

        } catch (Exception e) {
            Log.e(CLASS_NAME, ExceptionUtils.getString(e));
        }
    }

    /**
     * Prepare all data from widgets for call to the function that save data
     * into ddbb in Settings object
     * 
     */
    private void prepareSaveMailOperation() {

        try {

            String user = userEditText.getText().toString();
            String passwd = passwdEditText.getText().toString();
            String subject = subjectEditText.getText().toString();
            String body = bodyEditText.getText().toString();

            // ---- Log traces ---//
            Log.d(CLASS_NAME, "user: " + user);
            Log.d(CLASS_NAME, "passwd: " + passwd);
            Log.d(CLASS_NAME, "subject: " + subject);
            Log.d(CLASS_NAME, "body: " + body);
            // ------------------//

            if (MailOperations.saveMailSettings(user, passwd, subject, body)) {
                // All right, start the service was ok!
                es.cesar.quitesleep.utils.Toast.r(this,
                        this.getString(R.string.mailsettings_toast_save),
                        Toast.LENGTH_SHORT);
            }

        } catch (Exception e) {
            Log.e(CLASS_NAME, ExceptionUtils.getString(e));
        }
    }

    @Override
    public void saveEmailSMSConfig() {
        prepareSaveMailOperation();
    }

}
