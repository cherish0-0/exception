package springmvc.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * API 예외 처리
 * 단순 회원 조회, id가 "ex"인 경우 예외 발생
 */
@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        /**
         * id가 "bad"인 경우 IllegalArgumentException 예외 발생
         * 이 예외 발생했을 때 컨트롤러에서 처리하지 못하고 WAS까지 도달하면
         * 코드가 500으로 나감
         * 이를 400으로 처리할 예정 TODO
         */
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String id;
        private String name;
    }
}
