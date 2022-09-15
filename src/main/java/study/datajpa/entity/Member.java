package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 파라미터가 없는 기본 생성자를 생성해줌(access는 protected)
@ToString(of = {"id", "username", "age"})   // toString() 메소드를 자동으로 생성!(단,! 연관관계 있는 필드는 넣어주지 않는 것이 좋다! 무한루프에 빠질 수 있음!)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)  // *ToOne 연관관계는 전부다 (fetch = FetchType.LAZY)을 주어야 한다.
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    // 서로 연관관계를 생성하는 메소드를 생성!
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
