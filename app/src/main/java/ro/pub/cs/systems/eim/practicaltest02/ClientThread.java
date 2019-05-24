package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String cuvant;
    private String lungime;
    private TextView resultsText;

    private Socket socket;

    public ClientThread(String address, int port, String cuvant, String lungime, TextView weatherForecastTextView) {
        this.address = address;
        this.port = port;
        this.cuvant = cuvant;
        this.lungime = lungime;
        this.resultsText = weatherForecastTextView;
    }

    @Override
    public void run() {
        Log.d(Constants.TAG, "CLINET HERE");
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(cuvant);
            printWriter.flush();
            printWriter.println(lungime);
            printWriter.flush();
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                final String finalizedInformation = info;
                resultsText.post(new Runnable() {
                    @Override
                    public void run() {
                        resultsText.setText(finalizedInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
//            if (Constants.DEBUG) {
//                ioException.printStackTrace();
//            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
//                    if (Constants.DEBUG) {
//                        ioException.printStackTrace();
//                    }
                }
            }
        }
    }
}
