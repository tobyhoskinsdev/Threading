package examples.aaronhoskins.com.threading;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

public class AsyncThreading extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
        //Runs on Main Thread
        //set up anything that needs to be set up before thread runs
        Log.d("TAG", "onPreExecute: ABOUT TO RUN");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        int controller = 0;
        while(controller < 100) {
            //send the current thread update info to onProgressUpdate
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress("RAN " + controller++ + " Time(s)");
        }
        //Runs on WORKER thread
        return "COMPLETE " + controller;
    }

    //Handles updates from the thread(doInBackground method above)
    @Override
    protected void onProgressUpdate(String... values) {
        //Runs on main Thread
        super.onProgressUpdate(values);
        EventBus.getDefault().post(new AsyncTaskEvent(values[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        //Runs on main thread
        //Reports the results
        super.onPostExecute(s);
        EventBus.getDefault().post(new AsyncTaskEvent(s));
    }
}
