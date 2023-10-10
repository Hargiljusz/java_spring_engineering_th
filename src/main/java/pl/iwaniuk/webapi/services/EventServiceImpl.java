package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.Event;
import pl.iwaniuk.webapi.repository.EventRepository;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    FileService fileService;

    @Override
    public Page<Event> getAll(Pageable page) {
        return eventRepository.findAll(page);
    }

    @Override
    public Optional<Event> getById(String id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event create(Event eventReq, MultipartFile multipartFile) {
        String src = fileService.saveFile(multipartFile);
        src = "http://localhost:8080/api/files/"+src;
        eventReq.setImg_src(src);
        return eventRepository.save(eventReq);
    }

    @Override
    public Event update(Event eventReq, Event event, MultipartFile multipartFile) {
        eventReq.setId(event.getId());
        eventReq.setAuthorId(event.getAuthorId());
        eventReq.setCreationDate(event.getCreationDate());
        eventReq.setImg_src(event.getImg_src());

        if(multipartFile != null) {
            fileService.deleteFile(event.getImg_src().split("/")[5]);

            String src = fileService.saveFile(multipartFile);
            src = "http://localhost:8080/api/files/"+src;
            eventReq.setImg_src(src);
        }


        return eventRepository.save(eventReq);
    }

    @Override
    public void delete(Event event) {
        fileService.deleteFile(event.getImg_src().split("/")[5]);
        eventRepository.delete(event);
    }
}
