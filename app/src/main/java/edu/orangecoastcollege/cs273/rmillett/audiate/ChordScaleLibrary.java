package edu.orangecoastcollege.cs273.rmillett.audiate;

import java.util.ArrayList;
import java.util.List;

// TODO: make parcelable
/**
 *
 * @author Ryan Millett
 * @version 1.0
 */
public class ChordScaleLibrary {

    private String mName;
    private int mSize;
    private List<List<ChordScale>> mRecords;

    public ChordScaleLibrary(String name, int size) {
        mName = name;
        mSize = size;
        mRecords = new ArrayList<>(mSize);
    }

    public void addList(List<ChordScale> newList) {
        mRecords.add(newList);
        mSize++;
    }
}
