package springmvc.exception.servlet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

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

    /**
     * sendError 메서드로 HTTP 오류 상태 코드와 메시지를 클라이언트에게 전송
     * 실제로 예외가 발생한 것은 아니고 가상의 상태를 전달함
     * 전달받은 상태 코드는 WAS에서 오류로 인식됨
     * 서블릿이 오류 처리를 해 에러 뷰 렌더링 됨
     * @param response
     * @throws IOException
     */
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
