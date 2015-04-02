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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "POSEIDON-DB";
	private static final String ROUTE_TABLE_CREATE = "create table saved_routes (_id integer primary key autoincrement,"
			+ "title text,"
            + "start_location text,"
            + "end_location text,"
			+ "start_longitude text,"
			+ "start_latitude text,"
			+ "end_longitude text,"
			+ "end_latitude text,"
			+ "resource text);";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ROUTE_TABLE_CREATE);
		insertTestData(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public DbHelper(Context context) {
		super(context,DB_NAME, null, DATABASE_VERSION);
	}

	private void insertTestData(SQLiteDatabase db) {
		//db.execSQL("insert into saved_routes values (1, 'Going Home', 'Home', 'School', '-0.2254701', '51.586176', '0.5990241', '51.5211244', '')");

	}


}
