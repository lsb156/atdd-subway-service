package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthRestHelper;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = MemberRestHelper.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberRestHelper.회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberRestHelper.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberRestHelper.회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = MemberRestHelper.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        String accessToken = AuthRestHelper.토큰_구하기(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> findResponse = MemberRestHelper.내_정보_조회(accessToken);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> updateResponse = MemberRestHelper.내_정보_수정(memberRequest, accessToken);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberRestHelper.내_정보_삭제(accessToken);
        // then
        회원_삭제됨(deleteResponse);

    }

    @DisplayName("잘못된 토큰으로 나의 정보를 관리")
    @Test
    void shouldBeExceptionWhenManageMyInfo() {

        String accessToken = "BAD_TOKEN";

        // when
        ExtractableResponse<Response> findResponse = MemberRestHelper.내_정보_조회(accessToken);
        // then
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> updateResponse = MemberRestHelper.내_정보_수정(memberRequest, accessToken);
        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        ExtractableResponse<Response> deleteResponse = MemberRestHelper.내_정보_삭제(accessToken);
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }


    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
