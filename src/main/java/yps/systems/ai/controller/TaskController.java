package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Task;
import yps.systems.ai.repository.ITaskRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
@RestController
@RequestMapping("/taskService")
public class TaskController {

    private final ITaskRepository taskRepository;

    @Autowired
    public TaskController(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/{taskElementId}")
    ResponseEntity<Task> getByElementId(@PathVariable String taskElementId) {
        Optional<Task> optionalTask = taskRepository.findById(taskElementId);
        return optionalTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<String> save(@RequestBody Task task) {
        Task saveTask = taskRepository.save(task);
        return new ResponseEntity<>("Task saved with ID: " + saveTask.getElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{elementId}")
    ResponseEntity<String> delete(@PathVariable String elementId) {
        Optional<Task> optionalTask = taskRepository.findById(elementId);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(elementId);
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not founded", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{elementId}")
    ResponseEntity<String> update(@PathVariable String elementId, @RequestBody Task task) {
        Optional<Task> optionalTask = taskRepository.findById(elementId);
        if (optionalTask.isPresent()) {
            task.setElementId(optionalTask.get().getElementId());
            taskRepository.save(task);
            return new ResponseEntity<>("Task updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not founded", HttpStatus.NOT_FOUND);
    }

}
