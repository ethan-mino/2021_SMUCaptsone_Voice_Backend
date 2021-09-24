package com.smu.urvoice.service.chat.impl;

import com.smu.urvoice.dto.chat.ChatBotDetailDto;
import com.smu.urvoice.dto.chat.ChatDetailDto;
import com.smu.urvoice.mapper.chat.ChatMapper;
import com.smu.urvoice.service.chat.ChatService;
import com.smu.urvoice.vo.chat.ChatBotVO;
import com.smu.urvoice.vo.chat.ChatModeVO;
import com.smu.urvoice.vo.chat.ChatVO;
import com.smu.urvoice.vo.chat.VoiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatMapper chatMapper;

    @Override
    public List<ChatModeVO> getChatModes() {
        return chatMapper.getChatModes();
    }

    @Override
    public List<ChatDetailDto> getChatByBotId(String owner, int chatBotId) {
        List<ChatDetailDto> chatDetailList = chatMapper.getChatByBotId(owner, chatBotId);

        for (ChatDetailDto chatDetailDto: chatDetailList){
            boolean isBot = chatDetailDto.getIsBot();
            Map<String, Object> sender = new HashMap<>();
            String senderName;

            if(isBot){
                int botId = chatDetailDto.getChatBotId();
                ChatBotDetailDto chatBotDetailDto = chatMapper.getChatBotById(owner, botId);
                senderName = chatBotDetailDto.getName();
            }else{
                senderName = owner;
            }

            sender.put("name", senderName);
            chatDetailDto.setSender(sender);
        }

        return chatDetailList;
    }

    @Override
    public int insertChat(ChatVO chatVO) {
        return chatMapper.insertChat(chatVO);
    }

    @Override
    public ChatBotDetailDto getChatBotById(String owner, int ChatBotId) {
        ChatBotDetailDto chatBotDetailDto = chatMapper.getChatBotById(owner, ChatBotId);

        int botId = chatBotDetailDto.getId();
        String lastChat = chatMapper.getLastChatByBotId(owner, botId);
        chatBotDetailDto.setLastChat((lastChat == null) ? "" : lastChat);

        return chatBotDetailDto;
    }

    @Override
    public List<ChatBotDetailDto> getChatBotByUser(String owner) {
        List<ChatBotDetailDto> chatBotDetailList = chatMapper.getChatBotByUser(owner);

        for (ChatBotDetailDto chatBotDetail : chatBotDetailList){
            int botId = chatBotDetail.getId();
            String lastChat = chatMapper.getLastChatByBotId(owner, botId);
            chatBotDetail.setLastChat((lastChat == null) ? "" : lastChat);
        }

        return chatBotDetailList;
    }

    @Override
    public int insertChatBot(ChatBotVO chatBotVO) {
        return chatMapper.insertChatBot(chatBotVO);
    }

    @Override
    public int deleteChatBotById(String owner, int chatBotId) {
        return chatMapper.deleteChatBotById(owner, chatBotId);
    }


    @Override
    public List<VoiceVO> getVoiceByUser(String owner) {
        return chatMapper.getVoiceByUser(owner);
    }

    @Override
    public int insertVoice(VoiceVO voiceVO) {
        return chatMapper.insertVoice(voiceVO);
    }

    @Override
    public int deleteVoiceById(String owner, int voiceId) {
        return chatMapper.deleteVoiceById(owner, voiceId);
    }
}
