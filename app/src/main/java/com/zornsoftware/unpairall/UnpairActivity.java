package com.zornsoftware.unpairall;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.Set;

public class UnpairActivity extends AppCompatActivity {

    private static final String TAG = UnpairActivity.class.getSimpleName();
    private Button _unpairButton;
    private Set<BluetoothDevice> _pairedDevices;
    private TextView _statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpair);
        _unpairButton = findViewById(R.id.unpairAllButton);
        _unpairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unpairAll();
            }
        });

        BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
        _pairedDevices = b.getBondedDevices();

        _statusText = findViewById(R.id.statusText);
        _statusText.setText(String.format("There is currently %d paired bluetooth devices.", _pairedDevices.size()));
    }

    private void unpairAll() {
        _unpairButton.setEnabled(false);
        _unpairButton.setText("Please wait...");
        BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = b.getBondedDevices();
        int unpairCount = 0;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                try {
                    Method m = device.getClass().getMethod("removeBond", (Class[]) null);
                    m.invoke(device, (Object[]) null);
                    unpairCount++;
                } catch (Exception e) {
                    Log.w(TAG, "Failed to un-pair device.");
                }
            }
        }

        _statusText.setText(String.format("%d of %d devices un-paired.", unpairCount, _pairedDevices.size()));
        _unpairButton.setText("Un-pair all");
        findViewById(R.id.unpairAllButton).setEnabled(true);
    }
}
