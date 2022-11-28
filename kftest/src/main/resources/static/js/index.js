function sendOrder() {
   $("#result").empty();
   var rec = {};
   // change it to data collected from form fields:
   rec.orderId = $("#orderId").val();
   rec.productId = $("#productId").val();
   var obj = {};
   obj.method = "sp_new_order";
   obj.tag = "NEW_ORDER";
   obj.data = [rec];
   callajax("send", obj);
}

// asyn processing result
function proccessResult(obj) {
   $("#result").html(obj.result);
   $("#message").html(obj.message);
}

function callajax(serverurl, obj) {
   var req = JSON.stringify(obj);
   $(document.body).css("cursor", "progress");
   console.log("----ajax-req=" + JSON.stringify(obj));
   $.ajax({
      type: "post",
      url: serverurl,
      async: true,
      dataType: "json",
      contentType: 'application/json;charset=UTF-8',
      headers: {
         accept: "application/json;charset=UTF-8",
         reqstr: encodeURIComponent(req)
      },
      data: req,
      success: function(result, status) {
         $(document.body).css("cursor", "default");
         proccessResult(result);
      },
      error: function(xhr) {
         $(document.body).css("cursor", "default");
      }
   });
}