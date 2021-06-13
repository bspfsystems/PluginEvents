/*
 * This file is part of the MCPluginEvents library for
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/**
 * Used to mark methods as ones that will handle {@link Event}s. 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    
    /**
     * Gets the {@link EventPriority} of the {@link Event}.
     * 
     * @return The {@link EventPriority} of the {@link Event}.
     */
    @NotNull
    EventPriority priority() default EventPriority.NORMAL;
    
    /**
     * Determines if this {@link EventHandler} should ignore cancelled
     * {@link Event}s.
     * 
     * @return If the {@link Event} is cancelled and this {@link EventHandler}
     *         ignores cancelled {@link Event}s, then do not execute this
     *         {@link EventHandler}. Otherwise, this {@link EventHandler} will
     *         always handle the {@link Event}.
     * @see Cancellable#setCancelled(boolean)
     */
    boolean ignoreCancelled() default false;
}
