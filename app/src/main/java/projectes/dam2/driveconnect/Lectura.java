package projectes.dam2.driveconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Manel on 19/11/2015.
 */
public class Lectura extends AsyncTask<DriveFile, Void, Boolean> {
    GoogleApiClient googleApiClient = MainActivity.getGoogleApiClient();
    EditText editText;
    String text;
    boolean s;

    @Override
    protected Boolean doInBackground(DriveFile... args) {
        DriveFile file = args[0];

//        try {
        DriveApi.DriveContentsResult driveContentsResult = file.open(
                googleApiClient, DriveFile.MODE_READ_ONLY, null).await();
        if (!driveContentsResult.getStatus().isSuccess()) {
            return false;
        }

        DriveContents driveContents = driveContentsResult.getDriveContents();
        try {
            ParcelFileDescriptor pfd = driveContents.getParcelFileDescriptor();
            FileInputStream fileInputStream = new FileInputStream(pfd.getFileDescriptor());
            FileReader fr = new FileReader(pfd.getFileDescriptor());
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            while(br.ready()){
                String lectura = br.readLine();
                builder.append(lectura);
                builder.append(System.getProperty("line.separator"));
            }
            br.close();
            text = builder.toString();
            Main22Activity.text = text;
            Log.i("TAG", text);

            /*StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fileInputStream.read()) != -1){
                builder.append((char)ch);
            }

            text = builder.toString();
            Main22Activity.text = text;
            Log.i("TAG", text);*/

        } catch (IOException e) {
            e.printStackTrace();
        }





        return false;
    }

    public String getText() {
        return text;
    }



    @Override
    protected void onPostExecute(Boolean result) {
        if (!result) {
            Log.e(" ", "Error while editing contents");
            return;
        }
        Log.e(" ", "Estic aci!! "+text);
    }

    public boolean getState() {
        return this.s;
    }


}

