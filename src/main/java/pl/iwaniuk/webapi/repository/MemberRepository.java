package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.edges.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends ArangoRepository<Member,String> {

    Page<Member> findByUser_Id(String id, Pageable pageable);

    Page<Member> findByGroup_IdAndIsAccept(String id,boolean isAccept, Pageable pageable);

    Optional<Member> findByGroup_IdAndUser_Id(String groupId,String userId);


    boolean existsByGroup_IdAndUser_Id(String group_id,String user_id);

    void deleteAllByGroup_Id(String groupId);
}
