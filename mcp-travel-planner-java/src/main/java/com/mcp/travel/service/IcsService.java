package com.mcp.travel.service;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IcsService {
    
    private static final Pattern DAY_PATTERN = Pattern.compile("Day (\\d+)[:\\s]+(.*?)(?=Day \\d+|$)", Pattern.DOTALL);
    
    public byte[] generateIcsContent(String planText, LocalDate startDate) {
        try {
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//AI Travel Planner//github.com//"));
            calendar.getProperties().add(Version.VERSION_2_0);
            
            if (startDate == null) {
                startDate = LocalDate.now();
            }
            
            Matcher matcher = DAY_PATTERN.matcher(planText);
            boolean foundDays = false;
            
            while (matcher.find()) {
                foundDays = true;
                int dayNum = Integer.parseInt(matcher.group(1));
                String dayContent = matcher.group(2).trim();
                
                LocalDate currentDate = startDate.plusDays(dayNum - 1);
                Date eventDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                
                VEvent event = new VEvent(new net.fortuna.ical4j.model.Date(eventDate), 
                                          "Day " + dayNum + " Itinerary");
                event.getProperties().add(new Description(dayContent));
                event.getProperties().add(new Uid(java.util.UUID.randomUUID().toString()));
                event.getProperties().add(new DtStamp(new DateTime(new Date())));
                
                calendar.getComponents().add(event);
            }
            
            if (!foundDays) {
                Date eventDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                VEvent event = new VEvent(new net.fortuna.ical4j.model.Date(eventDate), 
                                          "Travel Itinerary");
                event.getProperties().add(new Description(planText));
                event.getProperties().add(new Uid(java.util.UUID.randomUUID().toString()));
                event.getProperties().add(new DtStamp(new DateTime(new Date())));
                
                calendar.getComponents().add(event);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, outputStream);
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ICS file", e);
        }
    }
}
