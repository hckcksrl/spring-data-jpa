package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

/**
 * @Repository 어노테이션을 사용하지 않아도 된다.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
}
