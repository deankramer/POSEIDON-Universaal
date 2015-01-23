/*Copyright 2014 POSEIDON Project

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
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.directions.route.MetaParser;

/**
 * A class to import POSEIDON Navigation Archives
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public class RouteImporter {

    private static final String routeRootFolder = "/POSEIDON/routes/";
	private Context mContext;
	private NavigationalDBImpl mDataBase;



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

    public boolean importRouteArchive(String file) {

        File zipfile = new File(file);
        String filename = zipfile.getName();
        String directoryName = filename.replaceFirst("[.][^.]+$", "");
        String extDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            //check if it has been moved to the root already. If not, move it.
            if ( ! file.contains(extDirectory + routeRootFolder)) {
               zipfile = moveFileToRootDirectory(zipfile);
            }

            String directory = extDirectory + routeRootFolder;

            if (zipfile != null) {

                //read and unzip archive
                unzipFile(directory, zipfile);

                //parse meta.json
                directory += directoryName;
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

    private static File moveFileToRootDirectory(File zipfile) {

        InputStream in = null;
        OutputStream out = null;
        try
        {
            File target = new File (Environment.getExternalStorageDirectory().getAbsolutePath()
                    + routeRootFolder + zipfile.getName());
            ensureTargetDirectoryExists(target.getParentFile());

            in = new FileInputStream(zipfile);
            out = new FileOutputStream(target);

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

            zipfile.delete();

            return target;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean unzipFile(String path, File zipfile)
            throws FileNotFoundException, IOException {

        FileInputStream fin = new FileInputStream(zipfile);
		ZipInputStream zin = new ZipInputStream(fin);

		try {
			ZipEntry entry;

			while ((entry = zin.getNextEntry()) != null) {

                String filename = entry.getName();
                if (entry.isDirectory()) {
                    File directory = new File(path + filename);
                    directory.mkdirs();
                    continue;
                }

                ensureTargetDirectoryExists(new File(path + filename).getParentFile());

				int size;
				byte[] buffer = new byte[1024];

				FileOutputStream fos = new FileOutputStream(path + "/" + filename);
				BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

				while ((size = zin.read(buffer, 0, buffer.length)) != -1) {
					bos.write(buffer, 0, size);
				}

				bos.flush();
				bos.close();

			}
		} finally {
			zin.close();
			fin.close();
		}

		return true;
	}

    private static void ensureTargetDirectoryExists(File aTargetDir){
        if(!aTargetDir.exists()){
            aTargetDir.mkdirs();
        }
    }

}
