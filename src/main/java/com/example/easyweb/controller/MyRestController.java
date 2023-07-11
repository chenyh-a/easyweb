package com.example.easyweb.controller;

import com.example.easyweb.Constants;
import com.example.easyweb.dao.QueryDao;
import com.example.easyweb.dao.QueryPageDao;
import com.example.easyweb.dao.UpdateDao;
import com.example.easyweb.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
@RestController
public class MyRestController {
    private static final Logger log = LoggerFactory.getLogger(MyRestController.class);
    /**
     * unique number passed from front end dataTables
     */
    private static final String DRAW = "draw";

    @Autowired
    private QueryDao queryDao;

    @Autowired
    private QueryPageDao queryPageDao;

    @Autowired
    private UpdateDao updateDao;

    /**
     * This is a routing controller to process a paging list. parameter will be
     * passed from front-end jQuery component dataTables
     */
    @GetMapping(value = "/getPageList")
    public String getPageList(@RequestParam Map<String, String> params) {
        QueryPageResponse rsp = new QueryPageResponse();
        QueryPageRequest req = new QueryPageRequest();
        String str = "";
        try {
            if (params.containsKey(DRAW)) {
                req.draw = Integer.valueOf(params.get("draw"));
                req.start = Integer.valueOf(params.get("start"));
                req.length = Integer.valueOf(params.get("length"));
            }
            req.userCode = params.get("userCode");
            String iorder = params.get("order[0][column]");
            if (iorder != null) {
                req.orderColumn = params.get("columns[" + iorder + "][data]");
                req.orderDir = params.get("order[0][dir]");
            }
            String search = params.get("search[value]");
            req.criteria.put("search", search);
            for (String key : params.keySet()) {
                String val = params.get(key);
                // System.out.println("---param:" + key + "=" + params.get(key));
                if (key.startsWith("data[")) {
                    int len = key.length();
                    String key0 = key.substring(5, len - 1);
                    req.criteria.put(key0, val);
                }
            }
            req.method = params.get("method");

            if (!Constants.RESULT_FAIL.equals(rsp.result)) {
                rsp = req.copy();
                rsp = queryPageDao.execute(req);
                rsp.recordsFiltered = rsp.recordsTotal;
            }
        } catch (Exception e) {
            rsp.result = Constants.RESULT_FAIL;
            log.error(e.getMessage(), e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            str = mapper.writeValueAsString(rsp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug(str);
        return str;
    }


    /**
     * regular query, e.g. get one record,etc, it is not exactly a strict RESTful,
     * front-end with POST criteria
     */
    @PostMapping(value = {"/getList","/getOne"})
    public String getCustomerById(@RequestBody QueryRequest req) {
        QueryResponse rsp = new QueryResponse();
        String str = "";
        try {
            if (!Constants.RESULT_FAIL.equals(rsp.result)) {
                rsp = req.copy();
                rsp = queryDao.execute(req);
            }
        } catch (Exception e) {
            rsp.result = Constants.RESULT_FAIL;
            log.error(e.getMessage(), e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            str = mapper.writeValueAsString(rsp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug(str);
        return str;
    }

    /**
     * This is a routing controller for all create/update/delete operation, passed
     * all original stored at UpdateRequrest.data which can contains more than one
     * record. value "update" contains "create", see the DB SP "sp_upd_customer",
     * the SP name will pass from the front-end, no need to change the controller
     * and DAO at all. you need to maintain SP only :)
     */
    @PostMapping(value = {"/update", "/delete"})
    public String updatePost(@RequestBody UpdateRequest req) {
        UpdateResponse rsp = new UpdateResponse();
        String str = "";
        try {
            if (!Constants.RESULT_FAIL.equals(rsp.result)) {
                rsp = req.copy();
                rsp = updateDao.execute(req);
            }
        } catch (Exception e) {
            rsp.result = Constants.RESULT_FAIL;
            log.error(e.getMessage(), e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            str = mapper.writeValueAsString(rsp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug(str);
        return str;
    }

}