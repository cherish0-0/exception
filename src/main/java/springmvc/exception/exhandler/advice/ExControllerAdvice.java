package springmvc.exception.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springmvc.exception.exception.UserException;
import springmvc.exception.exhandler.ErrorResult;

/**
 * @RestControllerAdvice: @ControllerAdvice와 @ResponseBody가 결합된 형태
 * - @ControllerAdvice: 모든 컨트롤러에 적용되는 전역 예외 처리기 (컨트롤러에 @ExceptionHandler, @InitBinder 같은 기능 부여해줌)
 * - @ResponseBody: 메서드 반환값을 HTTP 응답 본문으로 변환
 * - 이 클래스는 모든 컨트롤러에서 발생하는 예외를 처리함
 */
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

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
}
