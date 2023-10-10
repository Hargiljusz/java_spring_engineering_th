package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.iwaniuk.webapi.models.Event;
import pl.iwaniuk.webapi.models.Kind;

import java.util.Optional;

public interface KindService {
    Page<Kind> getAll(Pageable page);

    Optional<Kind> getById(String id);

    Kind create(Kind kindReq);

    Kind update(Kind kindReq);

    void delete(Kind kind);
}
