package com.assist.grievance.validator;

import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class GrievanceRegistrationValidator {

    public List<String> validate(GrievanceRegistrationDTO request) {
        List<String> errors = new ArrayList<>();
        String insComName = request.getInsuranceCompanyName();
        if (insComName == null && insComName.trim().isEmpty()) {
            errors.add("Insurance Company Name is mandatory");
        }
        if (insComName.equalsIgnoreCase("NIC") || insComName.equalsIgnoreCase("NIA")
                || insComName.equalsIgnoreCase("UIIC") || insComName.equalsIgnoreCase("OIC")) {
            return errors;
        }else{
            errors.add("Insurance Company Name is Not Valid:-" + insComName);
        }
        return errors;
    }
}

