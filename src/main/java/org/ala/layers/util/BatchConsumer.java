/**************************************************************************
 *  Copyright (C) 2010 Atlas of Living Australia
 *  All Rights Reserved.
 *
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 ***************************************************************************/
package org.ala.layers.util;

import org.ala.layers.dao.LayerIntersectDAO;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Adam
 */
public class BatchConsumer {
    static BatchConsumerThread thread = null;
    static LinkedBlockingQueue<String> waitingBatchDirs;

    synchronized public static void start(LayerIntersectDAO layerIntersectDao, String batchDir) {
        if (thread == null) {
            waitingBatchDirs = new LinkedBlockingQueue<String>();
            thread = new BatchConsumerThread(waitingBatchDirs, layerIntersectDao, batchDir);
            thread.start();
        }
    }

    public static void addBatch(String batchDir) throws InterruptedException {
        waitingBatchDirs.put(batchDir);
    }

    static void end() {
        thread.interrupt();
    }
}

class BatchConsumerThread extends Thread {
    LinkedBlockingQueue<String> waitingBatchDirs;
    LayerIntersectDAO layerIntersectDao;
    String batchDir;

    public BatchConsumerThread(LinkedBlockingQueue<String> waitingBatchDirs, LayerIntersectDAO layerIntersectDao
            , String batchDir) {
        this.waitingBatchDirs = waitingBatchDirs;
        this.layerIntersectDao = layerIntersectDao;
        this.batchDir = batchDir;
    }

    private static void writeToFile(String filename, String string, boolean append) throws IOException {
        FileWriter fw = new FileWriter(filename, append);
        fw.write(string);
        fw.close();
    }

    private static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    public void run() {
        //get jobs that may have been interrupted
        File f = new File(batchDir);
        File[] files = f.listFiles();
        java.util.Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()
                    && !(new File(files[i].getPath() + File.separator + "error.txt")).exists()
                    && !(new File(files[i].getPath() + File.separator + "finished.txt")).exists()) {
                waitingBatchDirs.add(files[i].getPath() + File.separator);
            }
        }

        boolean repeat = true;
        while (repeat) {
            String currentBatch = null;
            try {
                currentBatch = waitingBatchDirs.take();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss:SSS");
                writeToFile(currentBatch + "status.txt", "started at " + sdf.format(new Date()), true);
                writeToFile(currentBatch + "started.txt", sdf.format(new Date()), true);

                String fids = readFile(currentBatch + "fids.txt");
                String points = readFile(currentBatch + "points.txt");

                ArrayList<String> sample = layerIntersectDao.sampling(fids, points);

                FileOutputStream fos = new FileOutputStream(currentBatch + "sample.zip");
                ZipOutputStream zip = new ZipOutputStream(fos);
                zip.putNextEntry(new ZipEntry("sample.csv"));
                IntersectUtil.writeSampleToStream(fids.split(","), points.split(","), sample, zip);
                zip.close();
                fos.close();

                writeToFile(currentBatch + "status.txt", "finished at " + sdf.format(new Date()), true);
                writeToFile(currentBatch + "finished.txt", sdf.format(new Date()), true);

            } catch (InterruptedException e) {
                //thread stop request
                repeat = false;
                break;
            } catch (Exception e) {
                if (currentBatch != null) {
                    try {
                        writeToFile(currentBatch + "status.txt", "error " + e.getMessage(), true);
                        writeToFile(currentBatch + "error.txt", e.getMessage(), true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
            }

            currentBatch = null;
        }
    }
}