package com.company.smiss.service;

import com.company.smiss.entity.Client;
import com.company.smiss.utils.ExceptionUtils;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Component
public class ClientLoader {


    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;
    @Inject
    private DataManager dataManager;

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClientLoader.class);

    public void loadData() {
        Map<String, Pair<String, String>> dataMap = getDataMap();

        try {
            CommitContext commitContext = new CommitContext();
            commitContext.setDiscardCommitted(true);
            for (Map.Entry<String, Pair<String, String>> entry : dataMap.entrySet()) {

                String email = entry.getKey();
                String name = entry.getValue().getLeft();
                String surname = entry.getValue().getRight();

                Client client = metadata.create(Client.class);
                client.setEmail(email);
                client.setName(name);
                client.setSurname(surname);

                commitContext.addInstanceToCommit(client);

            }

            dataManager.commit(commitContext);

        } catch (Exception e) {
            ExceptionUtils.logErrors(e);
        }

    }


    private Map<String, Pair<String, String>> getDataMap() {
        Map<String, Pair<String, String>> map = new HashMap<>();

        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\mmmss\\Desktop\\no\\st\\smiss\\1.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Строки
            Iterator rows = sheet.rowIterator();
            //Проход по всем строкам

            while (rows.hasNext()) {
                XSSFRow currentRow = (XSSFRow) rows.next();
                Iterator<? extends Cell> cells = currentRow.cellIterator();
                String name = null;
                String surname = null;
                String email = null;

                //Проход по ячейкам
                while (cells.hasNext()) {
                    XSSFCell currentCell = (XSSFCell) cells.next();
                    CellType currentCellType = currentCell.getCellType();
                    switch (currentCell.getColumnIndex()) {
                        case 0:
                            if (currentCellType.equals(CellType.STRING)) {
                                name = currentCell.getStringCellValue();
                            }
                            break;
                        case 1:
                            if (currentCellType.equals(CellType.STRING)) {
                                surname = currentCell.getStringCellValue();
                            }
                            break;
                        case 2:
                            if (currentCellType.equals(CellType.STRING)) {
                                email = currentCell.getStringCellValue();
                            }
                            break;
                        default:
                            break;
                    }
                }

                if (email != null) {
                    map.put(email, new MutablePair<>(name, surname));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return map;
    }
}
