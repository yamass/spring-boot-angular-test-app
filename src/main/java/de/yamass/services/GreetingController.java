package de.yamass.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.yamass.model.Todo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = "data/todos")
public class GreetingController {

    private final Map<Integer, Todo> todos = Collections.synchronizedMap(new LinkedHashMap<>());
    private static AtomicInteger idCounter = new AtomicInteger(1);

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getTodos() {
        return new ArrayList<>(todos.values());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Todo addTodo(@RequestBody Todo todo) {
        int id = idCounter.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Todo updateTodo(@RequestBody Todo todo) {
        Todo persistedTodo = todos.get(todo.getId());
        if (persistedTodo == null) {
            // TODO throw error
        } else {
            persistedTodo.setDone(todo.isDone());
            persistedTodo.setText(todo.getText());
        }
        return persistedTodo;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public boolean deleteTodo(@PathVariable("id") int id) {
        Todo removedTodo = todos.remove(id);
        return removedTodo != null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deleteDone")
    public List<Todo> deleteDoneTodos() {
        for (Iterator<Map.Entry<Integer, Todo>> iterator = todos.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Integer, Todo> entry = iterator.next();
            if (entry.getValue().isDone()) {
                iterator.remove();
            }
        }
        return getTodos();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Todo getTodo(@PathVariable("id") int id) {
        return todos.get(id);
    }

}