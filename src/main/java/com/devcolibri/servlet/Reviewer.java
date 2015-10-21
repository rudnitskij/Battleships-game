package com.devcolibri.servlet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class is used to inspect and process xls-tables
 * It checks if proper values are put in proper cells
 * and fills empty cells with calculated values if necessary
 *
 * @author Vitaliy Morgun
 * @since 1.0
 */

public class Reviewer {
    public StringBuilder result;

    public boolean cellsFilledProperly(File file) throws IOException {
        result=new StringBuilder();
        boolean quantityPresent=false,netWeightPresent=false;
        float netWeightSummary,quantitySummary;
        result.append(file.getName()).append("<br>");

        int index = 0, lastRowNumber = 0, value = -10, rowNumber;
        int sheetsNumber, activeSheetNumber;
        String key = "";
        float averageWeight, editionValue;
        InputStream in = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(in);
        Sheet sheet = wb.getSheetAt(0);
        Row row, nextRow, helpRow;
        sheetsNumber = wb.getNumberOfSheets();
        for (int i = 0; i < sheetsNumber; i++) {
            sheet = wb.getSheetAt(i);
            row = sheet.getRow(15);
            boolean STYLE = (String.valueOf(cellValue(row, 3))).contains("STYLE");
            boolean NET_WEIGHT = (String.valueOf(cellValue(row, 8))).contains("NET WEIGHT") ||
                    (String.valueOf(cellValue(row, 8))).contains("NET") ||
                    (String.valueOf(cellValue(row, 8))).contains("WEIGHT");
            if (STYLE && NET_WEIGHT) {
                value = i;
                //result.append("Упаковочный лист находится на листе № ").append(value + 1).append("<br>");
            }
            if (value != -10) {
                break;
            }
        }
        if (value == -10) {
            result.append("STYLE и NETWEIGHT не на своих местах <br>");
            in.close();
            return false;
        }
        activeSheetNumber = value;
        wb.setActiveSheet(value);
        index = -1;
        row = sheet.getRow(15);
        nextRow = sheet.getRow(16);
        rowNumber = nextRow.getRowNum();
        while (true) {
            for (int i = 0; i < 10; i++) {
                if (String.valueOf(cellValue(nextRow, i)).contains("TOTAL")) {
                    lastRowNumber=rowNumber;
                    index = 1;
                    break;
                }
            }
            if(rowIsEmpty(nextRow)&&rowIsEmpty(sheet.getRow(rowNumber+1))){
                lastRowNumber=rowNumber;
                index=1;
            }
            if (index == 1) {
                break;
            }
            if (rowIsEmpty(nextRow) && !(nextRowContainsTOTAL(sheet,rowNumber)||nextRowContainsSUMMARY(sheet,rowNumber)||
            nextRowContainsSTYLE(sheet,rowNumber))) {
                result.append("пустой ряд посреди таблицы - ").append(rowNumber + 1).append("<br>");
                in.close();
                return false;
            }
            if (!cellsHaveNumbers(nextRow)) {
                result.append("строка номер ").append(nextRow.getRowNum() + 1).append(" : <br>");
                result.append("цифры в колонках 4 и 9 неверно расставлены.<br>");
                in.close();
                return false;
            }
            rowNumber++;
            nextRow = sheet.getRow(rowNumber);
        }
        helpRow = nextRow;
        nextRow = sheet.getRow(nextRow.getRowNum() + 1);
        index = -1;
        int quantity = 1, netWeight = 1;
        value = 2;
        while (true) {
            for (int i = 0; i < 10; i++) {
                if (String.valueOf(cellValue(nextRow, i)).contains("STYLE")) {
                    index = i;
                    quantity=index+4;
                    netWeight=index+5;
                    for (int j = i; j < 10; j++) {
                        if ((String.valueOf(cellValue(nextRow, j))).contains("QUANTITY")) {
                            quantityPresent=true;
                            quantity = j;
                            continue;
                        }
                        if (String.valueOf(cellValue(nextRow, j)).contains("NET WEIGHT")) {
                            netWeightPresent=true;
                            netWeight = j;
                            break;
                        }
                    }
                    break;
                }
            }
            if (index != -1) {
                break;
            }
            while (true) {
                try {
                    nextRow = sheet.getRow
                            (helpRow.getRowNum() + value);
                    break;
                } catch (NullPointerException e) {
                    value++;
                }
            }
            value++;
        }
        if(!(quantityPresent&&netWeightPresent)){
            result.append("Столбцы NET WEIGHT и QUANTITY в таблице отсутствуют.<br>");
        }
        nextRow = sheet.getRow(nextRow.getRowNum() + 1);
        rowNumber=nextRow.getRowNum();

        /** HashMap averageWeights stores article number as the key and according average weight as the corresponding value*/
        Map<String, Float> averageWeights = new HashMap<String, Float>();

        /** HashMap edition stores actual values of average weight for corresponding article number and is taken from here
         * in order to fill the empty cell*/
        Map<Integer, Float> edition = new HashMap<Integer, Float>();

        /**
         * HashMap articleCounter is made for counting and storing of number of times which every article number
         * we run across in the packing list
         */
        Map<String, Integer> articleCounter = new HashMap<String, Integer>();

        while (!(nextRowContainsTOTAL(sheet,rowNumber-1))) {
            if (String.valueOf(cellValue(nextRow, index)).equals("")) {
                nextRow = sheet.getRow(rowNumber + 1);
                rowNumber++;
                continue;
            }
            if (!isNumeric(String.valueOf(cellValue(nextRow, index)))) {
                result.append("Артикул должен состоять из цифр - здесь ").append(String.valueOf(cellValue(nextRow, index))).append("<br>");
                in.close();
                return false;
            }
            netWeightSummary= Float.valueOf(String.valueOf(cellValue(nextRow,netWeight)));
            quantitySummary=Float.valueOf(String.valueOf(cellValue(nextRow,quantity)));
            averageWeight = netWeightSummary/quantitySummary;

            averageWeights.put(String.valueOf(cellValue(nextRow, index)), averageWeight);
            articleCounter.put(String.valueOf(cellValue(nextRow, index)), 0);
            rowNumber++;
            nextRow = sheet.getRow(rowNumber);
        }
        for (int i = row.getRowNum() + 1; i < lastRowNumber; i++) {
            nextRow = sheet.getRow(i);
            if (String.valueOf(cellValue(nextRow, 3)).equals("")) {
                continue;
            }
            key = String.valueOf(cellValue(nextRow, 3));
            value = articleCounter.get(key);
            if (averageWeights.containsKey(key)) {
                averageWeight = averageWeights.get(key);
                articleCounter.put(key, value + 1);
                if (averageWeight > 1.5) {
                    result.append("Средний вес слишком большой - артикул ").append(key).append("<br>");
                    in.close();
                    return false;
                }
                if (String.valueOf(cellValue(nextRow, 8)).equals("")) {
                    edition.put(nextRow.getRowNum(), averageWeight);
                    result.append("В строке № ").append(nextRow.getRowNum() + 1).append(" вес не был указан, установлено значение из саммари.<br>");
                    continue;
                }
                if (!differsLessThan10percent(averageWeight, Double.valueOf(String.valueOf(cellValue(nextRow, 8))))) {
                    result.append("В строке ").append(nextRow.getRowNum() + 1).append(" файла ").append(file.getName()).append(" вес отличается более чем на 10%<br>");
                }
            } else {
                result.append("артикул в упаковочном листе не указан в саммари.<br>");
                result.append("артикул равен ").append(key).append("<br>");
                in.close();
                return false;
            }
        }
        Set<String> articleSet = articleCounter.keySet();
        for (String art : articleSet) {
            if (articleCounter.get(art) == 0) {
                result.append("Артикул из саммари № ").append(art).append(" не встретился ни разу в упаковочном листе файла ").append(file.getName()).append("<br>");
                in.close();
                return false;
            }
        }
        in.close();
        if (edition.isEmpty()) {
            result.append("<strong>Упаковочный лист заполнен ВЕРНО!</strong><br>");
            return true;
        } else {
            OutputStream out = new FileOutputStream(file);
            wb.setActiveSheet(activeSheetNumber);
            Set<Integer> rowSet = edition.keySet();
            for (Integer rowNum : rowSet) {
                editionValue = (int) (edition.get(rowNum) * 1000);
                editionValue /= 1000;
                nextRow = sheet.getRow(rowNum);
                nextRow.getCell(8).setCellValue(editionValue);
            }
            wb.write(out);
            out.close();
        }
        result.append("<strong>Упаковочный лист заполнен ВЕРНО!</strong><br>");
        return true;
    }

