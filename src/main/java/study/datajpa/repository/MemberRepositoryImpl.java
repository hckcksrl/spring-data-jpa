package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 클래스 네임은 MemberRepository + Impl 이 규칙이다.
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
