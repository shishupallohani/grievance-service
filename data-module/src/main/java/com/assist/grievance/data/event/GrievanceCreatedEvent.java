package com.assist.grievance.data.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrievanceCreatedEvent {
    private String grievanceNo;
    private String grievanceTxnNo;
    private String email;
    private String name;
    private String createdAt;

}