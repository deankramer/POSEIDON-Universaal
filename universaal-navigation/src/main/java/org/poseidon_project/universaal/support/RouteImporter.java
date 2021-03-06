/*Copyright 2015 POSEIDON Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.poseidon_project.universaal.support;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.directions.route.MetaParser;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * A class to import POSEIDON Navigation Archives
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public class RouteImporter {

    private String mRouteRootFolder = "/POSEIDON/routes/";
	private Context mContext;
	private NavigationalDBImpl mDataBase;
    private static final String LOGTAG = "RouteImporter";



    public boolean testUnzip(int number) {

        if (! mDataBase.getAllRoutes().isEmpty())
            return true;

        AssetManager assetManager = mContext.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try
        {
            String filename = String.valueOf(number) + ".zip";
            in = assetManager.open(filename);
            File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            out = new FileOutputStream(output);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            return importRouteArchive(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }


	public RouteImporter(Context context) {
		mContext = context;
		mDataBase = new NavigationalDBImpl(context);
	}

    public RouteImporter(Context context, String rootRouteFolder) {
        mContext = context;
        mDataBase = new NavigationalDBImpl(context);
        mRouteRootFolder = rootRouteFolder;
    }

    public boolean importRouteArchive(String file) {

        File zipfile = new File(file);
        String filename = zipfile.getName();
        String directoryName = filename.replaceFirst("[.][^.]+$", "");
        String extDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            //check if it has been moved to the root already. If not, move it.
            if ( ! file.contains(extDirectory + mRouteRootFolder)) {
               zipfile = moveFileToRootDirectory(zipfile, mRouteRootFolder);
            }

            String directory = extDirectory + mRouteRootFolder;

            if (zipfile != null) {

                //read and unzip archive
                String routeID = unzipFile(directory, zipfile);

                if (routeID.isEmpty()) {
                    return false;
                }

                //parse meta.json
                directory += routeID;
                MetaParser parser = new MetaParser(directory + "/meta.json", true);
                POSEIDONRoute route = parser.parse();

                //import into db
                mDataBase.insertRoute(route);

                //delete archive file
                zipfile.delete();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static File moveFileToRootDirectory(File zipfile, String routeRootFolder) {

        InputStream in = null;
        OutputStream out = null;
        try
        {
            File target = new File (Environment.getExternalStorageDirectory().getAbsolutePath()
                    + routeRootFolder + zipfile.getName());
            ensureTargetDirectoryExists(target.getParentFile());

            in = new FileInputStream(zipfile);
            out = new FileOutputStream(target);

            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) >= 0)
            {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            zipfile.delete();

            return target;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public static String unzipFile(String path, File zipfile)
            throws FileNotFoundException, IOException {

        FileInputStream fin = new FileInputStream(zipfile);
        ZipArchiveInputStream zin = new ZipArchiveInputStream(fin);

        String routeID = "";

		try {
			ZipArchiveEntry entry;
			while ((entry = zin.getNextZipEntry()) != null) {

                String filename = entry.getName();
                if (entry.isDirectory()) {
                    File directory = new File(path + filename);
                    directory.mkdirs();
                    continue;
                }

                File parent = new File(path + filename).getParentFile();
                ensureTargetDirectoryExists(parent);

                routeID = parent.getName();

				int size;
				byte[] buffer = new byte[8192];

				FileOutputStream fos = new FileOutputStream(path + "/" + filename);
				BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

                while ((size = zin.read(buffer)) >= 0) {
					bos.write(buffer, 0, size);
				}

				bos.flush();
				bos.close();

			}
		} finally {
			zin.close();
			fin.close();
		}

		return routeID;
	}

    private static void ensureTargetDirectoryExists(File aTargetDir){
        if(!aTargetDir.exists()){
            aTargetDir.mkdirs();
        }
    }

}
