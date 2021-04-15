package com.example.nfc_post_get;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  private NfcAdapter mAdapter;
  private PendingIntent mPendingIntent;
  private IntentFilter[] mIntentFilters;
  private MifareClassic mfc;
  private String[][] mTechList;
  private StringBuilder ex_id = new StringBuilder();

  private TextView labelID;
  private TextView idView;
  private TextView responseView;
  private Button buttonPost;
  private Button buttonGetLast;
  private Button buttonGetWithID;

  private void initViews() {
    labelID = findViewById(R.id.label);
    responseView = findViewById(R.id.httpResponse);
    idView = findViewById(R.id.tagid);
    buttonPost = findViewById(R.id.btnPost);
    buttonGetLast = findViewById(R.id.btnGetLast);
    buttonGetWithID = findViewById(R.id.btnGetWithID);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();

    mAdapter = NfcAdapter.getDefaultAdapter(this);
    if (mAdapter == null) {
      this.finish();
      return;
    }
    mPendingIntent =
            PendingIntent.getActivity(
                    this,
                    0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    0
            );

    IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
    try {
      filter.addDataType("*/*");
    } catch (IntentFilter.MalformedMimeTypeException e) {
      e.printStackTrace();
    }
    mIntentFilters = new IntentFilter[]{filter};
    mTechList = new String[][]{new String[]{MifareClassic.class.getName()}}; // 只處理 MifareClassic 的 tag
  }

  protected void onResume() {
    super.onResume();
    mAdapter.enableForegroundDispatch(
      this,
      mPendingIntent,
      mIntentFilters,
      mTechList
    );
  }

  protected void onPause() {
    super.onPause();
    mAdapter.disableForegroundDispatch(this);
  }

  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
      return;
    }
    byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    ex_id = Converter.getHexString(myNFCID, myNFCID.length);
    idView.setText(ex_id); //只讀 tag 的 id
    responseView.setText("Tag is detected!");
  }
}
