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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private int unlockCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides status bar
        makeFullScreen();

        startService(new Intent(this,LockScreenService.class));

        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        this.getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Removes the KeyGuard all together (no security)
        //this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().setType(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_main);

        unlockCount = getUnlockCount();

        //Set count in textview
        TextView countText = (TextView)findViewById(R.id.unlock_count);
        countText.setText(Integer.toString(unlockCount));
    }

    private int getUnlockCount() {
        SharedPreferences prefs = this.getSharedPreferences("unlock_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //Get current day
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_YEAR);

        //Fetch preferences
        int lastDayLogged = prefs.getInt("last_logged", 0);
        int count = prefs.getInt("count", 0);

        //If same day, increment. Else reset day.
        if(lastDayLogged == day){
            count++;
        } else{
            count = 0;
        }

        //Save updated shared preference
        editor.putInt("last_logged", day);
        editor.putInt("count", count);
        editor.apply();

        return count;
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
        //Don't Do Anything
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Unlock screen
    public void unlockScreen(View view) {
        //Instead of using finish(), destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
