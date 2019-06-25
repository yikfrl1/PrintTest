package com.example.hannto.printtest;

import android.print.PrintAttributes;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrinterDiscoverySession;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.hannto.printtest.bean.ConnectRunnable;
import com.example.hannto.printtest.bean.ScanDeviceTool;

import java.util.ArrayList;
import java.util.List;

public class MyPrintDiscoverySession extends PrinterDiscoverySession {
    private static final String TAG = "MyPrintDiscoverySession";
    private final MyPrintService myPrintService;
    private ScanDeviceTool mScanDeviceTool;

    public MyPrintDiscoverySession(MyPrintService myPrintService) {
        Log.d(TAG, "MyPrintDiscoverySession()");
        this.myPrintService = myPrintService;
        mScanDeviceTool = new ScanDeviceTool();
    }

    @Override
    public void onStartPrinterDiscovery(@NonNull List<PrinterId> priorityList) {
        Log.d(TAG, "onStartPrinterDiscovery()");

//        mScanDeviceTool.scan();
//        new ConnectRunnable().run();

        List<PrinterInfo> printers = this.getPrinters();
        String name = "printer1";
        PrinterId printerId = myPrintService.generatePrinterId(name);

        Log.d(TAG, "onStartPrinterDiscovery() printerId.toString() = "+printerId.toString());


        PrinterInfo myprinter = new PrinterInfo
                .Builder(printerId, name, PrinterInfo.STATUS_BUSY)
                .build();
        printers.add(myprinter);
        addPrinters(printers);
    }

    @Override
    public void onStopPrinterDiscovery() {
        Log.d(TAG, "onStopPrinterDiscovery()");
    }

    @Override
    public void onValidatePrinters(@NonNull List<PrinterId> printerIds) {
        Log.d(TAG, "onValidatePrinters()");
    }

    @Override
    public void onStartPrinterStateTracking(@NonNull PrinterId printerId) {
        Log.d(TAG, "onStartPrinterStateTracking()");
        PrinterInfo printer = findPrinterInfo(printerId);
        if(printer != null){
            PrinterCapabilitiesInfo capabilities =
                    new PrinterCapabilitiesInfo.Builder(printerId)
                        .setMinMargins(new PrintAttributes.Margins(200,200,200,200))
                        .addMediaSize(PrintAttributes.MediaSize.ISO_A4, true)
                        .addMediaSize(new PrintAttributes.MediaSize("4*6","6寸照片",4000,6000), false)
//                        .addMediaSize(PrintAttributes.MediaSize.NA_INDEX_4X6, false)
//                        .addResolution(new PrintAttributes.Resolution("R1","200x200",200,200), false)
                        .addResolution(new PrintAttributes.Resolution("R2", "300x300", 300, 300), true)
                        .setColorModes(PrintAttributes.COLOR_MODE_COLOR | PrintAttributes.COLOR_MODE_MONOCHROME,
                                 PrintAttributes.COLOR_MODE_MONOCHROME)
                        .build();
            printer = new PrinterInfo.Builder(printer)
                    .setCapabilities(capabilities)
                    .setStatus(PrinterInfo.STATUS_BUSY)
                    .setDescription("fake printer1...")
                    .build();
            List<PrinterInfo> printers = new ArrayList<PrinterInfo>();

            printers.add(printer);
            addPrinters(printers);

        }
    }

    @Override
    public void onStopPrinterStateTracking(@NonNull PrinterId printerId) {
        Log.d(TAG, "onStopPrinterStateTracking()");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
    }



    private PrinterInfo findPrinterInfo(PrinterId printerId){
        List<PrinterInfo> printers = getPrinters();
        final int printerCount = getPrinters().size();
        for (int i = 0; i < printerCount; i++) {
            PrinterInfo printer = printers.get(i);
            if (printer.getId().equals(printerId)){
                return printer;
            }
        }

        return null;
    }
}
