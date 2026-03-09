package com.assist.grievance.service;

import com.assist.grievance.data.model.request.GrievanceRegistrationLogDto;

public interface GrievanceRegistrationLogService {

    GrievanceRegistrationLogDto saveGrievanceRegistrationLog(GrievanceRegistrationLogDto request);
}
