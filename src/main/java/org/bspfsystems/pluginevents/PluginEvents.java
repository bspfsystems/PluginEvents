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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * This represents the main class where all {@link EventListener}s are
 * registered and all {@link Event}s are called.
 */
public final class PluginEvents {
    
    private static final PluginEvents INSTANCE = new PluginEvents();
    
    private final ConcurrentHashMap<Event, TreeMap<Integer, HashSet<Method>>> registeredEvents;
    private final ConcurrentHashMap<Method, Boolean> ignoreCancelled;
    private final ConcurrentHashMap<Method, Logger> loggers;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private PluginEvents() {
        this.registeredEvents = new ConcurrentHashMap<Event, TreeMap<Integer, HashSet<Method>>>();
        this.ignoreCancelled = new ConcurrentHashMap<Method, Boolean>();
        this.loggers = new ConcurrentHashMap<Method, Logger>();
    }
    
    /**
     * Gets the instance of this {@link PluginEvents}.
     * 
     * @return The instance of this {@link PluginEvents}.
     */
    @NotNull
    public static PluginEvents getInstance() {
        return PluginEvents.INSTANCE;
    }
    
    /**
     * Registers an {@link EventListener} and all of its {@link EventHandler}
     * methods.
     * 
     * @param listener The {@link EventListener} to register.
     * @param logger The {@link Logger} to use with the listener for logging
     *               messages (errors, warnings, debugging, etc).
     */
    public void registerListener(@NotNull final EventListener listener, @NotNull final Logger logger) {
    
        final HashSet<Method> methods = new HashSet<Method>(Arrays.asList(listener.getClass().getMethods()));
        methods.addAll(Arrays.asList(listener.getClass().getDeclaredMethods()));
        
        for (final Method method : methods) {
            final EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler == null) {
                continue;
            }
            
            final Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) {
                logger.log(Level.WARNING, "Method marked as an EventHandler has too " + (parameters.length == 0 ? "few" : "many") + " parameters.");
                logger.log(Level.WARNING, "Cannot use as an EventHandler.");
                logger.log(Level.WARNING, "EventListener Class: " + listener.getClass().getName());
                logger.log(Level.WARNING, "Method Name: " + method.getName());
                logger.log(Level.WARNING, "Parameters: " + Arrays.toString(parameters));
                continue;
            }
            
            final Class<?> parameter = parameters[0];
            if (!(parameter.isInstance(Event.class))) {
                logger.log(Level.WARNING, "Method marked as EventHandler does not have an Event parameter.");
                logger.log(Level.WARNING, "Cannot use as an EventHandler.");
                logger.log(Level.WARNING, "EventListener Class: " + listener.getClass().getName());
                logger.log(Level.WARNING, "Method Name: " + method.getName());
                logger.log(Level.WARNING, "Parameter Class: " + parameter.getName());
                continue;
            }
            
            final Event event = Event.class.cast(parameter);
            final TreeMap<Integer, HashSet<Method>> byPriority = this.registeredEvents.computeIfAbsent(event, newByPriority -> new TreeMap<Integer, HashSet<Method>>());
            final HashSet<Method> eventHandlers = byPriority.computeIfAbsent(eventHandler.priority().ordinal(), newEventHandlers -> new HashSet<Method>());
            eventHandlers.add(method);
            this.ignoreCancelled.put(method, eventHandler.ignoreCancelled());
            this.loggers.put(method, logger);
        }
    }
    
    /**
     * Calls the specified {@link Event}, invoking all {@link EventHandler}s
     * registered to listen for the {@link Event}.
     * 
     * @param event The {@link Event} that is called.
     */
    public void callEvent(@NotNull final Event event) {
        
        final TreeMap<Integer, HashSet<Method>> byPriority = this.registeredEvents.get(event);
        if (byPriority == null) {
            return;
        }
    
        for (final Map.Entry<Integer, HashSet<Method>> entry : byPriority.entrySet()) {
            this.handleEvent(event, entry.getValue(), entry.getKey() >= EventPriority.MONITOR.ordinal());
        }
    }
    
    /**
     * Handles the {@link Event} with the specified {@link EventHandler}
     * methods.
     * 
     * @param event The {@link Event} to handle.
     * @param methods The {@link EventHandler} {@link Method}s to process the
     *                      {@link Event}.
     * @param forceHandle If <code>true</code>, the {@link Event} will be
     *                    handled regardless of whether it has been cancelled
     *                    or not (used for the {@link EventPriority#MONITOR}
     *                    stage). Otherwise, if <code>false</code>, the handlers
     *                    will respect the cancel status.
     */
    private void handleEvent(@NotNull final Event event, @NotNull final HashSet<Method> methods, final boolean forceHandle) {
        
        for (final Method method : methods) {
            
            Logger logger = this.loggers.get(method);
            if (logger == null) {
                logger = Logger.getLogger(this.getClass().getSimpleName());
                logger.log(Level.WARNING, "No Logger defined for EventHandler method.");
                logger.log(Level.WARNING, "Default logger being used.");
                logger.log(Level.WARNING, "EventHandler Class: " + method.getClass().getName());
                logger.log(Level.WARNING, "Method Name: " + method.getName());
            }
            
            if (!forceHandle && event instanceof Cancellable && ((Cancellable) event).isCancelled() && this.ignoreCancelled.get(method) != null && this.ignoreCancelled.get(method)) {
                logger.log(Level.CONFIG, "Skipping as event is cancelled.");
                logger.log(Level.CONFIG, "Event: " + event.getClass().getSimpleName());
                logger.log(Level.CONFIG, "EventHandler Class: " + method.getClass().getName());
                logger.log(Level.CONFIG, "Method Name: " + method.getName());
                continue;
            }
            
            try {
                method.invoke(null, event);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException | ExceptionInInitializerError e) {
                logger.log(Level.WARNING, "Unable to invoke EventHandler method.");
                logger.log(Level.WARNING, e.getClass().getSimpleName() + " thrown.", e);
                logger.log(Level.WARNING, "Event: " + event.getClass().getSimpleName());
                logger.log(Level.WARNING, "EventHandler Class: " + method.getClass().getName());
                logger.log(Level.WARNING, "Method Name: " + method.getName());
            }
        }
    }
}