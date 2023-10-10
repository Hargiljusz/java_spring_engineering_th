package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.exceptions.MyEventNotFoundException;
import pl.iwaniuk.webapi.models.Event;
import pl.iwaniuk.webapi.services.EventService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public Page<Event> getEvents(@RequestParam(defaultValue = "0") int site,
                                 @RequestParam(defaultValue = "10") int size){
        Pageable page = PageRequest.of(site,size,Sort.by("creationDate").descending());
        return eventService.getAll(page);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") String id) throws MyEventNotFoundException {
        Event event = eventService.getById(id).orElseThrow(MyEventNotFoundException::new);
        return event;
    }

    @PostMapping("/")
    public Event addEvent(@Valid @RequestPart("event") Event eventReq,
                          @RequestPart("img")MultipartFile multipartFile) {
        Event event = eventService.create(eventReq,multipartFile);
        return event;
    }

    @PutMapping("/{id}")
    public Event updateEventById(@Valid @RequestPart("event") Event eventReq,
                                 @PathVariable("id") String id,
                                 @RequestPart(value = "img", required = false)MultipartFile multipartFile) throws MyEventNotFoundException {
        Event event = eventService.getById(id).orElseThrow(MyEventNotFoundException::new);
        return eventService.update(eventReq,event,multipartFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEventById(@PathVariable("id") String id) throws MyEventNotFoundException {
        Event event = eventService.getById(id).orElseThrow(MyEventNotFoundException::new);
        eventService.delete(event);
        return noContent().build();
    }
}
