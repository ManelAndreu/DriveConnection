package projectes.dam2.driveconnect;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Main2Activity extends AppCompatActivity {
    private EditText et;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE_CREATOR = 1;
    private static final int CONNECTION_REQUEST_CODE = 2;
    private static final int CREATE_FILE_REQUEST_CODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et = (EditText) findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

      googleApiClient = MainActivity.getGoogleApiClient();

        DriveId driveId = DriveId.decodeFromString(getIntent().getExtras().getString("Did"));
        DriveFile driveFile;
        driveFile = driveId.asDriveFile();
            driveFile.open(googleApiClient, DriveFile.MODE_READ_WRITE,null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult driveContentsResult) {

                if (!driveContentsResult.getStatus().isSuccess()) {
                    Toast.makeText(getApplicationContext(), "Error " + driveContentsResult.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Vaig guai", Toast.LENGTH_SHORT).show();
                DriveContents driveContents = driveContentsResult.getDriveContents();

                FileInputStream fis = (FileInputStream) driveContents.getInputStream();
                DataInputStream dis = new DataInputStream(fis);
                byte[] buff = new byte[1024];
                try {
                    dis.read(buff);
                    et.setText(new String(buff));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Read to the end of the file.
               /* try {
                    fis.read(new byte[fileInputStream.available()]);
                    FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor
                            .getFileDescriptor());

                    Writer writer = new OutputStreamWriter(fileOutputStream);
                    writer.write("hello world");

                    driveContents.commit(googleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            // Handle the response status
                        }
                    });
                } catch (IOException e1) {
                    e1.printStackTrace();
                }*/


            }
        });
    }

}
