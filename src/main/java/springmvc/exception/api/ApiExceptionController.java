package springmvc.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import springmvc.exception.exception.BadRequestException;
import springmvc.exception.exception.UserException;

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

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        /**
         * id가 "bad"인 경우 IllegalArgumentException 예외 발생
         * 이 예외 발생했을 때 컨트롤러에서 처리하지 못하고 WAS까지 도달하면
         * 코드가 500으로 나감
         * 이를 400으로 처리할 예정
         */
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        return new MemberDto(id, "hello " + id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
        public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
        // 이 메서드는 Integer 타입의 data 파라미터를 받는데, 만약 잘못된 타입이 들어오면
        // 스프링이 기본적으로 제공하는 예외 처리 메커니즘에 의해 400 Bad Request가 발생한다.
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String id;
        private String name;
    }
}
