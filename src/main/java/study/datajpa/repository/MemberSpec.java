package study.datajpa.repository;

import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.*;

/**
 * 실무에서 JPA Criteria를 쓰기에는 너무 번잡하다. 쿼리문이 복잡해진다면?
 * 실무에서는 JPA Criteria를 거의 안쓴다! 대신에 'QueryDSL'을 사용하자.
 */
public class MemberSpec {

    public static Specification<Member> teamName(final String teamName) {
        return (root, query, criteriaBuilder) -> {

            if (StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 조인
            return criteriaBuilder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("username"), username);
    }
}
