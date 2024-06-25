package com.pda.asset_service.sms;


import com.pda.utils.exception.sms.SmsCertificationException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
//@RequiredArgsConstructor
@AllArgsConstructor
public class SMSCertificationService {
    @Autowired
    private final SmsCertificationUtil smsUtil;
    @Autowired
    private final SMSCertificationRepository smsCertificationRepository;

    public void sendSms(UserDto.SmsCertificationRequest requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationRepository.createSmsCertification(to,certificationNumber);
    }

    public void verifySms(UserDto.SmsCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new SmsCertificationException.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationRepository.deleteSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(UserDto.SmsCertificationRequest requestDto) {

        return !(smsCertificationRepository.hasKey(requestDto.getPhone()) &&
                smsCertificationRepository.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }
}
