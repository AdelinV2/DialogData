package com.dialogdata.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CsvDto {

    Integer successCount;

    Set<Integer> failedLines;

    public void addFailedLine(Integer line) {
        failedLines.add(line);
    }
}
