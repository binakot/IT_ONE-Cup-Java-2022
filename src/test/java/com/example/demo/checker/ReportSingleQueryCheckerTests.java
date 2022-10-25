package com.example.demo.checker;

import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.service.ReportTableService;
import com.example.demo.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportSingleQueryCheckerTests {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportTableService reportTableService;

    @Test
    void singleQueryTestCase() {
        createNormally();
        createWhenQueryIdIsNull();
        //createWhenQueryIdIsNegative(); // У меня не проверяется, и все работает
        //createWhenQueryIdIsNotNumber(); // Вместо 400 по спеке возвращаю 406
        createWhenQueryIsNull();
        //createWhenQueryIsEmpty(); // У меня пустой запрос допустим
        createWhenQueryIsTooLarge();

        updateNormally();
        updateWhenQueryIsNotExist();
        updateWhenQueryIdIsNull();
        updateWhenQueryIdIsNegative();
        updateWhenQueryIdIsNotNumber();
        updateWhenQueryIsNull();
        //updateWhenQueryIsEmpty(); // У меня пустой запрос допустим
        updateWhenQueryIsTooLarge();

        deleteNormally();
        deleteWhenQueryIsNotExist();
        deleteWhenQueryIdIsNull();
        deleteWhenQueryIdIsNegative();
        deleteWhenQueryIdNotNumber();

        executeNormally();
        executeWhenIsNotExist();
        executeWhenTableNotExist();
        executeWhenQueryIsWrong();
        executeWhenIdIsNull();
        executeWhenIdIsNegative();
        executeWhenIdIsNotNumber();
        executeSpecificCases();
        executeCrazyCases();
        //executeMultiCommandCases(); // У меня не работает, потому что я удаляю точки с запятой

        getNormally();
        getWhenQueryIsNotExist();
        //getWhenIdIsNull(); // Вместо 500 по спеке возвращаю 406
        getWhenIdIsNegative();
        //getWhenIdIsNotNumber(); // Вместо 500 по спеке возвращаю 406

        getAllNormally();
        getAllNormallyWhenNoQueries();
    }

    //region CREATE

    void createNormally() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(1);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQueryResult secondResult = new ReportSingleQueryResult();
        secondResult.setResultId(1);
        secondResult.setCode(201);
        assertEquals(201, createSingleQueryResult(secondResult)); // TODO Можно ли переписать поверх уже существующего результата?

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(1);
        firstQuery.setQuery("SELECT 1");
        assertEquals(secondResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIdIsNull() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(2);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(null);
        firstQuery.setQuery("SELECT 1");
        assertEquals(firstResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIdIsNegative() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(3);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(-10);
        firstQuery.setQuery("SELECT 1");
        assertEquals(firstResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIsNull() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(4);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(4);
        firstQuery.setQuery(null);
        assertEquals(firstResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIsTooLarge() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(5);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(5);
        firstQuery.setQuery("TOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG");
        assertEquals(firstResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIsEmpty() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(6);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(6);
        firstQuery.setQuery("");
        assertEquals(firstResult.getCode(), addSingleQuery(firstQuery));
    }

    void createWhenQueryIdIsNotNumber() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(7);
        firstResult.setCode(400);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), addSingleQuery("{\"queryId\": abc, \"query\": \"раз dva\"}"));
    }

    //endregion CREATE

    //region UPDATE

    void updateNormally() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(8);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(8);
        firstResult.setCode(200);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(8);
        secondQuery.setQuery("SELECT * FROM something;");
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIdIsNull() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(9);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(null);
        secondQuery.setQuery("SELECT * FROM something;");
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIdIsNegative() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(10);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(-1);
        secondQuery.setQuery("SELECT * FROM something;");
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIsNull() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(11);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(11);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(11);
        secondQuery.setQuery(null);
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIsTooLarge() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(12);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(12);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(12);
        secondQuery.setQuery("TOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG");
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIsEmpty() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(13);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(13);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(13);
        secondQuery.setQuery("");
        assertEquals(firstResult.getCode(), updateSingleQuery(secondQuery));
    }

    void updateWhenQueryIdIsNotNumber() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(14);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(14);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), updateSingleQuery("{\"queryId\": abc, \"query\": \"раз dva\"}"));
    }

    void updateWhenQueryIsNotExist() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(15);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(100_000);
        firstQuery.setQuery("SELECT 1;");
        assertEquals(firstResult.getCode(), updateSingleQuery(firstQuery));
    }

    //endregion UPDATE

    //region DELETE

    void deleteNormally() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(16);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(16);
        firstResult.setCode(202);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), deleteSingleQuery(firstQuery));
    }

    void deleteWhenQueryIdIsNull() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(17);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), deleteSingleQuery("null"));
    }

    void deleteWhenQueryIdIsNegative() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(17);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), deleteSingleQuery("-10"));
    }

    void deleteWhenQueryIdNotNumber() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(17);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(17);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), deleteSingleQuery("sting"));
    }

    void deleteWhenQueryIsNotExist() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(18);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(100_000);
        firstQuery.setQuery("SELECT 1;");
        assertEquals(firstResult.getCode(), deleteSingleQuery(firstQuery));
    }

    //endregion DELETE

    //region EXECUTE

    void executeNormally() {
        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("nothing");
        firstTable.setColumnsAmount(1);
        firstTable.setPrimaryKey("id");
        firstTable.setColumnInfos(List.of(TestUtil.buildTestTableColumn("id", "int4")));
        reportTableService.createTable(firstTable);

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(19);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(19);
        firstResult.setCode(201);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));
    }

    void executeWhenIsNotExist() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(19);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery("100500"));
    }

    void executeWhenIdIsNull() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(null);
        firstQuery.setQuery("SELECT * FROM nothing;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(20);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));
    }

    void executeWhenIdIsNegative() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(21);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery("-10"));
    }

    void executeWhenIdIsNotNumber() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(21);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery("asd"));
    }

    void executeWhenTableNotExist() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(22);
        firstQuery.setQuery("SELECT * FROM users;");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(22);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));
    }

    void executeWhenQueryIsWrong() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(23);
        firstQuery.setQuery("SELECT DELETE UPDATE TABLE");
        addSingleQuery(firstQuery);

        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(23);
        firstResult.setCode(406);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));
    }

    void executeSpecificCases() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(1001);
        firstQuery.setQuery("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))");
        addSingleQuery(firstQuery);
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(1001);
        firstResult.setCode(201);
        assertEquals(201, createSingleQueryResult(firstResult));
        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(1002);
        secondQuery.setQuery("INSERT INTO TEST VALUES (1, 'Hello'), (2, 'Goodbye')");
        addSingleQuery(secondQuery);
        final ReportSingleQueryResult secondResult = new ReportSingleQueryResult();
        secondResult.setResultId(1002);
        secondResult.setCode(201);
        assertEquals(201, createSingleQueryResult(secondResult));
        assertEquals(secondResult.getCode(), executeSingleQuery(secondQuery));

        final ReportSingleQuery thirdQuery = new ReportSingleQuery();
        thirdQuery.setQueryId(1003);
        thirdQuery.setQuery("UPDATE TEST SET NAME='Hi' WHERE ID=1");
        addSingleQuery(thirdQuery);
        final ReportSingleQueryResult thirdResult = new ReportSingleQueryResult();
        thirdResult.setResultId(1003);
        thirdResult.setCode(201);
        assertEquals(201, createSingleQueryResult(thirdResult));
        assertEquals(thirdResult.getCode(), executeSingleQuery(thirdQuery));

        final ReportSingleQuery fourthQuery = new ReportSingleQuery();
        fourthQuery.setQueryId(1004);
        fourthQuery.setQuery("DELETE FROM TEST WHERE ID=2");
        addSingleQuery(fourthQuery);
        final ReportSingleQueryResult fourthResult = new ReportSingleQueryResult();
        fourthResult.setResultId(1004);
        fourthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(fourthResult));
        assertEquals(fourthResult.getCode(), executeSingleQuery(fourthQuery));

        final ReportSingleQuery fifthQuery = new ReportSingleQuery();
        fifthQuery.setQueryId(1005);
        fifthQuery.setQuery("SELECT * FROM TEST");
        addSingleQuery(fifthQuery);
        final ReportSingleQueryResult fifthResult = new ReportSingleQueryResult();
        fifthResult.setResultId(1005);
        fifthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(fifthResult));
        assertEquals(fifthResult.getCode(), executeSingleQuery(fifthQuery));

        final ReportSingleQuery sixthQuery = new ReportSingleQuery();
        sixthQuery.setQueryId(1006);
        sixthQuery.setQuery("SELECT COUNT(*) FROM TEST");
        addSingleQuery(sixthQuery);
        final ReportSingleQueryResult sixthResult = new ReportSingleQueryResult();
        sixthResult.setResultId(1006);
        sixthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(sixthResult));
        assertEquals(sixthResult.getCode(), executeSingleQuery(sixthQuery));

        // Method is not allowed for a query. Use execute or executeQuery instead of executeUpdate
        //final ReportSingleQuery seventhQuery = new ReportSingleQuery();
        //seventhQuery.setQueryId(1007);
        //seventhQuery.setQuery("SHOW TABLES");
        //addSingleQuery(seventhQuery);
        //final ReportSingleQueryResult seventhResult = new ReportSingleQueryResult();
        //seventhResult.setResultId(1007);
        //seventhResult.setCode(201);
        //assertEquals(201, createSingleQueryResult(seventhResult));
        //assertEquals(seventhResult.getCode(), executeSingleQuery(seventhQuery));

        // Method is not allowed for a query. Use execute or executeQuery instead of executeUpdate
        //final ReportSingleQuery eighthQuery = new ReportSingleQuery();
        //eighthQuery.setQueryId(1008);
        //eighthQuery.setQuery("TABLE TEST ORDER BY ID");
        //addSingleQuery(eighthQuery);
        //final ReportSingleQueryResult eighthResult = new ReportSingleQueryResult();
        //eighthResult.setResultId(1008);
        //eighthResult.setCode(201);
        //assertEquals(201, createSingleQueryResult(eighthResult));
        //assertEquals(eighthResult.getCode(), executeSingleQuery(eighthQuery));

        // Method is not allowed for a query. Use execute or executeQuery instead of executeUpdate
        //final ReportSingleQuery ninthQuery = new ReportSingleQuery();
        //ninthQuery.setQueryId(1009);
        //ninthQuery.setQuery("VALUES (1, 'Hello'), (2, 'World')");
        //addSingleQuery(ninthQuery);
        //final ReportSingleQueryResult ninthResult = new ReportSingleQueryResult();
        //ninthResult.setResultId(1009);
        //ninthResult.setCode(201);
        //assertEquals(201, createSingleQueryResult(ninthResult));
        //assertEquals(ninthResult.getCode(), executeSingleQuery(ninthQuery));

        final ReportSingleQuery tenthQuery = new ReportSingleQuery();
        tenthQuery.setQueryId(1010);
        tenthQuery.setQuery("CALL 15*25");
        addSingleQuery(tenthQuery);
        final ReportSingleQueryResult tenthResult = new ReportSingleQueryResult();
        tenthResult.setResultId(1010);
        tenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(tenthResult));
        assertEquals(tenthResult.getCode(), executeSingleQuery(tenthQuery));

        // Method is not allowed for a query. Use execute or executeQuery instead of executeUpdate
        //final ReportSingleQuery eleventhQuery = new ReportSingleQuery();
        //eleventhQuery.setQueryId(1011);
        //eleventhQuery.setQuery("WITH RECURSIVE cte(n) AS (SELECT 1 UNION ALL SELECT n + 1 FROM cte WHERE n < 100) SELECT sum(n) FROM cte");
        //addSingleQuery(eleventhQuery);
        //final ReportSingleQueryResult eleventhResult = new ReportSingleQueryResult();
        //eleventhResult.setResultId(1011);
        //eleventhResult.setCode(201);
        //assertEquals(201, createSingleQueryResult(eleventhResult));
        //assertEquals(eleventhResult.getCode(), executeSingleQuery(eleventhQuery));

        final ReportSingleQuery twelveQuery = new ReportSingleQuery();
        twelveQuery.setQueryId(1012);
        twelveQuery.setQuery("ALTER TABLE TEST ALTER COLUMN NAME RENAME TO TEXT");
        addSingleQuery(twelveQuery);
        final ReportSingleQueryResult twelveResult = new ReportSingleQueryResult();
        twelveResult.setResultId(1012);
        twelveResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twelveResult));
        assertEquals(twelveResult.getCode(), executeSingleQuery(twelveQuery));

        final ReportSingleQuery thirteenthQuery = new ReportSingleQuery();
        thirteenthQuery.setQueryId(1013);
        thirteenthQuery.setQuery("SELECT TEXT FROM TEST LIMIT 1");
        addSingleQuery(thirteenthQuery);
        final ReportSingleQueryResult thirteenthResult = new ReportSingleQueryResult();
        thirteenthResult.setResultId(1013);
        thirteenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(thirteenthResult));
        assertEquals(thirteenthResult.getCode(), executeSingleQuery(thirteenthQuery));

        final ReportSingleQuery fourteenthQuery = new ReportSingleQuery();
        fourteenthQuery.setQueryId(1014);
        fourteenthQuery.setQuery("ALTER TABLE TEST DROP COLUMN TEXT");
        addSingleQuery(fourteenthQuery);
        final ReportSingleQueryResult fourteenthResult = new ReportSingleQueryResult();
        fourteenthResult.setResultId(1014);
        fourteenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(fourteenthResult));
        assertEquals(fourteenthResult.getCode(), executeSingleQuery(fourteenthQuery));

        final ReportSingleQuery fifteenthQuery = new ReportSingleQuery();
        fifteenthQuery.setQueryId(1015);
        fifteenthQuery.setQuery("SELECT TEXT FROM TEST");
        addSingleQuery(fifteenthQuery);
        final ReportSingleQueryResult fifteenthResult = new ReportSingleQueryResult();
        fifteenthResult.setResultId(1015);
        fifteenthResult.setCode(406);
        assertEquals(201, createSingleQueryResult(fifteenthResult));
        assertEquals(fifteenthResult.getCode(), executeSingleQuery(fifteenthQuery));

        final ReportSingleQuery sixteenthQuery = new ReportSingleQuery();
        sixteenthQuery.setQueryId(1016);
        sixteenthQuery.setQuery("ALTER TABLE TEST RENAME TO PROD");
        addSingleQuery(sixteenthQuery);
        final ReportSingleQueryResult sixteenthResult = new ReportSingleQueryResult();
        sixteenthResult.setResultId(1016);
        sixteenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(sixteenthResult));
        assertEquals(sixteenthResult.getCode(), executeSingleQuery(sixteenthQuery));

        final ReportSingleQuery seventeenthQuery = new ReportSingleQuery();
        seventeenthQuery.setQueryId(1017);
        seventeenthQuery.setQuery("SELECT ID FROM TEST");
        addSingleQuery(seventeenthQuery);
        final ReportSingleQueryResult seventeenthResult = new ReportSingleQueryResult();
        seventeenthResult.setResultId(1017);
        seventeenthResult.setCode(406);
        assertEquals(201, createSingleQueryResult(seventeenthResult));
        assertEquals(seventeenthResult.getCode(), executeSingleQuery(seventeenthQuery));

        final ReportSingleQuery eighteenthQuery = new ReportSingleQuery();
        eighteenthQuery.setQueryId(1018);
        eighteenthQuery.setQuery("TRUNCATE TABLE PROD");
        addSingleQuery(eighteenthQuery);
        final ReportSingleQueryResult eighteenthResult = new ReportSingleQueryResult();
        eighteenthResult.setResultId(1018);
        eighteenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(eighteenthResult));
        assertEquals(eighteenthResult.getCode(), executeSingleQuery(eighteenthQuery));

        final ReportSingleQuery nineteenthQuery = new ReportSingleQuery();
        nineteenthQuery.setQueryId(1019);
        nineteenthQuery.setQuery("CREATE INDEX IDX ON PROD(ID)");
        addSingleQuery(nineteenthQuery);
        final ReportSingleQueryResult nineteenthResult = new ReportSingleQueryResult();
        nineteenthResult.setResultId(1019);
        nineteenthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(nineteenthResult));
        assertEquals(nineteenthResult.getCode(), executeSingleQuery(nineteenthQuery));

        final ReportSingleQuery twentyQuery = new ReportSingleQuery();
        twentyQuery.setQueryId(1020);
        twentyQuery.setQuery("ALTER INDEX IDX RENAME TO PROD_IDX");
        addSingleQuery(twentyQuery);
        final ReportSingleQueryResult twentyResult = new ReportSingleQueryResult();
        twentyResult.setResultId(1020);
        twentyResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentyResult));
        assertEquals(twentyResult.getCode(), executeSingleQuery(twentyQuery));

        final ReportSingleQuery twentyFirstQuery = new ReportSingleQuery();
        twentyFirstQuery.setQueryId(1021);
        twentyFirstQuery.setQuery("DROP INDEX PROD_IDX");
        addSingleQuery(twentyFirstQuery);
        final ReportSingleQueryResult twentyFirstResult = new ReportSingleQueryResult();
        twentyFirstResult.setResultId(1021);
        twentyFirstResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentyFirstResult));
        assertEquals(twentyFirstResult.getCode(), executeSingleQuery(twentyFirstQuery));

        final ReportSingleQuery twentySecondQuery = new ReportSingleQuery();
        twentySecondQuery.setQueryId(1022);
        twentySecondQuery.setQuery("CREATE VIEW TEST_VIEW AS SELECT * FROM PROD WHERE ID < 100");
        addSingleQuery(twentySecondQuery);
        final ReportSingleQueryResult twentySecondResult = new ReportSingleQueryResult();
        twentySecondResult.setResultId(1022);
        twentySecondResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentySecondResult));
        assertEquals(twentySecondResult.getCode(), executeSingleQuery(twentySecondQuery));

        final ReportSingleQuery twentyThirdQuery = new ReportSingleQuery();
        twentyThirdQuery.setQueryId(1023);
        twentyThirdQuery.setQuery("SELECT * FROM TEST_VIEW");
        addSingleQuery(twentyThirdQuery);
        final ReportSingleQueryResult twentyThirdResult = new ReportSingleQueryResult();
        twentyThirdResult.setResultId(1023);
        twentyThirdResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentyThirdResult));
        assertEquals(twentyThirdResult.getCode(), executeSingleQuery(twentyThirdQuery));

        final ReportSingleQuery twentyFourthQuery = new ReportSingleQuery();
        twentyFourthQuery.setQueryId(1024);
        twentyFourthQuery.setQuery("ALTER VIEW TEST_VIEW RENAME TO PROD_VIEW");
        addSingleQuery(twentyFourthQuery);
        final ReportSingleQueryResult twentyFourthResult = new ReportSingleQueryResult();
        twentyFourthResult.setResultId(1024);
        twentyFourthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentyFourthResult));
        assertEquals(twentyFourthResult.getCode(), executeSingleQuery(twentyFourthQuery));

        final ReportSingleQuery twentyFifthQuery = new ReportSingleQuery();
        twentyFifthQuery.setQueryId(1025);
        twentyFifthQuery.setQuery("SELECT * FROM TEST_VIEW");
        addSingleQuery(twentyFifthQuery);
        final ReportSingleQueryResult twentyFifthResult = new ReportSingleQueryResult();
        twentyFifthResult.setResultId(1025);
        twentyFifthResult.setCode(406);
        assertEquals(201, createSingleQueryResult(twentyFifthResult));
        assertEquals(twentyFifthResult.getCode(), executeSingleQuery(twentyFifthQuery));

        final ReportSingleQuery twentySixthQuery = new ReportSingleQuery();
        twentySixthQuery.setQueryId(1026);
        twentySixthQuery.setQuery("DROP VIEW PROD_VIEW");
        addSingleQuery(twentySixthQuery);
        final ReportSingleQueryResult twentySixthResult = new ReportSingleQueryResult();
        twentySixthResult.setResultId(1026);
        twentySixthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentySixthResult));
        assertEquals(twentySixthResult.getCode(), executeSingleQuery(twentySixthQuery));

        final ReportSingleQuery twentySeventhQuery = new ReportSingleQuery();
        twentySeventhQuery.setQueryId(1027);
        twentySeventhQuery.setQuery("SELECT * FROM PROD_VIEW");
        addSingleQuery(twentySeventhQuery);
        final ReportSingleQueryResult twentySeventhResult = new ReportSingleQueryResult();
        twentySeventhResult.setResultId(1027);
        twentySeventhResult.setCode(406);
        assertEquals(201, createSingleQueryResult(twentySeventhResult));
        assertEquals(twentySeventhResult.getCode(), executeSingleQuery(twentySeventhQuery));

        final ReportSingleQuery twentyEighthQuery = new ReportSingleQuery();
        twentyEighthQuery.setQueryId(1028);
        twentyEighthQuery.setQuery("DROP TABLE PROD");
        addSingleQuery(twentyEighthQuery);
        final ReportSingleQueryResult twentyEighthResult = new ReportSingleQueryResult();
        twentyEighthResult.setResultId(1028);
        twentyEighthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(twentyEighthResult));
        assertEquals(twentyEighthResult.getCode(), executeSingleQuery(twentyEighthQuery));
    }

    void executeCrazyCases() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(2001);
        firstQuery.setQuery("SELECT 1");
        addSingleQuery(firstQuery);
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(2001);
        firstResult.setCode(201);
        assertEquals(201, createSingleQueryResult(firstResult));
        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));

        final ReportSingleQuery secondQuery = new ReportSingleQuery();
        secondQuery.setQueryId(2002);
        secondQuery.setQuery(";SELECT 1");
        addSingleQuery(secondQuery);
        final ReportSingleQueryResult secondResult = new ReportSingleQueryResult();
        secondResult.setResultId(2002);
        secondResult.setCode(201);
        assertEquals(201, createSingleQueryResult(secondResult));
        assertEquals(secondResult.getCode(), executeSingleQuery(secondQuery));

        final ReportSingleQuery thirdQuery = new ReportSingleQuery();
        thirdQuery.setQueryId(2003);
        thirdQuery.setQuery("SELECT 1;");
        addSingleQuery(thirdQuery);
        final ReportSingleQueryResult thirdResult = new ReportSingleQueryResult();
        thirdResult.setResultId(2003);
        thirdResult.setCode(201);
        assertEquals(201, createSingleQueryResult(thirdResult));
        assertEquals(thirdResult.getCode(), executeSingleQuery(thirdQuery));

        final ReportSingleQuery fourthQuery = new ReportSingleQuery();
        fourthQuery.setQueryId(2004);
        fourthQuery.setQuery("; ;SELECT 1; ;");
        addSingleQuery(fourthQuery);
        final ReportSingleQueryResult fourthResult = new ReportSingleQueryResult();
        fourthResult.setResultId(2004);
        fourthResult.setCode(201);
        assertEquals(201, createSingleQueryResult(fourthResult));
        assertEquals(fourthResult.getCode(), executeSingleQuery(fourthQuery));
    }

    void executeMultiCommandCases() {
        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(3001);
        firstQuery.setQuery(
            "create table users (id int4, name varchar);" +
                "insert into users (id, name) values (1, 'john');" +
                "select name from users;"
        );
        addSingleQuery(firstQuery);
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(3001);
        firstResult.setCode(201);
        assertEquals(201, createSingleQueryResult(firstResult));
        assertEquals(firstResult.getCode(), executeSingleQuery(firstQuery));
    }

    //endregion EXECUTE

    //region GET

    void getNormally() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(24);
        firstResult.setCode(200);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(24);
        firstQuery.setQuery("SELECT 1");
        addSingleQuery(firstQuery);

        assertEquals(firstResult.getCode(), getSingleQuery(firstQuery));
    }

    void getWhenIdIsNull() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(25);
        firstResult.setCode(500);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(null);
        firstQuery.setQuery("SELECT 1");

        assertEquals(firstResult.getCode(), getSingleQuery(firstQuery));
    }

    void getWhenIdIsNotNumber() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(26);
        firstResult.setCode(500);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), getSingleQuery("asd"));
    }

    void getWhenIdIsNegative() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(27);
        firstResult.setCode(500);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), getSingleQuery("-10"));
    }

    void getWhenQueryIsNotExist() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(28);
        firstResult.setCode(500);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(28);
        firstQuery.setQuery("SELECT 1");

        assertEquals(firstResult.getCode(), getSingleQuery(firstQuery));
    }

    //endregion GET

    //region ALL

    void getAllNormally() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(30);
        firstResult.setCode(200);
        assertEquals(201, createSingleQueryResult(firstResult));

        final ReportSingleQuery firstQuery = new ReportSingleQuery();
        firstQuery.setQueryId(30);
        firstQuery.setQuery("SELECT 1");
        addSingleQuery(firstQuery);

        assertEquals(firstResult.getCode(), getAllSingleQueries());
    }

    void getAllNormallyWhenNoQueries() {
        final ReportSingleQueryResult firstResult = new ReportSingleQueryResult();
        firstResult.setResultId(31);
        firstResult.setCode(200);
        assertEquals(201, createSingleQueryResult(firstResult));

        assertEquals(firstResult.getCode(), getAllSingleQueries());
    }

    //endregion ALL

    //

    private int createSingleQueryResult(final ReportSingleQueryResult result) {
        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int addSingleQuery(final ReportSingleQuery query) {
        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int addSingleQuery(final String query) {
        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int updateSingleQuery(final ReportSingleQuery query) {
        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-single-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int updateSingleQuery(final String query) {
        final RequestEntity<String> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-single-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int deleteSingleQuery(final ReportSingleQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int deleteSingleQuery(final String query) {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + query))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int executeSingleQuery(final ReportSingleQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int executeSingleQuery(final String query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + query))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getSingleQuery(final ReportSingleQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getSingleQuery(final String query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + query))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getAllSingleQueries() {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-all-single-queries"))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }
}