    /**
     * Checks if the row is empty or not
     *
     * @param row - the row to be checked
     * @return
     */
    public boolean rowIsEmpty(Row row) {
        int i = 0;
        while (i < 10) {
            try {
                if (String.valueOf(cellValue(row, i)).length() > 0) {
                    return false;
                }
            } catch (IllegalStateException ex) {
                continue;
            }
            i++;
        }
        return true;
    }

    /**
     * Checks if there are numeric values in the particular cells of the given row
     *
     * @param row - the row to be checked
     * @return boolean
     */
    public boolean cellsHaveNumbers(Row row) {
        boolean cell1HasNumber, cell2HasNumber;
        Cell cell1 = row.getCell(3);
        Cell cell2 = row.getCell(8);
        try {
            cell1HasNumber = cell1.toString().length() > 0;
        } catch (NullPointerException npe) {
            cell1HasNumber = false;
        }
        try {
            cell2HasNumber = cell2.toString().length() > 0;
        } catch (NullPointerException ex) {
            cell2HasNumber = false;
        }
        if (!(cell1HasNumber) && !(cell2HasNumber)) return true;
        return cell1HasNumber;
    }

    /**
     * Checks if 'a' and 'b' numbers differ for more than 10 percent from each other
     *
     * @param a - double
     * @param b - double
     * @return boolean
     */
    public boolean differsLessThan10percent(double a, double b) {
        double diff = Math.abs(a - b);
        return (diff / a) < 0.1;
    }

