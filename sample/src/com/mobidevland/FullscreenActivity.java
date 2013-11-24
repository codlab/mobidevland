package com.mobidevland;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

public class FullscreenActivity extends CaptureActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

	}

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scale)
    {
        super.handleDecode(rawResult,barcode,scale);
        Toast.makeText(this.getApplicationContext(), "Scanned code " + rawResult.getText(), Toast.LENGTH_LONG).show();

        //could use OnScanListener.onScan

        restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
    }
}
