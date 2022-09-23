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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, MemberRepositoryCustom_2, JpaSpecificationExecutor<Member> {

    /**
     *     이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다.
     *     그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.
     *     이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.
     *     자칫하면 메소드 명이 길어질 수 있다.
     *     기능이 간단할 때 사용하면 좋다!
     *     JpaRepository 인터페이스: 공통 CRUD 제공
     *     제네릭은 <엔티티 타입, 식별자 타입> 설정
     *
     *      주요 메서드
     *     save(S) : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.
     *     delete(T) : 엔티티 하나를 삭제한다. 내부에서 EntityManager.remove() 호출
     *     findById(ID) : 엔티티 하나를 조회한다. 내부에서 EntityManager.find() 호출
     *     getOne(ID) : 엔티티를 프록시로 조회한다. 내부에서 EntityManager.getReference() 호출
     *     findAll(…) : 모든 엔티티를 조회한다. 정렬( Sort )이나 페이징( Pageable ) 조건을 파라미터로 제공할 수 있다.
     */
    
    // 메소드 이름으로 쿼리 생성 findByUsernameAndAgeGreaterThan
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    List<Member> findTop3HelloBy();

    /**
     * 쿼리 메소드 기능
     * 메소드 이름으로 쿼리 생성
     * NamedQuery
     * @Query - 리파지토리 메소드에 쿼리 정의
     * 파라미터 바인딩
     * 반환 타입
     * 페이징과 정렬
     * 벌크성 수정 쿼리
     * @EntityGraph
     */

    /**
     * 쿼리 메소드 기능 3가지
     *
     * 메소드 이름으로 쿼리 생성
     * 메소드 이름으로 JPA NamedQuery 호출
     * @Query 어노테이션을 사용해서 리파지토리 인터페이스에 쿼리 직접 정의
     */

//    @Query(name = "Member.findByUsername") //없애도 잘 실행 된다! 관례상 네임드쿼리를 먼저 확인하고 다음으로는 메소드를 확인함!
    List<Member> findByUsername(@Param("username") String username);
    /**
     * 네임드 쿼리는 실무에서는 거의 사용하지 않는다!
     * *** 단! 네임드 쿼리의 가장큰 장점은 애플리케이션 로딩 시점에 파싱을 해서 오류가 있으면 그 문법 오류를 알려준다! ***
     * 네임드 쿼리의 장점을 가지는 '리포지토리 메소드에 쿼리 정의하는 방법'이 막강함!
     */

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

    List<Member> findListByUsername(String username);  //컬렉션
    Member findMemberByUsername(String username);  //단건
    Optional<Member> findOptionalByUsername(String username);  //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")  //countQuery를 분리할 수도 있다. 전체 데이터 개수 조회에서 성능 이슈가 있을 경우에!
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

    /**
     * @Modifying 에 대해서
     * @Query Annotation으로 작성 된 변경, 삭제 쿼리 메서드를 사용할 때 필요
     * 데이터에 변경이 일어나는 INSERT, UPDATE, DELETE, DDL 에서 사용, 주로 '벌크 연산 시'에 사용
     * @Modifying(clearAutomatically = true) -> 영속성 컨텍스트를 초기화 해주는 어노미테이션 옵션
     */
    @Modifying 
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) // 연관된 엔티티들을 SQL 한번에 조회하는 방법 페치 조인 JPQL 없이!!
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = ("team"))
//    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all") // 네임드 쿼리에 엔티티그래프 적용하기
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) //변경감지 체크를 안 함
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

//    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

//    List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);

    /**
     * 동적 Projections
     */
    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
