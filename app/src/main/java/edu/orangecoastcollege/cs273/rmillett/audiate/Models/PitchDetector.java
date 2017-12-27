package edu.orangecoastcollege.cs273.rmillett.audiate.Models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
 * The <code>PitchDetector</code> class is used to monitor incoming audio signal and determine the
 * approximate fundamental frequency of the sound.
 *
 * In order to return the most accurate results, incoming audio should be in the form of a single,
 * sustained pitch--similar to a conventional tuner.
 *
 * @author Ryan Millett
 * @version 2.0
 */
public class PitchDetector {

    private static final String TAG = "PitchDetector";

    // permission constants
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final int DENIED = PackageManager.PERMISSION_DENIED;

    public static final int DEFAULT_COLLECTION_LIMIT = 2 * SoundObjectPlayer.DEFAULT_PLAYBACK_SAMPLE_RATE;

    private int mHasAudioPerm;

    private Context mContext;
    private Activity mActivity;

    private double mFrequencySum;

    private int mCollectionLimit;
    private int mCollectionCounter;

    private Thread mAudioThread;

    /**
     * Constructor.
     *
     * Must take context/activity for audio permissions purposes.
     *
     * @param context Context
     * @param activity Activity
     */
    public PitchDetector(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mCollectionLimit = DEFAULT_COLLECTION_LIMIT;
        mCollectionCounter = 0;
        mFrequencySum = 0.0;
        getAudioPermissions(context, activity);
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

    /**
     * Activates pitch detection function to run in the background.
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
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            collectFrequencies(frequencyInHz);
                        }
                    });
                }
            };
            // FFT + lowpass
            AudioProcessor pitchProcessor =
                    new PitchProcessor(PitchProcessor
                            .PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);

            mAudioThread = new Thread(dispatcher, "Audio Thread");
            mAudioThread.start();
        }
    }

    private void collectFrequencies(float frequencyInHz) {
        while (frequencyInHz > 0 && mCollectionCounter++ < mCollectionLimit) {
            mFrequencySum += frequencyInHz;
            Log.i(TAG, "collections size-> " + mCollectionCounter + "/" + mCollectionLimit);
        }
        //Log.i(TAG, "Collections bin is full!");
    }

    public double getFrequencyAverage() {
        return mFrequencySum /= mCollectionCounter;
    }

    public void resetCollections() {
        mCollectionCounter = 0;
        mFrequencySum = 0.0;
    }

    /**
     * Takes a collection of consecutively sampled frequencies and creates an average in order to
     * account for occasional deviations caused by the FFT algorithm.
     *
     * The averaged frequency is passed into a <code>Music</code> class method which returns the
     * closest corresponding pitch-class.
     *
     * @return a String representing the approximate pitch-class of the received frequency in Hertz
     */
    public String parsePitchFromFreqAvg(double freq) {
        if (freq > 0) {
            return Music.parsePitchClassFromFrequency(freq, Music._12_TET_PITCH_FREQUENCIES);
        }
        return "No Pitch Detected";
    }
}