    /**
     * returns value of the cell despite its type
     *
     * @param row  - the needed row
     * @param cell - number of the needed cell
     * @return Object
     */
    public Object cellValue(Row row, int cell) {
        try {
            return row.getCell(cell).getStringCellValue();
        } catch (IllegalStateException e) {
            if (row.getCell(cell).getNumericCellValue() > 100000) {
                return (int) (row.getCell(cell).getNumericCellValue());
            } else {
                return (float)row.getCell(cell).getNumericCellValue();
            }
        } catch (NullPointerException e) {
            return "";
        } catch (ClassCastException e) {
            return 0;
        }
    }

    /**
     * checks if there is word TOTAL in the next row of the table
     *
     * @param sheet - the row to be checked
     * @param rowNumber - number of the row
     * @return boolean
     */
    public boolean nextRowContainsTOTAL(Sheet sheet,int rowNumber) {
        Row nextRow = sheet.getRow(rowNumber + 1);
        for (int i = 0; i < 10; i++) {
            if ((String.valueOf(cellValue(nextRow, i))).contains("TOTAL")||(String.valueOf(cellValue(nextRow, i))).contains("Total")) {
                return true;
            }
        }
        return false;
    }

    public boolean nextRowContainsSUMMARY(Sheet sheet,int rowNumber) {
        Row nextRow = sheet.getRow(rowNumber + 1);
        for (int i = 0; i < 10; i++) {
            if ((String.valueOf(cellValue(nextRow, i))).contains("SUMMARY")||(String.valueOf(cellValue(nextRow, i))).contains("Summary")) {
                return true;
            }
        }
        return false;
    }

    public boolean nextRowContainsSTYLE(Sheet sheet,int rowNumber) {
        Row nextRow = sheet.getRow(rowNumber + 1);
        for (int i = 0; i < 10; i++) {
            if ((String.valueOf(cellValue(nextRow, i))).contains("STYLE")||(String.valueOf(cellValue(nextRow, i))).contains("Style")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks if argument string consists of numbers or not
     *
     * @param string - the article number to be checked
     * @return boolean
     */
    public boolean isNumeric(String string) {
        byte[] bytes = string.getBytes();
        for (byte b : bytes) {
            if ((b > 57) || (b < 48)) {
                return false;
            }
        }
        return true;
    }
}
