package com.easyappointments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.easyappointments.common.Validator;
import com.easyappointments.db.SettingsModel;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.service.provider.ProviderService;
import com.easyappointments.remote.ea.service.ProviderServiceFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private SettingsModel settings = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mUrl;
    private CheckBox mRememberMe;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUrl = (EditText) findViewById(R.id.url);
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mRememberMe = (CheckBox) findViewById(R.id.rememberme);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HideSoftKeyboard();
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        settings = SettingsModel.loadSettings();
        mUrl.setText(settings.url);
        mUsernameView.setText(settings.username);
        mPasswordView.setText(settings.password);
        mRememberMe.setChecked(settings.rememberme);
    }

    private void HideSoftKeyboard(){
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mUrl.setError(null);


        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String url = mUrl.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(Validator.absoluteUrlIsValid(url) == false){
            mUrl.setError(getString(R.string.error_invalid_url));
            focusView = mUrl;
            cancel = true;
        }else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this, username, password, url);
            mAuthTask.execute((Void) null);
        }
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private String errorMessage = getString(R.string.unknown_error);
        private final Context ctx;
        private final String mUsername;
        private final String mPassword;
        private final String mUrl;

        private ProviderModel providerModel;

        UserLoginTask(Context ctx, String username, String password, String url) {
            this.ctx=ctx;
            mUsername = username;
            mPassword = password;
            mUrl = url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ProviderService t = ProviderServiceFactory.getInstance(mUsername,mPassword, mUrl);
            try {
                Map<String, String> filters = new HashMap<>();
                filters.put(Options.fields, ProviderModel.fields.id.toString());
                filters.put(Options.fields, ProviderModel.fields.firstName.toString());
                filters.put(Options.fields, ProviderModel.fields.lastName.toString());
                filters.put(Options.fields, ProviderModel.fields.email.toString());

                Response<List<ProviderModel>> r = t.get(filters).execute();
                List<ProviderModel> pms = r.body();

                if(pms != null && pms.size() == 1) {
                    providerModel = pms.get(0);
                    return true;
                }

                errorMessage = getString(R.string.provider_not_found);
                return false;
            } catch (IOException e) {
                errorMessage = getString(R.string.error_network);
                return false;
            } catch (Exception ex){
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                settings.url = mUrl;

                if(mRememberMe.isChecked()){
                    settings.username = mUsername;
                    settings.password = mPassword;
                    settings.providerId = providerModel.id;
                    settings.firstName = providerModel.firstName;
                    settings.lastName = providerModel.lastName;
                    settings.email = providerModel.email;
                }

                settings.rememberme = mRememberMe.isChecked();

                settings.save();

                Intent homeActivity = new Intent(this.ctx, MainActivity.class);
                startActivity(homeActivity);
            } else {
                Snackbar.make(mLoginFormView, getString(R.string.error_login)+": "+errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

