package projectes.dam2.driveconnect;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Manel on 19/11/2015.
 */
public class Lectura extends AsyncTask<DriveFile, Void, Boolean> {
    GoogleApiClient googleApiClient = MainActivity.getGoogleApiClient();
    String text;
    boolean s;

    @Override
    protected Boolean doInBackground(DriveFile... args) {
        DriveFile file = args[0];
//        try {
            DriveApi.DriveContentsResult driveContentsResult = file.open(
                    googleApiClient, DriveFile.MODE_WRITE_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return false;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            this.text = driveContents.getParcelFileDescriptor().getFileDescriptor().toString();
     /*       OutputStream outputStream = driveContents.getOutputStream();
            outputStream.write(this.text.getBytes());
            com.google.android.gms.common.api.Status status =
                    driveContents.commit(googleApiClient, null).await();
            this.s = status.getStatus().isSuccess();
            return status.getStatus().isSuccess();
        } catch (IOException e) {
            Log.e(" ", "IOException while appending to the output stream", e);
        }*/
        return false;
    }

    public String getText(){
        return this.text;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (!result) {
            Log.e(" ", "Error while editing contents");
            return;
        }
        Log.e(" ", "Successfully edited contents");
    }

    public boolean getState(){
        return this.s;
    }


}

