package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     *     이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다.
     *     그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.
     *     이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.
     *     자칫하면 메소드 명이 길어질 수 있다.
     *     기능이 간단할 때 사용하면 좋다!
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") //없애도 잘 실행 된다! 관례상 네임드쿼리를 먼저 확인하고 다음으로는 메소드를 확인함!
    List<Member> findByUsername(@Param("username") String username);
    // 네임드 쿼리는 실무에서는 거의 사용하지 않는다!
    // *** 단! 네임드 쿼리의 가장큰 장점은 애플리케이션 로딩 시점에 파싱을 해서 오류가 있으면 그 문법 오류를 알려준다! ***
    // 네임드 쿼리의 장점을 가지는 리포지토리 메소드에 쿼리 정의하는 방법이 막강함!

    /**
     *  리포지토리 메소드에 쿼리 정의
     *  JPQL을 작성하고 메소드명을 간략하게 할 수 있다.
     *  정적 쿼리 내용이 복잡해지면, 정적쿼리를 정의하고 메소드 명을 간략하게 하자!
     *  동적 쿼리는 QueryDsl로 해결하자!
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

}
