package org.dimaservers.repositories;

import org.dimaservers.models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public int save(User user) {
        int id = idGenerator.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    public User findById(int id) {
        return users.get(id);
    }

    public User findByLogin(String login) {
        return users.values().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }
}