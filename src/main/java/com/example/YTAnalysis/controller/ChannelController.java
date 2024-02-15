package com.example.YTAnalysis.controller;

import com.example.YTAnalysis.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadChannels(@RequestParam("file") MultipartFile file) {
        try {
            List<String> channelIds = readExcel(file);
            channelService.saveChannels(channelIds);
            return new ResponseEntity<>("Channels added successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process the Excel file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> readExcel(MultipartFile file) throws IOException {
        List<String> channelIds = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Cell cell = currentRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                channelIds.add(cell.toString().trim());
            }
        }
        return channelIds;
    }

}
