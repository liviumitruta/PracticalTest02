package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private Button serverStartButton = null;

    // Client widgets
    private EditText cuvantEditText = null;
    private EditText lungimeEditText = null;
    private Button startButton = null;
    private TextView resultsText = null;
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private StartServerClickListener StartServerClickListener = new StartServerClickListener();
    private class StartServerClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server has been started!", Toast.LENGTH_SHORT).show();
        }

    }

    private goButtonClickListener goButtonClickListener = new goButtonClickListener();
    private class goButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String cuvant = cuvantEditText.getText().toString();
            String lungime = lungimeEditText.getText().toString();
            if (cuvant == null || cuvant.isEmpty() || lungime == null || lungime.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultsText.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), cuvant, lungime, resultsText
            );
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = findViewById(R.id.server_port);
        serverStartButton = findViewById(R.id.server_start);

        cuvantEditText = findViewById(R.id.client_cuvant);
        lungimeEditText = findViewById(R.id.client_lungime);
        startButton = findViewById(R.id.client_go);
        resultsText = findViewById(R.id.client_result);
        clientAddressEditText = findViewById(R.id.client_address);
        clientPortEditText = findViewById(R.id.client_port);

        serverStartButton.setOnClickListener(StartServerClickListener);
        startButton.setOnClickListener(goButtonClickListener);

    }
}
