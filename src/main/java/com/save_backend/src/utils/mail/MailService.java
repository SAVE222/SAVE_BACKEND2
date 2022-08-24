package com.save_backend.src.utils.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String mailAddressFrom;

    // 메일 내용 생성 함수
    public MailDTO createMail(String memberEmail, String tempPassword) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setEmailAddress(memberEmail);
        mailDTO.setEmailTitle("[SAVE] 임시비밀번호 안내 이메일 입니다.");
        mailDTO.setEmailText("안녕하세요. SAVE 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        return mailDTO;
    }

    // 임시 비밀번호 생성 함수
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        // 랜덤으로 마지막 특수문자 포함 11자 생성
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str+"!";
    }

    // 메일 전송 함수
    public void mailSend(MailDTO mailDTO, String userMail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");  // true는 멀티파트 메세지를 사용하겠다는 의미

            mimeMessageHelper.setFrom(mailAddressFrom);
            mimeMessageHelper.setTo(userMail);
            mimeMessageHelper.setSubject(mailDTO.getEmailTitle());
            mimeMessageHelper.setText(mailDTO.getEmailText(), true);  // true는 html을 사용하겠다는 의미

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
