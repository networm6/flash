package android.kz.BaseBean;
import android.app.Activity;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;
import android.view.Window;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public abstract class Base extends Activity
{
	private int mview;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (!this.isTaskRoot()) {
			Intent intent = getIntent();
			if (intent != null) {
				String action = intent.getAction();
				if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
					finish();
					return;
				}
			}
		}

		setContentView(this.mview);
		getWindow().addFlags(0x08000000);
		getWindow().addFlags(1024);
		if(getActionBar()!=null)
			getActionBar().hide();
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
			View v = this.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			//for new api versions.
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
		}
		if((getRequestedOrientation()==2?true:null)!=null){
			SinkFullScreen s= SinkFullScreen.INSTANCE;
			s.blockStatusCutout(getWindow());
		}else{
			SinkFullScreen s= SinkFullScreen.INSTANCE;
			s.extendStatusCutout(getWindow(),this);
		}

	}
	public void back(View v){
		this.finish();
	}
	public void inlayout(int inviewlayout)
	{
		this.mview = inviewlayout;
	}
	public View Bar(int bar){
		int result = 0;
		int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) 
			result = this.getResources().getDimensionPixelSize(resourceId);
		View topbar=findViewById(bar);
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) topbar.getLayoutParams();
		linearParams.height = result;
		topbar.setLayoutParams(linearParams); 
		return topbar;
	}
	
	@Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;


    // * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
	//  *
	//  * @param intent          用于跳转的 Intent 对象
	//  * @return                检查通过返回true, 检查不通过返回false

    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        }else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        }else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }
}


