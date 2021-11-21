package de.minebench.tresor;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2020 Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TresorUtils {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Create a future and complete it asynchronously using our own executor service
     * @param <T>       The return type of the callable
     * @param callable  The callable
     * @return The future which will be completed
     */
    public static <T> CompletableFuture<T> asyncFuture(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                future.complete(callable.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    /**
     * Create a future and instantly complete it with the callable (or exceptionally)
     * @param callable  The callable
     * @param <T>       The return type of the callable
     * @return An already completed completable future
     */
    public static <T> CompletableFuture<T> future(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            future.complete(callable.call());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }
}
