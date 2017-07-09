package org.enventureenterprises.enventure.ui.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.CurrentUserType;
import org.enventureenterprises.enventure.data.model.User;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.HomeActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @Inject
    CurrentUserType currentUser;


    private User user;
    public static int APP_REQUEST_CODE = 99;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.facebook_login_button)
    public void phoneLogin(final View view) {
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    public void onSuccess() {
        setResult(Activity.RESULT_OK);
        final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
        //finish();
    }


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                currentUser.login(loginResult.getAccessToken().getToken());
                onSuccess();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

}
