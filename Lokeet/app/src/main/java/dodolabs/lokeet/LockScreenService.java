package dodolabs.lokeet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Hayde on 28-Jun-17.
 */

public class LockScreenService extends Service {
    BroadcastReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
