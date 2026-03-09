package com.assist.grievance.service.Impl;

import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.data.specification.GrievanceSpecification;
import com.assist.grievance.exception.ExportException;
import com.assist.grievance.service.GrievanceExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrievanceExportServiceImpl implements GrievanceExportService {

    private final GrievanceRepository grievanceRepository;
    private final GrievanceSpecification specification;

    private static final int DIRECT_EXPORT_LIMIT = 50000;
    private static final int BATCH_SIZE = 1000;


    /* Export is limited to 50,000 records (synchronous). Async processing required for higher volumes.*/
    @Override
    @Transactional(readOnly = true)
    public byte[] exportGrievances(GrievancePaginatedRequest request) {

        log.info("Starting grievance export process");

        Specification<GrievanceEntity> spec = specification.build(request);
        long totalCount = grievanceRepository.count(spec);

        log.info("Total records found for export: {}", totalCount);

        if (totalCount == 0) {
            log.warn("No data available for export");
            throw new ExportException(Constants.DATA_NOT_AVAILABLE, HttpStatus.NOT_FOUND);
        }

        if (totalCount > DIRECT_EXPORT_LIMIT) {
            log.warn("Export limit exceeded. Limit: {}", DIRECT_EXPORT_LIMIT);
            throw new ExportException(Constants.EXPORT_LIMIT_EXCEED + DIRECT_EXPORT_LIMIT, HttpStatus.BAD_REQUEST);
        }
        return generateExcel(spec, request);
    }

    private byte[] generateExcel(Specification<GrievanceEntity> spec, GrievancePaginatedRequest request) {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Grievances");
            createHeader(sheet);

            int rowNum = 1;
            int page = 0;

            Sort sort = buildSort(request);
            Page<GrievanceEntity> grievancePage;

            do {
                Pageable pageable = PageRequest.of(page, BATCH_SIZE, sort);
                grievancePage = grievanceRepository.findAll(spec, pageable);

                for (GrievanceEntity entity : grievancePage.getContent()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(entity.getAcknowledgementNo());
                    row.createCell(1).setCellValue(entity.getInsuranceCompany());
                    row.createCell(2).setCellValue(entity.getReceivedFrom());
                    row.createCell(3).setCellValue(entity.getMode());
                    row.createCell(4).setCellValue(entity.getGrievanceType());
                    row.createCell(5).setCellValue(entity.getStatus());
                }

                page++;

            } while (grievancePage.hasNext());

            workbook.write(out);
            workbook.dispose();

            log.info("Excel file generated successfully with {} records", rowNum - 1);

            return out.toByteArray();

        } catch (Exception e) {

            log.error("Error while generating Excel file", e);

            throw new ExportException(Constants.FAILED_GENERATE, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Acknowledgement No");
        header.createCell(1).setCellValue("Insurance Company");
        header.createCell(2).setCellValue("Received From");
        header.createCell(3).setCellValue("Mode");
        header.createCell(4).setCellValue("Grievance Type");
        header.createCell(5).setCellValue("Status");
    }

    private Sort buildSort(GrievancePaginatedRequest request) {

        if (request.getSort() == null || request.getSort().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        Sort.Direction direction =
                "asc".equalsIgnoreCase(request.getSortDirection())
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return Sort.by(direction, request.getSort().toArray(new String[0]));
    }
}
