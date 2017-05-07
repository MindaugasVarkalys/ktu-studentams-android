package lt.chocolatebar.ktustudentams;

import android.app.ProgressDialog;
import android.content.Context;

public final class LoadingDialog {

    private static ProgressDialog progressDialog;

    public static void show(Context context) {
        progressDialog = ProgressDialog.show(context, context.getString(R.string.loading), context.getString(R.string.please_wait));
    }

    public static void dismiss() {
        progressDialog.dismiss();
    }

}
