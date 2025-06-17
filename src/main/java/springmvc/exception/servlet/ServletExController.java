package springmvc.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
/**
 * @Controller와 @RestController의 차이점
 * @Controller: 주로 웹페이지 반환하는 컨트롤러
 * - 메서드 반환값을 뷰 이름으로 인식
 *
 * @RestController: 주로 JSON,XML 같은 RESTful API 반환하는 컨트롤러
 * - 메서드 반환값을 HTTP 응답 본문으로 인식
 * - @ResponseBody가 기본적으로 내장되어 있음
 */
@Controller
public class ServletExController {
    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");
    }
}
