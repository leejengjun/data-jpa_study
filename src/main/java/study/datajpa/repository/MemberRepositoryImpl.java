package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    /**
     * 사용자 정의 구현 클래스
     * 규칙: 리포지토리 인터페이스 이름 + Impl 왠만하면 관례를 지키는 것을 권장
     * 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
     * 만약 규모가 커지게 됨에 따라서 사용자 정의 리포지토리에 정의된 복잡한 쿼리를 수행하는 메소드들이 많아진다면 리포지토리를 분리해서 구성하는 것도 좋을 것 같다.
     * 핵심 비즈니스 리포지토리와 단순한 기능들로 구성된 리포지토리의 클래스를 쪼개는 것
     *
     * 애플리케이션의 규모가 커짐에 따른 고민?
     * - 커멘드와 쿼리를 분리 (https://hardlearner.tistory.com/383 사이트 참고 )
     * - 핵심 비즈니스 로직과 단순 로직을 분리
     * - 라이프 사이클에 따라서 뭘 변경해야하는지가 달라지는 것?
     * - 다각적으로 고민해서 클래스를 쪼개는 것을 권장.
     */
    
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
