package adegokeobasa.me.gitwatch.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class UIUtils {
    public static void makeToast(Context context, String message, int... type){
        Toast.makeText(context, message, type.length > 0 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
                .show();
    }
}
