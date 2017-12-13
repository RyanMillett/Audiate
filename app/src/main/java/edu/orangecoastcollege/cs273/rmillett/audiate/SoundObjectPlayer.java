package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The <code>SoundObjectPlayer</code> class is used to read a <code>SoundObject</code> then write
 * and play an audio signal constructed according the parameters of the passed <code>SoundObject</code>.
 *
 * <code>SoundObjectPlayer</code> objects can perform single tones, chords, melodies, chord sequences,
 * or any other combination of <cocde>SoundObject</cocde> instances.
 *
 * To successfully perform an audio playback of a <code>SoundObject</code>, simply pass an initialized
 * <code>SoundObject</code> into the .playSoundObject() method.
 *
 * Example:
 *          <code>
 *              // Play sound object
 *              soundObjectPlayer.playSoundObject(soundObject);
 *          </code>
 *
 * @author Ryan Millett
 * @version 2.2
 */
public class SoundObjectPlayer {

    /**
     * int constant used to set the sample rate in Hertz that <code>SoundObjectPlayer</code> objects
     * write and playback audio signals.
     *
     * Do not set this lower than 8k
     */
    public static final int DEFAULT_SAMPLE_RATE = 8000; // TODO: link this to general settings

    private static AudioTrack mAudioTrack;

    // TODO: create parametrized constructors for special circumstances

    public void playSoundObject(SoundObject soundObject) {
        loadSoundObject(soundObject);
        play();
    }

    /**
     * Loads a <code>SoundObject</code> into the <code>SoundObjectPlayer</code> and parses it based
     * on subclass. The <code>SoundObject</code> is sent to a private inner method which constructs
     * a waveform and writes it to an audio track.
     *
     * @param soundObject <code>SoundObject</code> to be parsed and interpreted as an audio signal.
     */
    private void loadSoundObject(SoundObject soundObject) {
        // Parse SoundObject and playback mode
        if (soundObject instanceof Note ||
                (soundObject instanceof ChordScale && ((ChordScale) soundObject).getPlayBackMode()
                        .equalsIgnoreCase(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER))) {
            createWaveform(soundObject);
        }
        else {
            createSequencedWaveform((ChordScale) soundObject);
        }
    }

    private void createWaveform(SoundObject soundObject) {
        // Get frequencies
        double[] frequencies;
        if (soundObject instanceof Note) {
            frequencies = new double[1];
            frequencies[0] = ((Note) soundObject).getPitchFrequency();
        }
        else {
            frequencies = new double[((ChordScale) soundObject).getSize()];
            frequencies = ((ChordScale) soundObject).getAllChordMemberFrequencies();
        }

        // Create waveform
        int numSamples = (soundObject.getDurationMilliseconds() / 1000) * DEFAULT_SAMPLE_RATE;
        double[] sample = new double[numSamples];
        for (int i = 0; i < numSamples; ++i) {
            double valueSum = 0;

            for (int j = 0; j < frequencies.length; ++j) {
                valueSum += Math.sin(2 * Math.PI * i / (DEFAULT_SAMPLE_RATE / frequencies[j]));
            }

            sample[i] = valueSum / frequencies.length;
        }

        // Generate sound from waveform
        generateSoundDataFromSamples(sample, numSamples);
    }

    private void createSequencedWaveform(ChordScale chordScale) {

        // Determine sequence order
        ArrayList<Note> sequence = new ArrayList<>();
        if (chordScale.getPlayBackMode().equalsIgnoreCase(ChordScale.PLAYBACK_MODE_CHORDSCALE_UP)) {
            sequence = chordScale.getAllChordMembers();
        }
        else if (chordScale.getPlayBackMode().equalsIgnoreCase(ChordScale.PLAYBACK_MODE_CHORDSCALE_DOWN)) {
            sequence = chordScale.getAllChordMembers();
            Collections.reverse(sequence);
        }
        else if (chordScale.getSize() == 3 && chordScale.getPlayBackMode()
                .equalsIgnoreCase(ChordScale.PLAYBACK_MODE_ALBERTI)) {
            sequence.add(chordScale.getChordMemberAtPos(0));
            sequence.add(chordScale.getChordMemberAtPos(2));
            sequence.add(chordScale.getChordMemberAtPos(1));
            sequence.add(chordScale.getChordMemberAtPos(2));
        }

        // Create waveform
        ArrayList<double[]> samples = new ArrayList<>();
        int numSamples = 0;
        for (final Note note : sequence) {
            int num = note.getDurationMilliseconds() * DEFAULT_SAMPLE_RATE / 1000;
            double[] sample = new double[num];

            for (int i = 0; i < num; ++i) {
                sample[i] = Math.sin(2 * Math.PI * i * note.getPitchFrequency() / DEFAULT_SAMPLE_RATE);
            }
            samples.add(sample);
            numSamples += num;
        }

        // Generate sound from waveform
        generateSoundDataFromSamples(samples, numSamples);
    }

    private void generateSoundDataFromSamples(ArrayList<double[]> samples, int numSamples) {
        byte generatedSound[] = new byte[2 * numSamples];
        int i = 0;
        for (double[] sample : samples) {
            for (final double dVal : sample) {
                final short val = (short) ((dVal * 32767));
                generatedSound[i++] = (byte) (val & 0x00ff);
                generatedSound[i++] = (byte) ((val & 0xff00) >>> 8);
            }
        }

        // Write audio track
        writeAudioTrack(generatedSound);
    }

    private void generateSoundDataFromSamples(double[] sample,int numSamples) {
        byte generatedSound[] = new byte[2 * numSamples];
        int i = 0;
        for (double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            generatedSound[i++] = (byte) (val & 0x00ff);
            generatedSound[i++] = (byte) ((val & 0xff00) >>> 8);
        }

        // Write audio track
        writeAudioTrack(generatedSound, numSamples);
    }

    private void writeAudioTrack(byte[] generatedSnd, int numSamples) {
        mAudioTrack = new AudioTrack(AudioManager
                .STREAM_MUSIC, DEFAULT_SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);

        mAudioTrack.write(generatedSnd, 0, generatedSnd.length);
    }

    private void writeAudioTrack(byte[] generatedSound) {
        mAudioTrack = new AudioTrack(AudioManager
                .STREAM_MUSIC, DEFAULT_SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSound.length,
                AudioTrack.MODE_STATIC);

        mAudioTrack.write(generatedSound, 0, generatedSound.length);
    }

    private void play() {
        // TODO: thread this
        mAudioTrack.play();
    }

    // TODO: create method for flushing AudioTrack data
}
