package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.exception.RestApiException;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ✅ FolderController 클래스는 폴더 관련 웹 요청을 처리하는 REST 컨트롤러입니다.
 *
 *    ➡️ 폴더 추가와 같은 폴더 관련 작업을 처리합니다.
 */
@RestController // REST API를 제공하는 컨트롤러로 선언합니다.
@RequestMapping("/api") // 이 컨트롤러의 모든 요청은 "/api" 경로를 기본으로 합니다.
@RequiredArgsConstructor // Lombok 어노테이션으로 생성자를 자동으로 생성합니다.
public class FolderController {

    private final FolderService folderService; // 폴더 서비스 인스턴스

    /**
     * ✅ 사용자가 요청한 폴더 이름을 기반으로 새로운 폴더를 추가합니다.
     *
     *    ➡️ 클라이언트로부터 폴더 이름 목록을 받아서 로그인한 사용자에게 폴더를 생성합니다.
     *    ➡️ 요청 본문에 포함된 `FolderRequestDto` 객체를 통해 폴더 이름 목록을 받습니다.
     *
     * @param folderRequestDto 폴더 이름 목록이 포함된 DTO 객체입니다.
     * @param userDetails 현재 인증된 사용자의 상세 정보입니다.
     */
    @PostMapping("/folders") // "/api/folders" 경로로 POST 요청을 처리합니다.
    public void addFolders(@RequestBody FolderRequestDto folderRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<String> folderNames = folderRequestDto.getFolderNames(); // DTO에서 폴더 이름 목록을 추출합니다.

        // 폴더 이름 목록과 현재 인증된 사용자를 기반으로 폴더를 추가합니다.
        folderService.addFolders(folderNames, userDetails.getUser());
    }

    /**
     * ✅ 현재 인증된 사용자가 가진 모든 폴더의 목록을 반환합니다.
     *
     *    ➡️ 클라이언트로부터 인증된 사용자 정보를 기반으로 사용자가 가진 폴더 목록을 조회합니다.
     *    ➡️ 요청 헤더의 인증 정보를 통해 현재 인증된 사용자의 정보를 가져오고,
     *    ➡️ 이를 기반으로 폴더 목록을 조회하여 `FolderResponseDto` 객체의 리스트를 반환합니다.
     *
     * @param userDetails 현재 인증된 사용자의 상세 정보가 포함된 `UserDetailsImpl` 객체입니다.
     * @return List<FolderResponseDto> 현재 인증된 사용자가 가진 폴더의 목록을 포함하는 DTO 객체 리스트입니다.
     */
    @GetMapping("/folders") // "/api/folders" 경로로 GET 요청을 처리합니다.
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 정보를 통해 폴더 목록을 조회하고 반환합니다.
        return folderService.getFolders(userDetails.getUser());
    }

    /**
     * ✅ IllegalArgumentException 예외를 처리하는 메서드입니다.
     *
     *    ➡️ 폴더 관련 작업 중 `IllegalArgumentException`이 발생할 경우, 이 메서드가 실행됩니다.
     *    ➡️ 발생한 예외의 메시지를 바탕으로 `RestApiException` 객체를 생성하고,
     *       HTTP 상태 코드 400 (BAD_REQUEST)와 함께 클라이언트에게 응답합니다.
     *
     * @param ex 발생한 IllegalArgumentException 예외 객체입니다.
     * @return ResponseEntity<RestApiException> 에러 메시지와 상태 코드가 포함된 응답 객체입니다.
     */
    @ExceptionHandler({IllegalArgumentException.class}) // IllegalArgumentException 발생 시 이 메서드가 호출됩니다.
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        // 예외 메시지와 HTTP 상태 코드를 담은 RestApiException 객체를 생성합니다.
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());

        // RestApiException 객체와 HTTP 상태 400 (BAD_REQUEST)을 응답으로 반환합니다.
        return new ResponseEntity<>(
            restApiException, // HTTP 응답 본문에 포함될 내용입니다.
            HttpStatus.BAD_REQUEST // HTTP 응답 상태 코드를 400으로 설정합니다.
        );
    }
}