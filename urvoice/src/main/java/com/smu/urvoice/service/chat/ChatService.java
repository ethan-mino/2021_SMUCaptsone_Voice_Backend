package com.smu.urvoice.service.chat;

import com.smu.urvoice.dto.chat.ChatBotDetailDto;
import com.smu.urvoice.dto.chat.ChatDetailDto;
import com.smu.urvoice.vo.chat.ChatBotVO;
import com.smu.urvoice.vo.chat.ChatModeVO;
import com.smu.urvoice.vo.chat.ChatVO;
import com.smu.urvoice.vo.chat.VoiceVO;

import java.util.List;

public interface ChatService {
    List<ChatModeVO> getChatModes();

    List<ChatDetailDto> getChatByBotId(String owner, int chatBotId);
    int insertChat(ChatVO chatVO);

    List<ChatBotDetailDto> getChatBotByUser(String owner);
    ChatBotDetailDto getChatBotById(String owner, int chatBotId);
    int insertChatBot(ChatBotVO chatBotVO) throws Exception;
    int deleteChatBotById(String owner, int chatBotId);

    List<VoiceVO> getVoiceByUser(String owner);
    int insertVoice(VoiceVO voiceVO);
    int deleteVoiceById(String owner, int voiceId);
}
