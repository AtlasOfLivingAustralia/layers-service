/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ala.spatial.analysis.index;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeSet;
import org.ala.spatial.analysis.legend.Legend;
import org.ala.spatial.analysis.service.LayerImgService;
import org.ala.spatial.analysis.service.SamplingService;
import org.ala.spatial.util.TabulationSettings;

/**
 * Retrieve attribute column records of varying types.
 * 
 * Can transform attribute values to an int ARGB colour (A = 0xFF).
 * 
 * Can generate legends for colours exported.
 *
 * @author Adam
 */
public class SpeciesColourOption {

    static final int startColour = 0xFFFFFF00;
    static final int endColour = 0xFFFF0000;
    int[] iArray;
    double[] dArray;
    boolean[] bArray;
    String[] sArray;
    int[] sArrayHash;
    String name;
    String displayName;
    int type;
    String key;
    boolean highlight;
    int taxon;
    boolean colourMode;
    double dMin, dMax;
    int iMin, iMax;

    void makeSArrayHash() {
        sArrayHash = new int[sArray.length];
        for (int i = 0; i < sArray.length; i++) {
            sArrayHash[i] = 0xFF000000 | sArray[i].hashCode();
        }
    }

    SpeciesColourOption(String name, String displayName, int type, String key, boolean highlight, int taxon, boolean colourMode) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.key = key;
        this.highlight = highlight;
        this.taxon = taxon;
        this.colourMode = colourMode;
    }

    SpeciesColourOption(int idx, boolean colourMode) {
        this.name = TabulationSettings.geojson_property_names[idx];
        this.displayName = TabulationSettings.geojson_property_display_names[idx];
        this.type = TabulationSettings.geojson_property_types[idx];
        this.key = null;
        this.highlight = false;
        this.taxon = -1;
        this.colourMode = colourMode;
    }

    public static String getColourLegend(String lsid, String colourMode) {
        ArrayList<SpeciesColourOption> other = new ArrayList<SpeciesColourOption>();
        other.add(SpeciesColourOption.fromName(colourMode, true));
        SamplingService ss = SamplingService.newForLSID(lsid);
        double[] points = ss.sampleSpeciesPoints(lsid, null, null, other);
        String legend = null;
        if (points != null && points.length > 0) {
            for (int j = 0; j < other.size(); j++) {
                //colour mode!
                legend = other.get(0).getLegendString();
            }
        }
        if (legend != null) {
            try {
                String pid = String.valueOf(System.currentTimeMillis());

                StringBuilder str = new StringBuilder();
                str.append("name, red, green, blue").append("\n");
                str.append(legend);

                //register
                LayerImgService.registerLayerLegend(TabulationSettings.base_output_dir, pid, str.toString());

                return pid;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    static Object loadData(String index_path, int idx) {
        System.out.println("loading: idx=" + idx + ", name=" + TabulationSettings.geojson_property_names[idx]);
        Object data = null;
        try {
            FileInputStream fis = new FileInputStream(index_path
                    + TabulationSettings.geojson_property_names[idx]
                    + ".dat");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            data = ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    static void saveData(String index_path, int idx, Object array) {
        System.out.println("saving: idx=" + idx + ", name=" + TabulationSettings.geojson_property_names[idx]);
        try {
            FileOutputStream fos = new FileOutputStream(index_path
                    + TabulationSettings.geojson_property_names[idx]
                    + ".dat");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            Object[] output;

            switch (TabulationSettings.geojson_property_types[idx]) {
                case 0: //double
                    //get global min/max
                    output = new Object[2];
                    output[0] = array;
                    output[1] = SpeciesColourOption.getMinMax((double[]) array);
                    oos.writeObject(output);
                    break;
                case 1: //int
                    //get global min/max
                    output = new Object[2];
                    output[0] = array;
                    output[1] = SpeciesColourOption.getMinMax((int[]) array);
                    oos.writeObject(output);
                    break;
                case 2: //boolean
                    output = new Object[1];
                    output[0] = array;
                    oos.writeObject(output);
                    break;
                case 3: //String
                    //convert to String [] for unique Strings and int [] for ref
                    output = SpeciesColourOption.toStringLookup((String[]) array);
                    oos.writeObject(output);
                    break;
            }

            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double[] getMinMax(double[] d) {
        double min = d[0];
        double max = d[0];
        for (int i = 1; i < d.length; i++) {
            if (!Double.isNaN(d[i])) {
                if (Double.isNaN(min) || min > d[i]) {
                    min = d[i];
                }
                if (Double.isNaN(max) || max < d[i]) {
                    max = d[i];
                }
            }
        }
        double[] output = new double[2];
        output[0] = min;
        output[1] = max;
        System.out.println(min + " " + max);

        return output;
    }

    private static int[] getMinMax(int[] d) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 1; i < d.length; i++) {
            if (d[i] != Integer.MIN_VALUE && min > d[i]) {
                min = d[i];
            }
            if (max < d[i]) {
                max = d[i];
            }
        }
        System.out.println(min + " " + max);
        int[] output = new int[2];
        output[0] = min;
        output[1] = max;

        return output;
    }

    private static Object[] toStringLookup(String[] strings) {
        //make unique list
        TreeSet<String> ts = new TreeSet<String>();
        for (String s : strings) {
            ts.add(s);
        }

        String[] sOutput = new String[ts.size()];
        ts.toArray(sOutput);
        java.util.Arrays.sort(sOutput);

        //make record to unique list lookup
        int[] iOutput = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            iOutput[i] = java.util.Arrays.binarySearch(sOutput, strings[i]);
        }

        Object[] output = new Object[2];
        output[0] = sOutput;
        output[1] = iOutput;

        return output;
    }

    static SpeciesColourOption fromSpeciesColourOption(SpeciesColourOption sco) {
        return new SpeciesColourOption(sco.name, sco.displayName, sco.type, sco.key, sco.highlight, sco.taxon, sco.colourMode);
    }

    public static SpeciesColourOption fromMode(String mode, boolean colourMode) {
        for (int i = 0; i < TabulationSettings.geojson_property_display_names.length; i++) {
            if (mode.equalsIgnoreCase(TabulationSettings.geojson_property_display_names[i])) {
                return new SpeciesColourOption(i, colourMode);
            }
        }

        for (int i = 1; i < TabulationSettings.occurrences_csv_field_pairs.length; i += 2) {
            if (mode.equalsIgnoreCase(TabulationSettings.occurrences_csv_field_pairs[i])) {
                return new SpeciesColourOption(mode, mode, 3, null, false, (i - 1) / 2, colourMode);
            }
        }

        return null;
    }

    public static SpeciesColourOption fromName(String name, boolean colourMode) {
        for (int i = 0; i < TabulationSettings.geojson_property_display_names.length; i++) {
            if (name.equalsIgnoreCase(TabulationSettings.geojson_property_names[i])) {
                return new SpeciesColourOption(i, colourMode);
            }
        }

        for (int i = 1; i < TabulationSettings.occurrences_csv_field_pairs.length; i += 2) {
            if (name.equalsIgnoreCase(TabulationSettings.occurrences_csv_field_pairs[i])) {
                String mode = TabulationSettings.occurrences_csv_field_pairs[i];
                return new SpeciesColourOption(mode, mode, 3, null, false, (i - 1) / 2, colourMode);
            }
        }

        return null;
    }

    public static String getColourOptions(String lsid) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < TabulationSettings.geojson_property_display_names.length; i++) {
            sb.append(TabulationSettings.geojson_property_display_names[i]).append("\n");
        }

        for (int i = 1; i < TabulationSettings.occurrences_csv_field_pairs.length; i += 2) {
            sb.append(TabulationSettings.occurrences_csv_field_pairs[i]).append("\n");
        }

        return sb.toString();
    }

    public void assignData(int[] r, Object list) {
        Object[] data = (Object[]) list;
        switch (type) {
            case 0: //double
                double[] input0 = (double[]) data[0];
                dMin = ((double[]) data[1])[0];
                dMax = ((double[]) data[1])[1];
                dArray = new double[r.length];
                for (int i = 0; i < r.length; i++) {
                    dArray[i] = input0[r[i]];
                }
                break;
            case 1: //int
                int[] input1 = (int[]) data[0];
                iMin = ((int[]) data[1])[0];
                iMax = ((int[]) data[1])[1];
                iArray = new int[r.length];
                for (int i = 0; i < r.length; i++) {
                    iArray[i] = input1[r[i]];
                }
                break;
            case 2: //boolean
                boolean[] input2 = (boolean[]) data[0];
                bArray = new boolean[r.length];
                for (int i = 0; i < r.length; i++) {
                    bArray[i] = input2[r[i]];
                }
                break;
            case 3: //string
                sArray = (String[]) data[0];
                int[] input3 = (int[]) data[1];
                iArray = new int[r.length];
                for (int i = 0; i < r.length; i++) {
                    iArray[i] = input3[r[i]];
                }
                if (isColourMode()) {
                    makeSArrayHash();
                }
                break;
        }
    }

    public void append(SpeciesColourOption sco) {
        if (sco.type != type) {
            //error
        }
        switch (type) {
            case 0: //double
                dMin = sco.getDMin();
                dMax = sco.getDMax();
                if (dArray == null) {
                    dArray = sco.getDblArray();
                } else {
                    double[] dAdd = sco.getDblArray();
                    double[] dNew = new double[dArray.length + dAdd.length];
                    System.arraycopy(dArray, 0, dNew, 0, dArray.length);
                    System.arraycopy(dAdd, 0, dNew, dArray.length, dAdd.length);
                    dArray = dNew;
                }
                break;
            case 1: //int
            case 3: //string, appending lookup values
                iMin = sco.getIMin();
                iMax = sco.getIMax();
                if (iArray == null) {
                    iArray = sco.getIntArray();
                    sArray = sco.getSArray();
                    sArrayHash = sco.getSArrayHash();
                } else {
                    int[] iAdd = sco.getIntArray();
                    int[] iNew = new int[iArray.length + iAdd.length];
                    System.arraycopy(iArray, 0, iNew, 0, iArray.length);
                    System.arraycopy(iAdd, 0, iNew, iArray.length, iAdd.length);
                    iArray = iNew;
                }
                break;
            case 2: //boolean
                if (bArray == null) {
                    bArray = sco.getBoolArray();
                } else {
                    boolean[] bAdd = sco.getBoolArray();
                    boolean[] bNew = new boolean[bArray.length + bAdd.length];
                    System.arraycopy(bArray, 0, bNew, 0, bArray.length);
                    System.arraycopy(bAdd, 0, bNew, bArray.length, bAdd.length);
                    bArray = bNew;
                }
                break;
        }
    }

    public String[] getStrArray() {
        String[] s = new String[iArray.length];
        for (int i = 0; i < iArray.length; i++) {
            s[i] = sArray[iArray[i]];
        }
        return s;
    }

    public boolean[] getBoolArray() {
        return bArray;
    }

    public int[] getIntArray() {
        return iArray;
    }

    public double[] getDblArray() {
        return dArray;
    }

    public String getName() {
        return name;
    }

    public boolean isHighlight() {
        return highlight;
    }

    void assignHighlightData(int[] records, int record_start, String hash) {
        String k = hash + key;
        boolean[] highlight = RecordSelectionLookup.getSelection(k);
        int len = records.length;
        bArray = new boolean[len];
        for (int i = 0; i < len; i++) {
            bArray[i] = highlight[records[i] - record_start];
        }
    }

    static public SpeciesColourOption fromHighlight(String key, boolean colourMode) {
        return new SpeciesColourOption("h", "Selection", 2, key, true, -1, colourMode);
    }

    boolean isTaxon() {
        return (taxon >= 0);
    }

    void assignTaxon(int[] r, int[] speciesNumberInRecordsOrder, int[] speciesIndexLookup) {
        iArray = new int[r.length];

        if (colourMode) {
            for (int i = 0; i < r.length; i++) {
                if (speciesNumberInRecordsOrder[r[i]] >= 0) {
                    //set alpha
                    //iArray[i] = 0xFF000000 | SpeciesIndex.getHash(taxon, speciesIndexLookup[speciesNumberInRecordsOrder[r[i]]]);
                    iArray[i] = speciesIndexLookup[speciesNumberInRecordsOrder[r[i]]];
                } else {
                    //white
                    iArray[i] = -1;
                }
            }
        } else {
            for (int i = 0; i < r.length; i++) {
                iArray[i] = speciesIndexLookup[speciesNumberInRecordsOrder[r[i]]];
            }
        }
    }

    public int[] getColours() {
        int[] colours = null;
        switch (type) {
            case 0: //double
                colours = dblColours();
                break;
            case 1: //int
                colours = intColours();
                break;
            case 2: //boolean
                colours = boolColours();
                break;
            case 3: //string
                if (isTaxon()) {
                    colours = taxonColours();
                } else {
                    colours = strColours();
                }
                break;
        }
        return colours;
    }

    int[] dblColours() {
        int[] c = new int[dArray.length];
        //double range = dMax - dMin;
        for (int i = 0; i < dArray.length; i++) {
            c[i] = Legend.getColour(dArray[i], dMin, dMax);
            //c[i] = 0xFF000000 | (int) (((dArray[i] - dMin) / range) * 0x00FFFFFF);
        }
        return c;
    }

    int[] intColours() {
        int[] c = new int[iArray.length];
        //double range = iMax - iMin;
        double logIMin = Math.log10(iMin);
        if (logIMin < 0) {
            logIMin = 0;
        }
        double logIMax = Math.log10(iMax);
        for (int i = 0; i < iArray.length; i++) {
            if (iArray[i] != Integer.MIN_VALUE) {
                c[i] = Legend.getLinearColour(Math.log10(iArray[i]), logIMin, logIMax, startColour, endColour);
            } else {
                c[i] = 0xFF000000;
            }
            //c[i] = 0xFF000000 | (int) (((iArray[i] - iMin) / range) * 0x00FFFFFF);
        }
        return c;
    }

    int[] boolColours() {
        int[] c = new int[bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            if (bArray[i]) {
                c[i] = 0xFF000000;
            } else {
                c[i] = 0xFFFFFFFF;
            }
        }
        return c;
    }

    int[] strColours() {
        int[] c = new int[iArray.length];
        for (int i = 0; i < iArray.length; i++) {
            c[i] = sArrayHash[iArray[i]];
        }
        return c;
    }

    public void assignMissingData(int size) {
        switch (type) {
            case 0: //double
                dArray = new double[size];
                break;
            case 1: //int
                iArray = new int[size];
                break;
            case 2: //boolean
                bArray = new boolean[size];
                break;
            case 3: //string
                sArray = new String[1];
                iArray = new int[size];
                break;
        }
    }

    public boolean isColourMode() {
        return colourMode;
    }

    private String[] getSArray() {
        return sArray;
    }

    private int[] getSArrayHash() {
        return sArrayHash;
    }

    private int getIMin() {
        return iMin;
    }

    private int getIMax() {
        return iMax;
    }

    private double getDMin() {
        return dMin;
    }

    private double getDMax() {
        return dMax;
    }

    private String getLegendString() {
        StringBuilder legend = new StringBuilder();
        switch (type) {
            case 0: //double
                legend.append(dMin).append(",").append(RGBcsv(Legend.getLinearColour(dMin, dMin, dMax, startColour, endColour))).append("\n");
                legend.append(dMax).append(",").append(RGBcsv(Legend.getLinearColour(dMax, dMin, dMax, startColour, endColour))).append("\n");
                legend.append("unknown").append(",").append(RGBcsv(0xFF000000)).append("\n");
                break;
            case 1: //int
                legend.append(iMin).append(",").append(RGBcsv(Legend.getLinearColour(Math.log10(iMin), Math.log10(iMin), Math.log10(iMax), startColour, endColour))).append("\n");
                legend.append(iMax).append(",").append(RGBcsv(Legend.getLinearColour(Math.log10(iMax), Math.log10(iMin), Math.log10(iMax), startColour, endColour))).append("\n");
                legend.append("unknown").append(",").append(RGBcsv(0xFF000000)).append("\n");
                break;
            case 2: //boolean
                legend.append("true").append(",").append(RGBcsv(0xFFFFFFFF)).append("\n");
                legend.append("false").append(",").append(RGBcsv(0xFF000000)).append("\n");
                break;
            case 3: //string
                if (isTaxon()) {
                    legend.append(taxonLegend());
                } else {
                    legend.append(strLegend());
                }
                break;
        }

        return legend.toString();
    }

    static String RGBcsv(int colour) {
        int red = (colour >> 16) & 0x000000FF;
        int green = (colour >> 8) & 0x000000FF;
        int blue = (colour) & 0x000000FF;

        return String.format("%s,%s,%s", red, green, blue);
    }

    String strLegend() {
        StringBuilder legend = new StringBuilder();

        //flag presence
        boolean[] flag = new boolean[sArray.length];
        for (int i = 0; i < iArray.length; i++) {
            flag[iArray[i]] = true;
        }

        //iterate and write
        for (int i = 0; i < flag.length; i++) {
            if (flag[i]) {
                legend.append(sArray[i]).append(",").append(RGBcsv(sArrayHash[i])).append("\n");
            }
        }

        return legend.toString();
    }

    String taxonLegend() {
        StringBuilder legend = new StringBuilder();

        //copy & sort
        int[] iList = new int[iArray.length];
        for (int i = 0; i < iArray.length; i++) {
            iList[i] = SpeciesIndex.getParentPos(taxon, iArray[i]);
        }
        java.util.Arrays.sort(iList);

        for (int i = 0; i < iList.length; i++) {
            if (i == 0 || iList[i - 1] != iList[i]) {
                if (iList[i] >= 0) {
                    legend.append(SpeciesIndex.getScientificName(iList[i])).append(",").append(RGBcsv(SpeciesIndex.getHash(taxon, iList[i]))).append("\n");
                } else {
                    legend.append("unknown").append(",").append(RGBcsv(SpeciesIndex.getHash(taxon, iList[i]))).append("\n");
                }
            }
        }

        return legend.toString();
    }

    private int[] taxonColours() {
        int[] colours = new int[iArray.length];
        for (int i = 0; i < iArray.length; i++) {
            if (iArray[i] >= 0) {
                //set alpha
                colours[i] = 0xFF000000 | SpeciesIndex.getHash(taxon, iArray[i]);
            } else {
                //white
                colours[i] = 0xFFFFFFFF;
            }
        }
        return colours;
    }
}