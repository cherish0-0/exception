package springmvc.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.exception.exception.UserException;
import springmvc.exception.exhandler.ErrorResult;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * @ExceptionHandler: 특정 예외가 발생했을 때 ExceptionHandlerExceptionResolver에 의해 해당 메서드가 호출됨
     * - 예외를 생략하면 메서드 파라미터의 예외가 지정됨
     * - 예외가 발생하면 해당 메서드가 호출되어 예외를 처리함
     * - 이 메서드는 컨트롤러 내에서만 동작함
     * - WAS까지 도달하지 않음
     * - RestController에서 사용되기에 JSON 형태로 응답함
     * * @ResponseStatus: HTTP 상태 코드를 지정할 수 있음 (정상 응답이지만 예외가 발생했다는 걸 알리고 싶을 때 200 -> xxx 로 변경 가능)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("bad", e.getMessage());
    }

    /**
     * ResponseEntity: HTTP 상태 코드와 응답 본문을 함께 반환할 수 있음
     * - errorResult가 응답 본문에 담기고 HTTP 상태 코드는 BAD_REQUEST로 설정됨
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("user-ex", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("ex", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
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

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String id;
        private String name;
    }
}
