package com.example.backend.batch.EmailReminder;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class EmailReminderProcessor implements ItemProcessor<Object, Map<String, String>> {
    @Override
    public Map<String, String> process(Object item) {
        Map<String, String> emailData = new HashMap<>();
        if (item instanceof User) {
            User student = (User) item;
            emailData.put("to", student.getUsername());
            emailData.put("subject", "Nhắc nhở: Đăng ký khóa học");
            emailData.put("body", "Kính gửi " + student.getFullName() + ",\n\n"
                    + "Bạn vẫn chưa đăng ký khóa học nào. Vui lòng đăng ký khóa học sớm.\n\n"
                    + "Trân trọng,\nĐại học ABC");
        } else if (item instanceof Course) {
            Course course = (Course) item;
            emailData.put("to", "all_students");
            emailData.put("subject", "Nhắc nhở: Khóa học " + course.getName() + " cần thêm học viên");
            emailData.put("body", "Khóa học " + course.getName() + " vẫn đang mở đăng ký và cần thêm học viên.\n"
                    + "Vui lòng cân nhắc đăng ký vào khóa học này sớm.\n\n"
                    + "Trân trọng,\nĐại học ABC");
        }
        return emailData;
    }
}