package build.dream.platform.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class PoiUtils {
    public static Workbook buildGoodsTemplate(String sheetName, String[] titles, List<Map<String, Object>> categories, List<Map<String, Object>> units, String[] statuses) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Row titleRow = sheet.createRow(0);
        titleRow.setHeight((short) 300);

        int length = titles.length;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());

        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        cellStyle.setFont(font);
        for (int index = 0; index < length; index++) {
            Cell cell = titleRow.createCell(index);
            cell.setCellValue(titles[index]);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(index, 5000);
        }

        Sheet categoryNameHiddenSheet = workbook.createSheet("category_name_hidden");
        Sheet categoryIdHiddenSheet = workbook.createSheet("category_id_hidden");
        int categoriesSize = categories.size();
        for (int index = 0; index < categoriesSize; index++) {
            Map<String, Object> category = categories.get(index);
            categoryNameHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getString(category, "name"));
            categoryIdHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getDoubleValue(category, "id"));
        }

        Sheet unitNameHiddenSheet = workbook.createSheet("unit_name_hidden");
        Sheet unitIdHiddenSheet = workbook.createSheet("unit_id_hidden");
        int unitsSize = units.size();
        for (int index = 0; index < unitsSize; index++) {
            Map<String, Object> unit = units.get(index);
            unitNameHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getString(unit, "name"));
            unitIdHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getDoubleValue(unit, "id"));
        }

        CellRangeAddressList categoriesCellRangeAddressList = new CellRangeAddressList(1, 65535, 4, 4);
        CellRangeAddressList unitsCellRangeAddressList = new CellRangeAddressList(1, 65535, 5, 5);
        CellRangeAddressList statusesCellRangeAddressList = new CellRangeAddressList(1, 65535, 10, 10);


        DataValidationConstraint categoriesDataValidationConstraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, DataValidationConstraint.OperatorType.IGNORED, "category_name_hidden!$A$1:$A$" + categoriesSize);
        DataValidationConstraint unitsDataValidationConstraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, DataValidationConstraint.OperatorType.IGNORED, "unit_name_hidden!$A$1:$A$" + unitsSize);
        DataValidationConstraint statusesDataValidationConstraint = new XSSFDataValidationConstraint(statuses);

        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        DataValidation categoriesDataValidation = dataValidationHelper.createValidation(categoriesDataValidationConstraint, categoriesCellRangeAddressList);
        DataValidation unitsDataValidation = dataValidationHelper.createValidation(unitsDataValidationConstraint, unitsCellRangeAddressList);
        DataValidation statusesDataValidation = dataValidationHelper.createValidation(statusesDataValidationConstraint, statusesCellRangeAddressList);

        sheet.addValidationData(categoriesDataValidation);
        sheet.addValidationData(unitsDataValidation);
        sheet.addValidationData(statusesDataValidation);
        workbook.setSheetHidden(1, true);
        workbook.setSheetHidden(2, true);
        workbook.setSheetHidden(3, true);
        workbook.setSheetHidden(4, true);
        return workbook;
    }
}
