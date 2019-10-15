package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    public static final Comparator<User> COMPARE_BY_NAME = (o1, o2) -> o1.getName().compareTo(o2.getName());

    {
        List<User> userList = Arrays.asList(
                new User(counter.incrementAndGet(), "Vasya", "fr@mail.ru", "21", Role.ROLE_USER),
                new User(counter.incrementAndGet(), "Petya", "rf@mail.ru", "12", Role.ROLE_ADMIN)
        );
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id)!=null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> list = new ArrayList<>(repository.values());
        list.sort(COMPARE_BY_NAME);
        return list;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
       for(Map.Entry<Integer,User> map:repository.entrySet()){
            if(map.getValue().getEmail().equals(email))
                return map.getValue();
       }
        return null;
    }
}
