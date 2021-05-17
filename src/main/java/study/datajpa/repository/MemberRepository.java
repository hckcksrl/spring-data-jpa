package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * 핵심 비즈니스로직 리포지토리와 화면에 맞춘 쿼리를 사용하는 사용자정의 리포지토리를
 * 서로 다른 리포지토리로 만들어서 사용하는것이 좋다.
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

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

    /**
     * 복잡한 sql을 사용할 경우 카운트 쿼리를 분리하는것이 좋다. (실무에서 중요함)
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * count 가 없다. limit + 1개를 조회해서 다음 페이지 여부를 확인
     */
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    /**
     * Spring Data JPA는 @Modifying이 있어야 excuteUpdate()같은 변경 메소드를 실행한다.
     * 해당 어노테이션이 없을경우 getSingleResult, getResultList 들을 실행한다.
     * clearAutomatically = true 넣어주면 해당 쿼리를 적용한 이후 영속성 컨텍스트가 클리어된다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * @QueryHints를 사용하면 쿼리를 사용해 데이터를 꺼내올때 스냅샷을 만들지 않는다.
     * 스냅샷을 만들지 않아서 변경감지가 일어나지 않음
     * => setUsername같이 데이터 수정을 해도 업데이트 쿼리가 발생하지 않음
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
