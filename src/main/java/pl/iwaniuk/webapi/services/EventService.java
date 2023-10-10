package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.Event;

import java.util.Optional;

public interface EventService {
    Page<Event> getAll(Pageable page);

    Optional<Event> getById(String id);

    Event create(Event eventReq, MultipartFile multipartFile);

    Event update(Event eventReq, Event event, MultipartFile multipartFile);

    void delete(Event event);
}
