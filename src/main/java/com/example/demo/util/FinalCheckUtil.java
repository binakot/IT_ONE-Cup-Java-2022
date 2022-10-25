package com.example.demo.util;

import com.example.demo.model.dto.ReportBodyResult;
import com.example.demo.model.dto.ReportBodySingleQueryResult;
import com.example.demo.model.dto.ReportBodyTableQueryResult;
import com.example.demo.model.dto.ReportBodyTableResult;
import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.dto.ReportReadDto;
import com.example.demo.model.dto.ReportSingleQueryDto;
import com.example.demo.model.dto.ReportTableDto;
import com.example.demo.model.dto.ReportTableQueryDto;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportResult;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.model.entity.ReportTableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public enum FinalCheckUtil {
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalCheckUtil.class);

    private static final AtomicInteger TOTAL_COUNTER = new AtomicInteger(1);

    private static final AtomicInteger TABLE_TESTS_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger TABLE_QUERY_TESTS_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger SINGLE_QUERY_TESTS_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger REPORT_TESTS_COUNTER = new AtomicInteger(1);

    private static ReportTable TABLE_1;
    private static Report REPORT;

    static {
        TABLE_1 = new ReportTable();
        TABLE_1.setTableName("myFirstTable");
        TABLE_1.setColumnsAmount(2);
        TABLE_1.setPrimaryKey("id");
        TABLE_1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));

        REPORT = new Report();
        REPORT.setReportId(1);
        REPORT.setTableAmount(2);
        REPORT.setTables(List.of(TABLE_1));
    }

    /**
     * Привередливые: 201,202
     * +202 /api/table/add-create-table-result                    - только 201,406
     * +202 /api/table/add-get-table-by-name-result               - только 200
     * +202 /api/table/add-drop-table-result                      - только 201,406
     *
     * +201 /api/table-query/add-new-query-to-table-result        - только 201,406
     * +202 /api/table-query/modify-query-in-table-result         - только 200,406
     * +201 /api/table-query/delete-table-query-by-id-result      - только 201,406      должно 202,406
     * +201 /api/table-query/execute-table-query-by-id-result     - только 201,406
     * +201 /api/table-query/get-table-query-by-id-result         - только 200,500
     * +201 /api/table-query/get-all-queries-by-table-name-result - только 200
     *
     * +201 /api/single-query/add-new-query-result                - только 201,400
     * +202 /api/single-query/add-modify-result                   - только 200,406
     * +202 /api/single-query/add-delete-result                   - только 202,406
     * +202 /api/single-query/add-execute-result                  - только 201,406
     * +202 /api/single-query/add-get-single-query-by-id-result   - только 200,500
     *
     * Не превиредливые: 202
     * +202 /api/report/add-get-report-by-id-result               - любой               должно 201,406
     * +202 /api/report/add-create-report-result                  - любой               должно 201,406
     *
     * Этих вообще нет: 404
     * 404 /api/table-query/get-all-table-queries-result                                должно 201,400
     * 404 /api/single-query/get-all-single-queries-result                              должно 201,406
     * 404 /api/table-query/add-get-all-table-queries-result                            должно 201,400
     * 404 /api/single-query/add-get-all-single-queries-result                          должно 201,406
     */
    public static void killThemAll(final String hostName, final RestTemplate restTemplate, final int targetNumber) {

        //if (List.of(1, 2, 5, 17, 18).contains(targetNumber)) {
        //    ((ch.qos.logback.classic.Logger) LOGGER).setLevel(Level.ERROR);
        //} else {
        //    ((ch.qos.logback.classic.Logger) LOGGER).setLevel(Level.DEBUG);
        //}

        TABLE_TESTS_COUNTER.set(1);
        TABLE_QUERY_TESTS_COUNTER.set(1);
        SINGLE_QUERY_TESTS_COUNTER.set(1);
        REPORT_TESTS_COUNTER.set(1);

        //region POSITIVE

        /*

        createTable(TABLE_1,
            201, hostName, restTemplate);
        getTableByName(TABLE_1,
            200, hostName, restTemplate);
        deleteTableByName(TABLE_1.getTableName(),
            201, hostName, restTemplate);

        createSingleQuery(new ReportSingleQuery(1, "SELECT count(*) FROM myFirstTable"),
            201, hostName, restTemplate);
        modifySingleQuery(new ReportSingleQuery(1, "SELECT * FROM myFirstTable ORDER BY id ASC"),
            200, hostName, restTemplate);
        executeSingleQuery(1,
            201, hostName, restTemplate);
        getSingleQueryById(new ReportSingleQuery(1, "SELECT * FROM myFirstTable ORDER BY id ASC"),
            200, hostName, restTemplate);
        deleteSingleQuery(1,
            202, hostName, restTemplate);
        getAllSingleQueries(); // Нет документации.

        createTableQuery(new ReportTableQuery(1, TABLE_1.getTableName(), "SELECT * FROM myFirstTable LIMIT 1"),
            201, hostName, restTemplate);
        modifyTableQuery(new ReportTableQuery(1, TABLE_1.getTableName(), "SELECT * FROM myFirstTable ORDER BY id ASC"),
            200, hostName, restTemplate);
        executeTableQuery(1,
            201, hostName, restTemplate);
        getTableQueryById(new ReportTableQuery(1, TABLE_1.getTableName(), "SELECT * FROM myFirstTable ORDER BY id ASC"),
            200, hostName, restTemplate);
        deleteTableQuery(1,
            201, hostName, restTemplate);
        getAllQueriesByTableName(TABLE_1.getTableName(),
            200, hostName, restTemplate); // Нет документации.
        getAllTableQueries(); // Нет документации.

        createReport(REPORT,
            201, hostName, restTemplate);
        executeReport(REPORT,
            201, hostName, restTemplate);

         */

        //endregion POSITIVE

        //region NEGATIVE

        // Таблица без колонок 1
        final ReportTable noColumnsTable1 = new ReportTable();
        noColumnsTable1.setTableName("noColumnsTable1");
        noColumnsTable1.setColumnsAmount(0);
        noColumnsTable1.setPrimaryKey("id");
        noColumnsTable1.setColumnInfos(Collections.emptyList());
        createTable(noColumnsTable1,
            406, hostName, restTemplate);
        // Таблица без колонок 2
        final ReportTable noColumnsTable2 = new ReportTable();
        noColumnsTable2.setTableName("noColumnsTable2");
        noColumnsTable2.setColumnsAmount(0);
        noColumnsTable2.setPrimaryKey("id");
        noColumnsTable2.setColumnInfos(List.of(new ReportTableColumn("id", "int4")));
        createTable(noColumnsTable2,
            406, hostName, restTemplate);
        // Таблица без колонок 3
        final ReportTable noColumnsTable3 = new ReportTable();
        noColumnsTable3.setTableName("noColumnsTable3");
        noColumnsTable3.setColumnsAmount(1);
        noColumnsTable3.setPrimaryKey("id");
        noColumnsTable3.setColumnInfos(Collections.emptyList());
        createTable(noColumnsTable3,
            406, hostName, restTemplate);
        // Таблица без первичного ключа
        final ReportTable noPrimaryKeyTable = new ReportTable();
        noPrimaryKeyTable.setTableName("noPrimaryKeyTable");
        noPrimaryKeyTable.setColumnsAmount(1);
        noPrimaryKeyTable.setPrimaryKey("id");
        noPrimaryKeyTable.setColumnInfos(List.of(new ReportTableColumn("name", "varchar")));
        createTable(noPrimaryKeyTable,
            406, hostName, restTemplate);
        // Таблица с таким именем уже существует
        createTable(TABLE_1,
            406, hostName, restTemplate);
        // Недопустимое имя таблицы
        final ReportTable wrongNameTable = new ReportTable();
        wrongNameTable.setTableName("Wrong Name Table");
        wrongNameTable.setColumnsAmount(1);
        wrongNameTable.setPrimaryKey("id");
        wrongNameTable.setColumnInfos(List.of(new ReportTableColumn("id", "int4")));
        createTable(wrongNameTable,
            406, hostName, restTemplate);
        // Недопустимое название типа данных
        final ReportTable wrongDataTypeTable = new ReportTable();
        wrongDataTypeTable.setTableName("wrongDataTypeTable");
        wrongDataTypeTable.setColumnsAmount(1);
        wrongDataTypeTable.setPrimaryKey("id");
        wrongDataTypeTable.setColumnInfos(List.of(new ReportTableColumn("id", "число")));
        createTable(wrongDataTypeTable,
            406, hostName, restTemplate);
        // Недопустимое название типа данных
        final ReportTable secondWrongDataTypeTable = new ReportTable();
        secondWrongDataTypeTable.setTableName("wrongDataTypeTable");
        secondWrongDataTypeTable.setColumnsAmount(1);
        secondWrongDataTypeTable.setPrimaryKey("id");
        secondWrongDataTypeTable.setColumnInfos(List.of(new ReportTableColumn("id", " I N T ")));
        createTable(secondWrongDataTypeTable,
            406, hostName, restTemplate);
        // Список полей содержит недопустимые имена полей
        final ReportTable wrongColumnNameTable = new ReportTable();
        wrongColumnNameTable.setTableName("wrongColumnNameTable");
        wrongColumnNameTable.setColumnsAmount(2);
        wrongColumnNameTable.setPrimaryKey("id");
        wrongColumnNameTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("+поле+", "varchar")
        ));
        createTable(wrongColumnNameTable,
            406, hostName, restTemplate);
        // Таблицы с таким именем не существует
        final ReportTable notExistTable = new ReportTable();
        notExistTable.setTableName("notExistTable");
        notExistTable.setColumnsAmount(2);
        notExistTable.setPrimaryKey("id");
        notExistTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        getTableByName(notExistTable,
            200, hostName, restTemplate);
        // Таблицы с таким именем не существует
        deleteTableByName(notExistTable.getTableName(),
            406, hostName, restTemplate);
        // Удаленная ранее таблица
        deleteTableByName(TABLE_1.getTableName(),
            406, hostName, restTemplate);

        final ReportTable oneMoreTable1 = new ReportTable();
        oneMoreTable1.setTableName("oneMoreTable1");
        oneMoreTable1.setColumnsAmount(-1);
        oneMoreTable1.setPrimaryKey("id");
        oneMoreTable1.setColumnInfos(Collections.emptyList());
        createTable(oneMoreTable1,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable2 = new ReportTable();
        oneMoreTable2.setTableName("oneMoreTable2");
        oneMoreTable2.setColumnsAmount(null);
        oneMoreTable2.setPrimaryKey("id");
        oneMoreTable2.setColumnInfos(Collections.emptyList());
        createTable(oneMoreTable2,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable3 = new ReportTable();
        oneMoreTable3.setTableName("oneMoreTable3");
        oneMoreTable3.setColumnsAmount(Integer.MAX_VALUE);
        oneMoreTable3.setPrimaryKey("id");
        oneMoreTable3.setColumnInfos(Collections.emptyList());
        createTable(oneMoreTable3,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable4 = new ReportTable();
        oneMoreTable4.setTableName("oneMoreTable4");
        oneMoreTable4.setColumnsAmount(2);
        oneMoreTable4.setPrimaryKey("id");
        oneMoreTable4.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("id", "int4")
        ));
        createTable(oneMoreTable4,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable5 = new ReportTable();
        oneMoreTable5.setTableName("oneMoreTable5");
        oneMoreTable5.setColumnsAmount(2);
        oneMoreTable5.setPrimaryKey("id");
        oneMoreTable5.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("id", "varchar")
        ));
        createTable(oneMoreTable5,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable6 = new ReportTable();
        oneMoreTable6.setTableName("oneMoreTable6");
        oneMoreTable6.setColumnsAmount(3);
        oneMoreTable6.setPrimaryKey("id");
        oneMoreTable6.setColumnInfos(List.of(
            new ReportTableColumn("id", "serial not null primary key"),
            new ReportTableColumn("id", ""),
            new ReportTableColumn("id", "null")
        ));
        createTable(oneMoreTable6,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable7 = new ReportTable();
        oneMoreTable7.setTableName("oneMoreTable7");
        oneMoreTable7.setColumnsAmount(3);
        oneMoreTable7.setPrimaryKey("id");
        oneMoreTable7.setColumnInfos(null);
        createTable(oneMoreTable7,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable8 = new ReportTable();
        oneMoreTable8.setTableName("oneMoreTable8");
        oneMoreTable8.setColumnsAmount(1);
        oneMoreTable8.setPrimaryKey("id");
        oneMoreTable8.setColumnInfos(List.of(
            new ReportTableColumn("null", "binary")
        ));
        createTable(oneMoreTable8,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable9 = new ReportTable();
        oneMoreTable9.setTableName("oneMoreTable9");
        oneMoreTable9.setColumnsAmount(1);
        oneMoreTable9.setPrimaryKey("value");
        oneMoreTable9.setColumnInfos(List.of(
            new ReportTableColumn("value", "text")
        ));
        createTable(oneMoreTable9,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable10 = new ReportTable();
        oneMoreTable10.setTableName("oneMoreTable10");
        oneMoreTable10.setColumnsAmount(2);
        oneMoreTable10.setPrimaryKey("value");
        oneMoreTable10.setColumnInfos(List.of(
            new ReportTableColumn("id int4,", "name varchar"),
            new ReportTableColumn("amount", "numeric")
        ));
        createTable(oneMoreTable10,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable11 = new ReportTable();
        oneMoreTable11.setTableName("oneMoreTable11");
        oneMoreTable11.setColumnsAmount(2);
        oneMoreTable11.setPrimaryKey("value");
        oneMoreTable11.setColumnInfos(List.of(
            new ReportTableColumn("id,", "serial"),
            new ReportTableColumn("counter", "timestamp with time zone")
        ));
        createTable(oneMoreTable11,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable13 = new ReportTable();
        oneMoreTable13.setTableName("Customerлала");
        oneMoreTable13.setColumnsAmount(1);
        oneMoreTable13.setPrimaryKey("id");
        oneMoreTable13.setColumnInfos(List.of(
            new ReportTableColumn("id,", "int4")
        ));
        createTable(oneMoreTable13,
            406, hostName, restTemplate);

        final ReportTable oneMoreTable14 = new ReportTable();
        oneMoreTable14.setTableName("Customerхаха");
        oneMoreTable14.setColumnsAmount(1);
        oneMoreTable14.setPrimaryKey("id");
        oneMoreTable14.setColumnInfos(List.of(
            new ReportTableColumn("id,", "int4")
        ));
        createTable(oneMoreTable14,
            406, hostName, restTemplate);

        // Пример неправильного запроса
        createSingleQuery(new ReportSingleQuery(100, "раз dva"),
            400, hostName, restTemplate);
        // Запроса с таким id не существует
        modifySingleQuery(new ReportSingleQuery(200, "SELECT * FROM myFirstTable ORDER BY id ASC"),
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        executeSingleQuery(300,
            406, hostName, restTemplate);
        // Синтаксис запроса неверный
        createSingleQuery(new ReportSingleQuery(400, "INSERT SELECT UPDATE DELETE * FROM DATABASE"),
            201, hostName, restTemplate);
        getSingleQueryById(new ReportSingleQuery(400, "INSERT SELECT UPDATE DELETE * FROM DATABASE"),
            200, hostName, restTemplate);
        executeSingleQuery(400,
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        getSingleQueryById(new ReportSingleQuery(500, "SELECT * FROM myFirstTable ORDER BY id ASC"),
            400, hostName, restTemplate);
        // Запроса с таким id не существует
        deleteSingleQuery(600,
            406, hostName, restTemplate);
        // Удаление ранее удаленного запроса
        deleteSingleQuery(1,
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        deleteSingleQuery(-1,
            406, hostName, restTemplate);


        // Таблица с таким именем не существует
        createTableQuery(new ReportTableQuery(1000, "notExistTable", "SELECT * FROM notExistTable LIMIT 1"),
            406, hostName, restTemplate);
        // Таблица с таким именем не существует
        modifyTableQuery(new ReportTableQuery(2000, "notExistTable", "SELECT * FROM mySecondTable LIMIT 3"),
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        executeTableQuery(3000,
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        getTableQueryById(new ReportTableQuery(4000, TABLE_1.getTableName(), "SELECT 1;"),
            500, hostName, restTemplate);
        // Таблица с таким именем не существует
        deleteTableQuery(5000,
            406, hostName, restTemplate);
        // Таблицы с таким именем не существует
        getAllQueriesByTableName("notExistTable",
            200, hostName, restTemplate); // Нет документации.
        getAllTableQueries(); // Нет документации.
        // Выполняемый удаленный ранее запрос
        executeTableQuery(null,
            406, hostName, restTemplate);
        executeTableQuery(0,
            406, hostName, restTemplate);
        executeTableQuery(1,
            406, hostName, restTemplate);
        executeTableQuery(2,
            406, hostName, restTemplate);
        executeTableQuery(-10,
            406, hostName, restTemplate);
        executeTableQuery(Integer.MAX_VALUE,
            406, hostName, restTemplate);


        // Репорт с таким reportId уже существует
        createReport(REPORT,
            406, hostName, restTemplate);
        // Таблица не существует
        final Report notExistTableReport = new Report();
        notExistTableReport.setReportId(10000);
        notExistTableReport.setTableAmount(1);
        notExistTableReport.setTables(List.of(notExistTable));
        createReport(notExistTableReport,
            406, hostName, restTemplate);
        // Неверный тип данных с колонке
        final Report wrongDateTypeTableReport = new Report();
        wrongDateTypeTableReport.setReportId(20000);
        wrongDateTypeTableReport.setTableAmount(1);
        wrongDateTypeTableReport.setTables(List.of(secondWrongDataTypeTable));
        createReport(wrongDateTypeTableReport,
            406, hostName, restTemplate);
        // Неверное имя колонки
        final Report wrongColumnNameTableReport = new Report();
        wrongColumnNameTableReport.setReportId(30000);
        wrongColumnNameTableReport.setTableAmount(1);
        wrongColumnNameTableReport.setTables(List.of(wrongColumnNameTable));
        createReport(wrongColumnNameTableReport,
            406, hostName, restTemplate);
        // Запроса с таким id не существует
        executeReport(notExistTableReport,
            406, hostName, restTemplate);

        //endregion NEGATIVE

        //region NOTES

        // При изменении имени имени таблицы с помощью sql-запросов или их модификации,
        // запросы, привязанные к данной таблице, также должны менять привязку к таблице на новую
        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("noteFirstTable");
        firstTable.setColumnsAmount(2);
        firstTable.setPrimaryKey("id");
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        createTable(firstTable,
            201, hostName, restTemplate);

        createTableQuery(new ReportTableQuery(100_000, firstTable.getTableName(), "SELECT * FROM noteFirstTable"),
            201, hostName, restTemplate);
        createTableQuery(new ReportTableQuery(200_000, firstTable.getTableName(), "SELECT * FROM noteFirstTable"),
            201, hostName, restTemplate);

        createSingleQuery(new ReportSingleQuery(300_000, "ALTER TABLE noteFirstTable RENAME TO renamedNoteFirstTable"),
            201, hostName, restTemplate);
        executeSingleQuery(300_000,
            201, hostName, restTemplate);
        executeSingleQuery(300_000,
            406, hostName, restTemplate);

        getTableQueryById(new ReportTableQuery(100_000, "renamedNoteFirstTable", "SELECT * FROM noteFirstTable"),
            200, hostName, restTemplate);
        getTableQueryById(new ReportTableQuery(200_000, "renamedNoteFirstTable", "SELECT * FROM noteFirstTable"),
            200, hostName, restTemplate);

        executeTableQuery(100_000,
            201, hostName, restTemplate);
        executeTableQuery(200_000,
            201, hostName, restTemplate);

        // При удалении таблицы все связанные вопросы должны удаляться
        deleteTableByName("renamedNoteFirstTable",
            201, hostName, restTemplate);
        deleteTableByName("renamedNoteFirstTable",
            406, hostName, restTemplate);
        getTableQueryById(new ReportTableQuery(100_000, "renamedNoteFirstTable", "SELECT * FROM noteFirstTable"),
            500, hostName, restTemplate);
        getTableQueryById(new ReportTableQuery(100_000, "renamedNoteFirstTable", "SELECT * FROM noteFirstTable"),
            500, hostName, restTemplate);

        // В репортах нужно делать проверку, совпадают ли столбцы и типы с реальными
        final ReportTable wrongSecondTable = new ReportTable();
        wrongSecondTable.setTableName("mySecondTable");
        wrongSecondTable.setColumnsAmount(2);
        wrongSecondTable.setPrimaryKey("id");
        wrongSecondTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "varchar"),
            new ReportTableColumn("data", "int4")
        ));

        final Report wrongTableReport = new Report();
        wrongTableReport.setReportId(1_000_000);
        wrongTableReport.setTableAmount(2);
        wrongTableReport.setTables(List.of(TABLE_1, wrongSecondTable));
        createReport(wrongTableReport,
            406, hostName, restTemplate);

        final ReportTable anotherWrongSecondTable = new ReportTable();
        anotherWrongSecondTable.setTableName("mySecondTable");
        anotherWrongSecondTable.setColumnsAmount(2);
        anotherWrongSecondTable.setPrimaryKey("id");
        anotherWrongSecondTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("volume", "varchar")
        ));

        final Report anotherWrongTableReport = new Report();
        anotherWrongTableReport.setReportId(2_000_000);
        anotherWrongTableReport.setTableAmount(2);
        anotherWrongTableReport.setTables(List.of(TABLE_1, anotherWrongSecondTable));
        createReport(anotherWrongTableReport,
            406, hostName, restTemplate);

        // Две таблицы с одинаковым именем не могут существовать
        createSingleQuery(new ReportSingleQuery(10_000_000, "CREATE TABLE Customerлала(id INT PRIMARY KEY, name VARCHAR(255))"),
            201, hostName, restTemplate);
        executeSingleQuery(10_000_000,
            201, hostName, restTemplate);
        executeSingleQuery(10_000_000,
            406, hostName, restTemplate);

        // Таблицу, добавленную любым способом можно использовать в репортах
        final ReportTable queryTable = new ReportTable();
        queryTable.setTableName("Customerлала");
        queryTable.setColumnsAmount(2);
        queryTable.setPrimaryKey("id");
        queryTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "INT"),
            new ReportTableColumn("name", "VARCHAR(255)")
        ));

        final Report queryTableReport = new Report();
        queryTableReport.setReportId(20_000_000);
        queryTableReport.setTableAmount(1);
        queryTableReport.setTables(List.of(queryTable));
        createReport(queryTableReport,
            201, hostName, restTemplate);
        executeReport(queryTableReport,
            201, hostName, restTemplate);

        //endregion NOTES

        //region SPECIFIC

        final ReportTable reportTable = new ReportTable();
        reportTable.setTableName("REPORTS");
        reportTable.setColumnsAmount(2);
        reportTable.setPrimaryKey("ID");
        reportTable.setColumnInfos(List.of(
            new ReportTableColumn("ID", "INTEGER"),
            new ReportTableColumn("NAME", "VARCHAR")
        ));
        createTable(reportTable, 201, hostName, restTemplate);

        final Report report = new Report();
        report.setTableAmount(1);
        report.setTables(List.of(reportTable));

        reportTable.setColumnsAmount(1);
        report.setReportId(100_000_001);
        createReport(report, 406, hostName, restTemplate);

        reportTable.setColumnsAmount(2);
        report.getTables().get(0).getColumnInfos().get(0).setTitle("WOW");
        report.setReportId(100_000_002);
        createReport(report, 406, hostName, restTemplate);

        report.getTables().get(0).getColumnInfos().get(0).setTitle("ID");
        report.getTables().get(0).getColumnInfos().get(0).setType("TEXT");
        report.setReportId(100_000_003);
        createReport(report, 406, hostName, restTemplate);

        report.setTableAmount(1000);
        report.setReportId(100_000_004);
        createReport(report, 406, hostName, restTemplate);

        //endregion SPECIFIC

        //region SQL

        int singleQueryId = 1_000_000_000;

        createSingleQuery(new ReportSingleQuery(singleQueryId++,
                "create table users (id int4, name varchar);insert into users (id, name) values (1, 'john');select name from users;"),
            201, hostName, restTemplate);
        executeSingleQuery(singleQueryId,
            201, hostName, restTemplate);

        createSingleQuery(new ReportSingleQuery(singleQueryId++,
                "; ;SELECT 1; ;"),
            201, hostName, restTemplate);
        executeSingleQuery(singleQueryId,
            201, hostName, restTemplate);

        //endregion SQL

        //region REPORT AGAIN

        final ReportTable customerTable = new ReportTable();
        customerTable.setTableName("Customers");
        customerTable.setColumnsAmount(3);
        customerTable.setPrimaryKey("id");
        customerTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        createTable(customerTable, 201, hostName, restTemplate);

        int reportCounter = 0;
        for (int i = 0; i < 5; i++) {

            final ReportCreateDto.ReportCreateTableDto customerTable1 = new ReportCreateDto.ReportCreateTableDto();
            customerTable1.setTableName("Customers");
            customerTable1.setColumns(List.of(
                new ReportCreateDto.ReportCreateTableColumnDto("name", "varchar"),
                new ReportCreateDto.ReportCreateTableColumnDto("age", "int4")
            ));
            final ReportCreateDto customerReport1 = new ReportCreateDto();
            customerReport1.setReportId(100500 + reportCounter++);
            customerReport1.setTableAmount(1);
            customerReport1.setTables(List.of(customerTable1));
            createReport(customerReport1, 406, hostName, restTemplate);


            final ReportCreateDto.ReportCreateTableDto customerTable2 = new ReportCreateDto.ReportCreateTableDto();
            customerTable2.setTableName("Customers");
            customerTable2.setColumns(List.of(
                new ReportCreateDto.ReportCreateTableColumnDto("name", "varchar"),
                new ReportCreateDto.ReportCreateTableColumnDto("enabled", "boolean")
            ));
            final ReportCreateDto customerReport2 = new ReportCreateDto();
            customerReport2.setReportId(100500 + reportCounter++);
            customerReport2.setTableAmount(1);
            customerReport2.setTables(List.of(customerTable2));
            createReport(customerReport2, 406, hostName, restTemplate);


            final ReportCreateDto.ReportCreateTableDto customerTable3 = new ReportCreateDto.ReportCreateTableDto();
            customerTable3.setTableName("Customers");
            customerTable3.setColumns(List.of(
                new ReportCreateDto.ReportCreateTableColumnDto("name", "int4"),
                new ReportCreateDto.ReportCreateTableColumnDto("age", "int4")
            ));
            final ReportCreateDto customerReport3 = new ReportCreateDto();
            customerReport3.setReportId(100500 + reportCounter++);
            customerReport3.setTableAmount(1);
            customerReport3.setTables(List.of(customerTable3));
            createReport(customerReport3, 406, hostName, restTemplate);
        }

        final ReportCreateDto.ReportCreateTableDto wrontTypeTable1 = new ReportCreateDto.ReportCreateTableDto();
        wrontTypeTable1.setTableName("Customers");
        wrontTypeTable1.setColumns(List.of(
            new ReportCreateDto.ReportCreateTableColumnDto("name", "varchar"),
            new ReportCreateDto.ReportCreateTableColumnDto("age", "int4")
        ));
        final ReportCreateDto customerReport1 = new ReportCreateDto();
        customerReport1.setReportId(100500 + reportCounter++);
        customerReport1.setTableAmount(2);
        customerReport1.setTables(List.of(ReportCreateDto.ReportCreateTableDto.build(TABLE_1), wrontTypeTable1));
        createReport(customerReport1, 406, hostName, restTemplate);

        final ReportCreateDto.ReportCreateTableDto wrongColumnTable2 = new ReportCreateDto.ReportCreateTableDto();
        wrongColumnTable2.setTableName("Customers");
        wrongColumnTable2.setColumns(List.of(
            new ReportCreateDto.ReportCreateTableColumnDto("name", "varchar"),
            new ReportCreateDto.ReportCreateTableColumnDto("enabled", "boolean")
        ));
        final ReportCreateDto customerReport2 = new ReportCreateDto();
        customerReport2.setReportId(100500 + reportCounter++);
        customerReport2.setTableAmount(1);
        customerReport2.setTables(List.of(ReportCreateDto.ReportCreateTableDto.build(TABLE_1), wrongColumnTable2));
        createReport(customerReport2, 406, hostName, restTemplate);

        //endregion REPORT AGAIN
    }

    //region TABLE

    static void createTable(final ReportTable table, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code createTable");
        }

        final ReportTableResult result = new ReportTableResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportTableDto> request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportTableDto.build(table));
        executeActionRequest(restTemplate, request);

        TABLE_TESTS_COUNTER.incrementAndGet();
    }

    static void getTableByName(final ReportTable table, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200) {
            LOGGER.error("Wrong code getTableByName");
        }

        final ReportBodyTableResult result = new ReportBodyTableResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);
        result.setTable(ReportTableDto.build(table));

        final RequestEntity<ReportBodyTableResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table/add-get-table-by-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/table/get-table-by-name/" + table.getTableName() + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_TESTS_COUNTER.incrementAndGet();
    }

    static void deleteTableByName(final String tableName, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code deleteTableByName");
        }

        final ReportTableResult result = new ReportTableResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/" + tableName + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_TESTS_COUNTER.incrementAndGet();
    }

    //endregion TABLE

    //region TABLE QUERY

    static void createTableQuery(final ReportTableQuery tableQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code createTableQuery");
        }

        final ReportBodyTableQueryResult result = new ReportBodyTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);
        result.setTableQuery(ReportTableQueryDto.build(tableQuery));

        final RequestEntity<ReportBodyTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportTableQueryDto> request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportTableQueryDto.build(tableQuery));
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void getAllQueriesByTableName(final String tableName, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200) {
            LOGGER.error("Wrong code getAllQueriesByTableName");
        }

        final ReportTableQueryResult result = new ReportTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-all-queries-by-table-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-all-queries-by-table-name/" + tableName + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void getTableQueryById(final ReportTableQuery tableQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200 && code != 500) {
            LOGGER.error("Wrong code getTableQueryById");
        }

        final ReportBodyTableQueryResult result = new ReportBodyTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);
        result.setTableQuery(ReportTableQueryDto.build(tableQuery));

        final RequestEntity<ReportBodyTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-table-query-by-id/" + tableQuery.getQueryId() + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void executeTableQuery(final Integer tableQueryId, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code executeTableQuery");
        }

        final ReportTableQueryResult result = new ReportTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/execute-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/execute-table-query-by-id/" + tableQueryId + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void deleteTableQuery(final Integer tableQueryId, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code deleteTableQuery"); // По доке 200/406, по факту 201/405
        }

        final ReportTableQueryResult result = new ReportTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/delete-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create(hostName + "/api/table-query/delete-table-query-by-id/" + tableQueryId + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void modifyTableQuery(final ReportTableQuery tableQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200 && code != 406) {
            LOGGER.error("Wrong code modifyTableQuery");
        }

        final ReportTableQueryResult result = new ReportTableQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportTableQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/table-query/modify-query-in-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportTableQueryDto> request = RequestEntity
            .put(URI.create(hostName + "/api/table-query/modify-query-in-table?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportTableQueryDto.build(tableQuery));
        executeActionRequest(restTemplate, request);

        TABLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void getAllTableQueries() {
        // /api/table-query/get-all-table-queries-result 404
        // /api/table-query/add-get-all-table-queries-result 404
        // TODO Документация отсутствует!
    }

    //endregion TABLE QUERY

    //region SINGLE QUERY

    static void createSingleQuery(final ReportSingleQuery singleQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 400) {
            LOGGER.error("Wrong code createSingleQuery");
        }

        final ReportSingleQueryResult result = new ReportSingleQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportSingleQueryDto> request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportSingleQueryDto.build(singleQuery));
        executeActionRequest(restTemplate, request);

        SINGLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void modifySingleQuery(final ReportSingleQuery singleQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200 && code != 406) {
            LOGGER.error("Wrong code modifySingleQuery");
        }

        final ReportSingleQueryResult result = new ReportSingleQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-modify-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportSingleQueryDto> request = RequestEntity
            .put(URI.create(hostName + "/api/single-query/modify-single-query?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportSingleQueryDto.build(singleQuery));
        executeActionRequest(restTemplate, request);

        SINGLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void deleteSingleQuery(final Integer singleQueryId, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 202 && code != 406) {
            LOGGER.error("Wrong code deleteSingleQuery");
        }

        final ReportSingleQueryResult result = new ReportSingleQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create(hostName + "/api/single-query/delete-single-query-by-id/" + singleQueryId + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        SINGLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void executeSingleQuery(final Integer singleQueryId, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code executeSingleQuery");
        }

        final ReportSingleQueryResult result = new ReportSingleQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-execute-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/execute-single-query-by-id/" + singleQueryId + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        SINGLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void getSingleQueryById(final ReportSingleQuery singleQuery, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 200 && code != 400) {
            LOGGER.error("Wrong code getSingleQueryById");
        }

        final ReportBodySingleQueryResult result = new ReportBodySingleQueryResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);
        result.setQueryId(singleQuery.getQueryId());
        result.setQuery(singleQuery.getQuery());

        final RequestEntity<ReportBodySingleQueryResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-get-single-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/get-single-query-by-id/" + singleQuery.getQueryId() + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        SINGLE_QUERY_TESTS_COUNTER.incrementAndGet();
    }

    static void getAllSingleQueries() {
        // /api/single-query/get-all-single-queries-result 404
        // /api/single-query/add-get-all-single-queries-result 404
        // TODO Документация отсутствует!
    }

    //endregion SINGLE QUERY

    //region REPORT

    static void createReport(final Report report, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code createReport");
        }

        final ReportResult result = new ReportResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/report/add-create-report-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create(hostName + "/api/report/create-report?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(ReportCreateDto.build(report));
        executeActionRequest(restTemplate, request);

        REPORT_TESTS_COUNTER.incrementAndGet();
    }

    static void executeReport(final Report report, final Integer code, final String hostName, final RestTemplate restTemplate) {
        if (code != 201 && code != 406) {
            LOGGER.error("Wrong code executeReport");
        }

        final ReportBodyResult result = new ReportBodyResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);
        result.setGetReport(ReportReadDto.build(report));

        final RequestEntity<ReportBodyResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/report/add-get-report-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create(hostName + "/api/report/get-report-by-id/" + report.getReportId() + "?resultId=" + result.getResultId()))
            .build();
        executeActionRequest(restTemplate, request);

        REPORT_TESTS_COUNTER.incrementAndGet();
    }

    //endregion REPORT

    //

    static void executeResultRequest(final RestTemplate restTemplate, final RequestEntity<?> requestEntity) {
        int respCode;
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            respCode = responseEntity.getStatusCodeValue();
        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT {} | REQ: {} | RESP: {}", TOTAL_COUNTER.get(), requestEntity, respCode);
        }
    }

    static void executeActionRequest(final RestTemplate restTemplate, final RequestEntity<?> requestEntity) {
        int respCode;
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            respCode = responseEntity.getStatusCodeValue();
        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION {} | {} | REQ: {} | RESP: {}", TOTAL_COUNTER.get(), countLimit(requestEntity), requestEntity, respCode);
        }
    }

    static String countLimit(final RequestEntity<?> requestEntity) {
        if (requestEntity.getUrl().toString().contains("/table/")) {
            if (TABLE_TESTS_COUNTER.get() > 30) {
                return "OVERLIMIT T " + TABLE_TESTS_COUNTER.get() + "/30";
            }
            return "T " + TABLE_TESTS_COUNTER.get() + "/30";
        }
        if (requestEntity.getUrl().toString().contains("/table-query/")) {
            if (TABLE_QUERY_TESTS_COUNTER.get() > 20) {
                return "OVERLIMIT TQ " + TABLE_QUERY_TESTS_COUNTER.get() + "/20";
            }
            return "TQ " + TABLE_QUERY_TESTS_COUNTER.get() + "/20";
        }
        if (requestEntity.getUrl().toString().contains("/single-query/")) {
            if (SINGLE_QUERY_TESTS_COUNTER.get() > 20) {
                return "OVERLIMIT SQ " + SINGLE_QUERY_TESTS_COUNTER.get() + "/20";
            }
            return "SQ " + SINGLE_QUERY_TESTS_COUNTER.get() + "/20";
        }
        if (requestEntity.getUrl().toString().contains("/report/")) {
            if (REPORT_TESTS_COUNTER.get() > 30) {
                return "OVERLIMIT R " + REPORT_TESTS_COUNTER.get() + "/30";
            }
            return "R " + REPORT_TESTS_COUNTER.get() + "/30";
        }
        return "?";
    }

    //

    static void createReport(final ReportCreateDto report, final Integer code, final String hostName, final RestTemplate restTemplate) {
        final ReportResult result = new ReportResult();
        result.setResultId(TOTAL_COUNTER.getAndIncrement());
        result.setCode(code);

        final RequestEntity<ReportResult> resultRequest = RequestEntity
            .post(URI.create(hostName + "/api/report/add-create-report-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);
        executeResultRequest(restTemplate, resultRequest);

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create(hostName + "/api/report/create-report?resultId=" + result.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report);
        executeActionRequest(restTemplate, request);

        REPORT_TESTS_COUNTER.incrementAndGet();
    }
}
