/*
 * This file is part of the PluginEvents library for
 * plugins that do not depend or do not want to depend
 * on the Bukkit API or BungeeCord API Events.
 *
 * Copyright 2021 BSPF Systems, LLC
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
 */

package org.bspfsystems.pluginevents;

/**
 * Implemented by some {@link Event}s if they can be cancelled, preventing
 * further {@link EventHandler}s from processing the {@link Event}.
 * <p>
 * The {@link Event} can always be un-cancelled by an {@link EventHandler} that
 * does not care whether the {@link Event} is cancelled or not.
 */
public interface Cancellable {
    
    /**
     * Gets the cancel status of the {@link Event}.
     * 
     * @return <code>true</code> if the {@link Event} has been cancelled,
     *         <code>false</code> otherwise.
     */
    boolean isCancelled();
    
    /**
     * Sets the cancel status of the {@link Event}.
     * <p>
     * Only {@link EventHandler}s that have <code>ignoreCancelled</code> set to
     * <code>true</code> will not process the {@link Event}. All others will
     * process as if the {@link Event} was not cancelled.
     * 
     * @param cancelled <code>true</code> if the {@link Event} is to be
     *                  cancelled, <code>false</code> otherwise.
     */
    void setCancelled(boolean cancelled);
}
