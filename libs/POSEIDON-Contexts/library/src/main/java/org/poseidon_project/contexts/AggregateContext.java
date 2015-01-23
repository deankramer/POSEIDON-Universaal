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
package org.poseidon_project.contexts;

import java.util.Map;

public class AggregateContext implements ContextObserver, ContextReceiver{


	private ContextReceiver mReceiver;

	@Override
	public void newContextValue(String name, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newContextValue(String name, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newContextValue(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newContextValue(String name, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newContextValue(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newContextValues(Map<String, String> values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newUIEvent(UIEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean start() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setContextReceiver(ContextReceiver cr) {
		mReceiver = cr;

	}

	@Override
	public String getName() {
		return "AggregateContext";
	}

}
