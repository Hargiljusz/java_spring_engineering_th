package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Group;

import java.util.List;

@Repository
public interface GroupeRepository extends ArangoRepository<Group,String> {

    Page<Group> findByKind_Id(Pageable pageable, String kindID);
    Page<Group> findAllByOwnerId(Pageable pageable,String ownerId);
    Page<Group> findAllByOwnerIdAndKind_Id(Pageable pageable,String ownerId,String KindID);

    Page<Group>  findByMembersId(String id,Pageable pageable);
  //  Page<Group>  findByMembersIdAndMem(String id,boolean isAccept,Pageable pageable);

    List<Group> findByNameIsContainingIgnoreCaseOrKind_NameIsContainingIgnoreCase(String name, String kName);

    List<Group> findByOwnerId(String id);


}
