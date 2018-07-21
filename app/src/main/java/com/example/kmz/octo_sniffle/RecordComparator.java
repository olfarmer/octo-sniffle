package com.example.kmz.octo_sniffle;

import java.util.Comparator;

public class RecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        return o1.time.compareTo(o2.time);
    }
}
