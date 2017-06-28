package dodolabs.lokeet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView countText;
    private int unlockCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Up LockScreen
        makeFullScreen();

        startService(new Intent(this,LockScreenService.class));

        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        this.getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Removes the KeyGuard all together (no security)
        //this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().setType(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences("unlock_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        unlockCount = prefs.getInt("count", 0);
        unlockCount++;
        editor.putInt("count", unlockCount);
        editor.apply();

        countText = (TextView)findViewById(R.id.unlock_count);
        countText.setText(Integer.toString(unlockCount));
    }

    private void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Don't Do Anything
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Unlock screen
    public void unlockScreen(View view) {
        unlockCount++;
        //Instead of using finish(), destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
