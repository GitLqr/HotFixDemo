package com.lqr;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lqr.jnitest.JniUtil;
import com.lqr.tinker_bugly.R;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.lib.library.TinkerLoadLibrary;

public class MainActivity extends AppCompatActivity {

    private TextView mTvAbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvAbi = findViewById(R.id.tv_abi);
        mTvAbi.setText(mTvAbi.getText() + android.os.Build.CPU_ABI);
    }

    public void say(View view) {
//        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Hello World 123", Toast.LENGTH_SHORT).show();
    }

    public void string_from_so(View view) {
        String string = JniUtil.hello();
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    public void install_patch(View view) {
        Beta.applyTinkerPatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
    }

    public void uninstall_patch(View view) {
        boolean now = true;
        Beta.cleanTinkerPatch(now);
    }

    public void check_upgrade(View view) {
        Beta.checkUpgrade();
    }

    /**
     * 用户主动下载补丁文件
     * <p>
     * 适用于Beta.canAutoDownloadPatch = false;的情况。 开发者自己选择下载补丁的时机。
     */
    public void download_patch(View view) {
        Beta.downloadPatch();
    }

    /**
     * 用户主动合成补丁
     * <p>
     * 适用于Beta.canAutoPatch = true;的情况。 开发者自己选择合成补丁的时机。
     */
    public void apply_patch(View view) {
        Beta.applyDownloadedPatch();
    }

    public void kill_myself(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /*------------------ Tinker封装好的非hack方式加载so库方法 begin ------------------*/
    // 使用非Hack的方式（测试失败）
    public void load_library_no_hack(View view) {
        Beta.loadLibrary(JniUtil.LIB_NAME);
    }

    public void load_library_armeabi(View view) {
        Beta.loadArmLibrary(getApplicationContext(), JniUtil.LIB_NAME);// 底层使用的跟Beta.loadLibrary一样，故测试失败
    }

    public void load_library_armeabi_v7a(View view) {
        Beta.loadArmV7Library(getApplicationContext(), JniUtil.LIB_NAME);// 底层使用的跟Beta.loadLibrary一样，故测试失败
    }
    /*------------------ Tinker封装好的非hack方式加载so库方法 end ------------------*/

    /*------------------ Tinker的原生接口 ------------------*/
    // 使用Hack的方式（测试成功）
    public void load_library_hack(View view) {
        String CPU_ABI = android.os.Build.CPU_ABI;
        // 将tinker library中的 CPU_ABI架构的so 注册到系统的library path中。
        TinkerLoadLibrary.installNavitveLibraryABI(this, CPU_ABI);
    }

}
