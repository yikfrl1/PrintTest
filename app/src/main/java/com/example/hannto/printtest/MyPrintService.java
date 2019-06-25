package com.example.hannto.printtest;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PrintJobInfo;
import android.printservice.PrintDocument;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyPrintService extends PrintService {

    private static final String TAG = "MyPrintService";


    @Nullable
    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        Log.d(TAG, "onCreatePrinterDiscoverySession()");
        return new MyPrintDiscoverySession(this);
    }

    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {
        Log.d(TAG, "onRequestCancelPrintJob()");
        printJob.block("");
        printJob.cancel();
    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {
        int contentType = printJob.getDocument().getInfo().getContentType();
        printJob.getInfo().getAttributes().getMediaSize();
        Log.d(TAG, "onPrintJobQueued() contentType = "+contentType);
        PrintJobInfo printJobInfo = printJob.getInfo();
        String Label = printJobInfo.getLabel();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onPrintJobQueued() Label = " + Label);
        PrintDocument printDocument = printJob.getDocument();
//        if(printJob.isQueued()){
//            Log.e(TAG, "onPrintJobQueued() printJob.isQueued()");
//            return;
//        }
        printJob.start();

        String filename = "docu.pdf";
        if(Label.contains("img")){
            filename = "image.pdf";
        }else if(Label.contains("Document")){
            filename = "docu.pdf";
        }
//        File outFile = new File(this.getFilesDir(), filename);
        File outFile = new File(Environment.getExternalStorageDirectory().getPath(), filename);
//        outFile.delete();
        FileInputStream file = new ParcelFileDescriptor.AutoCloseInputStream(printDocument.getData());

        byte[] bbuf = new byte[1024];

        int hasRead = 0;

        try{
            FileOutputStream outputStream = new FileOutputStream(outFile);
            while((hasRead = file.read(bbuf)) > 0){
                outputStream.write(bbuf);
            }
            outputStream.close();
        }catch (IOException e){
            Log.e(TAG, "onPrintJobQueued() error finish");
            e.printStackTrace();
            printJob.complete();
        }finally {
            try{
                file.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printJob.complete();
        Log.d(TAG, "onPrintJobQueued() finish");
    }
}
