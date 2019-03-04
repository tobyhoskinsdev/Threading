package examples.aaronhoskins.com.threading;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    TextView tvJavaThreadResults;
    TextView tvAsyncResults;
    TextView tvLooperResults;

    private static int threadId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvJavaThreadResults = findViewById(R.id.tvResultsJavaThread);
        tvAsyncResults = findViewById(R.id.tvAsyncResults);
        tvLooperResults = findViewById(R.id.tvLooperResults);

        //Declare our async task
        AsyncThreading asyncThreading;
        //instantiate the task
        asyncThreading = new AsyncThreading();
        //start the task
        asyncThreading.execute();


        //startThread();
        //startThread();
        //uiThredDemo();

        //Looper
        LooperDemoThread looperDemoThread;
        //instantiate the looper, and handle results from the looper to the main Looper
        looperDemoThread = new LooperDemoThread(new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //get info back out of message received
                Bundle bundle = msg.getData();
                tvLooperResults.setText(bundle.getString("key"));
            }
        });
        looperDemoThread.start();
        looperDemoThread.workerThreadHandler.sendMessage(new Message());

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //Standard Java Runnable for Thread
    private Runnable runnableForThread(final int id) {

        return new Runnable() {
            @Override
            public void run() {
                int x = 0;
                int y = 1;
                while(x < 100) {
                    x = x + y;
                    //Log.d("TAG THREAD " + id, "run: X = " + x);

                    tvJavaThreadResults.setText("run: X = " + x);
                }
            }
        };
    }

    //Standard Java Thread
    private void startThread() {
        threadId++;

        Thread javaThread = new Thread(runnableForThread(threadId));
        javaThread.start();

    }

    //Thread that will run on the UI
    private void uiThredDemo() {
        runOnUiThread(runnableForThread(1000));

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAsyncMessageReceived(AsyncTaskEvent asyncTaskEvent) {
        tvAsyncResults.setText(asyncTaskEvent.getMessage());
    }

}
