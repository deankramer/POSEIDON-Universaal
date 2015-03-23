/*
 * Copyright 2015 POSEIDON Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * /
 */

package org.poseidon_project.universaal.support;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.compress.utils.IOUtils;
import org.poseidon_project.universaal.activities.RouteActivity;
import org.poseidon_project.universaal.fragments.ImportRouteDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to handle Socket connections to the desktop to receive the navigation archives
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class RouteReceiver {

    private boolean mIsSecureConnection = false;
    private Context mContext;
    private int mPortNumber;
    private static final String LOGTAG = "RouteReceiver";
    private String mFileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();


    public RouteReceiver(Context c) {
        mContext = c;
        mPortNumber = 3105;
    }

    public RouteReceiver(Context c, boolean isSecure, int portNumber) {
        mContext = c;
        mIsSecureConnection = isSecure;
        mPortNumber = portNumber;
    }

    public String beginListening() {
        String fileLocation = "";
        InputStream in;
        OutputStream out;
        BufferedOutputStream bout;
        try {
                byte[] fileInfo = new byte[48];
                ServerSocket serverSocket = new ServerSocket(mPortNumber);

                Socket clientSocket = serverSocket.accept();
               /* clientSocket.getInputStream().read(fileInfo);

                String roundTrip = new String(fileInfo, "UTF-8");
                String fileName = roundTrip.split(":")[0];
                int size = Integer.parseInt(getFileSize(roundTrip.split(":")[1]));
                String sizeS = String.valueOf(size / 1048576) + "MB";
                roundTrip.indexOf("");
                Log.d(LOGTAG, "file info: " + roundTrip);
                //byte[] mybytearray = new byte[8192];*/
                byte[] mybytearray = new byte[clientSocket.getReceiveBufferSize()];
                fileLocation = mFileLocation + "/temp.zip";
                File file = new File(fileLocation);

                int dataSize;
                int readData = 0;
                //int percent = 0;
                if (file.createNewFile()) {
                    out = new FileOutputStream(file);
                    bout = new BufferedOutputStream(out);
                    while((dataSize = clientSocket.getInputStream().read(mybytearray)) >= 0) {
                        if(readData == 0) {
                            byte[] header = new byte[16];
                            for (int i = 0; i <header.length; i++) {
                                header[i] = mybytearray[i];
                            }

                            Log.e(LOGTAG, bytesToHex(header));
                        }

                        bout.write(mybytearray, 0, dataSize);
                        /*readData = readData + dataSize;
                        String readDataS = String.valueOf(readData /1048576);
                        String text = readDataS + "MB / " + sizeS;
                        int newpercent = Math.round(readData / size);
                        if (newpercent != percent) {
                            percent = newpercent;
                        }*/


                    }
                    bout.flush();
                    bout.close();
                }
                clientSocket.close();
                serverSocket.close();

        } catch (Exception e) {
            Log.e(LOGTAG, e.getStackTrace().toString());
            return "";
        }
        return fileLocation;
    }

    private String getFileSize(String str) {
        int strlength = str.length();
        int endpos = 0;
        for(int i=0; i < strlength; i++) {
            char digit = str.charAt(i);
            if(digit >= '0' && digit <= '9') {
                continue;
            }
            endpos = i;
            break;
        }
        return str.substring(0,endpos);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
