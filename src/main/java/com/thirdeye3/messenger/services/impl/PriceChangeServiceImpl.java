package com.thirdeye3.messenger.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messenger.dtos.Group;
import com.thirdeye3.messenger.dtos.Message;
import com.thirdeye3.messenger.dtos.PriceChange;
import com.thirdeye3.messenger.dtos.Stock;
import com.thirdeye3.messenger.dtos.TelegramChatId;
import com.thirdeye3.messenger.dtos.TelegramMessage;
import com.thirdeye3.messenger.enums.WorkType;
import com.thirdeye3.messenger.services.MessageBrokerService;
import com.thirdeye3.messenger.services.PriceChangeService;
import com.thirdeye3.messenger.services.PropertyService;
import com.thirdeye3.messenger.services.StockService;
import com.thirdeye3.messenger.services.GroupService;
import com.thirdeye3.messenger.utils.TimeManager;

@Service
public class PriceChangeServiceImpl implements PriceChangeService {

    private static final Logger logger = LoggerFactory.getLogger(PriceChangeServiceImpl.class);

    @Autowired
    private MessageBrokerService messageBrokerService;

    @Autowired
    private StockService stockService;

    @Autowired
    private TimeManager timeManager;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private GroupService groupService;

    private Map<Long, ConcurrentLinkedDeque<String>> queueMap = new ConcurrentHashMap<>();
    
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    @Override
    public Boolean fetchNewChanges() {
        List<Message<PriceChange>> messages = messageBrokerService.getMessage("thresold");
        if(messages == null || messages.size() == 0)
        {
        	return false;
        }
        for (Message<PriceChange> message : messages) {
            PriceChange priceChange = message.getMessage();

            Stock stock = stockService.getStockById(priceChange.getUniqueId());
            String stockMarket = stock.getUniqueCode() + " " + stock.getMarketCode();

            String changeUnit = priceChange.getType() == 0 ? " %" : " ₹";
            String formattedTime = timeFormatter.format(priceChange.getCurrTime());

            StringBuilder sb = new StringBuilder();

            sb.append("🏷 <b>Stock/Market:</b> <code>")
              .append(stockMarket)
              .append("</code>\n")
              .append("⚡ <b>Change:</b> <code>")
              .append(priceChange.getPriceChange())
              .append(changeUnit)
              .append("</code> ");

            if (priceChange.getTimeGap() == -1) {
                sb.append("from <b>current day opening price</b>\n");
            } else if (priceChange.getTimeGap() == -2) {
                sb.append("from <b>previous day closing price</b>\n");
            } else {
                sb.append("in last <code>")
                  .append(priceChange.getTimeGap())
                  .append("</code> seconds\n");
            }

            sb.append("💰 <b>Price:</b> <code>")
              .append(priceChange.getNewPrice())
              .append("</code>\n")
              .append("⏰ <b>Time:</b> <code>")
              .append(formattedTime)
              .append("</code>");

            queueMap
                .computeIfAbsent(priceChange.getGroupId(), k -> new ConcurrentLinkedDeque<>())
                .addLast(sb.toString());
        }
        return true;
    }

    @Override
    public void createMessageForUsers() {
        logger.info("Going to create message data");
        List<TelegramMessage> telegramMessages = new ArrayList<>();

        for (Map.Entry<Long, ConcurrentLinkedDeque<String>> entry : queueMap.entrySet()) {

            List<String> messagesCopy = new ArrayList<>(entry.getValue());
            if (messagesCopy.isEmpty()) continue;

            Group group = groupService.getGroup(entry.getKey());

            for (TelegramChatId telegramChatId : group.getTelegramChatIds()) {

                if (telegramChatId.getWorkType().equals(WorkType.THRESOLD)) {

                    TelegramMessage telegramMessage = new TelegramMessage();
                    telegramMessage.setChatId(telegramChatId.getChatId());
                    telegramMessage.setChatName(telegramChatId.getChatName());

                    StringBuilder message = new StringBuilder();
                    String formattedTime = timeFormatter.format(timeManager.getCurrentTime());

                    message.append("<b>📊 THIRD EYE 📊</b>\n")
                           .append("<i>🕒 ").append(formattedTime).append("</i>\n")
                           .append("<i>Your market update</i>\n\n");

                    for (String s : messagesCopy) {
                        if (message.length() + s.length() > propertyService.getMaximumMessageLength()) {
                            telegramMessage.getChats().add(message.toString());
                            message.setLength(0);
                        }
                        message.append(s).append("\n\n");
                    }

                    message.append("For more details, stay tuned with THIRDEYE updates!\n");
                    message.append("<b>✅ END OF UPDATES ✅</b>");

                    telegramMessage.getChats().add(message.toString());
                    telegramMessages.add(telegramMessage);
                }
            }

            entry.getValue().clear();
        }

        if (!telegramMessages.isEmpty()) {
            messageBrokerService.sendMessages("telegramthresold", telegramMessages);
        }
    }


    @Override
    public void clearMap() {
        queueMap.clear();
    }
}
