package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 실무에서 많이씀
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int agr);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /**
     * 컬렉션, 없을경우 빈컬렉션 반환
     * 일반 JPA의경우 없을경우 NoResultException이 반환되지만 Spring Data Jpa는 빈 컬렉션 반환
     */
    List<Member> findListByUsername(String username);

    /**
     * 단건, 없을경우 null 반환
     */
    Member findMemberByUsername(String username);

    /**
     * 단건 Optional타입
     * 단건이아니라 여러건이 반환될 경우 예외가 발생한다.
     */
    Optional<Member> findOptionalByUsername(String username);
}
