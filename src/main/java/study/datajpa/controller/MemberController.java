package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

/**
 * Web 확장 - 도메인 클래스 컨버터
 * HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩
 * HTTP 요청은 회원 id 를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
 *
 * 도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾음
 * > 주의: 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 '단순 조회용'으로만 사용해야 한다.
 * (트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.)
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

//    @GetMapping("/members")
//    // 페이징 관련 개별 설정(글로벌 설정보다 우선순위가 높음.)
//    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        return page;
//    }

    @GetMapping("/members")
    // 페이징 관련 개별 설정(글로벌 설정보다 우선순위가 높음.)
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member));
                .map(MemberDto::new); //람다로 길이를 줄임.
    }

    @PostConstruct
    public void init() {
//        memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) { // 스프링 데이터 페이징 정렬기능 실습을 위한 테스트 데이터 넣기
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
