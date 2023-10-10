package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iwaniuk.webapi.exceptions.KindNotFoundException;
import pl.iwaniuk.webapi.exceptions.MyEventNotFoundException;
import pl.iwaniuk.webapi.models.Event;
import pl.iwaniuk.webapi.models.Kind;
import pl.iwaniuk.webapi.services.EventService;
import pl.iwaniuk.webapi.services.KindService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/kinds")
public class KindController {

    @Autowired
    private KindService kindService;

    @GetMapping("/")
    public Page<Kind> getKinds(@RequestParam(defaultValue = "0") int site,
                                @RequestParam(defaultValue = "10") int size){

        Pageable page = PageRequest.of(site,size, Sort.by("name"));

        return kindService.getAll(page);
    }

    @GetMapping("/{id}")
    public Kind getKindById(@PathVariable("id") String id) throws KindNotFoundException {
        Kind kind = kindService.getById(id).orElseThrow(KindNotFoundException::new);
        return kind;
    }

    @PostMapping("/")
    public Kind addKind(@Valid @RequestBody Kind kindReq) {
        Kind kind = kindService.create(kindReq);
        return kind;
    }

    @PutMapping("/{id}")
    public Kind updateKindById(@Valid @RequestBody Kind kindReq,@PathVariable("id") String id) throws KindNotFoundException {
        Kind kind = kindService.getById(id).orElseThrow(KindNotFoundException::new);
        kindReq.setId(kind.getId());

        return kindService.update(kindReq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteKindById(@PathVariable("id") String id) throws KindNotFoundException {
        Kind kind = kindService.getById(id).orElseThrow(KindNotFoundException::new);
        kindService.delete(kind);

        return noContent().build();
    }
}
