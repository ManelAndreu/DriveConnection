package projectes.dam2.driveconnect;

import android.content.Intent;
import android.content.IntentSender;
import android.media.RemoteController;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CREATOR = 1;
    public static final int REQUEST_CODE_OPENER = 3;
    public static final int CONNECTION_REQUEST_CODE = 2;
    private static GoogleApiClient googleApiClient;
    private Button read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        read = (Button) findViewById(R.id.open);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!googleApiClient.isConnected()) {

                    Toast.makeText(getApplicationContext(), "S'esta conectant..., per favor espera.", Toast.LENGTH_SHORT).show();
                    googleApiClient.connect();
                } else {
                    IntentSender intentSender = Drive.DriveApi
                            .newOpenFileActivityBuilder()
                            .setMimeType(new String[]{"text/plain", "text/html"})
                            .build(getGoogleApiClient());
                    try {
                        startIntentSenderForResult(
                                intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.w("", "Unable to send intent", e);
                    }
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tractant d'accedir al servei, si us plau espera...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (!googleApiClient.isConnected()) {

                    Toast.makeText(getApplicationContext(), "S'esta conectant..., per favor espera.", Toast.LENGTH_SHORT).show();
                    googleApiClient.connect();
                } else {
                    Drive.DriveApi.newDriveContents(getGoogleApiClient())
                            .setResultCallback(driveContentsCallback);
                }
            }
        });

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Toast.makeText(getApplicationContext(), "Conectat existosament!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (googleApiClient.isConnected()) {
                            Toast.makeText(getApplicationContext(), "Desconectat.", Toast.LENGTH_SHORT).show();
                            googleApiClient.disconnect();
                        }
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        if (connectionResult.hasResolution()) {
                            try {
                                connectionResult.startResolutionForResult(MainActivity.this, CONNECTION_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                Toast.makeText(getApplicationContext(), "Error de conexió: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), MainActivity.this, CONNECTION_REQUEST_CODE).show();
                        }
                    }
                })
                .build();

        googleApiClient.connect();

    }

    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                            .setMimeType("text/html").build();
                    IntentSender intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.getDriveContents())
                            .build(getGoogleApiClient());
                    try {
                        startIntentSenderForResult(
                                intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.w("", "Unable to send intent", e);
                    }
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREATOR:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.i("TAG", "File created with ID: " + driveId);
                    Intent i = new Intent(getApplicationContext(), Main22Activity.class);
                    i.putExtra("id", driveId.encodeToString());
                    startActivityForResult(i, RESULT_OK);
                }
                break;
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.i("TAG", "File opened with ID: " + driveId);
                    Intent i = new Intent(getApplicationContext(), Main22Activity.class);
                    i.putExtra("id", driveId.encodeToString());
                    startActivityForResult(i, RESULT_OK);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void createFile(View view) {

    }

    public void readFile(View view) {

    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
