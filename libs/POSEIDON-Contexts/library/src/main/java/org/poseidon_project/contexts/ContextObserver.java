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

/**
 * The interface that all context observing components must implement.
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public interface ContextObserver {

	/*
	 * Used to start collecting and observing context data.
	 * @return   A boolean to indicate the context component started correctly.
	 */
	public boolean start();

	/*
	 * Used to pause collection of context data.
	 * @return   A boolean to indicate the context component paused correctly.
	 */
	public boolean pause();

	/*
	 * Used to resume collection of context data.
	 * @return   A boolean to indicate the context component paused correctly.
	 */
	public boolean resume();


	/*
	 * Used to stop collection of context data.
	 * @return   A boolean to indicate the context component paused correctly.
	 */
	public boolean stop();


	/*
	 * Sets the object required to receive the context data.
	 */
	public void setContextReceiver(ContextReceiver cr);

	/*
	 * Gets the name of the context component
	 * @return   A String containing the context name.
	 */
	public String getName();

}
