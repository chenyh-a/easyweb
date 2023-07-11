
let apiName = "";
let oid = 0;

function queryApiList() {
   refreshList("#apiList", "");
}

function proccessResult(obj) {
   if (obj.result === "FAIL") {
      error1("Operation failed", "Error: " + obj.message);
   }
}

function listApi() {
   let opt = new TOption();
   opt.pageLength = 10;

   let columns = [
      { data: 'db' },
     // { data: 'oid' },
      { data: 'name' },
      { data: 'comment' },
      { data: 'created' },
      { data: 'modified' },
   ];
   let columnsDefs = [];
   let queryData = {
      method: "sp_get_api_list",
      data: { db: "test" }
   };
   showList("#apiList", "getpagelist", opt, columns, columnsDefs, queryData);
}

$(document).ready(function() {
   listApi();
   let table = $('#apiList').DataTable();
   $('#apiList tbody').on('click', 'tr', function() {
      apiName = table.row(this).data().name;
      oid = table.row(this).data().oid;
      listParams();
   });

});

function listParams() {
   let table = $('#paramList').dataTable();
   table.fnClearTable();
   table.fnDestroy();

   let opt = new TOption();
   opt.searching = false;
   opt.paging = false;
   opt.pageLength = 15;
   opt.info = false;

   let columns = [
      { data: 'api_name' },
      { data: 'parameter_name' },
      { data: 'parameter_type' },
      { data: 'data_type' }
   ];
   let columnsDefs = [];
   let queryData = {
      tag: "PARAMS_LIST",
      method: "sp_get_api_params",
      data: { db: "test", name: apiName, oid: oid }
   };

   showList("#paramList", "getList", opt, columns, columnsDefs, queryData);
}