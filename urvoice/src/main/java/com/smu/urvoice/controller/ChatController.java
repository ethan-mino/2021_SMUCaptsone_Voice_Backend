package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.chat.ChatBotDetailDto;
import com.smu.urvoice.dto.chat.ChatDetailDto;
import com.smu.urvoice.vo.user.UserVO;
import com.smu.urvoice.service.chat.ChatService;
import com.smu.urvoice.vo.chat.ChatBotVO;
import com.smu.urvoice.vo.chat.ChatModeVO;
import com.smu.urvoice.vo.chat.ChatVO;
import com.smu.urvoice.vo.chat.VoiceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "REST API for chat", tags = "Chat")
@RestController
public class ChatController {
    @Autowired
    ChatService chatService;

    @ApiOperation(value = "어체 조회")
    @GetMapping("/chat/mode")
    public List<ChatModeVO> getChatModes() {
        return chatService.getChatModes();
    }

    @ApiOperation(value = "chatBot Id로 대화 내용 조회")
    @GetMapping("/chat")
    public List<ChatDetailDto> getChatByBotId(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                                              @RequestParam int chatBotId) {
        String owner = userVO.getLoginId();

        return chatService.getChatByBotId(owner, chatBotId);
    }

    @ApiOperation(value = "챗봇 생성")
    @PostMapping("/chat/chatBot")
    public ApiResponse insertChatBot(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                             @RequestBody ChatBotVO chatBotVO) {
        String owner = userVO.getLoginId();
        chatBotVO.setOwner(owner);

        int insertCnt = chatService.insertChatBot(chatBotVO);

        if (insertCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "챗봇 생성 실패!");
    }

    @ApiOperation(value = "chatBot Id로 유저의 챗봇 조회")
    @GetMapping("/chat/chatBot/{chatBotId}")
    public ChatBotDetailDto getChatBotByChatBotId(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                                                        @PathVariable int chatBotId){
        String owner = userVO.getLoginId();

        return chatService.getChatBotById(owner, chatBotId);
    }

    @ApiOperation(value = "유저의 챗봇 조회")
    @GetMapping("/chat/chatBot")
    public List<ChatBotDetailDto> getChatBotByUser(@ApiIgnore @AuthenticationPrincipal UserVO userVO){
        String owner = userVO.getLoginId();

        return chatService.getChatBotByUser(owner);
    }

    @ApiOperation(value = "챗봇 삭제")
    @DeleteMapping("/chat/chatBot/{chatBotId}")
    public ApiResponse deleteChatBotById(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                                 @PathVariable int chatBotId) {
        String owner = userVO.getLoginId();

        int deleteCnt = chatService.deleteChatBotById(owner, chatBotId);

        if (deleteCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "챗봇 삭제 실패!");
    }
    
    @ApiOperation(value = "메시지 생성")
    @PutMapping("/chat")
    public ApiResponse insertChat(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                          @RequestBody ChatVO chatVO) {
        String owner = userVO.getLoginId();
        chatVO.setOwner(owner);

        int insertCnt = chatService.insertChat(chatVO);

        if (insertCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "메시지 생성 실패!");
    }
    
    @ApiOperation(value = "유저의 음성 조회")
    @GetMapping("/chat/voice")
    public List<VoiceVO> getVoiceByUser(@ApiIgnore @AuthenticationPrincipal UserVO userVO) {
        String owner = userVO.getLoginId();

        return chatService.getVoiceByUser(owner);
    }

    @ApiOperation(value = "음성 생성")
    @PostMapping("/chat/voice")
    public ApiResponse insertVoice(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                           @RequestBody VoiceVO voiceVO) {
        String owner = userVO.getLoginId();

        voiceVO.setOwner(owner);
        int insertCnt = chatService.insertVoice(voiceVO);

        if (insertCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "음성 생성 실패!");
    }

    @ApiOperation(value = "음성 삭제")
    @DeleteMapping("/chat/voice/{voiceId}")
    public ApiResponse deleteVoiceById(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                               @PathVariable int voiceId) {
        String owner = userVO.getLoginId();

        int deleteCnt = chatService.deleteVoiceById(owner, voiceId);

        if (deleteCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "음성 삭제 실패!");
    }
}
