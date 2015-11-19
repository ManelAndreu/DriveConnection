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
    DriveApi.DriveContentsResult dcr;
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
        final DriveFile df = id.asDriveFile();
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









      /*  fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Drive.DriveApi.newDriveContents(googleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setMimeType("text/plane").build();

                    *//*    FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(String.valueOf(ed.getText()), Context.MODE_APPEND);
                            Log.i("TAG", "openFileOutput");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
*//*

                        FileInputStream fin = null;
                        try {
                            fin = openFileInput("Holaaa");
                            DataInputStream dis = new DataInputStream(fin);
                            byte[] buff = new byte[1024];
                            driveContentsResult.getDriveContents().getOutputStream().write(dis.read(buff));


                            Log.i("TAG", "openFileIntput");
                            fin.close();
                            dis.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // Write the bitmap data from it.
                       *//* OutputStream os =  driveContentsResult.getDriveContents().getOutputStream();
                        ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                        byte[] buff = bitmapStream.
                        try {
                            dos.write();


                            DataOutputStream dos = new DataOutputStream(fos);
                        // Write the bitmap data from it.
                        ByteArrayOutputStream bytearray = new ByteArrayOutputStream();*//*

                     *//*   try {

                            dos.writeBytes("hola")


                            Log.i("TAG", "Writing");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*


                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
                                .setInitialDriveContents(driveContentsResult.getDriveContents())
                                .build(googleApiClient);


                        try {
//                            Toast.makeText(getApplicationContext(), "Entre", Toast.LENGTH_SHORT).show();
                            startIntentSenderForResult(intentSender, MainActivity.REQUEST_CODE_CREATOR, null, 0, 0, 0);


                        } catch (IntentSender.SendIntentException e) {
                            // Handle the exception
                        }
                    }
                });

            }
        });*/
    }

    public void escriu(DriveContents dc) throws IOException {
        DriveContents driveContents = dc;
        OutputStream outputStream = driveContents.getOutputStream();
        outputStream.write("Hello world".getBytes());
        outputStream.flush();
        driveContents.commit(googleApiClient, null);

    }

}
