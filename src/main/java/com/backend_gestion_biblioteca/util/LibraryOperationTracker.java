package com.backend_gestion_biblioteca.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Component
public class LibraryOperationTracker {

    private final LinkedList<String> operations = new LinkedList<>();
    private final Deque<String> recentErrors = new ArrayDeque<>();

    public void recordOperation(String description) {
        operations.addFirst(LocalDateTime.now() + " - " + description);
        if (operations.size() > 50) {
            operations.removeLast();
        }
    }

    public void recordError(String error) {
        if (recentErrors.size() >= 10) {
            recentErrors.removeLast();
        }
        recentErrors.addFirst(LocalDateTime.now() + " - " + error);
    }

    public List<String> getRecentOperations() {
        return new LinkedList<>(operations);
    }

    public Deque<String> getRecentErrors() {
        return new ArrayDeque<>(recentErrors);
    }
}
