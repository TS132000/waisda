/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.model;

import java.util.Date;

public class Cache<T> implements Value<T> {

	private Value<T> content;
	private long maxAgeMs;

	private T cachedValue;
	private long lastUpdate;

	public Cache(Value<T> content, long maxAgeMs) {
		this.content = content;
		this.maxAgeMs = maxAgeMs;
	}

	private synchronized long getCurrentAge() {
		return new Date().getTime() - lastUpdate;
	}

	public synchronized T get() {
		if (getCurrentAge() > maxAgeMs) {
			cachedValue = content.get();
			lastUpdate = new Date().getTime();
		}
		return cachedValue;
	}

	public synchronized void invalidate() {
		lastUpdate = 0;
	}

}
