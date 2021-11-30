package com.anon.anon.ui.login;

import android.os.AsyncTask;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anon.anon.data.LoginRepository;
import com.anon.anon.data.Result;
import com.anon.anon.data.model.LoggedInUser;
import com.anon.anon.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }



    public void login(String username, String password) {
        new loginAsyncTask(loginRepository, loginResult).execute(username, password);
    }

    private static class loginAsyncTask extends AsyncTask<String, Void, Void> {

        private LoginRepository loginRepository;
        private MutableLiveData<LoginResult> loginResult;
        Result<LoggedInUser> result;

        loginAsyncTask(LoginRepository loginRepository, MutableLiveData<LoginResult> loginResult) {
            this.loginRepository = loginRepository;
            this.loginResult = loginResult;
        }

        @Override
        protected Void doInBackground(final String... params) {
            String username = params[0];
            String password = params[1];

            result = loginRepository.login(username, password);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}