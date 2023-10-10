package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iwaniuk.webapi.models.Kind;
import pl.iwaniuk.webapi.repository.KindRepository;

import java.util.Optional;

@Service
public class KindServiceImpl implements KindService {
    @Autowired
    KindRepository kindRepository;

    @Override
    public Page<Kind> getAll(Pageable page) {
        return kindRepository.findAll(page);
    }

    @Override
    public Optional<Kind> getById(String id) {
        return kindRepository.findById(id);
    }

    @Override
    public Kind create(Kind kindReq) {
        return kindRepository.save(kindReq);
    }

    @Override
    public Kind update(Kind kindReq) {
        return kindRepository.save(kindReq);
    }

    @Override
    public void delete(Kind kind) {
        kindRepository.delete(kind);
    }
}
