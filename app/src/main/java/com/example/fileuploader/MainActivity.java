package com.example.fileuploader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    Button mButton;
    // In the class declaration section:
    private DropboxAPI<AndroidAuthSession> mDBApi;

    final static private String APP_KEY = "vhldnkgk9nnosez";
    final static private String APP_SECRET = "xgzts5zijbiiris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        mButton = findViewById(R.id.button);
        mButton.setText("ABC");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// MyActivity below should be your activity class name
                File file = new File("working-draft.txt");
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                DropboxAPI.Entry response = null;
                try {
                    response = mDBApi.putFile("/magnum-opus.txt", inputStream,
                            file.length(), null, null);
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
                Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
            }
        });
        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);

    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
}
