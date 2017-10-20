package com.kirkiki.kirkikilib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.kirkiki.utillib.DeviceUtil;
import java.net.SocketException;
import org.pcc.webviewOverlay.WebViewOverlay;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try {
      Toast.makeText(this, DeviceUtil.getMacAddress(), Toast.LENGTH_SHORT).show();
    } catch (SocketException e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    WebViewOverlay webViewOverlay=new WebViewOverlay(this);
  }
}
