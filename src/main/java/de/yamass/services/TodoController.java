package de.yamass.services;

import de.yamass.model.Todo;
import de.yamass.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "data/todos")
public class TodoController {

    private final Map<Integer, Todo> todos = Collections.synchronizedMap(new LinkedHashMap<>());
    private static AtomicInteger idCounter = new AtomicInteger(1);

    private SimpMessagingTemplate template;

    @Autowired
    public TodoController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getTodos() {
        return new ArrayList<>(todos.values());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Todo addTodo(@RequestBody @Valid Todo todo, BindingResult bindingResult) {

        if (bindingResult.hasErrors())  {
            throw new ValidationException("blah", bindingResult);
        }
        for (Todo existingTodo : todos.values()) {
            if (existingTodo.getText().equals(todo.getText())) {
                throw new ValidationException("This TODO already exist!", bindingResult);
            }
        }

        int id = idCounter.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Todo updateTodo(@RequestBody Todo todo) {
        Todo persistedTodo = todos.get(todo.getId());
        if (persistedTodo == null) {
            throw new IllegalArgumentException("Todo not found:  " + todo.getId());
        } else {
            persistedTodo.setDone(todo.isDone());
            persistedTodo.setText(todo.getText());
        }
        this.template.convertAndSend("/topic/todos", persistedTodo);
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