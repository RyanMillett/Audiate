package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class PitchDetector {

    // permission constants
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final int DENIED = PackageManager.PERMISSION_DENIED;

    private int mHasAudioPerm;

    private Context mContext;
    private Activity mActivity;

    private double mFrequency;
    private double mFreqAvg;
    private String mPitchClass;

    private int mCollectionLimit;
    private int mCollectionCounter;

    private Thread mAudioThread;

    /**
     * Default constructor.
     *
     * @param context
     * @param activity
     */
    public PitchDetector(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mFrequency = 0.0;
        mPitchClass = "";
        mCollectionLimit = SoundObjectPlayer.SAMPLE_RATE;
        mCollectionCounter = 0;
        mFreqAvg = 0;
        getAudioPermissions(context, activity);
    }

    /**
     * Gets the current frequency in Hertz.
     *
     * @return double value representing frequency in Hertz
     */
    public double getFrequency() {
        return mFrequency;
    }

    /**
     * Gets the current pitch class.
     *
     * @return String representing pitch class
     */
    public String getPitchClass() {
        return mPitchClass;
    }

    /**
     * Activates pitch detection function.
     */
    public void activatePitchDetection() {
        // TODO: this method
        if (mHasAudioPerm == GRANTED) {
            AudioDispatcher dispatcher =
                    AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                    final float frequencyInHz = res.getPitch();
                    mFrequency = frequencyInHz;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            collectFrequencies(frequencyInHz);
                        }
                    });
                }
            };
            AudioProcessor pitchProcessor =
                    new PitchProcessor(PitchProcessor
                            .PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);

            mAudioThread = new Thread(dispatcher, "Audio Thread");
            mAudioThread.start();
        }
    }

    /**
     * Deactivates pitch detection function.
     */
    public void deactivatePitchDetection() {
        mAudioThread.stop();
    }

    private void collectFrequencies(float frequencyInHz) {
        while (mCollectionCounter++ < mCollectionLimit && frequencyInHz > 0) {
            mFreqAvg += frequencyInHz;
        }
    }

    public String parsePitchFromFreqAvg() {
        if (mFreqAvg > 0) {
            mFreqAvg /= mCollectionLimit;
            return Music.parsePitchClassFromFrequency(mFreqAvg);
        }
        return "No Pitch Detected";
    }

    private void getAudioPermissions(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

        List<String> permsList = new ArrayList<>();

        mHasAudioPerm = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
        if (mHasAudioPerm == DENIED) permsList.add(Manifest.permission.RECORD_AUDIO);

        // Some permissions have not been granted
        if (permsList.size() > 0)
        {
            // Convert the permsList to an array
            String[] permsArray = new String[permsList.size()];
            permsList.toArray(permsArray);

            // Ask user for them
            ActivityCompat.requestPermissions(mActivity, permsArray, 1337);
        }
    }
}
