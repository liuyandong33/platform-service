package build.dream.platform.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

public class PoiUtils {
    public static XSSFWorkbook buildGoodsTemplate(String sheetName, String[] titles, List<Map<String, Object>> categories, List<Map<String, Object>> units, String[] statuses) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
        Row titleRow = xssfSheet.createRow(0);
        titleRow.setHeight((short) 300);

        int length = titles.length;
        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        xssfCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setFontName("Arial");
        xssfFont.setFontHeightInPoints((short) 12);
        xssfFont.setBold(true);
        xssfCellStyle.setFont(xssfFont);
        for (int index = 0; index < length; index++) {
            Cell cell = titleRow.createCell(index);
            cell.setCellValue(titles[index]);
            cell.setCellStyle(xssfCellStyle);
            xssfSheet.setColumnWidth(index, 5000);
        }

        Sheet categoryNameHiddenSheet = xssfWorkbook.createSheet("category_name_hidden");
        Sheet categoryIdHiddenSheet = xssfWorkbook.createSheet("category_id_hidden");
        int categoriesSize = categories.size();
        for (int index = 0; index < categoriesSize; index++) {
            Map<String, Object> category = categories.get(index);
            categoryNameHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getString(category, "name"));
            categoryIdHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getDoubleValue(category, "id"));
        }

        Sheet unitNameHiddenSheet = xssfWorkbook.createSheet("unit_name_hidden");
        Sheet unitIdHiddenSheet = xssfWorkbook.createSheet("unit_id_hidden");
        int unitsSize = units.size();
        for (int index = 0; index < unitsSize; index++) {
            Map<String, Object> unit = units.get(index);
            unitNameHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getString(unit, "name"));
            unitIdHiddenSheet.createRow(index).createCell(0).setCellValue(MapUtils.getDoubleValue(unit, "id"));
        }

        CellRangeAddressList categoriesCellRangeAddressList = new CellRangeAddressList(1, 65535, 4, 4);
        CellRangeAddressList unitsCellRangeAddressList = new CellRangeAddressList(1, 65535, 5, 5);
        CellRangeAddressList statusesCellRangeAddressList = new CellRangeAddressList(1, 65535, 10, 10);


        XSSFDataValidationHelper xssfDataValidationHelper = new XSSFDataValidationHelper(xssfSheet);
        DataValidationConstraint categoriesDataValidationConstraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, DataValidationConstraint.OperatorType.IGNORED, "category_name_hidden!$A$1:$A$" + categoriesSize);
        DataValidationConstraint unitsDataValidationConstraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, DataValidationConstraint.OperatorType.IGNORED, "unit_name_hidden!$A$1:$A$" + unitsSize);
        DataValidationConstraint statusesDataValidationConstraint = new XSSFDataValidationConstraint(statuses);

        DataValidation categoriesDataValidation = xssfDataValidationHelper.createValidation(categoriesDataValidationConstraint, categoriesCellRangeAddressList);
        DataValidation unitsDataValidation = xssfDataValidationHelper.createValidation(unitsDataValidationConstraint, unitsCellRangeAddressList);
        DataValidation statusesDataValidation = xssfDataValidationHelper.createValidation(statusesDataValidationConstraint, statusesCellRangeAddressList);

        xssfSheet.addValidationData(categoriesDataValidation);
        xssfSheet.addValidationData(unitsDataValidation);
        xssfSheet.addValidationData(statusesDataValidation);
        xssfWorkbook.setSheetHidden(1, true);
        xssfWorkbook.setSheetHidden(2, true);
        xssfWorkbook.setSheetHidden(3, true);
        xssfWorkbook.setSheetHidden(4, true);
        return xssfWorkbook;
    }
}
