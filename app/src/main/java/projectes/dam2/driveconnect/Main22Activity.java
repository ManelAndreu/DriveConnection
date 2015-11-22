package projectes.dam2.driveconnect;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main22Activity extends AppCompatActivity {
    GoogleApiClient googleApiClient;
    private boolean isReaded = false;
    static String text;
     EditText ed;
    private DriveId id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        ed = (EditText) findViewById(R.id.editText2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        googleApiClient = MainActivity.getGoogleApiClient();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        id = DriveId.decodeFromString(getIntent().getExtras().getString("id"));
        text = "";
        final DriveFile df = id.asDriveFile();
        final Lectura llig = new Lectura();

        llig.execute(df);

        while(!isReaded){
            if(text == ""){

            }else{
                ed.setText(text);
                isReaded = true;
            }
        }
        final Escritura escriu = new Escritura();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Actualitzant...", Toast.LENGTH_SHORT).show();
                escriu.setText(ed.getText().toString());

                escriu.execute(df);


                Toast.makeText(getApplicationContext(), "Actualitzat!", Toast.LENGTH_SHORT).show();
                finishActivity(RESULT_OK);


            }
        });

        ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {

                            // display an error saying file can't be opened
                            return;
                        }
                        // DriveContents object contains pointers
                        // to the actual byte stream
                        DriveContents contents = result.getDriveContents();
                    }
                };

        PendingResult<DriveApi.DriveContentsResult> opened = df.open(googleApiClient, DriveFile.MODE_READ_WRITE, null);
        opened.setResultCallback(contentsOpenedCallback);

    }

    public static void setText(String s){
        text = s;
    }

}
