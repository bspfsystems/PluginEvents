/* 
 * This file is part of the PluginEvents library for
 * plugins that do not depend or do not want to depend
 * on the Bukkit API or BungeeCord API Events.
 * 
 * Copyright 2021-2022 BSPF Systems, LLC
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
 * Represents the priority that the {@link EventHandler} operates with on the
 * {@link Event}.
 * <p>
 * The {@link EventHandler EventHandlers} will run in order from the lowest
 * {@link EventPriority} to the highest one. The order is as specified:
 * <ol>
 * <li>LOWEST</li>
 * <li>LOWER</li>
 * <li>LOW</li>
 * <li>NORMAL</li>
 * <li>HIGH</li>
 * <li>HIGHER</li>
 * <li>HIGHEST</li>
 * <li>MONITOR</li>
 * </ol>
 * <p>
 * Please note that {@link EventPriority#MONITOR} is meant for monitoring when
 * an event has happened, usually after all other handling has occurred. While
 * it is possible to set an {@link Event} to cancelled, doing so in this phase
 * of event handling will have no effect. The only time a
 * {@link EventPriority#MONITOR} {@link EventHandler} will be skipped is if the
 * {@link Event} was cancelled before the Monitor handling phase started.
 */
public enum EventPriority {
    
    /**
     * The lowest priority. All other {@link EventHandler EventHandlers} will
     * have a say in what happens with the {@link Event}.
     */
    LOWEST,
    
    /**
     * Very low priority. Most other {@link EventHandler EventHandlers} should
     * be able to customize the outcome of the {@link Event}.
     */
    LOWER,
    
    /**
     * The {@link EventHandler} has a low importance on the outcome of the
     * {@link Event}.
     */
    LOW,
    
    /**
     * The {@link EventHandler} has a normal importance on the outcome of the
     * {@link Event}. This should be one of the most commonly-used
     * {@link EventPriority EventPriorities}.
     */
    NORMAL,
    
    /**
     * The {@link EventHandler} has a high importance on the outcome of the
     * {@link Event}.
     */
    HIGH,
    
    /**
     * Very high priority. Almost no other {@link EventHandler EventHandlers}
     * should be able to change the outcome of the {@link Event}.
     */
    HIGHER,
    
    /**
     * The highest priority. No other {@link EventHandler EventHandlers} will
     * have a say in what happens with the {@link Event}.
     */
    HIGHEST,
    
    /**
     * Used for monitoring when an {@link Event} is called. This can no longer
     * cancel {@link Cancellable} {@link Event Events}, and should never modify
     * the outcome of an {@link Event}. This should be one of the most
     * commonly-used {@link EventPriority EventPriorities}.
     */
    MONITOR;
}
