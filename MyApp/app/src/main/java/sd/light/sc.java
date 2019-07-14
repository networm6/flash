package sd.light;
import android.kz.BaseBean.Base;
import android.hardware.SensorListener;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.widget.TextView;
import android.os.Vibrator;
import android.view.WindowManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.hardware.Camera.Parameters;
import android.os.Build;

public class sc extends Base implements SensorListener
{
	private static final int SHAKE_THRESHOLD = 500;
//这个控制精度，越小表示反应越灵敏
	
	@Override
	public void onSensorChanged(int sensor, float[] values) {
// TODO Auto-generated method stub
		if(sensor == SensorManager.SENSOR_ACCELEROMETER){
			long curTime = System.currentTimeMillis();
//每100毫秒检测一次
			if((curTime-lastUpdate)>100){
				long diffTime = (curTime-lastUpdate);
				lastUpdate = curTime;

				x = values[SensorManager.DATA_X]; 

				y = values[SensorManager.DATA_Y]; 

				z = values[SensorManager.DATA_Z]; 
				float speed = Math.abs(x+y+z-last_x-last_y-last_z)/diffTime*10000;
				if(speed>SHAKE_THRESHOLD){
//这里写上自己的功能代码
					aa.setText("x="+(int)x+","+"y="+(int)y+","+"z="+(int)z); 
					Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
					vibrator.vibrate(10);
					flash();
					}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		} 
	}
	

	@Override
	public void onAccuracyChanged(int p1, int p2)
	{
		// TODO: Implement this method
	}
	
//	private SensorManager sensorMgr; 

// Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 

private float x, y, z,last_x,last_y,last_z; 
	private long lastUpdate;
TextView aa;
	CameraManager manager;
	Camera camera;
	Parameters parameters;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
						  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// TODO: Implement this method
		inlayout(R.layout.a);
		super.onCreate(savedInstanceState);
		
		
		manager = (CameraManager)this.getSystemService("camera");
		aa=(TextView)Bar(R.id.ab);
		SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE); 
		sensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER,
								   SensorManager.SENSOR_DELAY_GAME);
	}
	private void flash(){
		openFlash();
		closeflash();
	}
	private void closeflash(){
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				if (this.manager == null) {
					return;
				}

				this.manager.setTorchMode("0", false);
			} catch (Exception var2) {
				var2.printStackTrace();
			}
		} else {
			if (this.camera == null) {
				return;
			}

			this.parameters = this.camera.getParameters();
			this.parameters.setFlashMode("off");
			this.camera.setParameters(this.parameters);
			this.camera.stopPreview();
			this.camera.release();
		}
	}
	private void openFlash() {
		try {
			if (Build.VERSION.SDK_INT >= 23) {
				this.manager = (CameraManager)this.getSystemService("camera");
				if (this.manager != null) {
					this.manager.setTorchMode("0", true);
				}
			} else {
				this.camera = Camera.open();
				Parameters var1 = this.camera.getParameters();
				var1.setFlashMode("torch");
				this.camera.setParameters(var1);
			}
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		

	}




    


}
